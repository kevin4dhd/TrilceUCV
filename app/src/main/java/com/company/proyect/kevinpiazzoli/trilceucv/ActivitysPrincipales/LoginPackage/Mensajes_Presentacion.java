package com.company.proyect.kevinpiazzoli.trilceucv.ActivitysPrincipales.LoginPackage;

import android.content.Context;

import com.company.proyect.kevinpiazzoli.trilceucv.R;

import java.util.Random;

/**
 * Created by KevinPiazzoli on 23/11/2016.
 */

public final class Mensajes_Presentacion {


    private static int UltimaPosicion=-1;

    public static String RetornarMensajePresentacion(Context context){

        String[] Mensajes_Presentacion= {
                context.getString(R.string.puedes_cambiar_los_colores),
                context.getString(R.string.Podras_cambiar_el_diseño),
                context.getString(R.string.Puedes_buscar_a_compañeros_de_otras_escuelas),
                context.getString(R.string.cambia_tu_foto_de_perfil),
                context.getString(R.string.podras_acceder_a_la_aplicacion)};

        Random rand = new Random();
        int PosAleatorio;
        do {
            PosAleatorio = rand.nextInt(Mensajes_Presentacion.length);
        }while(UltimaPosicion == PosAleatorio);
        UltimaPosicion=PosAleatorio;
        return Mensajes_Presentacion[PosAleatorio];
    }
}
