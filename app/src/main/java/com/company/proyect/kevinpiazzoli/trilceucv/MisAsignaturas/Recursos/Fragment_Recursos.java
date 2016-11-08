package com.company.proyect.kevinpiazzoli.trilceucv.MisAsignaturas.Recursos;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.BaseDeDatosUCV;
import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.GuardarDatos;
import com.company.proyect.kevinpiazzoli.trilceucv.ConexionInternet.DetectarInternet;
import com.company.proyect.kevinpiazzoli.trilceucv.ConexionInternet.VolleyS;
import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Comunicador;
import com.company.proyect.kevinpiazzoli.trilceucv.R;

import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KevinPiazzoli on 26/10/2016.
 */

public class Fragment_Recursos extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private Comunicador comunicacion;
    private List<Archivos> archivos;
    private VolleyS volley;
    private String IP;
    protected RequestQueue fRequestQueue;
    private String path= Environment.getExternalStorageDirectory()+"/TrilceUCV/PDF/";
    private String pathArchivos = Environment.getExternalStorageDirectory()+"/TrilceUCV/Archivos/";
    private String AñoSilabo = "_silabo_201602.pdf";
    private BaseDeDatosUCV UCVdb;
    private String CodigoCurso;
    private static final String URLSilabo = "http://kpfp.pe.hu/pdfs/";
    private static final String URL = "http://kpfp.pe.hu/recursos/";
    static DecimalFormat formato = new DecimalFormat("0.000");
    static DecimalFormatSymbols custom=new DecimalFormatSymbols();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rv;
    private RVArchivosAdapter adapter;

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        comunicacion=(Comunicador) getActivity();
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipeMovieHits);
        swipeRefreshLayout.setOnRefreshListener(this);
        IP ="http://kpfp.pe.hu/conectkevin/UCV_datos_usuarios_GETALL.php"+"?id="+ GuardarDatos.CargarUsuario(getActivity());

        rv = (RecyclerView)getActivity().findViewById(R.id.recicleView);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        archivos = new ArrayList<>();
        adapter = new RVArchivosAdapter(archivos);

        Intent extras = getActivity().getIntent();
        Bundle bundle = extras.getExtras();

        if(bundle!=null){
            UCVdb = new BaseDeDatosUCV(getActivity());
            CodigoCurso = bundle.getString("key_curso");
            List<Recursos> Recursos = UCVdb.ObtenerRecursos(0,CodigoCurso);

            custom.setDecimalSeparator('.');
            formato.setDecimalFormatSymbols(custom);

            File Silabo = new File(path+CodigoCurso+AñoSilabo);

            archivos.add(new Archivos(
                    Silabo.exists(),
                    "Silabo ",
                    CodigoCurso+AñoSilabo,
                    path+CodigoCurso+AñoSilabo,
                    "Semana 1",
                    formato.format( Silabo.length() / (1024.0) / 1024.0),
                    path,
                    URLSilabo+CodigoCurso+AñoSilabo,
                    ".pdf"));

            for(int i=0;i<Recursos.size();i++){
                File file = new File(pathArchivos+CodigoCurso+" "+Recursos.get(i).NombreDelArchivo+"."+Recursos.get(i).extension);
                archivos.add(new Archivos(
                        file.exists(),
                        Recursos.get(i).NombreDelArchivo,
                        CodigoCurso+" "+Recursos.get(i).NombreDelArchivo+"."+Recursos.get(i).extension,
                        pathArchivos+CodigoCurso+" "+Recursos.get(i).NombreDelArchivo+"."+Recursos.get(i).extension,
                        Recursos.get(i).SemanaVista,
                        Recursos.get(i).Tamaño,
                        pathArchivos,
                        URL+CodigoCurso+"/"+Recursos.get(i).Semana+"/"+CodigoCurso+"_"+Recursos.get(i).extension+"_"+Recursos.get(i).NombreDelArchivo+"."+Recursos.get(i).extension,
                        "."+Recursos.get(i).extension));
            }
        }
        rv.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recursos, container, false);
    }


    @Override
    public void onRefresh() {
        if(DetectarInternet.isOnline(getActivity())) {
            volley = VolleyS.getInstance(getActivity().getApplicationContext());
            fRequestQueue = volley.getRequestQueue();
            makeRequest(fRequestQueue,this,volley,IP);
        }
        else{
            swipeRefreshLayout.setRefreshing(false);
            comunicacion.responder("Necesita estar conectado a internet");
        }
    }

    public void makeRequest(RequestQueue fRequestQueue, final Fragment fragment, VolleyS volley, String IP){
        JsonObjectRequest request = new JsonObjectRequest(IP, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                archivos = new ArrayList<>();
                adapter = new RVArchivosAdapter(archivos);
                UCVdb.actualizarDatos(0,jsonObject.toString());
                List<Recursos> Recursos = UCVdb.ObtenerRecursos(0,CodigoCurso);

                custom.setDecimalSeparator('.');
                formato.setDecimalFormatSymbols(custom);

                File Silabo = new File(path+CodigoCurso+AñoSilabo);

                archivos.add(new Archivos(
                        Silabo.exists(),
                        "Silabo ",
                        CodigoCurso+AñoSilabo,
                        path+CodigoCurso+AñoSilabo,
                        "Semana 1",
                        formato.format( Silabo.length() / (1024.0) / 1024.0),
                        path,
                        URLSilabo+CodigoCurso+AñoSilabo,
                        ".pdf"));

                for(int i=0;i<Recursos.size();i++){
                    File file = new File(pathArchivos+CodigoCurso+" "+Recursos.get(i).NombreDelArchivo+"."+Recursos.get(i).extension);
                    archivos.add(new Archivos(
                            file.exists(),
                            Recursos.get(i).NombreDelArchivo,
                            CodigoCurso+" "+Recursos.get(i).NombreDelArchivo+"."+Recursos.get(i).extension,
                            pathArchivos+CodigoCurso+" "+Recursos.get(i).NombreDelArchivo+"."+Recursos.get(i).extension,
                            Recursos.get(i).SemanaVista,
                            Recursos.get(i).Tamaño,
                            pathArchivos,
                            URL+CodigoCurso+"/"+Recursos.get(i).Semana+"/"+CodigoCurso+"_"+Recursos.get(i).extension+"_"+Recursos.get(i).NombreDelArchivo+"."+Recursos.get(i).extension,
                            "."+Recursos.get(i).extension));
                }

                rv.setAdapter(adapter);

                comunicacion.responder("Datos actualizados correctamente");
                swipeRefreshLayout.setRefreshing(false);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(),"Error al actualizar datos",Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        VolleyS.addToQueue(request,fRequestQueue,fragment,volley);
    }

}
