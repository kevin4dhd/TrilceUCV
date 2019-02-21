package com.company.proyect.kevinpiazzoli.trilceucv.TrilceChat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.BaseDeDatosUCV;
import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.GuardarDatos;
import com.company.proyect.kevinpiazzoli.trilceucv.ConexionInternet.DetectarInternet;
import com.company.proyect.kevinpiazzoli.trilceucv.ConexionInternet.VolleyS;
import com.company.proyect.kevinpiazzoli.trilceucv.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by KevinPiazzoli on 15/11/2016.
 */

public class Chat extends AppCompatActivity {

    public static final String NEW_MENSAJE = "NUEVO_MENSAJE";

    private BroadcastReceiver MensajeNotificacionRecivido;

    private EditText messageET;
    private ListView messagesContainer;
    private ImageButton sendBtn;
    private ChatAdapter adapter;
    private List<ChatMessage> chatHistory;
    private List<List<String>> Mensajes;
    private BaseDeDatosUCV bdUCV;
    private String USUARIO_RECEPTOR;
    private String URL_ENVIAR_MENSAJE = "http://kpfpservice.000webhostapp.com/Mensajes_ENVIAR.php";
    private String EMISOR;
    private String MENSAJE;
    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    private String NAME_USER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        EMISOR = GuardarDatos.CargarUsuario(this);
        volley = VolleyS.getInstance(this);

        Intent extras = getIntent();
        Bundle bundle = extras.getExtras();
        Mensajes = new ArrayList<>();
        if(bundle!=null){
            bdUCV = new BaseDeDatosUCV(this);
            String NombreToolbar = bundle.getString("Name");
            USUARIO_RECEPTOR= bundle.getString("key_user");
            Mensajes = bdUCV.ObtenerMensajes(0,USUARIO_RECEPTOR);
            NAME_USER = bdUCV.getNameUserPrincipal(0);
            setTitle(NombreToolbar);
        }
        initControls();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        MensajeNotificacionRecivido = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String JSON_MENSAJE = intent.getStringExtra("JSON_MENSAJE");
                try {
                    JSONObject JSON_Mensaje = new JSONObject(JSON_MENSAJE);
                    String Receptor[] = JSON_Mensaje.getString("id").split("_");
                    if(Receptor[0].compareTo(USUARIO_RECEPTOR)==0) {
                        String Fechas[] = JSON_Mensaje.getString("hora_del_mensaje").split(",");
                        ChatMessage chatMessage = new ChatMessage(JSON_Mensaje.getString("id"), JSON_Mensaje.getString("mensaje"), 2, Fechas[0]);
                        displayMessage(chatMessage);
                    }
                }catch(JSONException e){
                }
            }
        };

    }

    private void initControls() {
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (ImageButton) findViewById(R.id.chatSendButton);
        loadDummyHistory();
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MENSAJE = messageET.getText().toString();
                if(DetectarInternet.isOnline(v.getContext())) {
                    if (TextUtils.isEmpty(MENSAJE)) {
                        return;
                    }
                    EnviarMensaje(fRequestQueue,volley);
                    messageET.setText("");
                }
            }
        });
    }

    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void loadDummyHistory(){

        chatHistory = new ArrayList<ChatMessage>();

        for(int i=0;i<Mensajes.size();i++){
            String Fechas[] = Mensajes.get(i).get(3).split(",");
            ChatMessage msg = new ChatMessage(Mensajes.get(i).get(0),Mensajes.get(i).get(1),Integer.parseInt(Mensajes.get(i).get(2)),Fechas[0]);
            chatHistory.add(msg);
        }

        adapter = new ChatAdapter(Chat.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        for(int i=0; i<chatHistory.size(); i++) {
            ChatMessage message = chatHistory.get(i);
            displayMessage(message);
        }

    }

    private void EnviarMensaje(RequestQueue fRequestQueue, VolleyS volley){
        HashMap<String, String> parametros = new HashMap();
        parametros.put("emisor", EMISOR);
        parametros.put("receptor", USUARIO_RECEPTOR);
        parametros.put("mensaje", MENSAJE);
        parametros.put("Name_Emisor", NAME_USER);

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL_ENVIAR_MENSAJE,
                new JSONObject(parametros),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("id").compareTo("-1")!=0) {
                                String Fechas[] = response.getString("hora_del_mensaje").split(",");
                                ChatMessage chatMessage = new ChatMessage(response.getString("id"), response.getString("mensaje"), 1, Fechas[0]);
                                bdUCV.ActualizarMensajes(0, USUARIO_RECEPTOR, response.toString());
                                displayMessage(chatMessage);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Chat.this,"No se pudo enviar el mensaje",Toast.LENGTH_SHORT).show();
                    }
                });
        VolleyS.addToQueue(jsArrayRequest,fRequestQueue,Chat.this,volley);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(MensajeNotificacionRecivido, new IntentFilter(NEW_MENSAJE));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(MensajeNotificacionRecivido);
    }

}

