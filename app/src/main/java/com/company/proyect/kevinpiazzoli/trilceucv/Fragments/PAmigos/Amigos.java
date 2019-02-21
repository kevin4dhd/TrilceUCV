package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.PAmigos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.BoletaDeNotas.Boleta_De_Notas;
import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Comunicador;
import com.company.proyect.kevinpiazzoli.trilceucv.R;

/**
 * Created by KevinPiazzoli on 13/11/2016.
 */

public class Amigos extends Fragment {

    private OnFragmentInteractionListener mListener;

    private Comunicador comunicador;
    private Fragment_Amigos_Adapter mAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private int actualFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_amigos, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        comunicador = (Comunicador) getActivity();
        mAdapter = new Fragment_Amigos_Adapter(getChildFragmentManager(),getContext());
        viewPager = (ViewPager) getView().findViewById(R.id.pager);
        tabLayout = (TabLayout) getView().findViewById(R.id.tablelayout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        comunicador.setSearchView(false);
                        break;
                    case 1:
                        comunicador.setSearchView(true);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Boleta_De_Notas.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //Se cambio esta linea de codigo
        //comunicador.setSearchView(false);
    }

}
