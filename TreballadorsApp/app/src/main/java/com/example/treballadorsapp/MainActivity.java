package com.example.treballadorsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treballadorsapp.utils.MD5Utils;

import org.milaifontanals.model.Usuari;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static com.example.treballadorsapp.utils.MD5Utils.bytesToHex;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    // initialize socket and input output streams
    private static Socket socket  = null;

    private static final int port = 44444;
    private static final String address = "10.132.0.120";
    //private static final String address = "192.168.0.11";
    private static Socket sock;

    private EditText edtLogin;
    private EditText edtPassword;
    private Button btnConnectarBD;

    TextView txvToast;


    int toast=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicialitzar_ui();
        btnConnectarBD.setOnClickListener(this);



/*
// LOGOUT
        Observable.fromCallable(() -> {
            //---------------- START OF THREAD ------------------------------------
            try {


                // establish a connection
                try {
                    socket = new Socket(address, port);
                    sock = socket;
                    Log.e("PROVA", " CONNECTED");

                } catch (UnknownHostException u) {
                    Log.e("PROVA", "ERROR: " + u.getMessage());
                } catch (IOException i) {
                    Log.e("PROVA", "ERROR: " + i.getMessage());
                }


                try {
                    out = new DataOutputStream(socket.getOutputStream());
                } catch (IOException e) {
                    Log.e("PROVA","ERROR DATA OUTPUT STREAM");
                }

                btnConnectarBD.setOnClickListener(new View.OnClickListener(){


                    @Override
                    public void onClick(View view) {
                        //Encriptar la password
                        MD5Utils md5 = new MD5Utils();
                        byte[] md5InBytes = md5.digest(edtPassword.getText().toString().getBytes(StandardCharsets.UTF_8));
                        String hash = bytesToHex(md5InBytes);

                        // Write data
                        byte[] data;
                        try {
                            Log.e("PROVA","1");
                            data = hash.getBytes("UTF-8");
                            Log.e("PROVA","PROVAAASSSS "+data.length);
                            Log.e("PROVA","2");
                            if(hash!=null){
                                Log.e("PROVA","3");
                                if(out!=null){

                                    Log.e("PROVA","5");
                                    out.writeInt(data.length);
                                    out.write(data);
                                    Log.e("PROVA","ENVIAT CORRECTAMENT!");
                                }else{
                                    Log.e("PROVA","SOC NULL!");
                                }
                            }else{
                                Log.e("PROVA","4");
                                Log.e("PROVA","HASH NULL");
                            }


                        } catch (UnsupportedEncodingException e) {
                            Log.e("PROVA","ERRORRR: "+e.getMessage());
                        } catch (IOException e) {
                            Log.e("PROVA","ERRORRRIO: "+e.getMessage());
                        } catch (Exception ex){
                            Log.e("PROVA","ERROR GROS: "+ex.getMessage());
                        }
                    }
                });



            } catch (Exception ex) {
                Log.d("LOGOUT", ex.getMessage(), ex);
            }
            return true;
            //--------------- END OF THREAD-------------------------------------
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((loginTupleNull) -> {
                    //-------------  UI THREAD ---------------------------------------
                    Log.d("PROVA", "CONNECTAT AMB EL SERVIDOR");


                    //-------------  END OF UI THREAD ---------------------------------------
                });
        */


    }

    private void inicialitzar_ui() {
        edtLogin = findViewById(R.id.edtLogin);
        edtPassword = findViewById(R.id.edtPassword);
        btnConnectarBD = findViewById(R.id.btnConnectarBD);
    }

    @Override
    protected void onDestroy() {
        //TODO: AL TANCAR, CREA UNA NOVA CONNEXIO, REPASSAR-LO
        Observable.fromCallable(() -> {
            //---------------- START OF THREAD ------------------------------------
            try {

                // close the connection
                try {
                    socket = new Socket(address, port);
                    Log.e("PROVA", "DESCONNECTAT DEL SERVER CORRECTAMENT");
                } catch (IOException e) {
                    Log.e("PROVA", "ERROR: " + e.getMessage());
                }

                try {
                    socket.close();

                    Log.e("PROVA", "DESCONNECTAT DEL SERVIDOR ");
                } catch (Exception ex) {
                    Log.d("PROVA", ex.getMessage(), ex);
                }
            }catch (Exception ex){
                Log.e("PROVA", "ERROR: " + ex.getMessage());
            }
            return true;
            //--------------- END OF THREAD-------------------------------------
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((loginTupleNull) -> {
                    //-------------  UI THREAD ---------------------------------------
                    Log.d("PROVA", "TANCAT AMB EL SERVIDOR");

                    //-------------  END OF UI THREAD ---------------------------------------
                });

        super.onDestroy();



    }


    private void sendMessage(String user, String password) {


        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //Replace below IP with the IP of that device in which server socket open.
                    //If you change port then change the port number in the server side code also.

                    Socket s = new Socket(address, port);

                    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                    ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

                    out.writeInt(1);

                    out.writeObject(user);
                    out.writeObject(password);

                    Usuari usuari = (Usuari) ois.readObject();
                    if(usuari!=null){
                        toast = 0;
                        Log.e("PROVA","OBJECTE: "+usuari);
                        out.writeInt(0);

                        Intent i = new Intent(MainActivity.this,TasquesActivity.class);
                        i.putExtra("usuari", usuari);
                        out.close();
                        ois.close();
                        s.close();


                        startActivity(i);


                    }else{

                        toast = 1;




                        out.writeInt(1);

                    }
                    //PrintWriter output = new PrintWriter(out);

                    //output.println(msg);
                    //output.flush();
                    //ObjectInputStream input = new ObjectInputStream(s.getInputStream());
                    //final String st = (String) input.readObject();
                    //Log.d("XXX",st);

                    out.close();
                    ois.close();
                    s.close();
                } catch (IOException e) {
                    Log.e("PROVA","ERROR: "+e.getMessage());
                } catch (ClassNotFoundException e) {
                    Log.e("PROVA","ERROR: "+e.getMessage());
                }
            }

        });

        thread.start();

        //return correcte2;
    }


    @Override
    public void onClick(View view) {

        sendMessage(edtLogin.getText().toString(),edtPassword.getText().toString());

        if(toast==1){
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast_layout,
                    (ViewGroup) findViewById(R.id.toast_layout_root));
            txvToast = (TextView) layout.findViewById(R.id.txvToast);
            txvToast.setText("El login o la contraseña son incorrectos");

            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }

    }



}