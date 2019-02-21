package com.company.proyect.kevinpiazzoli.trilceucv.MisAsignaturas.Asistencias;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.BaseDeDatosUCV;
import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.GuardarDatos;
import com.company.proyect.kevinpiazzoli.trilceucv.R;

import java.util.List;

/**
 * Created by KevinPiazzoli on 03/12/2016.
 */

public class fragment_Asistencias extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private List<Asistencias> asistencias;
    private AsistenciasAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rv;
    private String IP;
    private BaseDeDatosUCV UCVdb;

    public static fragment_Asistencias newInstance() {
        return new fragment_Asistencias();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asistencias, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeMovieHits);
        swipeRefreshLayout.setOnRefreshListener(this);
        IP ="http://kpfpservice.000webhostapp.com/UCV_datos_usuarios_GETALL.php"+"?id="+ GuardarDatos.CargarUsuario(getActivity());

        rv = (RecyclerView) view.findViewById(R.id.recicleView);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        Intent extras = getActivity().getIntent();
        Bundle bundle = extras.getExtras();

        if(bundle!=null){
            UCVdb = new BaseDeDatosUCV(getActivity());
            String CodigoCurso = bundle.getString("key_curso");
            String Hora_Ingreso = bundle.getString("hora_ingreso");
            String Hora_Salida = bundle.getString("hora_salida");
            int week_day = Integer.parseInt(bundle.getString("week_day"));
            asistencias = UCVdb.ObtenerAsistencias(CodigoCurso,Hora_Ingreso,Hora_Salida,week_day, view.getContext());
        }

        adapter = new AsistenciasAdapter(asistencias,getContext());
        rv.setAdapter(adapter);

        AsistenciaTabla(view);

        return view;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    private void AsistenciaTabla(View view){
        TextView A = (TextView) view.findViewById(R.id.Asistencia);
        TextView T = (TextView) view.findViewById(R.id.Tardanza);
        TextView F = (TextView) view.findViewById(R.id.Falta);
        TextView TJ = (TextView) view.findViewById(R.id.TardanzaJustificada);
        TextView N = (TextView) view.findViewById(R.id.NoDefinida);
        TextView IP = (TextView) view.findViewById(R.id.InasistenciasPermitidas);
        TextView E = (TextView) view.findViewById(R.id.EstadoHoI);

        int nA = 0;
        int nT = 0;
        int nF = 0;
        int nTJ = 0;
        int nN = 0;
        int nTP;

        for(int i=0;i<asistencias.size();i++){
            switch (asistencias.get(i).getEstado()){
                case "A":
                    nA++;
                    break;
                case "T":
                    nT++;
                    break;
                case "F":
                    nF++;
                    break;
                case "TJ":
                    nTJ++;
                    break;
                case "N":
                    nN++;
                    break;
            }
        }

        A.setText(Integer.toString(nA));
        T.setText(Integer.toString(nT));
        F.setText(Integer.toString(nF));
        TJ.setText(Integer.toString(nTJ));
        N.setText(Integer.toString(nN));
        nTP = 6-(nF+(nT/4));
        if(nTP>0) {
            IP.setText(Integer.toString(nTP-1));
            E.setText("Habilitado");
            if(nTP<3) E.setTextColor(ContextCompat.getColor(getContext(), R.color.md_orange_500));
            else E.setTextColor(ContextCompat.getColor(getContext(), R.color.md_green_500));
        }
        else{
            IP.setText("0");
            E.setText("Inhabilitado");
            E.setTextColor(ContextCompat.getColor(getContext(), R.color.md_red_500));
        }

    }



}
