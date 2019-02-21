package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.VerNotificaciones;

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
 * Created by KevinPiazzoli on 29/11/2016.
 */

public class AdapterVerNotificaciones extends RecyclerView.Adapter<AdapterVerNotificaciones.NotificacionesViewHolder>{

    private List<VerNotificacionesData> DataNotificaciones;
    private Fragment_Notificaciones context;

    AdapterVerNotificaciones(List<VerNotificacionesData> archivos, Fragment_Notificaciones context){
        this.DataNotificaciones = archivos;
        this.context=context;
    }

    @Override
    public NotificacionesViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_ver_notificaciones, parent, false);
        return new NotificacionesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final NotificacionesViewHolder holder, final int i) {
        holder.Titulo.setText(DataNotificaciones.get(i).getTitulo());
        holder.Descripcion.setText(DataNotificaciones.get(i).getDescripcion());
        holder.Fecha.setText(DataNotificaciones.get(i).getFecha());
        holder.VerDetalles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragmentNotificacionDetalle dialog =
                        DialogFragmentNotificacionDetalle.newInstance(
                                DataNotificaciones.get(i).getTitulo(),
                                DataNotificaciones.get(i).getDescripcion(),
                                DataNotificaciones.get(i).getFecha());
                dialog.show(context.getFragmentManager(), "dialog");
            }
        });
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return DataNotificaciones.size();
    }

    static class NotificacionesViewHolder extends RecyclerView.ViewHolder{
        CardView Cardview;

        TextView Titulo;
        TextView Descripcion;
        TextView Fecha;

        Button VerDetalles;

        NotificacionesViewHolder(View itemView) {
            super(itemView);
            Cardview = (CardView)itemView.findViewById(R.id.card_layout);
            Titulo = (TextView)itemView.findViewById(R.id.Titulo);
            Descripcion = (TextView) itemView.findViewById(R.id.Descripcion);
            Fecha = (TextView)itemView.findViewById(R.id.Fecha);
            VerDetalles = (Button) itemView.findViewById(R.id.button);
        }

        public CardView getCardview() {
            return Cardview;
        }
    }

    public void Actualizar(){
        ActualizarMensaje();
        notifyDataSetChanged();
    }

    void dismissTVShow(int pos) {
        DataNotificaciones.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, getItemCount());
        ActualizarMensaje();
    }

    private void ActualizarMensaje(){
        if(DataNotificaciones.size()>0) context.VisualizacionMensaje(true);
        else context.VisualizacionMensaje(false);
    }

    void DeleteAll(){
        int limite = DataNotificaciones.size();
        for(int i=0;i<limite;i++){
            DataNotificaciones.remove(0);
            notifyItemRemoved(0);
            notifyItemRangeChanged(0, getItemCount());
        }
        ActualizarMensaje();
    }
}
