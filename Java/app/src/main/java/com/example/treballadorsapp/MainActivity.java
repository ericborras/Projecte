package com.example.treballadorsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    // initialize socket and input output streams
    private static Socket socket  = null;
    private DataInputStream input   = null;
    private DataOutputStream out     = null;

    private static final int port = 4444;
    private static final String address = "192.168.0.11";
    private static Socket sock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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







            }

    @Override
    protected void onDestroy() {

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
}
