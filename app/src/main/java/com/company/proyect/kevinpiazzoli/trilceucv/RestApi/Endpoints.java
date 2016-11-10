package com.company.proyect.kevinpiazzoli.trilceucv.RestApi;

import com.company.proyect.kevinpiazzoli.trilceucv.RestApi.model.UsuarioResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by KevinPiazzoli on 08/11/2016.
 */

public interface Endpoints {

    @FormUrlEncoded
    @POST(ConstantesRestApi.KEY_POST_ID_TOKEN)
    Call<UsuarioResponse> registrarTokenID(@Field("token") String token, @Field("user") String user);

    @GET(ConstantesRestApi.KEY_TOQUE_BUTTON)
    Call<UsuarioResponse> toqueButton(@Path("id") String id, @Path("user") String animal);

}
