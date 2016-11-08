package com.company.proyect.kevinpiazzoli.trilceucv.Tablas;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.GuardarDatos;
import com.company.proyect.kevinpiazzoli.trilceucv.R;

import java.util.ArrayList;

/**
 * Created by KevinPiazzoli on 28/10/2016.
 */

public class DatosAdapter extends ArrayAdapter<Tabla_datos> {

    private final Context contexto;
    private final ArrayList<Tabla_datos> array_datos;

    public DatosAdapter(Context context, ArrayList<Tabla_datos> array_datos) {
        super(context, -1, array_datos);
        this.contexto = context;
        this.array_datos = array_datos;
    }

    private class ViewHolder {
        TextView Titulo;
        TextView Subdato;
        TextView Dato;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            int StylePosition = GuardarDatos.CargarTablas((Activity) contexto);
            switch(StylePosition) {
                case 1:
                    vi = inflater.inflate(R.layout.list_item_tabla, parent, false);
                    break;
                case 2:
                    vi = inflater.inflate(R.layout.list_item_item_1, parent, false);
                    break;
                case 3:
                    vi = inflater.inflate(R.layout.list_item_item_2, parent, false);
                    break;
                case 4:
                    vi = inflater.inflate(R.layout.list_item_item_3, parent, false);
                    break;
                default:
                    vi = inflater.inflate(R.layout.list_item_tabla, parent, false);
                    break;
            }

            holder = new ViewHolder();
            holder.Titulo = (TextView) vi.findViewById(R.id.Titulo);
            holder.Subdato  = (TextView) vi.findViewById(R.id.dato1);
            holder.Dato  = (TextView) vi.findViewById(R.id.dato2);

            vi.setTag(holder);

        } else
            holder = (ViewHolder) vi.getTag();

        Tabla_datos personaActual = array_datos.get(position);

        holder.Titulo.setText(personaActual.getTitulo());
        holder.Subdato.setText(personaActual.getDato1());
        holder.Dato.setText(personaActual.getDato2());
        return vi;
    }
}
