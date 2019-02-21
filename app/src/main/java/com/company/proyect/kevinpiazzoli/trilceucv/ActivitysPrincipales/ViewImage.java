package com.company.proyect.kevinpiazzoli.trilceucv.ActivitysPrincipales;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.company.proyect.kevinpiazzoli.trilceucv.ConexionInternet.DetectarInternet;
import com.company.proyect.kevinpiazzoli.trilceucv.R;
import com.squareup.picasso.Picasso;

/**
 * Created by KevinPiazzoli on 12/11/2016.
 */

public class ViewImage extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        imageView = (ImageView) findViewById(R.id.imageView);
        setSupportActionBar(toolbar);
        setTitle("");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent extras = getIntent();
        Bundle bundle = extras.getExtras();

        if(bundle!=null){
            String Path = bundle.getString("key_bitmap");
            String user = bundle.getString("user");
            Bitmap bitmap = BitmapFactory.decodeFile(Path);
            if(DetectarInternet.isOnline(this)) Picasso.with(this).load("http://kpfpservice.000webhostapp.com/img/"+user+".png").into(imageView);
            else imageView.setImageBitmap(bitmap);
        }
    }

}
