package com.company.proyect.kevinpiazzoli.trilceucv.MisAsignaturas.Recursos;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.company.proyect.kevinpiazzoli.trilceucv.ConexionInternet.DetectarInternet;
import com.company.proyect.kevinpiazzoli.trilceucv.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KevinPiazzoli on 04/11/2016.
 */

public class RVArchivosAdapter extends RecyclerView.Adapter<RVArchivosAdapter.ArchivosViewHolder>{

    List<Archivos> archivos;

    RVArchivosAdapter(List<Archivos> archivos){
        this.archivos = archivos;
    }

    @Override
    public ArchivosViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_recursos, parent, false);
        return new ArchivosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ArchivosViewHolder holder, final int i) {

        holder.estadoDescargaFallida.setVisibility(archivos.get(i).estadoDescarga ? View.GONE : View.VISIBLE);
        holder.estadoDescargaCompleta.setVisibility(archivos.get(i).estadoDescarga ? View.VISIBLE : View.GONE);

        holder.NombreArchivo.setText(archivos.get(i).NombreDelArchivo);
        holder.FechaDelArchivo.setText(archivos.get(i).Semana);
        holder.TamañoArchivo.setText((archivos.get(i).Tamaño)+" Mb");
        holder.TamañoArchivoCancelar.setText((archivos.get(i).Tamaño)+" Mb");

        holder.CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(archivos.get(i).estadoDescarga && holder.Cancelar.getVisibility()==View.GONE || holder.estadoDescargaCompleta.getVisibility()==View.VISIBLE) {
                    Toast.makeText(view.getContext(), "Abriendo archivo...", Toast.LENGTH_SHORT).show();
                    File file = new File(archivos.get(i).AbrirFileRutaArchive);
                    Intent target = new Intent(Intent.ACTION_VIEW);
                    target.setDataAndType(Uri.fromFile(file), "application/pdf");
                    target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    Intent intent = Intent.createChooser(target, "Abrir Arhivo");
                    try {
                        view.getContext().startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(view.getContext(), "Por favor instale una aplicacion para leer pdfs", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(archivos.get(i).estadoDescarga) Toast.makeText(view.getContext(), "Debe esperar o cancelar la descarga para poder abrir el archivo", Toast.LENGTH_SHORT).show();
                else Toast.makeText(view.getContext(), "Para poder ver este archivo debe descargarlo", Toast.LENGTH_SHORT).show();
            }
        });

        holder.CardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                new AlertDialog.Builder(view.getContext())
                        .setMessage("Esta apunto de eliminar el archivo, ¿Desea continuar?")
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        File file = new File(archivos.get(i).AbrirFileRutaArchive);
                                        if(file.delete()){
                                            Toast.makeText(view.getContext(), "Archivo eliminado correctamente", Toast.LENGTH_SHORT).show();
                                            holder.estadoDescargaFallida.setVisibility(View.VISIBLE);
                                            holder.estadoDescargaCompleta.setVisibility(View.GONE);
                                        }
                                        else Toast.makeText(view.getContext(), "Error al eliminar el archivo", Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setNegativeButton(
                                "Cancelar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).show();
                return true;
            }
        });

        final DescargarArchivos[] descargarArchivos = new DescargarArchivos[archivos.size()];
        holder.Descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(DetectarInternet.isOnline(view.getContext()) && holder.estadoDescargaFallida.getVisibility()==View.VISIBLE) {
                    Toast.makeText(view.getContext(), "Descargando...", Toast.LENGTH_SHORT).show();
                    holder.Descargar.setVisibility(View.GONE);
                    holder.TamañoArchivo.setVisibility(View.GONE);
                    holder.Cancelar.setVisibility(View.VISIBLE);
                    holder.TamañoArchivoCancelar.setVisibility(View.VISIBLE);
                    holder.BarDescarga.setVisibility(View.VISIBLE);
                    holder.estadoDescargaFallida.setVisibility(View.GONE);
                    holder.estadoDescargaCompleta.setVisibility(View.GONE);
                    List<DescargaArchivosDatos> list = new ArrayList<>();

                    list.add(new DescargaArchivosDatos(
                            archivos.get(i).URLArchivo,
                            archivos.get(i).NombreDelArchivoFile,
                            archivos.get(i).RutaArchivo,
                            holder.BarDescarga,
                            holder.Descargar,
                            holder.Cancelar,
                            holder.estadoDescargaCompleta,
                            holder.estadoDescargaFallida,
                            holder.TamañoArchivo,
                            holder.TamañoArchivoCancelar,
                            view.getContext()));
                    descargarArchivos[i] = new DescargarArchivos();
                    descargarArchivos[i].execute(list);
                }

                else if(DetectarInternet.isOnline(view.getContext()) && holder.estadoDescargaCompleta.getVisibility()==View.VISIBLE){
                    new AlertDialog.Builder(view.getContext())
                            .setMessage("Este archivo ya existe en el dispositivo, ¿desea descargarlo de todas maneras y remplazar el antiguo?")
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            File file = new File(archivos.get(i).AbrirFileRutaArchive);
                                            if(file.delete()){
                                                Toast.makeText(view.getContext(), "Descargando...", Toast.LENGTH_SHORT).show();
                                                holder.Descargar.setVisibility(View.GONE);
                                                holder.TamañoArchivo.setVisibility(View.GONE);
                                                holder.Cancelar.setVisibility(View.VISIBLE);
                                                holder.TamañoArchivoCancelar.setVisibility(View.VISIBLE);
                                                holder.BarDescarga.setVisibility(View.VISIBLE);
                                                holder.estadoDescargaFallida.setVisibility(View.GONE);
                                                holder.estadoDescargaCompleta.setVisibility(View.GONE);
                                                List<DescargaArchivosDatos> list = new ArrayList<>();

                                                list.add(new DescargaArchivosDatos(
                                                        archivos.get(i).URLArchivo,
                                                        archivos.get(i).NombreDelArchivoFile,
                                                        archivos.get(i).RutaArchivo,
                                                        holder.BarDescarga,
                                                        holder.Descargar,
                                                        holder.Cancelar,
                                                        holder.estadoDescargaCompleta,
                                                        holder.estadoDescargaFallida,
                                                        holder.TamañoArchivo,
                                                        holder.TamañoArchivoCancelar,
                                                        view.getContext()));
                                                descargarArchivos[i] = new DescargarArchivos();
                                                descargarArchivos[i].execute(list);
                                            }
                                            else Toast.makeText(view.getContext(), "Error al actualizar el archivo", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                            .setNegativeButton(
                                    "Cancelar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    }).show();
                }

                else Toast.makeText(view.getContext(), "Para poder descargar este archivo debe estar conectado a internet", Toast.LENGTH_SHORT).show();
            }
        });

        holder.Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DetectarInternet.isOnline(view.getContext())) {
                    Toast.makeText(view.getContext(), "Cancelando descarga...", Toast.LENGTH_SHORT).show();
                    holder.Descargar.setVisibility(View.VISIBLE);
                    holder.TamañoArchivo.setVisibility(View.VISIBLE);
                    holder.Cancelar.setVisibility(View.GONE);
                    holder.TamañoArchivoCancelar.setVisibility(View.GONE);
                    holder.BarDescarga.setVisibility(View.GONE);
                    holder.estadoDescargaFallida.setVisibility(View.VISIBLE);
                    holder.estadoDescargaCompleta.setVisibility(View.GONE);
                    descargarArchivos[i].cancel(true);
                }
                else Toast.makeText(view.getContext(), "Tenga mas cuidado al cancelar las descargas sin internet", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return archivos.size();
    }

    static class ArchivosViewHolder extends RecyclerView.ViewHolder{
        CardView CardView;

        TextView estadoDescargaFallida;
        TextView estadoDescargaCompleta;

        TextView NombreArchivo;
        TextView FechaDelArchivo;

        TextView TamañoArchivo;
        TextView TamañoArchivoCancelar;

        Button Descargar;
        Button Cancelar;

        ProgressBar BarDescarga;

        ArchivosViewHolder(View itemView) {
            super(itemView);
            CardView = (CardView)itemView.findViewById(R.id.card_layout);
            estadoDescargaFallida = (TextView)itemView.findViewById(R.id.TextoDescargaFallida);
            estadoDescargaCompleta = (TextView) itemView.findViewById(R.id.TextoDescargaCompleta);
            NombreArchivo = (TextView)itemView.findViewById(R.id.NombreDelArchivo);
            FechaDelArchivo = (TextView)itemView.findViewById(R.id.Fecha);
            TamañoArchivo = (TextView)itemView.findViewById(R.id.TamañoDelArchivosinDescargar);
            TamañoArchivoCancelar = (TextView) itemView.findViewById(R.id.TamañoDelArchivoDescargando);
            Descargar = (Button) itemView.findViewById(R.id.Descargar);
            Cancelar = (Button) itemView.findViewById(R.id.Cancelar);
            BarDescarga = (ProgressBar) itemView.findViewById(R.id.BarDescarga);
        }
    }
}
