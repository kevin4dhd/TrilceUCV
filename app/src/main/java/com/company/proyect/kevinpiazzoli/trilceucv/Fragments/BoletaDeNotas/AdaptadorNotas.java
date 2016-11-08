package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.BoletaDeNotas;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.GuardarDatos;
import com.company.proyect.kevinpiazzoli.trilceucv.R;

import java.util.List;

/**
 * Created by KevinPiazzoli on 05/11/2016.
 */

public class AdaptadorNotas extends RecyclerView.Adapter<AdaptadorNotas.ViewHolder> {

    List<Notas> notas;

    AdaptadorNotas(List<Notas> notas){
        this.notas = notas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        int StylePosition = GuardarDatos.CargarTablas((Activity) parent.getContext());
        switch(StylePosition) {
            case 1:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tabla, parent, false);
                break;
            case 2:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_1, parent, false);
                break;
            case 3:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_2, parent, false);
                break;
            case 4:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_3, parent, false);
                break;
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tabla, parent, false);
                break;
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.Titulo.setText(notas.get(position).Titulo);
        holder.Subdato.setText(notas.get(position).Subdato);
        holder.Dato.setText(notas.get(position).Dato);
    }

    @Override
    public int getItemCount() {
        return notas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        TextView Titulo;
        TextView Subdato;
        TextView Dato;

        public ViewHolder(View v) {
            super(v);
            Titulo = (TextView) v.findViewById(R.id.Titulo);
            Subdato = (TextView) v.findViewById(R.id.dato1);
            Dato = (TextView) v.findViewById(R.id.dato2);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
