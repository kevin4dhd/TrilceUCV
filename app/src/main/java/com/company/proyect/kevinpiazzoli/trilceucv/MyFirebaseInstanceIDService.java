package com.company.proyect.kevinpiazzoli.trilceucv;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by KevinPiazzoli on 07/11/2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        String Token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + Token);

        EnviarToquenDeRegistro(Token);
    }

    private void EnviarToquenDeRegistro(String token) {
        //Maninipular Token
    }

}
