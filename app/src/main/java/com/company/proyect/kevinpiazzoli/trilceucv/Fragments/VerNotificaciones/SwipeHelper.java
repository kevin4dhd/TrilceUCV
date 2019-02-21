package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.VerNotificaciones;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by KevinPiazzoli on 30/11/2016.
 */

class SwipeHelper extends ItemTouchHelper.SimpleCallback {

    AdapterVerNotificaciones adapter;

    SwipeHelper(AdapterVerNotificaciones adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN,ItemTouchHelper.LEFT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.dismissTVShow(viewHolder.getAdapterPosition());
    }

}
