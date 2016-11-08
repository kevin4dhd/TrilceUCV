package com.company.proyect.kevinpiazzoli.trilceucv.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.BaseDeDatosUCV;
import com.company.proyect.kevinpiazzoli.trilceucv.Tablas.DatosAdapter;
import com.company.proyect.kevinpiazzoli.trilceucv.ConexionInternet.DetectarInternet;
import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.GuardarDatos;
import com.company.proyect.kevinpiazzoli.trilceucv.R;
import com.company.proyect.kevinpiazzoli.trilceucv.Tablas.Tabla_datos;
import com.company.proyect.kevinpiazzoli.trilceucv.ConexionInternet.VolleyS;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Datos_Basic extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private OnFragmentInteractionListener mListener;
    private Comunicador comunicacion;
    private String keys[] ={
            "apellidos",
            "nombres",
            "fecha_de_nacimiento",
            "genero","estado_civil",
            "filial","escuela",
            "tipo_de_plan",
            "curriculo",
            "codigo_de_pago"};

    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String IP;
    private ListView lista_datos;
    private ArrayList<Tabla_datos> ArrayDatos;
    private BaseDeDatosUCV UCVdb;
    private DatosAdapter adaptador;

    public Datos_Basic() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        IP = "http://kpfp.pe.hu/conectkevin/UCV_datos_usuarios_GETALL.php" + "?id=" + GuardarDatos.CargarUsuario(getActivity());
        comunicacion = (Comunicador) getActivity();

        lista_datos = (ListView)getActivity().findViewById(R.id.listView);

        ArrayDatos = new ArrayList<>();
        adaptador = new DatosAdapter(getActivity(),ArrayDatos);
        lista_datos.setAdapter(adaptador);

        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipeMovieHits);
        swipeRefreshLayout.setOnRefreshListener(this);

        UCVdb = new BaseDeDatosUCV(getActivity());
        String JSONdatos = UCVdb.obtenerDatosBasicos(0);
        ActualziarTablas(JSONdatos);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_datos__basic, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (fRequestQueue != null) {
            fRequestQueue.cancelAll(this);
        }
    }

    public void AñadirDatos(JSONObject datosjson) {
        comunicacion.responder("Datos actualizados Correctamente");
        ActualziarTablas(datosjson.toString());
        swipeRefreshLayout.setRefreshing(false);
    }

    public String[] AgregarDatos(String JSONdatos,String[] datos){
        //tabla.agregarCabecera(R.array.cabecera_tabla);
        JSONObject JSONinformacion;
        String DATOS_BASICOS[] = new String[datos.length];
        try {
            JSONinformacion = new JSONObject(JSONdatos);
            for (int i = 0; i < datos.length; i++) {
                    DATOS_BASICOS[i] = JSONinformacion.getString(keys[i]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return DATOS_BASICOS;
    }

    public void ActualziarTablas(String datosjson){
        ArrayDatos.clear();
        String datos[] = {
                "Apellidos",
                "Nombres",
                "F.Nacimiento",
                "Genero",
                "E.Civil",
                "Filial",
                "Escuela/Programa",
                "Tipo de plan",
                "Currículo",
                "Código de pago"
        };
        String DATOS_BASICOS[] = new String[datos.length];

        if(datosjson!=null) {
            DATOS_BASICOS = AgregarDatos(datosjson, datos);
        }
        ArrayDatos.add(new Tabla_datos("Datos Basicos","",""));
        for(int i = 0; i < keys.length; i++) {
            ArrayDatos.add(new Tabla_datos("",datos[i],DATOS_BASICOS[i]));
        }
        lista_datos.setAdapter(adaptador);
    }

    public void makeRequest(RequestQueue fRequestQueue, final Fragment fragment, VolleyS volley, String IP){
        JsonObjectRequest request = new JsonObjectRequest(IP, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                UCVdb.actualizar(0,jsonObject.toString());
                String JSON_BaseDeDatos = UCVdb.obtenerDatosBasicos(0);
                try {
                    AñadirDatos(new JSONObject(JSON_BaseDeDatos));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(),"Error al cargar datos",Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        VolleyS.addToQueue(request,fRequestQueue,fragment,volley);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

}
