package com.company.proyect.kevinpiazzoli.trilceucv.MisAsignaturas;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.company.proyect.kevinpiazzoli.trilceucv.MisAsignaturas.Informacion_del_curso.Fragment_IDcurso;
import com.company.proyect.kevinpiazzoli.trilceucv.MisAsignaturas.Recursos.Fragment_Recursos;

/**
 * Created by KevinPiazzoli on 26/10/2016.
 */

class FragmentAdapter extends FragmentPagerAdapter {

    private Context context;
    private int PageCount=AddTablayout.getELEMENTOS();

    FragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Fragment_IDcurso();
            case 1:
                return new Fragment_Recursos();
        }
        return null;
    }

    @Override
    public int getCount() {
        return PageCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return AddTablayout.TabAll()[position];
    }
}
