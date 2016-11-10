package com.company.proyect.kevinpiazzoli.trilceucv.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.GuardarDatos;
import com.company.proyect.kevinpiazzoli.trilceucv.R;
import com.company.proyect.kevinpiazzoli.trilceucv.RestApi.Endpoints;
import com.company.proyect.kevinpiazzoli.trilceucv.RestApi.adapter.RestApiAdapter;
import com.company.proyect.kevinpiazzoli.trilceucv.RestApi.model.UsuarioResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by KevinPiazzoli on 08/11/2016.
 */

public class NotificacionesBeta extends Fragment implements View.OnClickListener {

    private Datos_Basic.OnFragmentInteractionListener mListener;
    private EditText texto;
    private Button enviar;
    private Button ToqueButton;

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        texto = (EditText) getActivity().findViewById(R.id.MensajeNotificacion);
        enviar = (Button) getActivity().findViewById(R.id.EnviarNotificacion);
        ToqueButton = (Button) getActivity().findViewById(R.id.ToqueButton);

        enviar.setOnClickListener(this);
        ToqueButton.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notificaciones, container, false);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(),"true",Toast.LENGTH_SHORT).show();
        final UsuarioResponse usuarioResponse = new UsuarioResponse("-KW8lM8fuJw-_QVnYkBG","123","kfelix"); //emisor
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestAPI();
        Call<UsuarioResponse> usuarioResponseCall = endpoints.toqueButton(usuarioResponse.getId(),"kfelix");//receptor
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
    }

    private void enviarTokenRegistro(String token) {
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestAPI();
        Call<UsuarioResponse> usuarioResponseCall = endpoints.registrarTokenID(token, GuardarDatos.CargarUsuario(getActivity()));

        usuarioResponseCall.enqueue(new Callback<UsuarioResponse>() {
            @Override
            public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                UsuarioResponse usuarioResponse = response.body();
                Toast.makeText(getActivity(),usuarioResponse.getId(),Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),usuarioResponse.getToken(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {

            }
        });
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

}
