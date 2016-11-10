package com.company.proyect.kevinpiazzoli.trilceucv.RestApi.adapter;

import com.company.proyect.kevinpiazzoli.trilceucv.RestApi.ConstantesRestApi;
import com.company.proyect.kevinpiazzoli.trilceucv.RestApi.Endpoints;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by KevinPiazzoli on 08/11/2016.
 */

public class RestApiAdapter {

    public Endpoints establecerConexionRestAPI(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantesRestApi.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                ;
        return retrofit.create(Endpoints.class);
    }

}
