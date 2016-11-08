package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.BoletaDeNotas;

import android.os.Bundle;
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

/**
 * Created by KevinPiazzoli on 04/11/2016.
 */
public class Fragment_boleta_de_notas extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private Comunicador comunicacion;
    private String IP;
    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BaseDeDatosUCV UCVdb;
    private  RecyclerView reciclador;

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static Fragment_boleta_de_notas newInstance(int sectionNumber) {
        Fragment_boleta_de_notas fragment = new Fragment_boleta_de_notas();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boleta_de_notas_viewpager, container, false);

        comunicacion=(Comunicador) getActivity();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeMovieHits);
        IP ="http://kpfp.pe.hu/conectkevin/UCV_datos_usuarios_GETALL.php"+"?id="+ GuardarDatos.CargarUsuario(getActivity());
        UCVdb = new BaseDeDatosUCV(getActivity());

        swipeRefreshLayout.setOnRefreshListener(this);
        reciclador = (RecyclerView) view.findViewById(R.id.recicladorr);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        reciclador.setLayoutManager(llm);

        int indiceSeccion = getArguments().getInt(ARG_SECTION_NUMBER);

        AdaptadorNotas adaptador = new AdaptadorNotas(Notas.NOTASCompletas.get(indiceSeccion));

        reciclador.setAdapter(adaptador);

        return view;

    }

    public Fragment_boleta_de_notas() {
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
                UCVdb.actualizarDatos(0,jsonObject.toString());
                new CursosNotas(getContext());
                Notas.Actualizar();
                int indiceSeccion = getArguments().getInt(ARG_SECTION_NUMBER);
                AdaptadorNotas adaptador = new AdaptadorNotas(Notas.NOTASCompletas.get(indiceSeccion));
                reciclador.setAdapter(adaptador);
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
