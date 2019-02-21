package com.company.proyect.kevinpiazzoli.trilceucv.Fragments;

import android.support.v7.widget.SearchView;
import android.widget.ImageButton;

/**
 * Created by Kevin Piazzoli on 28/09/2016.
 */

public interface Comunicador {
    void responder(String datos);
    SearchView getSearchView();
    void setSearchView(boolean b);
    ImageButton getImageButton();
    void offImageButton();
}
