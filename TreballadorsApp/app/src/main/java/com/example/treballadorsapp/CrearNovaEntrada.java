package com.example.treballadorsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treballadorsapp.adapter.ProjecteAdapter;

import org.milaifontanals.model.Entrada;
import org.milaifontanals.model.Estat;
import org.milaifontanals.model.Projecte;
import org.milaifontanals.model.Tasca;
import org.milaifontanals.model.Usuari;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CrearNovaEntrada extends AppCompatActivity implements View.OnClickListener{


    private static final int port = 44444;
    //private static final String address = "10.132.0.120";
    private static final String address = "192.168.0.11";

    List<Usuari> usuaris = new ArrayList<>();


    private Tasca tasca;
    private Usuari usuari;
    private Projecte projecte;



    private EditText edtNomEntrada;
    private Spinner spnNovaAssignacioEntrada;
    private Spinner spnNouEstatEntrada;
    private Button btnAceptar;
    private Button btnCancelar;

    private boolean toast = false;
    TextView txvToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_nova_entrada);

        inicialitzar_ui();
        recive_data();

        emplenar_spinner_usuaris();


        ArrayAdapter<Estat> adapterSpnEstats = new ArrayAdapter<Estat>(this,R.layout.support_simple_spinner_dropdown_item,Estat.values());
        spnNouEstatEntrada.setAdapter(adapterSpnEstats);


    }

    private void emplenar_spinner_usuaris() {

        Observable.fromCallable(() -> {
            //---------------- START OF THREAD ------------------------------------
            // Això és el codi que s'executarà en un fil
            Socket s = new Socket(address, port);

            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

            out.writeInt(6);
            out.flush();

            Log.e("PROVA","PROJECTE ID: "+projecte.getId());
            out.writeInt(projecte.getId());
            out.flush();

            int qt_usuaris = ois.readInt();

            for(int i=0;i<qt_usuaris;i++){
                usuaris.add((Usuari) ois.readObject());
            }

            out.writeInt(0);
            out.flush();




            out.close();
            ois.close();
            s.close();

            return true;
            //--------------- END OF THREAD-------------------------------------
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((retornInutil) -> {
                    //-------------  UI THREAD ---------------------------------------
                    // El codi que tenim aquí s'executa només quan el fil
                    // ha acabat !! A més, aquest codi s'executa en el fil
                    // d'interfície gràfica.

                    for(Usuari u : usuaris){
                        Log.e("PROVA", "USUARI: "+u);
                    }

                    ArrayAdapter<Usuari> adapterSpnUsuari = new ArrayAdapter<Usuari>(this,R.layout.support_simple_spinner_dropdown_item,usuaris);
                    spnNovaAssignacioEntrada.setAdapter(adapterSpnUsuari);


                    //-------------  END OF UI THREAD ---------------------------------------
                });
    }

    private void recive_data() {
        if(getIntent().getExtras()!=null){
            tasca = (Tasca) getIntent().getSerializableExtra("tasca");
            usuari = (Usuari) getIntent().getSerializableExtra("usuari");
            projecte = (Projecte) getIntent().getSerializableExtra("projecte");
            Log.e("PROVA","TASCA:   "+tasca.getNom());
        }else{
            Log.e("PROVA","NO HI HA");
        }
    }

    private void inicialitzar_ui() {
        edtNomEntrada = findViewById(R.id.edtNomEntrada);
        spnNovaAssignacioEntrada = findViewById(R.id.spnNovaAssignacioEntrada);
        spnNouEstatEntrada = findViewById(R.id.spnNouEstatEntrada);
        btnAceptar = findViewById(R.id.btnAceptar);
        btnCancelar = findViewById(R.id.btnCancelar);

        btnAceptar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnAceptar){
            toast = false;

            Observable.fromCallable(() -> {
                //---------------- START OF THREAD ------------------------------------
                // Això és el codi que s'executarà en un fil

                if(edtNomEntrada.getText().toString().trim().length()>0){
                    Socket s = new Socket(address, port);

                    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                    ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

                    out.writeInt(5);
                    out.flush();

                    out.writeUTF(edtNomEntrada.getText().toString());
                    out.flush();

                    Usuari us = (Usuari) spnNovaAssignacioEntrada.getSelectedItem();
                    Log.e("PROVA","Usuari id: "+us.getId());
                    out.writeInt(us.getId());
                    out.flush();

                    Log.e("PROVA","Escriptor id: "+usuari.getId());
                    out.writeInt(usuari.getId());
                    out.flush();

                    Estat estat = (Estat) spnNouEstatEntrada.getSelectedItem();
                    Log.e("PROVA", "Estat: "+estat.ordinal());
                    out.writeInt(estat.ordinal());
                    out.flush();

                    Log.e("PROVA", "Tasca id: "+tasca.getId());
                    out.writeInt(tasca.getId());
                    out.flush();

                    if(ois.readInt()==1){
                        Log.e("PROVA", "S'HA INSERTAT CORRECTAMENT");
                    }else{
                        Log.e("PROVA","HI HA HAGUT ALGUN ERROR");
                    }




                    out.close();
                    ois.close();
                    s.close();

                }else{
                    toast = true;
                }


                return true;
                //--------------- END OF THREAD-------------------------------------
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((retornInutil) -> {
                        //-------------  UI THREAD ---------------------------------------
                        // El codi que tenim aquí s'executa només quan el fil
                        // ha acabat !! A més, aquest codi s'executa en el fil
                        // d'interfície gràfica.

                        //-------------  END OF UI THREAD ---------------------------------------
                    });

            if(toast){
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toast_layout,
                        (ViewGroup) findViewById(R.id.toast_layout_root));
                txvToast = (TextView) layout.findViewById(R.id.txvToast);
                txvToast.setText("El nombre de la entrada es obligatorio");

                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            }

        }else if(view.getId()==R.id.btnCancelar){

            edtNomEntrada.setText("");
        }
    }
}