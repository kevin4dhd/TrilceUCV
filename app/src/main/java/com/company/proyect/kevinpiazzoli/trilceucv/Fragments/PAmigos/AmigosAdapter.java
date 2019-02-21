package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.PAmigos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.company.proyect.kevinpiazzoli.trilceucv.ActivitysPrincipales.ViewImage;
import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.GuardarDatos;
import com.company.proyect.kevinpiazzoli.trilceucv.ConexionInternet.VolleyS;
import com.company.proyect.kevinpiazzoli.trilceucv.R;
import com.company.proyect.kevinpiazzoli.trilceucv.TrilceChat.Chat;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by KevinPiazzoli on 13/11/2016.
 */

public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.AmigosViewHolder>{

    ArrayList<DatosAmigos> Datos;
    fragment_view_amigos amigos;

    private static final String IP = "http://kpfpservice.000webhostapp.com/";
    private static final String ELIMINAR =              IP + "Amigos_DELETE.php";
    private static String APP_DIRECTORY = "TrilceUCV/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "Fotos";
    private String mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
            + File.separator;

    AmigosAdapter(ArrayList<DatosAmigos> datos, fragment_view_amigos amigos) {
        Datos = datos;
        this.amigos=amigos;
    }

    @Override
    public AmigosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_amigos_chat, parent, false);
        return new AmigosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AmigosViewHolder holder, final int position){
        Picasso.with(amigos.getContext())
                .load("http://kpfpservice.000webhostapp.com/img/"+Datos.get(position).user+".png").
                memoryPolicy(MemoryPolicy.NO_CACHE )
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .error(R.drawable.ic_contact_icon)
                .into(holder.imageView);

        holder.Nombre.setText(Datos.get(position).Nombre + " "+ Datos.get(position).Apellido);
        holder.estadoAmigos.setText("últ. vez hoy a las 12:00 A.m.");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Chat.class);
                intent.putExtra("key_user", Datos.get(position).user);
                intent.putExtra("Name", Datos.get(position).Nombre);
                view.getContext().startActivity(intent);
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                new AlertDialog.Builder(v.getContext())
                        .setMessage(v.getContext().getString(R.string.esta_apunto_de_eliminar_a)+Datos.get(position).Nombre+
                                v.getContext().getString(R.string.de_tu_lista_de_amigos_estas_seguro))
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        ImprimirTexto(v.getContext(),v.getContext().getString(R.string.eliminando));
                                        MandarDatosVolley(
                                                amigos.fRequestQueue,
                                                amigos,
                                                amigos.getVolley(),
                                                ELIMINAR,
                                                GuardarDatos.CargarUsuario(v.getContext()),
                                                Datos.get(position).user);
                                            }
                                        })
                                .setNegativeButton(
                                        v.getContext().getString(R.string.cancelar),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        }).show();
                        return true;
                    }
                });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ViewImage.class);
                intent.putExtra("key_bitmap", mPath + Datos.get(position).user + ".jpg");
                intent.putExtra("user",Datos.get(position).user);
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return Datos.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    static class AmigosViewHolder extends RecyclerView.ViewHolder{
        android.support.v7.widget.CardView cardView;
        TextView Nombre;
        TextView estadoAmigos;
        ImageView imageView;
        AmigosViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.card_layout);
            imageView = (ImageView) itemView.findViewById(R.id.circle_image);
            Nombre = (TextView)itemView.findViewById(R.id.textViewNombre);
            estadoAmigos = (TextView) itemView.findViewById(R.id.textViewEstadoAmigos);
        }
    }

    private void MandarDatosVolley(
            final RequestQueue fRequestQueue,
            final Fragment fragment,
            final VolleyS volley,
            final String IP,
            String emisor,
            String receptor){
        HashMap<String, String> parametros = new HashMap();
        parametros.put("emisor", emisor);
        parametros.put("receptor", receptor);
        parametros.put("Name_Emisor", amigos.NAME_USER);

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                IP,
                new JSONObject(parametros),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        amigos.makeRequest(fRequestQueue,fragment,volley,amigos.IP);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ImprimirTexto(fragment.getContext(),amigos.getContext().getString(R.string.formula));
                    }
                });
        VolleyS.addToQueue(jsArrayRequest,fRequestQueue,fragment,volley);
    }

    private void ImprimirTexto(Context context, String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }

    void setDatos(ArrayList<DatosAmigos> datos) {
        Datos = datos;
    }

    void Actualizar(){
        if(Datos.size()>0) amigos.VisualizarMensaje(true);
        else amigos.VisualizarMensaje(false);
        notifyDataSetChanged();
    }

}
