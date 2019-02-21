package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.VerNotificaciones;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KevinPiazzoli on 29/11/2016.
 */

public class Fragment_Notificaciones extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private Comunicador comunicacion;
    private List<VerNotificacionesData> ListaDeDatos;
    private VolleyS volley;
    private String IP;
    protected RequestQueue fRequestQueue;
    private BaseDeDatosUCV UCVdb;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rv;
    private AdapterVerNotificaciones adapter;
    private LinearLayout MensajeInicio;

    private String[] Titulos= {
            "Bienvenido a la universidad.",
            "Jesus te ha enviado un mensaje",
            "Edgar te ha enviado una solicitud de amistad",
            "Juan rechazo tu solicitud de amistad",
            "Nueva actividad DBU",
            "Matematica III ha subido un nuevo recurso"};

    private String[] Descripcion = {
            "Un gusto que estes con nosotros de nuevo.",
            "Para poder ver el mensaje ve a la pestaña de Amigos(Chat)",
            "Para poder ver la solicitud de amistad ve a la pestaña de Amigos(Chat)",
            "Desafortunadamente Juan rechazo tu solicitud de amistad",
            "Mañana nueva actividad DBU, Danzas Folclóricas, No te olvides de llevar tu Carnet",
            "Para poder ver el recurso ve a la pestaña de Mis Asignaturas e ingresa a la asignatura para poder descargar el nuevo recurso"
    };


    public Fragment_Notificaciones(){}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        comunicacion=(Comunicador) getActivity();

        ImageButton DeleteNotificationsAll = comunicacion.getImageButton();

        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipeMovieHits);
        rv = (RecyclerView)getActivity().findViewById(R.id.rv_notifications_list);
        MensajeInicio = (LinearLayout) getActivity().findViewById(R.id.NoNotificaciones);
        swipeRefreshLayout.setOnRefreshListener(this);
        IP ="http://kpfpservice.000webhostapp.com/UCV_datos_usuarios_GETALL.php"+"?id="+ GuardarDatos.CargarUsuario(getActivity());

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        ListaDeDatos = new ArrayList<>();
        adapter = new AdapterVerNotificaciones(ListaDeDatos,this);
        rv.setAdapter(adapter);

        ItemTouchHelper.Callback callback=new SwipeHelper(adapter);
        ItemTouchHelper helper=new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rv);

        UCVdb = new BaseDeDatosUCV(getActivity());
        for(int i=0;i<Titulos.length;i++) {
            VerNotificacionesData ListaTemporal = new VerNotificacionesData();
            ListaTemporal.setTitulo(Titulos[i]);
            ListaTemporal.setDescripcion(Descripcion[i]);
            ListaTemporal.setFecha("12/04/2017");
            ListaDeDatos.add(ListaTemporal);
        }
        adapter.Actualizar();

        DeleteNotificationsAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.DeleteAll();
            }
        });

    }

    public void VisualizacionMensaje(boolean bAmigos){
        rv.setVisibility(bAmigos ? View.VISIBLE : View.GONE);
        MensajeInicio.setVisibility(bAmigos ? View.GONE : View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ver_notificaciones, container, false);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
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
            comunicacion.responder(getContext().getString(R.string.necesita_estar_conectado_a_internet));
        }
    }

    public void makeRequest(RequestQueue fRequestQueue, final Fragment fragment, VolleyS volley, String IP){
        JsonObjectRequest request = new JsonObjectRequest(IP, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                swipeRefreshLayout.setRefreshing(false);
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
        comunicacion.getImageButton();
    }

    @Override
    public void onPause() {
        super.onPause();
        comunicacion.offImageButton();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(fRequestQueue != null)fRequestQueue.cancelAll(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (fRequestQueue != null)fRequestQueue.cancelAll(this);
    }

}
