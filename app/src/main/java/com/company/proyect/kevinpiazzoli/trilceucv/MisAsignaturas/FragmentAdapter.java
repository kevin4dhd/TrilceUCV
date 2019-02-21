package com.company.proyect.kevinpiazzoli.trilceucv.MisAsignaturas;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.company.proyect.kevinpiazzoli.trilceucv.MisAsignaturas.Asistencias.fragment_Asistencias;
import com.company.proyect.kevinpiazzoli.trilceucv.MisAsignaturas.Informacion_del_curso.Fragment_IDcurso;
import com.company.proyect.kevinpiazzoli.trilceucv.MisAsignaturas.Recursos.Fragment_Recursos;

/**
 * Created by KevinPiazzoli on 26/10/2016.
 */

class FragmentAdapter extends FragmentPagerAdapter {


    FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return Fragment_IDcurso.newInstance();
            case 1:
                return fragment_Asistencias.newInstance();
            case 2:
                return Fragment_Recursos.newInstance();
            default:
                return Fragment_IDcurso.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Información del Curso";
            case 1:
                return "Asistencia";
            case 2:
                return "Recursos";
            default:
                return "Recursos";
        }
    }
}
