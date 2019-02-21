package com.company.proyect.kevinpiazzoli.trilceucv.ActivitysPrincipales.LoginPackage;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.company.proyect.kevinpiazzoli.trilceucv.ActivitysPrincipales.ActivityPrincipal;
import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.BaseDeDatosUCV;
import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.GuardarDatos;
import com.company.proyect.kevinpiazzoli.trilceucv.ConexionInternet.DetectarInternet;
import com.company.proyect.kevinpiazzoli.trilceucv.ConexionInternet.MySingleton;
import com.company.proyect.kevinpiazzoli.trilceucv.ConexionInternet.VolleyS;
import com.company.proyect.kevinpiazzoli.trilceucv.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by Kevin Piazzoli on 26/09/2016.
 */

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button Ingresar;
    private TextInputLayout User, Password;
    private RadioButton Recordar;
    private BaseDeDatosUCV UCVbd;
    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    private String USUARIO_TRILCE_UCV;
    private String TOKEN;
    private String[] CODIGOS_PARA_DESCARGAR_CURSOS;
    private short RepetirAnimacionTWO = 1;
    private ProgressBar progressBar;
    private ProgressBar ProgressButton;
    private boolean PERMITIR_INGRESO = false;
    private RelativeLayout relativeLayout;
    private ImageView imgUser;
    private ImageView imgPassword;
    private ImageView CheckTrue;

    private static String APP_DIRECTORYFotos = "TrilceUCV/";
    private static String MEDIA_DIRECTORYFotos = APP_DIRECTORYFotos + "Fotos/";
    private String LOGIN = "http://kpfpservice.000webhostapp.com/UCV_datos_usuarios_GETALL.php";
    private String TOKEN_URL = "http://kpfpservice.000webhostapp.com/UCV_Token_INSERTandUPDATE.php";
    private String URLSilabosPDF = "http://kpfpservice.000webhostapp.com/pdfs/";
    private String AñoSilabo = "_silabo_201602.pdf";
    private static final String APP_DIRECTORY = "TrilceUCV/";
    private static final String MEDIA_DIRECTORY = APP_DIRECTORY + "PDF";

    private boolean flagBoolean = false;
    private boolean FOTO_DESCARGADA = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        if(GuardarDatos.CargarNo_Cerrar_Secion(this)){
            Ingreso();
        }
        volley = VolleyS.getInstance(this);
        UCVbd= new BaseDeDatosUCV(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Ingresar = (Button) findViewById(R.id.login_ingresar);
        Ingresar.setOnClickListener(this);
        User = (TextInputLayout) findViewById(R.id.Login_User);
        Password = (TextInputLayout) findViewById(R.id.Login_Password);
        Recordar = (RadioButton) findViewById(R.id.No_Cerrar_Sesión);
        relativeLayout = (RelativeLayout) findViewById(R.id.LoginPrincipal);
        imgUser = (ImageView) findViewById(R.id.img_user);
        imgPassword = (ImageView) findViewById(R.id.img_password);
        ProgressButton = (ProgressBar) findViewById(R.id.ProgressButton);
        ProgressButton.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.md_blue_500),android.graphics.PorterDuff.Mode.MULTIPLY);
        CheckTrue = (ImageView) findViewById(R.id.img_check);
        TextWatcher();

        Recordar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Recordar.isChecked()) {
                    if (!flagBoolean) {
                        Recordar.setChecked(true);
                        flagBoolean = true;
                    } else {
                        flagBoolean = false;
                        Recordar.setChecked(false);
                    }
                }
            }
        });
    }

    private void RunAnimationTextView(final TextView tv) {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.text_view_presentacion);
        a.reset();
        tv.clearAnimation();
        tv.startAnimation(a);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                RepetirAnimacionTWO++;
                if(RepetirAnimacionTWO>1) {
                    tv.setText(Mensajes_Presentacion.RetornarMensajePresentacion(Login.this));
                    RepetirAnimacionTWO=0;
                    if(PERMITIR_INGRESO && FOTO_DESCARGADA){
                        tv.clearAnimation();
                        tv.setVisibility(View.GONE);
                        GuardarDatos.GuardarNo_Cerrar_Secion(Login.this,Recordar.isChecked());
                        Ingreso();
                    }
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        hideKeyboard();
        String user = User.getEditText().getText().toString();
        String Pass = Password.getEditText().getText().toString();

        boolean a = ValidarUser(user);
        boolean b = ValidarPassWord(Pass);

        if (a && b) {
            if(ModoOffline() && !DetectarInternet.isOnline(this)){
                IniciarAnimacionButton();
                IniciarPresentacionOffLine();
            }
            else if (DetectarInternet.isOnline(this)) {
                IniciarAnimacionButton();
                VerificarDatos(LOGIN+"?id="+user);
            }
            else{
                Toast.makeText(this, R.string.no_tiene_conexion_a_internet,Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void IniciarAnimacionButton(){
        final Animation ClickAnimation = AnimationUtils.loadAnimation(this, R.anim.click_button);
        Ingresar.startAnimation(ClickAnimation);
        Ingresar.setEnabled(false);
        ClickAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                Ingresar.setVisibility(View.GONE);
                Ingresar.setEnabled(true);
                ProgressButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void TextWatcher(){
        EditText campoNombre = (EditText) findViewById(R.id.Login_User_campo);
        EditText campoPassword= (EditText) findViewById(R.id.Login_Password_campo);

        campoNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ValidarUser(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        campoPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Password.setError(null);
                imgPassword.setColorFilter(ContextCompat.getColor(Login.this,R.color.md_blue_500));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void ContraseñaIncorrecta(){
        Password.setError(getString(R.string.contraseña_incorrecta));
        imgPassword.setColorFilter(ContextCompat.getColor(this,R.color.md_red_500));
        Ingresar.setVisibility(View.VISIBLE);
        ProgressButton.setVisibility(View.GONE);
    }

    public void MensajeUserNoEncontrado(){
        User.setError(getString(R.string.el_usuario_no_existe));
        imgUser.setColorFilter(ContextCompat.getColor(this,R.color.md_red_500));
        Ingresar.setVisibility(View.VISIBLE);
        ProgressButton.setVisibility(View.GONE);
    }

    private boolean ValidarUser(String Usuario){
        Pattern patron = Pattern.compile("^[a-zA-Z0-9]+$");
            if(Usuario.length() < 1) {
                User.setError(getString(R.string.usuario_vacio));
                imgUser.setColorFilter(ContextCompat.getColor(Login.this, R.color.md_red_500));
                return false;
            }
            if (!patron.matcher(Usuario).matches() || Usuario.length() > 20) {
                User.setError(getString(R.string.usuario_invalido));
                imgUser.setColorFilter(ContextCompat.getColor(Login.this, R.color.md_red_500));
                return false;
            } else {
                User.setError(null);
                imgUser.setColorFilter(ContextCompat.getColor(Login.this, R.color.md_blue_500));
            }
            return true;
    }

    private boolean ValidarPassWord(String PassWord){
        if (PassWord.length() < 1) {
            Password.setError(getString(R.string.contraseña_vacia));
            imgPassword.setColorFilter(ContextCompat.getColor(Login.this,R.color.md_red_500));
            return false;
        } else{
            Password.setError(null);
            imgPassword.setColorFilter(ContextCompat.getColor(Login.this,R.color.md_blue_500));
        }
        return true;
    }

    boolean VerificarLogin(JSONObject JSONLOGIN){
        String Usuario =  User.getEditText().getText().toString();
        String Contraseña = Password.getEditText().getText().toString();
        Usuario = Usuario.toLowerCase();
        Contraseña = Contraseña.toLowerCase();
        try {
            String user = JSONLOGIN.getJSONObject("user").getString("id");
            String password = JSONLOGIN.getJSONObject("user").getString("Password");
            if (Usuario.compareTo(user)==0 && Contraseña.compareTo(password)==0) {
                GuardarUsuarioEnBaseDeDatos(user,JSONLOGIN.toString());
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    void GuardarUsuarioEnBaseDeDatos(final String user, final String CadenaDeDatos){
        final Animation AnimationCheck = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        ProgressButton.setVisibility(View.GONE);
        CheckTrue.setVisibility(View.VISIBLE);
        CheckTrue.startAnimation(AnimationCheck);
        AnimationCheck.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                IniciarPresentacion();
                USUARIO_TRILCE_UCV = user;
                UCVbd.agregar(0,user,CadenaDeDatos) ;
                UCVbd.actualizar(0,CadenaDeDatos);
                CODIGOS_PARA_DESCARGAR_CURSOS = UCVbd.obtenerCodigos(0);
                GuardarDatos.GuardarUsuario(Login.this,user);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
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
                        boolean pdfFiles = pdfFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    downloadFile(fileUrl, pdfFile, strings.length,i, MAX);
                }
                else{
                    publishProgress((int)(MAX*(i+1)));
                }
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            if(DetectarInternet.isOnline(Login.this)) {
                volley = VolleyS.getInstance(Login.this.getApplicationContext());
                fRequestQueue = volley.getRequestQueue();
                SubirTokenAlABaseDeDatos(fRequestQueue,volley);
                progressBar.setProgress(100);
            }
            else{
                Toast.makeText(Login.this, R.string.necesita_estar_conectado_a_internet,Toast.LENGTH_SHORT).show();
                finish();
            }
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
                    int TotalProgreso = (int)(((((((total*100)/totalSize)))/max))+prog*i);
                    publishProgress(TotalProgreso);
                    fileOutputStream.write(buffer, 0,(int) bufferLength);
                }
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class  DescargarFotoHilo extends AsyncTask<Bitmap, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Bitmap... bitmap) {
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            final File file = new File(extStorageDirectory,MEDIA_DIRECTORYFotos+"User_Foto.jpg");
            boolean isDirectoryCreated = file.getParentFile().mkdirs();
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(file);
                bitmap[0].compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            } catch (IOException ignored) {}
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            FOTO_DESCARGADA=true;
        }
    }

    boolean VerificarLoginOffline(String User, String password){
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

    boolean ModoOffline(){
        return VerificarLoginOffline(this.User.getEditText().getText().toString(),this.Password.getEditText().getText().toString());
    }

    void VerificarDatos(String url){
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                        Toast.makeText(Login.this, R.string.ocurrio_un_error,Toast.LENGTH_SHORT).show();
                        Ingresar.setVisibility(View.VISIBLE);
                        ProgressButton.setVisibility(View.GONE);
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    void Ingreso(){
        Intent i = new Intent(this,ActivityPrincipal.class);
        startActivity(i);
        finish();
    }

    private void SubirTokenAlABaseDeDatos(RequestQueue fRequestQueue, VolleyS volley){
        HashMap<String, String> parametros = new HashMap();
        parametros.put("id", USUARIO_TRILCE_UCV);
        TOKEN = FirebaseInstanceId.getInstance().getToken();
        if(TOKEN != null)parametros.put("token", TOKEN);
        else parametros.put("token", "No define");

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                TOKEN_URL,
                new JSONObject(parametros),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PERMITIR_INGRESO = true;
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        VolleyS.addToQueue(jsArrayRequest,fRequestQueue,Login.this,volley);
    }

    void IniciarPresentacion(){
        Animation a = AnimationUtils.loadAnimation(this, R.anim.left_in2);
        a.reset();
        relativeLayout.clearAnimation();
        relativeLayout.startAnimation(a);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setContentView(R.layout.login_presentacion);

                String URLImage = "http://kpfpservice.000webhostapp.com/img/"+GuardarDatos.CargarUsuario(Login.this)+".png";

                RelativeLayout Login_Presentacion = (RelativeLayout) findViewById(R.id.Login_Presentacion);
                Login_Presentacion.startAnimation(AnimationUtils.loadAnimation(Login.this, R.anim.derecha_in));
                TextView mensajes_Presentacion = (TextView) findViewById(R.id.Mensajes_Presentacion);
                TextView Nombre_User_Principal = (TextView) findViewById(R.id.Name_User_Principal);
                final ImageView FotoUsuario = (ImageView) findViewById(R.id.FotoUsuario);
                final ProgressBar loadingImage = (ProgressBar) findViewById(R.id.LoadingFotoUser);
                progressBar = (ProgressBar) findViewById(R.id.ActualizandoDatosBar);

                Picasso.with(Login.this).load(URLImage).error(R.drawable.ic_contact_icon).into(FotoUsuario, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        FotoUsuario.setVisibility(View.VISIBLE);
                        loadingImage.setVisibility(View.GONE);
                        new DescargarFotoHilo().execute(((BitmapDrawable)FotoUsuario.getDrawable()).getBitmap());
                    }
                    @Override
                    public void onError() {
                        FotoUsuario.setVisibility(View.VISIBLE);
                        loadingImage.setVisibility(View.GONE);
                        new DescargarFotoHilo().execute(((BitmapDrawable)FotoUsuario.getDrawable()).getBitmap());
                    }
                });

                Nombre_User_Principal.setText(UCVbd.getNameUserPrincipal(0));
                mensajes_Presentacion.setText(Mensajes_Presentacion.RetornarMensajePresentacion(Login.this));
                RunAnimationTextView(mensajes_Presentacion);
                new DescargarSilabos().execute(CODIGOS_PARA_DESCARGAR_CURSOS);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    void IniciarPresentacionOffLine(){
        Animation a = AnimationUtils.loadAnimation(this, R.anim.left_in2);
        a.reset();
        relativeLayout.clearAnimation();
        relativeLayout.startAnimation(a);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setContentView(R.layout.login_presentacion);

                RelativeLayout Login_Presentacion = (RelativeLayout) findViewById(R.id.Login_Presentacion);
                Login_Presentacion.startAnimation(AnimationUtils.loadAnimation(Login.this, R.anim.derecha_in));
                TextView mensajes_Presentacion = (TextView) findViewById(R.id.Mensajes_Presentacion);
                TextView Nombre_User_Principal = (TextView) findViewById(R.id.Name_User_Principal);
                ImageView FotoUsuario = (ImageView) findViewById(R.id.FotoUsuario);
                progressBar = (ProgressBar) findViewById(R.id.ActualizandoDatosBar);
                TextView Mensaje_Bioenvenida = (TextView) findViewById(R.id.message_bienvenida);
                ProgressBar loadingImage = (ProgressBar) findViewById(R.id.LoadingFotoUser);

                FotoUsuario.setVisibility(View.VISIBLE);
                loadingImage.setVisibility(View.GONE);
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORYFotos+"User_Foto.jpg");
                Bitmap bitmap;
                if(file.exists()) bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORYFotos+"User_Foto.jpg");
                else bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_contact_icon);
                FotoUsuario.setImageBitmap(bitmap);

                Nombre_User_Principal.setText(UCVbd.getNameUserPrincipal(0));
                mensajes_Presentacion.setText(Mensajes_Presentacion.RetornarMensajePresentacion(Login.this));
                RunAnimationTextView(mensajes_Presentacion);
                Mensaje_Bioenvenida.setText(R.string.mod_offline_activado);
                progressBar.setProgress(100);
                PERMITIR_INGRESO = true;
                FOTO_DESCARGADA = true;
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

}
