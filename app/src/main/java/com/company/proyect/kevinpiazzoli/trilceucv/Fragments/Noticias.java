package com.company.proyect.kevinpiazzoli.trilceucv.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.company.proyect.kevinpiazzoli.trilceucv.UltimasNoticias.Card;
import com.company.proyect.kevinpiazzoli.trilceucv.R;
import com.company.proyect.kevinpiazzoli.trilceucv.UltimasNoticias.SampleMaterialAdapter;

import java.util.ArrayList;

public class Noticias extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private SampleMaterialAdapter adapter;
    private ArrayList<Card> cardsList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private String[] Titulo = {"Hola soy kevin xDDD","lol"};
    private String[] Descripcion = {"Y estoy programando","x2"};
    private String[] Fecha = {"15/10/2016","sad"};

    private Comunicador comunicacion;

    public Noticias() {
    }

    @Override

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        comunicacion=(Comunicador) getActivity();
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipeMovieHits);
        swipeRefreshLayout.setOnRefreshListener(this);
        initCards();
        if (adapter == null) {
            adapter = new SampleMaterialAdapter(getActivity(), cardsList,comunicacion,this);
        }else{
            adapter = null;
            cardsList.clear();
            initCards();
            adapter = new SampleMaterialAdapter(getActivity(), cardsList,comunicacion,this);
        }
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.rv_notifications_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void initCards() {
        for (int i = 0; i < Titulo.length; i++) {
            Card card = new Card();
            card.setId((long) i);
            card.setTitulo(Titulo[i]);
            card.setDescripcion(Descripcion[i]);
            card.setFecha(Fecha[i]);
            cardsList.add(card);
        }
    }

    public String getTitulo(int i) {
        return Titulo[i];
    }

    public String getDescripcion(int i) {
        return Descripcion[i];
    }

    public String getFecha(int i) {
        return Fecha[i];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_noticias, container, false);
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
        comunicacion.responder("xDD");
        swipeRefreshLayout.setRefreshing(false);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
