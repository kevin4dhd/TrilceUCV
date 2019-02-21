package com.company.proyect.kevinpiazzoli.trilceucv.MisAsignaturas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.company.proyect.kevinpiazzoli.trilceucv.R;

/**
 * Created by KevinPiazzoli on 25/10/2016.
 */

public class MisAsignaturas_Activity extends AppCompatActivity{

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Theme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.misasignaturas_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Aula Virtual Alumno");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablelayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        themeChanged();
    }

    public void Theme() {
        sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        int theme = sharedPreferences.getInt("THEME", 0);
        settingTheme(theme);
    }

    private void themeChanged() {
        Boolean themeChanged = sharedPreferences.getBoolean("THEMECHANGED", false);
        Boolean homeButton = true;
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
