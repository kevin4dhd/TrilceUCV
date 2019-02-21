package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.PAmigos;

/**
 * Created by KevinPiazzoli on 27/11/2016.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by KevinPiazzoli on 13/11/2016.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchAdapterHolder> implements Filterable {

    ArrayList<DatosAmigos> Datos;
    List<List<String>> NuevosAmigos;
    CustomFilter filter;
    fragment_view_search amigos;

    private static final String IP = "http://kpfpservice.000webhostapp.com/";
    private static final String MANDAR_SOLICITUD =      IP + "Amigos_MANDAR_SOLICITUD.php";
    private static final String CANCELAR_SOLICITUD_EMISOR =    IP + "Amigos_CANCELAR_SOLICITUD_EMISOR.php";
    private static final String CANCELAR_SOLICITUD_RECEPTOR =    IP + "Amigos_CANCELAR_SOLICITUD_RECEPTOR.php";
    private static final String ACEPTAR_SOLICITUD =     IP + "Amigos_ACEPTAR_SOLICITUD.php";
    private static String APP_DIRECTORY = "TrilceUCV/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "Fotos";
    private String mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
            + File.separator;

    SearchAdapter(ArrayList<DatosAmigos> datos, List<List<String>> NuevosAmigos, fragment_view_search amigos) {
        this.NuevosAmigos=NuevosAmigos;
        Datos = datos;
        this.amigos=amigos;
    }

    @Override
    public SearchAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_amigos_search, parent, false);
        return new SearchAdapterHolder(v);
    }

    @Override
    public void onBindViewHolder(final SearchAdapterHolder holder, final int position){
        Picasso.with(amigos.getContext())
                .load("http://kpfpservice.000webhostapp.com/img/"+Datos.get(position).user+".png")
                .error(R.drawable.ic_contact_icon)
                .into(holder.imageView);
        holder.Nombre.setText(Datos.get(position).Nombre + " "+ Datos.get(position).Apellido);

        switch(Datos.get(position).Amigo){
            case "pendiente_emisor":
                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(amigos.getContext(), R.color.md_orange_400));
                holder.Nombre.setTextColor(ContextCompat.getColor(amigos.getContext(), R.color.md_text_white));
                holder.estadoAmigos.setTextColor(ContextCompat.getColor(amigos.getContext(), R.color.md_text_white));
                holder.estadoAmigos.setText(R.string.solicitud_pendiente);
                holder.AgregarAmigos.setText(R.string.cancelar_solicitud);
                holder.AgregarAmigos.setVisibility(View.VISIBLE);
                holder.CancelarSolicitud.setVisibility(View.GONE);
                holder.AgregarAmigos.setTextColor(ContextCompat.getColor(amigos.getContext(), R.color.md_red_900));
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImprimirTexto(view.getContext(),
                                view.getContext().getString(R.string.no_puedes_chatear_con_esta_persona));
                    }
                });
                holder.AgregarAmigos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        new AlertDialog.Builder(v.getContext())
                                .setMessage(R.string.estas_seguro_de_cancelar_la_solicitud)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                ImprimirTexto(v.getContext(),amigos.getContext().getString(R.string.cancelando_solicitud));
                                                MandarDatosVolley(
                                                        amigos.fRequestQueue,
                                                        amigos,
                                                        amigos.getVolley(),
                                                        CANCELAR_SOLICITUD_EMISOR,
                                                        GuardarDatos.CargarUsuario(v.getContext()),
                                                        Datos.get(position).user,
                                                        holder.AgregarAmigos);
                                                Datos.remove(position);
                                                notifyDataSetChanged();
                                            }
                                        })
                                .setNegativeButton(
                                        amigos.getContext().getString(R.string.cancelar),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        }).show();
                    }
                });
                break;

            case "pendiente_receptor":
                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(amigos.getContext(), R.color.md_orange_400));
                holder.estadoAmigos.setText(Datos.get(position).Nombre + amigos.getContext().getString(R.string.quiere_ser_tu_amigo));
                holder.Nombre.setTextColor(ContextCompat.getColor(amigos.getContext(), R.color.md_text_white));
                holder.estadoAmigos.setTextColor(ContextCompat.getColor(amigos.getContext(), R.color.md_text_white));
                holder.CancelarSolicitud.setText(amigos.getContext().getString(R.string.cancelar));
                holder.CancelarSolicitud.setTextColor(ContextCompat.getColor(amigos.getContext(), R.color.md_red_900));
                holder.CancelarSolicitud.setVisibility(View.VISIBLE);
                holder.AgregarAmigos.setText(amigos.getContext().getString(R.string.Aceptar));
                holder.AgregarAmigos.setVisibility(View.VISIBLE);
                holder.AgregarAmigos.setTextColor(ContextCompat.getColor(amigos.getContext(), R.color.md_blue_500));
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImprimirTexto(view.getContext(),
                                view.getContext().getString(R.string.No_puedes_chatear_con_esta_persona_hasta_que_sean_amigos));
                    }
                });
                holder.AgregarAmigos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.AgregarAmigos.setEnabled(false);
                        ImprimirTexto(view.getContext(),view.getContext().getString(R.string.añadiendo_a_tu_lista_de_amigos));
                        MandarDatosVolley(
                                amigos.fRequestQueue,
                                amigos,
                                amigos.getVolley(),
                                ACEPTAR_SOLICITUD,
                                GuardarDatos.CargarUsuario(view.getContext()),
                                Datos.get(position).user,
                                holder.AgregarAmigos);
                    }
                });

                holder.CancelarSolicitud.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.AgregarAmigos.setEnabled(false);
                        ImprimirTexto(view.getContext(),amigos.getContext().getString(R.string.cancelando_solicitud));
                        MandarDatosVolley(
                                amigos.fRequestQueue,
                                amigos,
                                amigos.getVolley(),
                                CANCELAR_SOLICITUD_RECEPTOR,
                                GuardarDatos.CargarUsuario(view.getContext()),
                                Datos.get(position).user,
                                holder.AgregarAmigos);
                    }
                });

                break;

            default:
                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(amigos.getContext(), R.color.md_white_1000));
                holder.Nombre.setTextColor(ContextCompat.getColor(amigos.getContext(), R.color.md_black_1000));
                holder.estadoAmigos.setTextColor(ContextCompat.getColor(amigos.getContext(), R.color.md_red_500));
                holder.AgregarAmigos.setTextColor(ContextCompat.getColor(amigos.getContext(), R.color.md_blue_500));
                holder.estadoAmigos.setText(R.string.no_son_amigos);
                holder.estadoAmigos.setTextColor(Color.RED);
                holder.AgregarAmigos.setText(R.string.agregar_a_amigos);
                holder.AgregarAmigos.setTextColor(Color.GREEN);
                holder.AgregarAmigos.setVisibility(View.VISIBLE);
                holder.CancelarSolicitud.setVisibility(View.GONE);

                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImprimirTexto(view.getContext(),amigos.getContext().getString(R.string.no_puedes_chatear_con_esa_persona_madar_solicitud_de_amistad));
                    }
                });

                holder.AgregarAmigos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.AgregarAmigos.setEnabled(false);
                        ImprimirTexto(v.getContext(),v.getContext().getString(R.string.enviando_solicitud_de_amistad));
                        MandarDatosVolley(
                                amigos.fRequestQueue,
                                amigos,
                                amigos.getVolley(),
                                MANDAR_SOLICITUD,
                                GuardarDatos.CargarUsuario(v.getContext()),
                                Datos.get(position).user,
                                holder.AgregarAmigos);
                    }
                });
                break;
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ViewImage.class);
                intent.putExtra("key_bitmap", mPath + Datos.get(position).user + ".jpg");
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

    @Override
    public Filter getFilter() {
        if(filter==null)filter = new CustomFilter(NuevosAmigos,this,Datos);
        else filter.ActualizarFiltro(NuevosAmigos,this,Datos);
        return filter;
    }

    static class SearchAdapterHolder extends RecyclerView.ViewHolder{
        android.support.v7.widget.CardView cardView;
        TextView Nombre;
        TextView estadoAmigos;
        ImageView imageView;
        Button AgregarAmigos;
        Button CancelarSolicitud;
        SearchAdapterHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.card_layout);
            imageView = (ImageView) itemView.findViewById(R.id.circle_image);
            Nombre = (TextView)itemView.findViewById(R.id.textViewNombre);
            estadoAmigos = (TextView) itemView.findViewById(R.id.textViewEstadoAmigos);
            AgregarAmigos=(Button) itemView.findViewById(R.id.SolicitudDeAmistad);
            CancelarSolicitud = (Button) itemView.findViewById(R.id.CancelarSolicitud);
        }
    }

    private void MandarDatosVolley(
            final RequestQueue fRequestQueue,
            final Fragment fragment,
            final VolleyS volley,
            final String IP,
            String emisor,
            String receptor,
            final Button button){
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
                        switch (IP){
                            case MANDAR_SOLICITUD:
                                button.setEnabled(true);
                                break;
                            case CANCELAR_SOLICITUD_EMISOR:

                                break;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ImprimirTexto(fragment.getContext(),amigos.getContext().getString(R.string.ocurrio_un_error));
                        button.setEnabled(true);
                    }
                });
        VolleyS.addToQueue(jsArrayRequest,fRequestQueue,fragment,volley);
    }

    private void ImprimirTexto(Context context, String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }

    void setNuevosAmigos(List<List<String>> nuevosAmigos) {
        NuevosAmigos = nuevosAmigos;
        notifyDataSetChanged();
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
