package com.company.proyect.kevinpiazzoli.trilceucv;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.company.proyect.kevinpiazzoli.trilceucv.ActivitysPrincipales.ActivityPrincipal;
import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.BaseDeDatosUCV;
import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.GuardarDatos;
import com.company.proyect.kevinpiazzoli.trilceucv.ConexionInternet.VolleyS;
import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.PAmigos.fragment_view_amigos;
import com.company.proyect.kevinpiazzoli.trilceucv.TrilceChat.Chat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KevinPiazzoli on 09/11/2016.
 */

public class NotificationService extends FirebaseMessagingService {

    BaseDeDatosUCV bdUCV;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String Tipo_De_Notificacion = remoteMessage.getData().get("Tipo_De_Notificacion");
        bdUCV = new BaseDeDatosUCV(getApplicationContext());
        switch (Tipo_De_Notificacion){
            case "Mensaje":
                showNotification(remoteMessage.getData().get("message"));
                AgregarMensaje(remoteMessage);
                break;
            case "Amigos":
                SolicitudAmistad(remoteMessage);
                switch (remoteMessage.getData().get("Tipo_De_Solicitud")){
                    case "Enviar_Solicitud":
                        showNotification(remoteMessage.getData().get("message"));
                        break;
                    case "Aceptar_Solicitud":
                        showNotification(remoteMessage.getData().get("message"));
                        break;
                    case "Eliminar_Amigo":
                        break;
                    case "Cancelar_Solicitud_Emisor":
                        break;
                    case "Cancelar_Solicitud_Receptor":
                        break;
                }
                break;
        }
    }

    private void SolicitudAmistad(RemoteMessage remoteMessage){
        String IP = "http://kpfpservice.000webhostapp.com/UCV_datos_usuarios_GETALL.php" + "?id=" + GuardarDatos.CargarUsuario(getApplicationContext());
        VolleyS volley = VolleyS.getInstance(getApplicationContext());
        makeRequest(volley.getRequestQueue(),getApplicationContext(),volley,IP);
    }

    private void AgregarMensaje(RemoteMessage remoteMessage) {
        Intent intent = new Intent(Chat.NEW_MENSAJE);
        intent.putExtra("JSON_MENSAJE", remoteMessage.getData().get("jsonArray"));
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        String JSON_MENSAJE = remoteMessage.getData().get("jsonArray");
        try {
            JSONObject JSON_Mensaje = new JSONObject(JSON_MENSAJE);
            String Receptor[] = JSON_Mensaje.getString("id").split("_");
            bdUCV.ActualizarMensajes(0, Receptor[0], JSON_Mensaje.toString());
        }catch(JSONException ignored){
        }
    }

    private void showNotification(String message) {
        Intent i = new Intent(this, ActivityPrincipal.class);
        i.putExtra("FragmentNotificacion","Mensajes");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("Trilce UCV")
                .setContentText(message)
                .setSound(defaultSoundUri)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setTicker(message)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());
    }

    public void makeRequest(RequestQueue fRequestQueue, Context context, VolleyS volley, String IP){
        JsonObjectRequest request = new JsonObjectRequest(IP, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                bdUCV.actualizar(0,jsonObject.toString());
                Intent intent = new Intent(fragment_view_amigos.NEW_SOLICITUD);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        VolleyS.addToQueue(request,fRequestQueue,context,volley);
    }

}
