package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.VerNotificaciones;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.company.proyect.kevinpiazzoli.trilceucv.R;

/**
 * Created by KevinPiazzoli on 08/12/2016.
 */

public class DialogFragmentNotificacionDetalle extends DialogFragment {

    private TextView Titulo;
    private TextView Descripcion;
    private TextView Fecha;
    private Button Cerrar;

    private String sTitulo;
    private String sDescripcion;
    private String sFecha;

    public static DialogFragmentNotificacionDetalle newInstance(String sTitulo, String sDescripcion, String sFecha) {
        DialogFragmentNotificacionDetalle f = new DialogFragmentNotificacionDetalle();
        Bundle args = new Bundle();
        args.putString("title", sTitulo);
        args.putString("description", sDescripcion);
        args.putString("fecha", sFecha);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sTitulo = getArguments().getString("title");
        sDescripcion = getArguments().getString("description");
        sFecha = getArguments().getString("fecha");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.fragment_view_notification, container, false);

        getDialog().setTitle("Detalles");

        Titulo = (TextView) dialogView.findViewById(R.id.Titulo);
        Descripcion = (TextView) dialogView.findViewById(R.id.Descripcion);
        Fecha = (TextView)dialogView.findViewById(R.id.Fecha);
        Cerrar = (Button)dialogView.findViewById(R.id.Cerrar);

        Titulo.setText(sTitulo);
        Descripcion.setText(sDescripcion);
        Fecha.setText(sFecha);

        Cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return dialogView;
    }

}
