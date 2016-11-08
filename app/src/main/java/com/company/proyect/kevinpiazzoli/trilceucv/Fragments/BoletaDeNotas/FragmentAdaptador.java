package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.BoletaDeNotas;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by KevinPiazzoli on 04/11/2016.
 */

public class FragmentAdaptador extends FragmentPagerAdapter {

    CursosNotas cursosNotas;

    FragmentAdaptador(FragmentManager fm, Context context) {
        super(fm);
        cursosNotas= new CursosNotas(context);
    }

    @Override
    public Fragment getItem(int position) {
        return Fragment_boleta_de_notas.newInstance(position);
    }

    @Override
    public int getCount() {
        return CursosNotas.getTitulosDeCurso().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String Titulos[] = CursosNotas.getTitulosDeCurso();
        return Titulos[position];
    }
}
