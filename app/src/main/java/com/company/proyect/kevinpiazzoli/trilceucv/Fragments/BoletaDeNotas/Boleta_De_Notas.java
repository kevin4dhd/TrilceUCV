package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.BoletaDeNotas;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.proyect.kevinpiazzoli.trilceucv.R;

public class Boleta_De_Notas extends Fragment {

    private OnFragmentInteractionListener mListener;

    private FragmentAdaptador mAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_boleta__de__notas, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new FragmentAdaptador(getChildFragmentManager(),getContext());
        viewPager = (ViewPager) getView().findViewById(R.id.pager);
        tabLayout = (TabLayout) getView().findViewById(R.id.tablelayout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(mAdapter);
        Notas.Actualizar();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
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
}
