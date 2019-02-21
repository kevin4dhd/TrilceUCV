package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Datos_Basicos;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.company.proyect.kevinpiazzoli.trilceucv.ActivitysPrincipales.ViewImage;
import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.BaseDeDatosUCV;
import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.GuardarDatos;
import com.company.proyect.kevinpiazzoli.trilceucv.ConexionInternet.DetectarInternet;
import com.company.proyect.kevinpiazzoli.trilceucv.ConexionInternet.VolleyS;
import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Comunicador;
import com.company.proyect.kevinpiazzoli.trilceucv.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Datos_Basic extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private static String APP_DIRECTORY = "TrilceUCV/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "Fotos";
    private static String USER_FOTO = "User_Foto";
    private String mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
            + File.separator + USER_FOTO + ".jpg";

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

    private List<Datos_B> ListaDeDatos;
    private RecyclerView rv;
    private Datos_B_Adapter adapter;

    protected RequestQueue fRequestQueue;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String IP;
    private BaseDeDatosUCV UCVdb;
    private ImageView profileimage;

    public Datos_Basic() {
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        IP = "http://kpfpservice.000webhostapp.com/UCV_datos_usuarios_GETALL.php" + "?id=" + GuardarDatos.CargarUsuario(getActivity());
        comunicacion = (Comunicador) getActivity();
        profileimage = (ImageView) getActivity().findViewById(R.id.circle_image);
        profileimage.setOnClickListener(this);
        profileimage.setImageBitmap(BitmapFactory.decodeFile(mPath));

        rv = (RecyclerView) getActivity().findViewById(R.id.listView);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        ListaDeDatos = new ArrayList<>();
        adapter = new Datos_B_Adapter(ListaDeDatos,getContext());
        rv.setAdapter(adapter);

        //swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipeMovieHits);
        //swipeRefreshLayout.setOnRefreshListener(this);

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
        if (fRequestQueue != null) {
            fRequestQueue.cancelAll(this);
        }
    }

    @Override
    public void onRefresh() {
        if(DetectarInternet.isOnline(getActivity())) {
            VolleyS volley = VolleyS.getInstance(getActivity().getApplicationContext());
            fRequestQueue = volley.getRequestQueue();
            makeRequest(fRequestQueue,this, volley,IP);
        }
        else{
            swipeRefreshLayout.setRefreshing(false);
            comunicacion.responder(getContext().getString(R.string.necesita_estar_conectado_a_internet));
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), ViewImage.class);
        intent.putExtra("key_bitmap", mPath);
        intent.putExtra("user",GuardarDatos.CargarUsuario(getContext()));
        startActivity(intent);
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
        comunicacion.responder(getContext().getString(R.string.datos_actualizados_correctamente));
        ActualziarTablas(datosjson.toString());
        swipeRefreshLayout.setRefreshing(false);
    }

    public String[] AgregarDatos(String JSONdatos,String[] datos){
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
        ListaDeDatos.clear();
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
        for(int i = 0; i < keys.length; i++) {
            Datos_B auxiliar= new Datos_B();
            auxiliar.setSubDato(datos[i]);
            auxiliar.setDato(DATOS_BASICOS[i]);
            ListaDeDatos.add(auxiliar);
        }
        rv.setAdapter(adapter);
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
                Toast.makeText(getActivity(),getContext().getString(R.string.error_al_actualizar_datos),Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        VolleyS.addToQueue(request,fRequestQueue,fragment,volley);
    }
    @Override
    public void onResume() {
        super.onResume();
    }
}
