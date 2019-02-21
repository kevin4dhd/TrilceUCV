package com.company.proyect.kevinpiazzoli.trilceucv.MisAsignaturas.Asistencias;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.company.proyect.kevinpiazzoli.trilceucv.R;

import java.util.List;

/**
 * Created by KevinPiazzoli on 03/12/2016.
 */

public class AsistenciasAdapter extends RecyclerView.Adapter<AsistenciasAdapter.AsistenciasHolder> {

    private List<Asistencias> array_datos;
    private Context context;

    AsistenciasAdapter(List<Asistencias> array_datos, Context context){
        this.array_datos=array_datos;
        this.context = context;
    }

    @Override
    public AsistenciasHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_asistencias,parent,false);
        return new AsistenciasHolder(view);
    }

    @Override
    public void onBindViewHolder( AsistenciasHolder holder, int position) {
        holder.Semana.setText(array_datos.get(position).getSemana());
        holder.Fecha.setText(String.format("Fecha: %s", array_datos.get(position).getFecha()));
        holder.Hora.setText(array_datos.get(position).getHoraDelCurso());
        holder.HoraDeLlegada.setText(String.format("Hora de llegada: %s", array_datos.get(position).getHoraDeLlegada()));
        holder.Justificar.setEnabled(true);
        switch(array_datos.get(position).getEstado()){
            case "A":
                holder.Cardview.setCardBackgroundColor(ContextCompat.getColor(context, R.color.md_green_500));
                holder.Semana.setTextColor(ContextCompat.getColor(context, R.color.md_white_1000));
                holder.Fecha.setTextColor(ContextCompat.getColor(context, R.color.md_white_1000));
                holder.Hora.setTextColor(ContextCompat.getColor(context, R.color.md_white_1000));
                holder.HoraDeLlegada.setTextColor(ContextCompat.getColor(context, R.color.md_white_1000));
                holder.Justificar.setEnabled(false);
                holder.Justificar.setVisibility(View.GONE);
                break;
            case "T":
                holder.Cardview.setCardBackgroundColor(ContextCompat.getColor(context, R.color.md_orange_500));
                holder.Semana.setTextColor(ContextCompat.getColor(context, R.color.md_white_1000));
                holder.Fecha.setTextColor(ContextCompat.getColor(context, R.color.md_white_1000));
                holder.Hora.setTextColor(ContextCompat.getColor(context, R.color.md_white_1000));
                holder.HoraDeLlegada.setTextColor(ContextCompat.getColor(context, R.color.md_white_1000));
                holder.Justificar.setEnabled(true);
                holder.Justificar.setVisibility(View.VISIBLE);
                holder.Justificar.setTextColor(ContextCompat.getColor(context, R.color.md_white_1000));
                break;
            case "F":
                holder.Cardview.setCardBackgroundColor(ContextCompat.getColor(context, R.color.md_red_500));
                holder.Semana.setTextColor(ContextCompat.getColor(context, R.color.md_white_1000));
                holder.Fecha.setTextColor(ContextCompat.getColor(context, R.color.md_white_1000));
                holder.Hora.setTextColor(ContextCompat.getColor(context, R.color.md_white_1000));
                holder.HoraDeLlegada.setTextColor(ContextCompat.getColor(context, R.color.md_white_1000));
                holder.Justificar.setEnabled(true);
                holder.Justificar.setVisibility(View.VISIBLE);
                holder.Justificar.setTextColor(ContextCompat.getColor(context, R.color.md_white_1000));
                break;
            case "TJ":
                holder.Cardview.setCardBackgroundColor(ContextCompat.getColor(context, R.color.md_deep_orange_500));
                holder.Semana.setTextColor(ContextCompat.getColor(context, R.color.md_white_1000));
                holder.Fecha.setTextColor(ContextCompat.getColor(context, R.color.md_white_1000));
                holder.Hora.setTextColor(ContextCompat.getColor(context, R.color.md_white_1000));
                holder.HoraDeLlegada.setTextColor(ContextCompat.getColor(context, R.color.md_white_1000));
                holder.Justificar.setEnabled(false);
                holder.Justificar.setTextColor(ContextCompat.getColor(context, R.color.md_white_1000));
                holder.Justificar.setVisibility(View.VISIBLE);
                holder.Justificar.setText("Tardanza Justificada");
                break;
            default:
                holder.Cardview.setCardBackgroundColor(ContextCompat.getColor(context, R.color.md_white_1000));
                holder.Semana.setTextColor(ContextCompat.getColor(context, R.color.md_blue_500));
                holder.Fecha.setTextColor(ContextCompat.getColor(context, R.color.md_black_1000));
                holder.Hora.setTextColor(ContextCompat.getColor(context, R.color.md_black_1000));
                holder.HoraDeLlegada.setTextColor(ContextCompat.getColor(context, R.color.md_black_1000));
                holder.Justificar.setEnabled(false);
                holder.Justificar.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return array_datos.size();
    }

    class AsistenciasHolder extends RecyclerView.ViewHolder{
        CardView Cardview;

        TextView Semana;
        TextView Fecha;
        TextView Hora;
        TextView HoraDeLlegada;
        Button Justificar;

        AsistenciasHolder(View itemView) {
            super(itemView);
            Cardview = (CardView)itemView.findViewById(R.id.card_layout);

            Semana = (TextView)itemView.findViewById(R.id.Semana);
            Fecha = (TextView) itemView.findViewById(R.id.Fecha);
            Hora = (TextView)itemView.findViewById(R.id.Hora);
            HoraDeLlegada = (TextView) itemView.findViewById(R.id.HoraDeLlegada);
            Justificar = (Button) itemView.findViewById(R.id.Justificar);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void Actualizar(){
        notifyDataSetChanged();
    }

}
