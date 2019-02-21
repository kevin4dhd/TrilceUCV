package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Asig_matriculadas;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.company.proyect.kevinpiazzoli.trilceucv.MisAsignaturas.MisAsignaturas_Activity;
import com.company.proyect.kevinpiazzoli.trilceucv.R;

import java.util.List;

/**
 * Created by KevinPiazzoli on 01/11/2016.
 */

public class Asignaturas_Adapter extends RecyclerView.Adapter<Asignaturas_Adapter.ViewHolder> {

    private List<Subdatos_Asignaturas_Matriculadas> array_datos;
    private Context context;

    Asignaturas_Adapter(List<Subdatos_Asignaturas_Matriculadas> array_datos, Context context){
        this.array_datos=array_datos;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_asignaturas,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if(array_datos.get(holder.getAdapterPosition()).isHeader()){

            holder.Cardview.setCardBackgroundColor(
                    array_datos.get(position).isFeriado() ?
                            ContextCompat.getColor(context, R.color.md_red_500) :
                            ContextCompat.getColor(context, R.color.md_blue_500));

            holder.CardFecha.setVisibility(View.VISIBLE);
            holder.CardInfo.setVisibility(View.GONE);
            holder.Dia.setText(array_datos.get(holder.getAdapterPosition()).getDia());
            holder.ProxClase.setText(array_datos.get(holder.getAdapterPosition()).getFechaProx());
            holder.Cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {}
            });
        }
        else {
            holder.CardInfo.setVisibility(View.VISIBLE);
            holder.CardFecha.setVisibility(View.GONE);
            holder.Cardview.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
            holder.Cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MisAsignaturas_Activity.class);
                    intent.putExtra("key_curso", array_datos.get(holder.getAdapterPosition()).getCodigoDelCurso());
                    String Hora[] = array_datos.get(holder.getAdapterPosition()).getHora().split("-");
                    intent.putExtra("hora_ingreso",Hora[0]);
                    intent.putExtra("hora_salida",Hora[1]);
                    intent.putExtra("week_day",array_datos.get(position).getWeekendDay());
                    view.getContext().startActivity(intent);
                }
            });

            holder.Curso.setText(array_datos.get(holder.getAdapterPosition()).getCurso());
            holder.Docente.setText(array_datos.get(holder.getAdapterPosition()).getDocente());
            holder.Ciclo.setText(array_datos.get(holder.getAdapterPosition()).getCiclo());
            holder.Hora.setText(array_datos.get(holder.getAdapterPosition()).getHora());
            holder.Aula.setText(String.format(context.getString(R.string.aula), array_datos.get(holder.getAdapterPosition()).getAula()));
        }
    }

    @Override
    public int getItemCount() {
        return array_datos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView Cardview;

        LinearLayout CardInfo;
        LinearLayout CardFecha;

        TextView Curso;
        TextView Docente;
        TextView Ciclo;
        TextView Hora;
        TextView Aula;
        TextView Dia;
        TextView ProxClase;

        ViewHolder(View itemView) {
            super(itemView);
            Cardview = (CardView)itemView.findViewById(R.id.card_layout);
            CardInfo = (LinearLayout) itemView.findViewById(R.id.CardDetallesCurso);
            CardFecha = (LinearLayout) itemView.findViewById(R.id.CardFecha);
            Curso = (TextView)itemView.findViewById(R.id.Curso);
            Docente = (TextView) itemView.findViewById(R.id.Docente);
            Ciclo = (TextView)itemView.findViewById(R.id.Ciclo);
            Hora = (TextView) itemView.findViewById(R.id.Hora);
            Aula = (TextView) itemView.findViewById(R.id.Aula);
            Dia = (TextView) itemView.findViewById(R.id.Dia);
            ProxClase = (TextView) itemView.findViewById(R.id.FechaDia);
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
