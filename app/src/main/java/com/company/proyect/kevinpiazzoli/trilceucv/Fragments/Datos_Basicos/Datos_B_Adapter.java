package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Datos_Basicos;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.GuardarDatos;
import com.company.proyect.kevinpiazzoli.trilceucv.R;

import java.util.List;

/**
 * Created by KevinPiazzoli on 08/12/2016.
 */

public class Datos_B_Adapter extends RecyclerView.Adapter<Datos_B_Adapter.Adapter>  {

    List<Datos_B> datos_Array;
    Context context;

    public Datos_B_Adapter(List<Datos_B> datos_Array, Context context) {
        this.datos_Array = datos_Array;
        this.context = context;
    }

    @Override
    public Adapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View vi;
        int StylePosition = GuardarDatos.CargarTablas((Activity) context);
        switch(StylePosition) {
            case 1:
                vi = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tabla, parent, false);
                break;
            case 2:
                vi = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_1, parent, false);
                break;
            case 3:
                vi = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_2, parent, false);
                break;
            case 4:
                vi = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_3, parent, false);
                break;
            default:
                vi = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tabla, parent, false);
                break;
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_ver_notificaciones, parent, false);
        return new Adapter(vi);
    }

    @Override
    public void onBindViewHolder(Adapter holder, int position) {
        holder.Subdato.setText(datos_Array.get(position).getSubDato());
        holder.Dato.setText(datos_Array.get(position).getDato());
    }

    @Override
    public int getItemCount() {
        return datos_Array.size();
    }

    static class Adapter extends RecyclerView.ViewHolder{
        TextView Subdato;
        TextView Dato;

        Adapter(View itemView) {
            super(itemView);
            Subdato = (TextView) itemView.findViewById(R.id.dato1);
            Dato = (TextView)itemView.findViewById(R.id.dato2);
        }
    }

}
