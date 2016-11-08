package com.company.proyect.kevinpiazzoli.trilceucv.Settings.Dialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.company.proyect.kevinpiazzoli.trilceucv.ActivitysPrincipales.Datos_Basicos;
import com.company.proyect.kevinpiazzoli.trilceucv.R;

public class ColorChooserDialog extends DialogFragment implements View.OnClickListener {
    CardView cardViewUCV, cardView1, cardView2, cardView3, cardView4, cardView5, cardView6, cardView7, cardView8, cardView9, cardView10, cardView11;
    Button buttonAgree;
    View view;
    int currentTheme;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(false);
        sharedPreferences = getActivity().getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        currentTheme = sharedPreferences.getInt("THEME", 0);
        view = inflater.inflate(R.layout.theme_dialog, container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogButtons();
        return view;
    }

    private void dialogButtons() {
        cardViewUCV = (CardView) view.findViewById(R.id.card_viewUCV);
        cardView1 = (CardView) view.findViewById(R.id.card_view1);
        cardView2 = (CardView) view.findViewById(R.id.card_view2);
        cardView3 = (CardView) view.findViewById(R.id.card_view3);
        cardView4 = (CardView) view.findViewById(R.id.card_view4);
        cardView5 = (CardView) view.findViewById(R.id.card_view5);
        cardView6 = (CardView) view.findViewById(R.id.card_view6);
        cardView7 = (CardView) view.findViewById(R.id.card_view7);
        cardView8 = (CardView) view.findViewById(R.id.card_view8);
        cardView9 = (CardView) view.findViewById(R.id.card_view9);
        cardView10 = (CardView) view.findViewById(R.id.card_view10);
        cardView11 = (CardView) view.findViewById(R.id.card_viewBlack);
        buttonAgree = (Button) view.findViewById(R.id.buttonAgree);

        cardViewUCV.setOnClickListener(this);
        cardView1.setOnClickListener(this);
        cardView2.setOnClickListener(this);
        cardView3.setOnClickListener(this);
        cardView4.setOnClickListener(this);
        cardView5.setOnClickListener(this);
        cardView6.setOnClickListener(this);
        cardView7.setOnClickListener(this);
        cardView8.setOnClickListener(this);
        cardView9.setOnClickListener(this);
        cardView10.setOnClickListener(this);
        cardView11.setOnClickListener(this);

        buttonAgree.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_viewUCV:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                ((Datos_Basicos) getActivity()).setThemeFragment(0);
                ResetWindow();
                break;
            case R.id.card_view1:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                ((Datos_Basicos) getActivity()).setThemeFragment(1);
                ResetWindow();
                break;
            case R.id.card_view2:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                ((Datos_Basicos) getActivity()).setThemeFragment(2);
                ResetWindow();
                break;
            case R.id.card_view3:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                ((Datos_Basicos) getActivity()).setThemeFragment(3);
                ResetWindow();
                break;
            case R.id.card_view4:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                ((Datos_Basicos) getActivity()).setThemeFragment(4);
                ResetWindow();
                break;
            case R.id.card_view5:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                ((Datos_Basicos) getActivity()).setThemeFragment(5);
                ResetWindow();
                break;
            case R.id.card_view6:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                ((Datos_Basicos) getActivity()).setThemeFragment(6);
                ResetWindow();
                break;
            case R.id.card_view7:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                ((Datos_Basicos) getActivity()).setThemeFragment(7);
                ResetWindow();
                break;
            case R.id.card_view8:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                ((Datos_Basicos) getActivity()).setThemeFragment(8);
                ResetWindow();
                break;
            case R.id.card_view9:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                ((Datos_Basicos) getActivity()).setThemeFragment(9);
                ResetWindow();
                break;
            case R.id.card_view10:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                ((Datos_Basicos) getActivity()).setThemeFragment(10);
                ResetWindow();
                break;
            case R.id.card_viewBlack:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                ((Datos_Basicos) getActivity()).setThemeFragment(11);
                ResetWindow();
                break;
            case R.id.buttonAgree:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                getDialog().dismiss();
                break;
        }
    }

    public void ResetWindow(){
        if(getActivity().getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else{
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }
}