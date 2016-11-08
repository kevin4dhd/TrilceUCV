package com.company.proyect.kevinpiazzoli.trilceucv.ActivitysPrincipales;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.BaseDeDatosUCV;
import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.GuardarDatos;
import com.company.proyect.kevinpiazzoli.trilceucv.ConexionInternet.DetectarInternet;
import com.company.proyect.kevinpiazzoli.trilceucv.ConexionInternet.MySingleton;
import com.company.proyect.kevinpiazzoli.trilceucv.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kevin Piazzoli on 26/09/2016.
 */

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button Ingresar;
    private TextView MostrarAvance;
    private EditText User, Password;
    private CheckBox Recordar;
    private BaseDeDatosUCV UCVbd;

    private static String APP_DIRECTORYFotos = "TrilceUCV/";
    private static String MEDIA_DIRECTORYFotos = APP_DIRECTORYFotos + "Fotos/";
    private String LOGIN = "http://kpfp.pe.hu/conectkevin/UCV_datos_usuarios_GETALL.php";
    private String URLSilabosPDF = "http://kpfp.pe.hu/pdfs/";
    private String AñoSilabo = "_silabo_201602.pdf";
    private static final String APP_DIRECTORY = "TrilceUCV/";
    private static final String MEDIA_DIRECTORY = APP_DIRECTORY + "PDF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        UCVbd= new BaseDeDatosUCV(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Ingresar = (Button) findViewById(R.id.login_ingresar);
        MostrarAvance = (TextView) findViewById(R.id.MostrarAvance);
        Ingresar.setOnClickListener(this);
        User = (EditText) findViewById(R.id.Login_User);
        Password = (EditText) findViewById(R.id.Login_Password);
        Recordar = (CheckBox) findViewById(R.id.No_Cerrar_Sesión);

        if(GuardarDatos.CargarNo_Cerrar_Secion(this)){
            Ingreso();
        }
    }

    @Override
    public void onClick(View v) {

        MostrarAvance.setText("Conectandose a la base de datos");

        if(ModoOffline() && !DetectarInternet.isOnline(this)){
            Toast.makeText(this,"Modo Offline Activado...",Toast.LENGTH_SHORT).show();
            Ingreso();
            return;
        }
        /*else if(!(ModoOffline() && !DetectarInternet.isOnline(this))){
            Toast.makeText(this,"Usuario o contraseña incorrecta",Toast.LENGTH_SHORT).show();
        }*/
        else if (DetectarInternet.isOnline(this)) {
            String user = User.getText().toString().toLowerCase();
            VerificarDatos(LOGIN+"?id="+user);
        }
        else{
            MostrarAvance.setText("");
            Toast.makeText(this,"No tiene conexion a internet",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean VerificarLogin(JSONObject JSONLOGIN){
        MostrarAvance.setText("Verificando contraseña");
        String Usuario =  User.getText().toString();
        String Contraseña = Password.getText().toString();
        Usuario = Usuario.toLowerCase();
        Contraseña = Contraseña.toLowerCase();
        try {
            String user = JSONLOGIN.getJSONObject("user").getString("id");
            String password = JSONLOGIN.getJSONObject("user").getString("Password");
            if (Usuario.compareTo(user)==0 && Contraseña.compareTo(password)==0) {
                boolean recordar;
                if(Recordar.isChecked())recordar = true;
                else recordar = false;
                GuardarUsuarioEnBaseDeDatos(user,JSONLOGIN.toString(),recordar);
                return true;
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void GuardarUsuarioEnBaseDeDatos(String user, String CadenaDeDatos,boolean recordar){
        UCVbd.agregar(0,user,CadenaDeDatos);
        String Codigos[] = UCVbd.obtenerCodigos(0);
        MostrarAvance.setText("Descargando Silabos...0%");
        new DescargarSilabos().execute(Codigos);
        GuardarDatos.GuardarUsuario(this,user);
        GuardarDatos.GuardarNo_Cerrar_Secion(this,recordar);
    }

    private class DescargarSilabos extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            double MAX = 100.0/strings.length;
            for(int i=0;i<strings.length;i++) {
                String fileUrl = URLSilabosPDF + strings[i] + AñoSilabo;
                String fileName = strings[i] + AñoSilabo;
                String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                File folder = new File(extStorageDirectory, MEDIA_DIRECTORY);
                boolean isDirectoryCreated = folder.mkdirs();
                File pdfFile = new File(folder, fileName);
                boolean ExisteElArchivo = pdfFile.exists();
                if(!ExisteElArchivo) {
                    try {
                        pdfFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    downloadFile(fileUrl, pdfFile, strings.length, (i + 1), MAX);
                }
                else{
                    publishProgress((int)(MAX*(i+1)));
                }
            }
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File file = new File(extStorageDirectory,MEDIA_DIRECTORYFotos+"User_Foto.jpg");
            boolean isDirectoryCreated = file.getParentFile().mkdirs();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                Bitmap foto = BitmapFactory.decodeResource(getResources(), R.drawable.ic_contact_icon);
                foto.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return isDirectoryCreated;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            MostrarAvance.setText("Descargando Silabos..."+ values[0] +"%");
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            if(aVoid){
                Toast.makeText(Login.this, "Carpetas creadas Correctamente", Toast.LENGTH_SHORT).show();
            }
            Ingreso();
        }

        private static final int  MEGABYTE = 1024 * 1024;

        void downloadFile (String fileUrl, File directory, int max, int i,double prog){
            try {
                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(directory);
                double totalSize = urlConnection.getContentLength();
                byte[] buffer = new byte[MEGABYTE];
                double bufferLength = 0;
                long total = 0;
                while((bufferLength = inputStream.read(buffer))>0 ){
                    total += bufferLength;
                    publishProgress((int)(((((((total*100)/totalSize)))/max))+prog*i));
                    fileOutputStream.write(buffer, 0,(int) bufferLength);
                }
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean VerificarLoginOffline(String User, String password){

        User = User.toLowerCase();
        password = password.toLowerCase();

        Cursor fila = UCVbd.obtenerALL();
        if (fila.moveToFirst()) {
            do {
                String datos = fila.getString(2);
                String user_data="";
                String password_data = "";
                try {
                    JSONObject JsonDatos = new JSONObject(datos);
                    user_data = JsonDatos.getJSONObject("user").getString("id");
                    password_data = JsonDatos.getJSONObject("user").getString("Password");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(user_data.compareTo(User)==0 && password_data.compareTo(password)==0){
                    if(Recordar.isChecked()) GuardarDatos.GuardarNo_Cerrar_Secion(this,true);
                    return true;
                }
            }while(fila.moveToNext());
        }
        return false;
    }

    public boolean ModoOffline(){
        return VerificarLoginOffline(this.User.getText().toString(),this.Password.getText().toString());
    }

    public void VerificarDatos(String url){

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MostrarAvance.setText("Verificando usuario");
                        try {
                            if(response.getString("estado").compareTo("1")==0){
                                if(!VerificarLogin(response)) ContraseñaIncorrecta();
                            }
                            else if (response.getString("estado").compareTo("2")==0) MensajeUserNoEncontrado();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MostrarAvance.setText("");
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    public void ContraseñaIncorrecta(){
        Toast.makeText(this,"Contraseña incorrecta",Toast.LENGTH_SHORT).show();
        MostrarAvance.setText("");
    }

    public void MensajeUserNoEncontrado(){
        MostrarAvance.setText("");
        Toast.makeText(this,"El usuario no existe",Toast.LENGTH_SHORT).show();
    }

    public void Ingreso(){
        Intent i = new Intent(this,Datos_Basicos.class);
        startActivity(i);
        //overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();
    }
}
