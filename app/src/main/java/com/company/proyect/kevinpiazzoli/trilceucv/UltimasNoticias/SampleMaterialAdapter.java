package com.company.proyect.kevinpiazzoli.trilceucv.UltimasNoticias;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Comunicador;
import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Noticias;
import com.company.proyect.kevinpiazzoli.trilceucv.R;
import java.util.ArrayList;

public class SampleMaterialAdapter extends RecyclerView.Adapter<SampleMaterialAdapter.ViewHolder> {
    private Context context;
    private Comunicador comunicacion;
    private ArrayList<Card> cardsList;
    private Noticias noticias;

    public SampleMaterialAdapter(Context context, ArrayList<Card> cardsList,Comunicador comunicacion,Noticias noticias) {
        this.comunicacion=comunicacion;
        this.context = context;
        this.cardsList = cardsList;
        this.noticias=noticias;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        String Titulo = cardsList.get(position).getTitulo();
        String Descripcion = cardsList.get(position).getDescripcion();
        String Fecha = cardsList.get(position).getFecha();

        TextView textView_Titulo = viewHolder.Titulotx;
        TextView textView_Descripcion = viewHolder.Descripciontx;
        TextView textView_Fecha = viewHolder.Fechatx;

        textView_Titulo.setText(Titulo);
        textView_Descripcion.setText(Descripcion);
        textView_Fecha.setText(Fecha);
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder viewHolder) {
        super.onViewDetachedFromWindow(viewHolder);
        viewHolder.itemView.clearAnimation();
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder viewHolder) {
        super.onViewAttachedToWindow(viewHolder);
    }

    @Override
    public int getItemCount() {
        if (cardsList.isEmpty()) {
            return 0;
        } else {
            return cardsList.size();
        }
    }

    @Override
    public long getItemId(int position) {
        return cardsList.get(position).getId();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater li = LayoutInflater.from(viewGroup.getContext());
        View v = li.inflate(R.layout.card_view_holder, viewGroup, false);
        return new ViewHolder(v);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView Titulotx;
        private TextView Descripciontx;
        private TextView Fechatx;
        private Button deleteButton;

        public ViewHolder(View v) {
            super(v);
            Titulotx = (TextView) v.findViewById(R.id.Titulo);
            Descripciontx = (TextView) v.findViewById(R.id.Descripcion);
            Fechatx = (TextView) v.findViewById(R.id.Fecha);
            deleteButton = (Button) v.findViewById(R.id.button);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    comunicacion.EntrarALaNoticia(
                            noticias.getTitulo(getAdapterPosition()),
                            noticias.getDescripcion(getAdapterPosition()),
                            noticias.getFecha(getAdapterPosition()));
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int requestCode = getAdapterPosition();
                    Log.e("hola",""+requestCode);
                }
            });
        }
    }
}
