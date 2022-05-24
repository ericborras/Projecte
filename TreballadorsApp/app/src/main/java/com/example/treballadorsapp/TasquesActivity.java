package com.example.treballadorsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import org.milaifontanals.model.Projecte;
import org.milaifontanals.model.Usuari;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class TasquesActivity extends AppCompatActivity implements View.OnClickListener{

    Usuari usuari;

    List<Projecte> projectes = new ArrayList<>();

    private static final int port = 4444;
    private static final String address = "192.168.0.11";

    private EditText edtSearchTitol;
    private EditText edtSearchDescripcio;
    private Spinner spnProjecte;
    private Spinner spnEstatTasca;
    private CheckBox chkTasquesTancades;
    private Button btnEsborraFiltres;
    private Button btnCerca;
    private RecyclerView rcyTasques;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasques);

        inicialitzar_ui();

        recive_data();

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

                    out.writeInt(2);

                    out.writeInt(8);


                    ois.readInt();



                    out.close();
                    ois.close();
                    s.close();
                } catch (IOException e) {
                    Log.e("PROVA","ERROR: "+e.getMessage());
                }
            }

        });

        thread.start();





    }

    private void inicialitzar_ui() {

        edtSearchTitol = findViewById(R.id.edtSearchTitol);
        edtSearchDescripcio = findViewById(R.id.edtSearchDescripcio);
        spnProjecte = findViewById(R.id.spnProjecte);
        spnEstatTasca = findViewById(R.id.spnEstatTasca);
        chkTasquesTancades = findViewById(R.id.chkTasquesTancades);
        btnEsborraFiltres = findViewById(R.id.btnEsborraFiltres);
        btnCerca = findViewById(R.id.btnCerca);
        rcyTasques = findViewById(R.id.rcyTasques);

        btnCerca.setOnClickListener(this);
        btnEsborraFiltres.setOnClickListener(this);




    }

    private void recive_data() {

        if(getIntent().getExtras()!=null){
            usuari = (Usuari) getIntent().getSerializableExtra("usuari");
        }

    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.btnEsborraFiltres){

            edtSearchTitol.setText("");
            edtSearchDescripcio.setText("");
            spnProjecte.setSelection(0);
            spnEstatTasca.setSelection(0);
            chkTasquesTancades.setChecked(false);

        }else if(view.getId()==R.id.btnCerca){

        }


    }
}