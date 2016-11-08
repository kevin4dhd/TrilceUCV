package com.company.proyect.kevinpiazzoli.trilceucv.MisAsignaturas.Recursos;

import android.content.Context;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by KevinPiazzoli on 06/11/2016.
 */

public class DescargaArchivosDatos {

    String URL;
    String NombreArchivo;
    String RutaArchivo;
    ProgressBar progressBar;
    Button Descargar;
    Button Cancelar;
    TextView DescargaCompleta;
    TextView DescargaFallida;
    TextView TamañoCompleto;
    TextView TamañoFallida;
    Context context;

    public DescargaArchivosDatos(String URL,
                                 String nombreArchivo,
                                 String rutaArchivo,
                                 ProgressBar progressBar,
                                 Button descargar,
                                 Button cancelar,
                                 TextView descargaCompleta,
                                 TextView descargaFallida,
                                 TextView tamañoCompleto,
                                 TextView tamañoFallida,
                                 Context context) {
        this.URL = URL;
        NombreArchivo = nombreArchivo;
        RutaArchivo = rutaArchivo;
        this.progressBar = progressBar;
        Descargar = descargar;
        Cancelar = cancelar;
        DescargaCompleta = descargaCompleta;
        DescargaFallida = descargaFallida;
        TamañoCompleto = tamañoCompleto;
        TamañoFallida = tamañoFallida;
        this.context = context;
    }
}

