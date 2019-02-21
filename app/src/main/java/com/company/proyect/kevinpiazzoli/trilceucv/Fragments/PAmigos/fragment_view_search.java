package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.PAmigos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KevinPiazzoli on 27/11/2016.
 */

public class fragment_view_search extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    public static final String NEW_SOLICITUD = "NUEVA_SOLICITUD";
    private BroadcastReceiver SolicitudRecivida;

    private Comunicador comunicador;
    private BaseDeDatosUCV bdUCV;
    private List<List<String>> Amigos;
    private List<List<String>> NuevosAmigos;
    private List<List<String>> EstadoAmigos;
    private RecyclerView rv;
    private LinearLayout MensajeInicio;
    private SearchAdapter adapter;
    private ArrayList<DatosAmigos> Datos;
    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    protected String IP;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView searchView;
    private static String APP_DIRECTORY = "TrilceUCV/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "Fotos";
    private String mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
            + File.separator;
    protected String NAME_USER;

    public fragment_view_search(){
    }

    public static fragment_view_search newInstance() {
        return new fragment_view_search();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amigos_search_amigos, container, false);

        comunicador = (Comunicador) getActivity();

        volley = VolleyS.getInstance(getActivity().getApplicationContext());
        fRequestQueue = volley.getRequestQueue();

        bdUCV = new BaseDeDatosUCV(getContext());

        Amigos = bdUCV.ObtenerAmigos(0);
        EstadoAmigos = bdUCV.ObtenerEstadoAmigos(0);
        NuevosAmigos = bdUCV.ObtenerBuscadorDeAmigosSearch(EstadoAmigos, GuardarDatos.CargarUsuario(getActivity()));
        NAME_USER = bdUCV.getNameUserPrincipal(0);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeMovieHits);
        swipeRefreshLayout.setOnRefreshListener(this);

        IP = "http://kpfpservice.000webhostapp.com/UCV_datos_usuarios_GETALL.php" + "?id=" + GuardarDatos.CargarUsuario(getActivity());
        rv = (RecyclerView)view.findViewById(R.id.recicleView);
        MensajeInicio = (LinearLayout) view.findViewById(R.id.NoSolicitudes);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        Datos = new ArrayList<>();
        adapter = new SearchAdapter(Datos,NuevosAmigos,this);
        rv.setAdapter(adapter);
        RellenarAmigos();

        SolicitudRecivida = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ActualizarTodo();
            }
        };

        Context c = getContext();
        searchView = comunicador.getSearchView();
        Toast.makeText(getContext(),""+searchView.getVisibility(), Toast.LENGTH_SHORT).show();
        final EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.WHITE);
        searchEditText.setHint("Buscar...");
        searchEditText.setHintTextColor(Color.WHITE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.setQuery("", false);
                searchView.setIconified(true);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)){
                    ActualizarTodo();
                }
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return view;
    }

    public void VisualizarMensaje(boolean bAmigos){
        rv.setVisibility(bAmigos ? View.VISIBLE : View.GONE);
        MensajeInicio.setVisibility(bAmigos ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(SolicitudRecivida, new IntentFilter(NEW_SOLICITUD));
    }

    private void ActualizarTodo(){
        Datos.clear();
        Amigos = bdUCV.ObtenerAmigos(0);
        EstadoAmigos = bdUCV.ObtenerEstadoAmigos(0);
        NuevosAmigos = bdUCV.ObtenerBuscadorDeAmigosSearch(EstadoAmigos,GuardarDatos.CargarUsuario(getActivity()));
        adapter.setNuevosAmigos(NuevosAmigos);
        adapter.setDatos(Datos);
        adapter.Actualizar();
        RellenarAmigos();
    }

    @Override
    public void onRefresh() {
        if(DetectarInternet.isOnline(getActivity())) {
            makeRequest(fRequestQueue,this,volley,IP);
        }
        else{
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(),"Necesita estar conectado a internet",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(SolicitudRecivida);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (fRequestQueue != null) {
            fRequestQueue.cancelAll(this);
        }
    }

    public void makeRequest(RequestQueue fRequestQueue, final Fragment fragment, VolleyS volley, String IP){
        JsonObjectRequest request = new JsonObjectRequest(IP, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                bdUCV.actualizar(0,jsonObject.toString());
                swipeRefreshLayout.setRefreshing(false);
                Datos.clear();
                Amigos = bdUCV.ObtenerAmigos(0);
                EstadoAmigos = bdUCV.ObtenerEstadoAmigos(0);
                NuevosAmigos = bdUCV.ObtenerBuscadorDeAmigosSearch(EstadoAmigos, GuardarDatos.CargarUsuario(getActivity()));
                adapter.setNuevosAmigos(NuevosAmigos);
                adapter.setDatos(Datos);
                adapter.Actualizar();
                RellenarAmigos();
                searchView.setQuery("", false);
                searchView.setIconified(true);
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

    public void RellenarAmigos(){
        for(int i=0;i<Amigos.size();i++){
            if(EstadoAmigos.get(i).get(1).compareTo("Amigos")!=0) {
                File file = new File(mPath+Amigos.get(i).get(0)+".jpg");
                if(!file.exists()) {
                    boolean isDirectoryCreated = file.getParentFile().mkdirs();
                    FileOutputStream fos;
                    try {
                        fos = new FileOutputStream(file);
                        Bitmap foto = BitmapFactory.decodeResource(getResources(), R.drawable.ic_contact_icon);
                        foto.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.flush();
                        fos.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                DatosAmigos auxiAmigos = new DatosAmigos();
                auxiAmigos.setFoto(R.drawable.ic_contact_icon);
                auxiAmigos.setUser(Amigos.get(i).get(0));
                auxiAmigos.setNombre(Amigos.get(i).get(1));
                auxiAmigos.setApellido(Amigos.get(i).get(2));
                auxiAmigos.setAmigo(EstadoAmigos.get(i).get(1));
                Datos.add(auxiAmigos);
            }
        }
        adapter.Actualizar();
    }

    public VolleyS getVolley() {
        return volley;
    }

}
