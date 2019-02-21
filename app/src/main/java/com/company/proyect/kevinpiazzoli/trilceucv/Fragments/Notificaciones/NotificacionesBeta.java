package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Notificaciones;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.BaseDeDatosUCV;
import com.company.proyect.kevinpiazzoli.trilceucv.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KevinPiazzoli on 08/11/2016.
 */

public class NotificacionesBeta extends Fragment {

    private OnFragmentInteractionListener mListener;
    private BaseDeDatosUCV bdUCV;
    private RetornarDatos NOMBRES_APELLIDOS;
    private RecyclerView rv;
    private NotificacionAdapter adapter;
    private List<DatosNotificaciones> Datos;

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bdUCV = new BaseDeDatosUCV(getContext());
        NOMBRES_APELLIDOS = bdUCV.ObtenerNombresyCarrera(0);


        rv = (RecyclerView)getActivity().findViewById(R.id.recicleView);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        Datos = new ArrayList<>();
        adapter = new NotificacionAdapter(Datos);
        /*String[][] ObtenerTokens = bdUCV.ObtenerTokens(0);

        for(int i=0;i<ObtenerTokens.length;i++){
            Datos.add(new DatosNotificaciones(
                    R.drawable.ic_contact_icon,//img
                    NOMBRES_APELLIDOS.vector1[i][1],//Nombre
                    NOMBRES_APELLIDOS.vector1[i][2],//Carrera
                    ObtenerTokens[i][0],
                    ObtenerTokens[i][2],
                    NOMBRES_APELLIDOS.Nombre ));
        }
        rv.setAdapter(adapter);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notificaciones, container, false);
    }

    /*public void onClick(View v) {
        String[][] ObtenerTokens = bdUCV.ObtenerTokens(0);
        Toast.makeText(getActivity(),"true",Toast.LENGTH_SHORT).show();
        final UsuarioResponse usuarioResponse = new UsuarioResponse(ObtenerTokens[0][2],"123",ObtenerTokens[0][0]); //Receptor
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestAPI();
        Call<UsuarioResponse> usuarioResponseCall = endpoints.toqueButton(usuarioResponse.getId(),NOMBRES_APELLIDOS[0]);//Emisor
        usuarioResponseCall.enqueue(new Callback<UsuarioResponse>() {
            @Override
            public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                UsuarioResponse usuarioResponse1 = response.body();
                Toast.makeText(getActivity(),usuarioResponse1.getId(),Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),usuarioResponse1.getToken(),Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),usuarioResponse1.getusuario(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {}});
    }*/

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

}
