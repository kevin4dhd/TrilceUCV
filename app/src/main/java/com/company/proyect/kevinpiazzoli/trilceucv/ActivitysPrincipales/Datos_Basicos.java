package com.company.proyect.kevinpiazzoli.trilceucv.ActivitysPrincipales;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.BaseDeDatosUCV;
import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.GuardarDatos;
import com.company.proyect.kevinpiazzoli.trilceucv.ConexionInternet.MySingleton;
import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Asig_matriculadas.Asignaturas_Matriculadas;
import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.BoletaDeNotas.Boleta_De_Notas;
import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Comunicador;
import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Datos_Basic;
import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Noticias;
import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.NotificacionesBeta;
import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.View_Noticia;
import com.company.proyect.kevinpiazzoli.trilceucv.R;
import com.company.proyect.kevinpiazzoli.trilceucv.Settings.Preferencias;
import com.company.proyect.kevinpiazzoli.trilceucv.notifications.PushNotificationsFragment;
import com.company.proyect.kevinpiazzoli.trilceucv.notifications.PushNotificationsPresenter;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;

/**
 * Created by Kevin Piazzoli on 27/09/2016.
 */

public class Datos_Basicos extends AppCompatActivity
        implements Comunicador,
        NavigationView.OnNavigationItemSelectedListener,
        Asignaturas_Matriculadas.OnFragmentInteractionListener,
        Boleta_De_Notas.OnFragmentInteractionListener,
        Datos_Basic.OnFragmentInteractionListener,
        Noticias.OnFragmentInteractionListener,
        View_Noticia.OnFragmentInteractionListener,
        NotificacionesBeta.OnFragmentInteractionListener,
        View.OnClickListener {

    private static String APP_DIRECTORY = "TrilceUCV/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "Fotos";
    private static String USER_FOTO = "User_Foto";
    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;
    private String mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
            + File.separator + USER_FOTO + ".jpg";
    private PushNotificationsFragment mNotificationsFragment;
    private PushNotificationsPresenter mNotificationsPresenter;

    private ImageView profileimage;
    private LinearLayout mRlView;

    private Bitmap foto;
    private String UPLOAD_URL ="http://kpfp.pe.hu/conectkevin/UploadToServer.php";
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int theme;
    private DrawerLayout drawer;
    private List<Integer> fragmentAuxiliar = new ArrayList<>();
    private Boolean homeButton = false, themeChanged;
    private BaseDeDatosUCV bd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Theme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_basicos);
        bd = new BaseDeDatosUCV(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);
        profileimage = (ImageView)headerLayout.findViewById(R.id.circle_image);
        TextView NombreyApellido = (TextView) headerLayout.findViewById(R.id.NombrePrincipal);
        TextView Carrera = (TextView) headerLayout.findViewById(R.id.Carrera);
        String NombreyCarrera[] = bd.ObtenerNombresyApellidos(0);
        NombreyApellido.setText(NombreyCarrera[0]);
        Carrera.setText(NombreyCarrera[1]);

        mRlView = (LinearLayout) headerLayout.findViewById(R.id.rl_view);

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))   {
            CargarFotoPerfil();
        } else {
            Log.i("SDCARDINFO","No se encuentra SD Card.");
        }

        //DescargarImagenFoto("http://kpfp.pe.hu/img/prolife_imagenkfelix.png");

        if(mayRequestStoragePermission())profileimage.setOnClickListener(this);
        themeChanged();

        if(fragmentAuxiliar.size()==0 && savedInstanceState == null) {
            IniciarLayout();
            fragmentAuxiliar.add(R.id.Noticias);
        }
    }

    @Override
    public void onClick(View view) {
        showOptions();
    }

    @Override
    public void responder(String datos) {
        Toast.makeText(this,datos,Toast.LENGTH_SHORT).show();
    }

    public void notificacion(String datos) {
        //Construccion de la accion del intent implicito
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("www.google.com"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);

        //Construccion de la notificacion
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle("Hoalsdasdsadsadsa");
        builder.setContentText("Momento para aprender :v");
        builder.setSubText("Tocame :v");

        //envia la notificacion
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());
    }

    @Override
    public void EntrarALaNoticia(String Titulo, String Descripcion, String Fecha) {
        Bundle arguments = new Bundle();
        arguments.putString("Descripcion", Descripcion);
        arguments.putString("Fecha", Fecha);

        View_Noticia fragment = View_Noticia.newInstance(arguments);

        FragmentTransaction frag = getSupportFragmentManager().beginTransaction();
        frag.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.derecha_in, R.anim.derecha_out);
        frag.addToBackStack(null);
        frag.replace(R.id.content_main, fragment).commit();
        getSupportActionBar().setTitle(Titulo);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(fragmentAuxiliar.size()>0)fragmentAuxiliar.remove(fragmentAuxiliar.size()-1);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawer.closeDrawers();
        int id = item.getItemId();
        String TituloBarActivity = "Error al agregar titulo";

        Fragment fragment = null;
        boolean fragmentTransaction = false;

        if (id == R.id.Noticias) {
            if (id != fragmentAuxiliar.get(fragmentAuxiliar.size() - 1)){
                IniciarNoticias();
                fragmentAuxiliar.add(id);
                return true;
            }
            return false;
        } else if (id == R.id.Datos_Basicos) {
            fragment = new Datos_Basic();
            fragmentTransaction = true;
            TituloBarActivity = "Información Personal";
        }
        else if (id == R.id.Notificaciones) {
            fragment = new NotificacionesBeta();
            fragmentTransaction = true;
            TituloBarActivity = "Notificaciones(Beta)";
        }
        else if (id == R.id.asignaturas_matriculadas) {
            fragment = new Asignaturas_Matriculadas();
            fragmentTransaction = true;
            TituloBarActivity = "Mis Asignaturas";
        } else if (id == R.id.boleta_de_notas) {
            fragment = new Boleta_De_Notas();
            fragmentTransaction = true;
            TituloBarActivity = "Mis Asignaturas";
        } else if (id == R.id.preferencias) {
            fragment = new Preferencias();
            fragmentTransaction = true;
            TituloBarActivity = "Configuración";
        } else if (id == R.id.CerrarSesion) {
            Intent i = new Intent(this, Login.class);
            startActivity(i);
            GuardarDatos.GuardarNo_Cerrar_Secion(this, false);
            finish();
        }

        if (id != fragmentAuxiliar.get(fragmentAuxiliar.size() - 1)) {
            if (fragmentTransaction) {
                FragmentTransaction frag = getSupportFragmentManager().beginTransaction();
                frag.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.derecha_in, R.anim.derecha_out);
                frag.addToBackStack(null);
                frag.replace(R.id.content_main, fragment).commit();
                getSupportActionBar().setTitle(TituloBarActivity);
            }
            ((FrameLayout) findViewById(R.id.content_main)).removeAllViews();
            fragmentAuxiliar.add(id);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void IniciarLayout(){
        //mNotificationsFragment = (PushNotificationsFragment) getSupportFragmentManager().findFragmentById(R.id.content_main);
        mNotificationsFragment = PushNotificationsFragment.newInstance();
        getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.derecha_in, R.anim.derecha_out)
                    .replace(R.id.content_main, mNotificationsFragment)
                    .commit();
        mNotificationsPresenter = new PushNotificationsPresenter(mNotificationsFragment, FirebaseMessaging.getInstance());
    }

    public void IniciarNoticias(){
        mNotificationsFragment = PushNotificationsFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.derecha_in, R.anim.derecha_out)
                .replace(R.id.content_main, mNotificationsFragment)
                .commit();
        mNotificationsPresenter = new PushNotificationsPresenter(mNotificationsFragment, FirebaseMessaging.getInstance());
        ((FrameLayout) findViewById(R.id.content_main)).removeAllViews();
        getSupportActionBar().setTitle("Noticias");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    private void showOptions() {
        final CharSequence[] option = {"Cámara", "Galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(Datos_Basicos.this);
        builder.setTitle("Elige una opción");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(option[which] == "Cámara")
                    CrearDirectorio(0);
                else if(option[which] == "Galeria"){
                    CrearDirectorio(1);
                }else {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    private void CrearDirectorio(int i) {
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){
            mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
                    + File.separator + USER_FOTO + ".jpg";

            File newFile = new File(mPath);

            switch (i) {
                case 0:
                    opencamera(newFile);
                    break;
                case 1:
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), SELECT_PICTURE);
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_CODE:
                    CargarFotoPerfil();
                    //uploadImage();
                    break;
                case SELECT_PICTURE:
                    Uri path = data.getData();
                    foto = null;
                    try {
                        foto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    HiloImagenGaleria imagen = new HiloImagenGaleria();
                    imagen.execute();
                    //uploadImage();
                    break;
                    }
            }
        }

    public void opencamera(File newFile){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
        startActivityForResult(intent, PHOTO_CODE);
    }

    public void CargarFotoPerfil(){
        HiloImagen imagen = new HiloImagen();
        imagen.execute();
    }

    private class HiloImagen extends AsyncTask<Void, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bitmap = BitmapFactory.decodeFile(mPath);
            while(bitmap.getWidth()>=600 || bitmap.getHeight()>=600)bitmap=redimensionarImagenMaximo(bitmap,bitmap.getWidth()/2,bitmap.getHeight()/2);
            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            profileimage.setImageBitmap(bitmap);
        }
    }

    private class HiloImagenGaleria extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {
            File newFile = new File(mPath);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(newFile);
                foto.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeFile(mPath);
            while(bitmap.getWidth()>=600 || bitmap.getHeight()>=600)bitmap=redimensionarImagenMaximo(bitmap,bitmap.getWidth()/2,bitmap.getHeight()/2);
            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            profileimage.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path", mPath);
        outState.putInt("fragment_auxiliar",fragmentAuxiliar.get(fragmentAuxiliar.size()-1));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(Datos_Basicos.this, "Permisos aceptados", Toast.LENGTH_SHORT).show();
                profileimage.setOnClickListener(this);
            }
        }else{
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Datos_Basicos.this);
        builder.setTitle("Permisos denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPath = savedInstanceState.getString("file_path");
        fragmentAuxiliar.add(savedInstanceState.getInt("fragment_auxiliar"));
    }

    private boolean mayRequestStoragePermission() {

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(mRlView, "Los permisos son necesarios para poder usar la aplicación",
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                }
            });
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }

        return false;
    }

    public Bitmap redimensionarImagenMaximo(Bitmap mBitmap, float newWidth, float newHeigth){
        //Redimensionamos
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeigth) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
    }

    public void DescargarImagenFoto(String url){
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        profileimage.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(Datos_Basicos.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(foto);

                //Getting Image Name
                String name = GuardarDatos.CargarUsuario(Datos_Basicos.this);

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }





    public void Theme() {
        sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        theme = sharedPreferences.getInt("THEME", 0);
        settingTheme(theme);
    }

    private void themeChanged() {
        themeChanged = sharedPreferences.getBoolean("THEMECHANGED", false);
        homeButton = true;
    }

    public void setThemeFragment(int theme) {
        switch (theme) {
            case 0:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 0).apply();
                break;
            case 1:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 1).apply();
                break;
            case 2:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 2).apply();
                break;
            case 3:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 3).apply();
                break;
            case 4:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 4).apply();
                break;
            case 5:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 5).apply();
                break;
            case 6:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 6).apply();
                break;
            case 7:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 7).apply();
                break;
            case 8:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 8).apply();
                break;
            case 9:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 9).apply();
                break;
            case 10:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 10).apply();
                break;
            case 11:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 11).apply();
                break;
            default:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 0).apply();
                break;
        }
    }

    public void settingTheme(int theme) {
        switch (theme) {
            case 0:
                setTheme(R.style.AppThemeUCV);
                break;
            case 1:
                setTheme(R.style.AppTheme);
                break;
            case 2:
                setTheme(R.style.AppTheme2);
                break;
            case 3:
                setTheme(R.style.AppTheme3);
                break;
            case 4:
                setTheme(R.style.AppTheme4);
                break;
            case 5:
                setTheme(R.style.AppTheme5);
                break;
            case 6:
                setTheme(R.style.AppTheme6);
                break;
            case 7:
                setTheme(R.style.AppTheme7);
                break;
            case 8:
                setTheme(R.style.AppTheme8);
                break;
            case 9:
                setTheme(R.style.AppTheme9);
                break;
            case 10:
                setTheme(R.style.AppTheme10);
                break;
            case 11:
                setTheme(R.style.AppTheme11);
                break;
            default:
                setTheme(R.style.AppThemeUCV);
                break;
        }
    }

}
