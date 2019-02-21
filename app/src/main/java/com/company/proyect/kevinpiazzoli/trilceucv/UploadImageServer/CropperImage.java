package com.company.proyect.kevinpiazzoli.trilceucv.UploadImageServer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.GuardarDatos;
import com.company.proyect.kevinpiazzoli.trilceucv.ConexionInternet.DetectarInternet;
import com.company.proyect.kevinpiazzoli.trilceucv.R;
import com.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by KevinPiazzoli on 07/12/2016.
 */

public class CropperImage extends AppCompatActivity {

    private static String APP_DIRECTORY = "TrilceUCV/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "Fotos";
    private static String USER_FOTO = "User_Foto";
    private String mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
            + File.separator + USER_FOTO + ".jpg";

    private CropImageView cropImageView;
    private String encoded_string, image_name;
    private File newFile;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropper_image);

        image_name = GuardarDatos.CargarUsuario(this) + ".png";

        dialog = new ProgressDialog(this);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        cropImageView = (CropImageView) findViewById(R.id.CropImageView);
        Button Cancelar = (Button) findViewById(R.id.CropperCanlcer);
        Button Aceptar = (Button) findViewById(R.id.CropperAceptar);
        Button Girar = (Button) findViewById(R.id.Girar);
        if(extras!=null) {
            Uri uri = Uri.parse(extras.getString("img"));
            cropImageView.setAspectRatio(10, 10);
            cropImageView.setFixedAspectRatio(true);
            cropImageView.setGuidelines(1);
            try {
                cropImageView.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri));
            } catch (IOException ignored) {
                String img = extras.getString("img");
                mPath=img;
                if(img!=null) {
                    newFile = new File(img);
                    if(newFile.exists()) cropImageView.setImageBitmap(BitmapFactory.decodeFile(img));
                }
            }
        }

        Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Girar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImageView.rotateImage(90);
            }
        });

        Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DetectarInternet.isOnline(CropperImage.this)){
                    ProgressDialog.show(CropperImage.this, "","Cargando. Por favor Espere...", true);
                    AceptarCropper();
                }
                else Toast.makeText(CropperImage.this,"Necesita conexion a internet",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void AceptarCropper(){
        Bitmap bitmap = cropImageView.getCroppedImage();
        CrearDirectorio(bitmap);
    }

    private void CrearDirectorio(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();
        if(!isDirectoryCreated) isDirectoryCreated = file.mkdirs();
        if(isDirectoryCreated){
            if (bitmap.getWidth() > 500 || bitmap.getHeight() > 500)
                bitmap = redimensionarImagenMaximo(bitmap, 500, 500);

            mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY + File.separator + USER_FOTO + ".jpg";
            File newFile = new File(mPath);
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(newFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if(bitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] array = stream.toByteArray();
                encoded_string = Base64.encodeToString(array, 0);
            }
            ActualizarFoto(bitmap);
        }
    }

    private void ActualizarFoto(Bitmap bitmap){
        if(bitmap!= null){
            makeRequest();
        }
    }

    private void makeRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, "http://kpfpservice.000webhostapp.com/UploadImage.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(CropperImage.this,error.toString(),Toast.LENGTH_SHORT).show();
                finish();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("encoded_string",encoded_string);
                map.put("image_name",image_name);

                return map;
            }
        };
        requestQueue.add(request);
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
}
