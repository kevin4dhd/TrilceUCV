package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Asig_matriculadas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.company.proyect.kevinpiazzoli.trilceucv.R;

import java.util.ArrayList;

/**
 * Created by KevinPiazzoli on 01/11/2016.
 */

public class Asignaturas_Adapter extends ArrayAdapter<Subdatos_Asignaturas_Matriculadas> {

    private final Context contexto;
    private final ArrayList<Subdatos_Asignaturas_Matriculadas> array_datos;

    public Asignaturas_Adapter(Context context,ArrayList<Subdatos_Asignaturas_Matriculadas> array_datos){
        super(context, -1, array_datos);
        this.contexto=context;
        this.array_datos=array_datos;
    }

    private class ViewHolder {
        TextView Curso;
        TextView Docente;
        TextView Ciclo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null){
            vi = inflater.inflate(R.layout.list_view_asignaturas_items, parent, false);
            holder = new ViewHolder();
            holder.Curso = (TextView) vi.findViewById(R.id.Curso);
            holder.Docente  = (TextView) vi.findViewById(R.id.Docente);
            holder.Ciclo  = (TextView) vi.findViewById(R.id.Ciclo);
            vi.setTag(holder);
        }
        else holder = (ViewHolder) vi.getTag();

        Subdatos_Asignaturas_Matriculadas personaActual = array_datos.get(position);

        holder.Curso.setText(personaActual.getCurso());
        holder.Docente.setText(personaActual.getDocente());
        holder.Ciclo.setText(personaActual.getCiclo());
        return vi;
    }
}
