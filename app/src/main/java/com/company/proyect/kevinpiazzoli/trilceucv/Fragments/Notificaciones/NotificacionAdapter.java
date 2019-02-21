package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Notificaciones;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.company.proyect.kevinpiazzoli.trilceucv.ConexionInternet.DetectarInternet;
import com.company.proyect.kevinpiazzoli.trilceucv.ConexionInternet.VolleyS;
import com.company.proyect.kevinpiazzoli.trilceucv.R;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by KevinPiazzoli on 10/11/2016.
 */

public class NotificacionAdapter extends RecyclerView.Adapter<NotificacionAdapter.NotificacionViewHolder>{

    List<DatosNotificaciones> Datos;
    String IP = "https://quiet-cove-93000.herokuapp.com/toque-button/";
    private VolleyS volley;
    protected RequestQueue fRequestQueue;

    NotificacionAdapter(List<DatosNotificaciones> archivos){
        this.Datos = archivos;
    }

    @Override
    public NotificacionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_notificaciones_chat, parent, false);
        return new NotificacionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NotificacionViewHolder holder, final int position) {
        holder.Nombre.setText(Datos.get(position).Nombre);
        holder.Carrera.setText(Datos.get(position).Carrera);
        holder.Foto.setImageResource(Datos.get(position).Foto);

        holder.EnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Toast.makeText(view.getContext(),"true",Toast.LENGTH_SHORT).show();

                if(DetectarInternet.isOnline(view.getContext())) {
                    IP = IP + Datos.get(position).Token_Device + "/" + Datos.get(position).MiNombre;
                    IP = IP.replaceAll(" ","%20");
                    volley = VolleyS.getInstance(view.getContext().getApplicationContext());
                    fRequestQueue = volley.getRequestQueue();
                    makeRequest(fRequestQueue,view.getContext(),volley,IP);
                    IP = "https://quiet-cove-93000.herokuapp.com/toque-button/";
                }
                else{
                    Toast.makeText(view.getContext(),view.getContext().getString(R.string.necesita_estar_conectado_a_internet),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void makeRequest(RequestQueue fRequestQueue, final Context context, VolleyS volley, String IP){
        JsonObjectRequest request = new JsonObjectRequest(IP, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Toast.makeText(context,"Mensaje enviado correctamente",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context,"Error al conectarse con la base de datos",Toast.LENGTH_SHORT).show();
            }
        });
        VolleyS.addToQueue(request,fRequestQueue,context,volley);
    }

    @Override
    public int getItemCount() {
        return Datos.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    static class NotificacionViewHolder extends RecyclerView.ViewHolder{
        android.support.v7.widget.CardView cardView;

        TextView Nombre;
        TextView Carrera;
        ImageView Foto;
        FloatingActionButton EnviarMensaje;

        NotificacionViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.card_layout);
            Nombre = (TextView)itemView.findViewById(R.id.Usuario);
            Carrera = (TextView) itemView.findViewById(R.id.Carrera);
            Foto = (ImageView) itemView.findViewById(R.id.img);
            EnviarMensaje = (FloatingActionButton) itemView.findViewById(R.id.EnviarMensaje);
        }
    }
}
