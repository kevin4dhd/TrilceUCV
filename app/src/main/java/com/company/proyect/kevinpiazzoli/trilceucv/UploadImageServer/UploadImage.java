package com.company.proyect.kevinpiazzoli.trilceucv.UploadImageServer;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.company.proyect.kevinpiazzoli.trilceucv.ConexionInternet.DetectarInternet;
import com.company.proyect.kevinpiazzoli.trilceucv.R;

import java.io.File;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;

/**
 * Created by KevinPiazzoli on 05/12/2016.
 */

public class UploadImage extends AppCompatActivity{

    private static String APP_DIRECTORY = "TrilceUCV/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "Fotos";
    private static String USER_FOTO = "User_Foto";
    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;
    private String mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
            + File.separator + USER_FOTO + ".jpg";

    private ImageView buttonCamera;
    private ImageView buttonGallery;
    private ImageView ivImage;
    private RelativeLayout mRlView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        buttonCamera = (ImageView) findViewById(R.id.ivCamera);
        buttonGallery = (ImageView) findViewById(R.id.ivGallery);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        mRlView = (RelativeLayout) findViewById(R.id.rl_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(mayRequestStoragePermission()){
            buttonCamera.setVisibility(View.VISIBLE);
            buttonGallery.setVisibility(View.VISIBLE);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DetectarInternet.isOnline(UploadImage.this))CrearDirectorio(0);
                else Toast.makeText(UploadImage.this,"No estas conectado a internet",Toast.LENGTH_SHORT).show();
            }
        });

        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DetectarInternet.isOnline(UploadImage.this))CrearDirectorio(1);
                else Toast.makeText(UploadImage.this,"No estas conectado a internet",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CargarFotoPerfil() {
        Bitmap bitmap = BitmapFactory.decodeFile(mPath);
        ivImage.setImageBitmap(bitmap);
    }

    private void CrearDirectorio(int i) {
        switch (i) {
            case 0:
                String mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY + File.separator + USER_FOTO + "temp.jpg";
                opencamera(new File(mPath));
                break;
            case 1:
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, getString(R.string.selecciona_una_imagen)), SELECT_PICTURE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_CODE:
                    String mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY + File.separator + USER_FOTO + "temp.jpg";
                    performCrop(Uri.parse(mPath));
                    break;
                case SELECT_PICTURE:
                    performCrop(data.getData());
                    break;
            }
        }
    }

    private void performCrop(Uri picUri){
            Intent cropIntent = new Intent(this,CropperImage.class);
            cropIntent.putExtra("img", picUri.toString());
            startActivityForResult(cropIntent, 2);
    }

    public void opencamera(File newFile){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
        startActivityForResult(intent, PHOTO_CODE);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        CargarFotoPerfil();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path", mPath);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(UploadImage.this, R.string.permisos_aceptados, Toast.LENGTH_SHORT).show();
                buttonCamera.setVisibility(View.VISIBLE);
                buttonGallery.setVisibility(View.VISIBLE);
            }
        }else{
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadImage.this);
        builder.setTitle(R.string.permisos_denegados);
        builder.setMessage(R.string.para_usar_las_funciones);
        builder.setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
    }

    private boolean mayRequestStoragePermission() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;
        if((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;
        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(mRlView, R.string.Los_permisos_son_necesarios_para_usar_la_aplicacion,
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

}
