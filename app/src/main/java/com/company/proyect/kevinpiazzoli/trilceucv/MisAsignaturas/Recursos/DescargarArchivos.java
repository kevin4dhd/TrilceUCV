package com.company.proyect.kevinpiazzoli.trilceucv.MisAsignaturas.Recursos;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by KevinPiazzoli on 05/11/2016.
 */
public class DescargarArchivos extends AsyncTask<List<DescargaArchivosDatos>, Integer, Boolean> {

    private ProgressBar progressBar;
    private Context context;
    private File file;
    private Button Descargar;
    private Button Cancelar;
    private TextView DescargaCompleta;
    private TextView DescargaFallida;
    private TextView TamañoCompleto;
    private TextView TamañoFallida;

    @Override
    protected Boolean doInBackground(List<DescargaArchivosDatos>... params) {
        try {

            Descargar = params[0].get(0).Descargar;
            Cancelar = params[0].get(0).Cancelar;
            DescargaCompleta = params[0].get(0).DescargaCompleta;
            DescargaFallida = params[0].get(0).DescargaFallida;
            TamañoCompleto = params[0].get(0).TamañoCompleto;
            TamañoFallida = params[0].get(0).TamañoFallida;
            context = params[0].get(0).context;
            progressBar = params[0].get(0).progressBar;


            String fileUrl = params[0].get(0).URL;
            String fileName = params[0].get(0).NombreArchivo;
            File folder = new File(params[0].get(0).RutaArchivo);
            boolean isDirectoryCreated = folder.mkdirs();
            File pdfFile = new File(folder, fileName);
            file = pdfFile;
            boolean ExisteElArchivo = pdfFile.exists();
            if (!ExisteElArchivo) {
                try {
                    pdfFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String url = fileUrl.replaceAll(" ", "");
                if(!(downloadFile(url, pdfFile))) return false;
            }
            return true;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Boolean aVoid) {
        super.onPostExecute(aVoid);
        progressBar.setProgress(0);
        TamañoCompleto.setVisibility(View.VISIBLE);
        Descargar.setVisibility(View.VISIBLE);

        Cancelar.setVisibility(View.GONE);

        if(aVoid){

            Toast.makeText(context, "Archivo descargado correctamente", Toast.LENGTH_SHORT).show();
            DescargaCompleta.setVisibility(View.VISIBLE);

            progressBar.setVisibility(View.GONE);
            TamañoFallida.setVisibility(View.GONE);
        }
        else{
            Toast.makeText(context, "Ocurrio un error, por favor contactese con el administrador", Toast.LENGTH_SHORT).show();
            DescargaFallida.setVisibility(View.VISIBLE);

            TamañoFallida.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        progressBar.setProgress(0);
        if(file.delete()){
            Toast.makeText(context, "Descarga cancelada correctamente", Toast.LENGTH_SHORT).show();

            Cancelar.setVisibility(View.GONE);
            TamañoCompleto.setVisibility(View.VISIBLE);
            Descargar.setVisibility(View.VISIBLE);
            DescargaFallida.setVisibility(View.VISIBLE);
            TamañoFallida.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);

        }
        else  Toast.makeText(context, "Error al cancelar la descarga", Toast.LENGTH_SHORT).show();
    }

    private static final int  MEGABYTE = 1024 * 1024;

    private boolean downloadFile(String fileUrl, File directory){
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            double totalSize = urlConnection.getContentLength();
            if(totalSize != -1){
                byte[] buffer = new byte[(int)totalSize];
                double bufferLength = 0;
                long total = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    total += bufferLength;
                    publishProgress((int) (((total * 100) / totalSize)));
                    fileOutputStream.write(buffer, 0, (int) bufferLength);
                }
                fileOutputStream.close();
            }
            else{
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
