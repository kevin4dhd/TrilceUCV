package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.PAmigos;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.company.proyect.kevinpiazzoli.trilceucv.R;

/**
 * Created by KevinPiazzoli on 27/11/2016.
 */

public class Fragment_Amigos_Adapter extends FragmentPagerAdapter {

    Context context;

    Fragment_Amigos_Adapter(FragmentManager fm, Context context) {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return fragment_view_amigos.newInstance();
            case 1:
                return fragment_view_search.newInstance();
            default:
                return fragment_view_amigos.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getString(R.string.chat);
            case 1:
                return context.getString(R.string.buscar_title);
            default:
                return context.getString(R.string.chat);
        }
    }

}
