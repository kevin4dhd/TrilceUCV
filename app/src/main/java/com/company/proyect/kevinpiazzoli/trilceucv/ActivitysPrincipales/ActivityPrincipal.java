package com.company.proyect.kevinpiazzoli.trilceucv.ActivitysPrincipales;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.company.proyect.kevinpiazzoli.trilceucv.ActivitysPrincipales.LoginPackage.Login;
import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.BaseDeDatosUCV;
import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.GuardarDatos;
import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Asig_matriculadas.Asignaturas_Matriculadas;
import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.BoletaDeNotas.Boleta_De_Notas;
import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Comunicador;
import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Datos_Basicos.Datos_Basic;
import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Notificaciones.NotificacionesBeta;
import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.PAmigos.Amigos;
import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.VerNotificaciones.Fragment_Notificaciones;
import com.company.proyect.kevinpiazzoli.trilceucv.R;
import com.company.proyect.kevinpiazzoli.trilceucv.Settings.Preferencias;
import com.company.proyect.kevinpiazzoli.trilceucv.UploadImageServer.UploadImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin Piazzoli on 27/09/2016.
 */

public class ActivityPrincipal extends AppCompatActivity
        implements Comunicador,
        NavigationView.OnNavigationItemSelectedListener,
        Asignaturas_Matriculadas.OnFragmentInteractionListener,
        Boleta_De_Notas.OnFragmentInteractionListener,
        Datos_Basic.OnFragmentInteractionListener,
        NotificacionesBeta.OnFragmentInteractionListener,
        Amigos.OnFragmentInteractionListener,
        Fragment_Notificaciones.OnFragmentInteractionListener,
        View.OnClickListener {

    private static String APP_DIRECTORY = "TrilceUCV/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "Fotos";
    private static String USER_FOTO = "User_Foto";
    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;
    private String mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
            + File.separator + USER_FOTO + ".jpg";

    private ImageView profileimage;
    private LinearLayout mRlView;
    private SearchView searchView;
    private ImageButton DeleteNotificationsAll;

    private Bitmap foto;
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int theme;
    private DrawerLayout drawer;
    private List<Integer> fragmentAuxiliar = new ArrayList<>();
    private BaseDeDatosUCV bd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Theme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_basicos);
        bd = new BaseDeDatosUCV(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchView= (SearchView) findViewById(R.id.Busqueda);
        DeleteNotificationsAll = (ImageButton) findViewById(R.id.DeleteNotificationsAll);

        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this, R.color.md_white_1000));
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);
        profileimage = (ImageView)headerLayout.findViewById(R.id.circle_image);
        profileimage.setOnClickListener(this);
        TextView NombreyApellido = (TextView) headerLayout.findViewById(R.id.NombrePrincipal);
        TextView Carrera = (TextView) headerLayout.findViewById(R.id.Carrera);

        String NombreyCarrera[] = bd.ObtenerNombresyApellidos(0);
        NombreyApellido.setText(NombreyCarrera[0]);
        Carrera.setText(NombreyCarrera[1]);
        Carrera.setOnClickListener(this);

        mRlView = (LinearLayout) headerLayout.findViewById(R.id.rl_view);
        themeChanged();

        if(fragmentAuxiliar.size()==0 && savedInstanceState == null) {
            fragmentAuxiliar.add(R.id.VerNotificaciones);
            IniciarLayout(R.id.VerNotificaciones);
        }

        Intent extras = getIntent();
        Bundle bundle = extras.getExtras();

        if(bundle!=null) {
            String FragmentoBundle = bundle.getString("FragmentNotificacion");
            if (FragmentoBundle != null) {
                switch (FragmentoBundle) {
                    case "Mensajes":
                        EntrarFragmento(R.id.Amigos);
                        break;
                    default:
                        EntrarFragmento(R.id.VerNotificaciones);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CargarFotoPerfil();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.circle_image:
                Intent intent = new Intent(ActivityPrincipal.this, UploadImage.class);
                startActivity(intent);
                break;
            case R.id.Carrera:
                drawer.closeDrawers();
                Fragment fragment = new Datos_Basic();
                String TituloBarActivity = getString(R.string.informacion_personal);
                if(fragmentAuxiliar.size()!=0){
                        if (R.id.Carrera != fragmentAuxiliar.get(fragmentAuxiliar.size() - 1)) {
                            FragmentTransaction frag = getSupportFragmentManager().beginTransaction();
                            frag.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.derecha_in, R.anim.derecha_out);
                            frag.addToBackStack(null);
                            frag.replace(R.id.content_main, fragment).commit();
                            setTitle(TituloBarActivity);
                            ((FrameLayout) findViewById(R.id.content_main)).removeAllViews();
                            fragmentAuxiliar.add(R.id.Carrera);
                        }
                }
                else {
                    FragmentTransaction frag = getSupportFragmentManager().beginTransaction();
                    frag.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.derecha_in, R.anim.derecha_out);
                    frag.addToBackStack(null);
                    frag.replace(R.id.content_main, fragment).commit();
                    setTitle(TituloBarActivity);
                    ((FrameLayout) findViewById(R.id.content_main)).removeAllViews();
                    fragmentAuxiliar.add(R.id.Carrera);
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                break;
        }
    }

    @Override
    public void responder(String datos) {
        Toast.makeText(this,datos,Toast.LENGTH_SHORT).show();
    }

    @Override
    public SearchView getSearchView() {
        return searchView;
    }

    @Override
    public void setSearchView(boolean b) {
        searchView.setVisibility(b ? View.VISIBLE : View.GONE);
    }


    @Override
    public ImageButton getImageButton() {
        DeleteNotificationsAll.setVisibility(View.VISIBLE);
        return DeleteNotificationsAll;
    }

    @Override
    public void offImageButton() {
        DeleteNotificationsAll.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            if(fragmentAuxiliar.size()>1) fragmentAuxiliar.remove(fragmentAuxiliar.size()-1);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawer.closeDrawers();
        int id = item.getItemId();
        return EntrarFragmento(id);
    }

    private boolean EntrarFragmento(int id){
        String TituloBarActivity = getString(R.string.error_al_agregar_el_titulo);
        Fragment fragment = null;
        boolean fragmentTransaction = false;

        if (id == R.id.VerNotificaciones) {
            fragment = new Fragment_Notificaciones();
            fragmentTransaction=true;
            TituloBarActivity = getString(R.string.notificaciones);
        }
        else if (id == R.id.Amigos) {
            fragment = new Amigos();
            fragmentTransaction = true;
            TituloBarActivity = getString(R.string.red_social);
        }
        else if (id == R.id.asignaturas_matriculadas) {
            fragment = new Asignaturas_Matriculadas();
            fragmentTransaction = true;
            TituloBarActivity = getString(R.string.mis_asignaturas);
        } else if (id == R.id.boleta_de_notas) {
            fragment = new Boleta_De_Notas();
            fragmentTransaction = true;
            TituloBarActivity = getString(R.string.mis_asignaturas);
        } else if (id == R.id.preferencias) {
            fragment = new Preferencias();
            fragmentTransaction = true;
            TituloBarActivity = getString(R.string.configuraci_n);
        } else if (id == R.id.CerrarSesion) {
            Intent i = new Intent(this, Login.class);
            startActivity(i);
            GuardarDatos.GuardarNo_Cerrar_Secion(this, false);
            finish();
        }

        if(fragmentAuxiliar.size()!=0) {
            if (id != fragmentAuxiliar.get(fragmentAuxiliar.size() - 1)) {
                if (fragmentTransaction) {
                    FragmentTransaction frag = getSupportFragmentManager().beginTransaction();
                    frag.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.derecha_in, R.anim.derecha_out);
                    frag.addToBackStack(null);
                    frag.replace(R.id.content_main, fragment).commit();
                    setTitle(TituloBarActivity);
                    ((FrameLayout) findViewById(R.id.content_main)).removeAllViews();
                    fragmentAuxiliar.add(id);
                }
            }
        }
        else {
            if (fragmentTransaction) {
                FragmentTransaction frag = getSupportFragmentManager().beginTransaction();
                frag.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.derecha_in, R.anim.derecha_out);
                frag.addToBackStack(null);
                frag.replace(R.id.content_main, fragment).commit();
                setTitle(TituloBarActivity);
                ((FrameLayout) findViewById(R.id.content_main)).removeAllViews();
                fragmentAuxiliar.add(id);
            }
        }
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void CargarFotoPerfil(){
        HiloImagen imagen = new HiloImagen();
        imagen.execute();
    }

    private class HiloImagen extends AsyncTask<Void, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bitmap = BitmapFactory.decodeFile(mPath);
            if(bitmap != null){
                while(bitmap.getWidth()>600 || bitmap.getHeight()>600) bitmap=redimensionarImagenMaximo(bitmap,600,600);
            }else{
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_contact_icon);
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            profileimage.setImageBitmap(bitmap);
        }
    }

    public Bitmap redimensionarImagenMaximo(Bitmap mBitmap, float newWidth, float newHeigth){
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float scaleWidth = newWidth / width;
        float scaleHeight = newHeigth / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
    }


    public void IniciarLayout(int id){
        Fragment  fragment = new Fragment_Notificaciones();
        FragmentTransaction frag = getSupportFragmentManager().beginTransaction();
        frag.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.derecha_in, R.anim.derecha_out);
        frag.replace(R.id.content_main, fragment).commit();
        setTitle(getString(R.string.notificaciones));
        ((FrameLayout) findViewById(R.id.content_main)).removeAllViews();
        fragmentAuxiliar.add(id);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}

    public void Theme() {
        sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        theme = sharedPreferences.getInt("THEME", 0);
        settingTheme(theme);
    }

    private void themeChanged() {
        Boolean themeChanged = sharedPreferences.getBoolean("THEMECHANGED", false);
        Boolean homeButton = true;
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
