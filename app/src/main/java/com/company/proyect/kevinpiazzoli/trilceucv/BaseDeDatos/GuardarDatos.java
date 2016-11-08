package com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Kevin Piazzoli on 11/10/2016.
 */

public class GuardarDatos {

    static public void GuardarUsuario(Activity actividad,String User){
        SharedPreferences preferencias;
        preferencias = PreferenceManager.getDefaultSharedPreferences(actividad);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("op_user", User);
        editor.apply();
    }

    static public void GuardarNo_Cerrar_Secion(Activity actividad,boolean checked){
        SharedPreferences preferencias;
        preferencias = PreferenceManager.getDefaultSharedPreferences(actividad);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putBoolean("No cerrar Sesión", checked);
        editor.apply();
    }

    static public String CargarUsuario(Activity actividad){
        SharedPreferences preferencias;
        preferencias = PreferenceManager.getDefaultSharedPreferences(actividad);
        return preferencias.getString("op_user","NoUser");
    }

    static public boolean CargarNo_Cerrar_Secion(Activity actividad){
        SharedPreferences preferencias;
        preferencias = PreferenceManager.getDefaultSharedPreferences(actividad);
        return preferencias.getBoolean("No cerrar Sesión",false);
    }

    static public int CargarTablas(Activity actividad){
        SharedPreferences preferencias;
        preferencias = PreferenceManager.getDefaultSharedPreferences(actividad);
        return Integer.parseInt(preferencias.getString("key_tabla","1"));
    }

}
