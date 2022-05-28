package com.example.treballadorsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.treballadorsapp.adapter.EntradaAdapter;
import com.example.treballadorsapp.adapter.TascaAdapter;

import org.milaifontanals.model.Entrada;
import org.milaifontanals.model.Projecte;
import org.milaifontanals.model.Tasca;
import org.milaifontanals.model.Usuari;
import org.w3c.dom.Text;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EntradesActivity extends AppCompatActivity implements View.OnClickListener{


    private static final int port = 44444;
    //private static final String address = "10.132.0.120";
    private static final String address = "192.168.0.11";

    List<Entrada> entrades = new ArrayList<>();
    EntradesViewModel entradesViewModel;

    private Tasca tasca;
    private Usuari usuari;
    private Projecte projecte;

    private TextView txvIdTascaEntrada;
    private TextView txvNomTascaEntrada;
    private TextView txvDescripcioTascaEntrada;
    private TextView txvDataCreacioTascaEntrada;
    private TextView txvDataLimitTascaEntrada;
    private TextView txvResponsableTascaEntrada;
    private TextView txvEstatTascaEntrada;
    private Button btnNovaEntrada;
    private RecyclerView rcyEntrades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrades);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        inicialitzar_ui();
        recive_data();

        if(tasca!=null){
            //Posar les dades de la tasca
            txvIdTascaEntrada.setText(tasca.getId()+"");
            txvNomTascaEntrada.setText(tasca.getNom());
            txvDescripcioTascaEntrada.setText(tasca.getDescripcio());
            txvDataCreacioTascaEntrada.setText(sdf.format(tasca.getDataCreacio()));
            if(tasca.getDataLimit()!=null){
                txvDataLimitTascaEntrada.setText(sdf.format(tasca.getDataLimit()));
            }
            if(tasca.getResponsable()!=null){

                if(tasca.getResponsable().getCognom2()!=null){
                    txvResponsableTascaEntrada.setText(tasca.getResponsable().getNom()+" "+tasca.getResponsable().getCognom1()+" "+tasca.getResponsable().getCognom2());
                }else{
                    txvResponsableTascaEntrada.setText(tasca.getResponsable().getNom()+" "+tasca.getResponsable().getCognom1());
                }
            }

            txvEstatTascaEntrada.setText(tasca.getEstat().toString());



            mostrarEstats();


        }




    }

    private void mostrarEstats() {

        Observable.fromCallable(() -> {
            //---------------- START OF THREAD ------------------------------------
            // Això és el codi que s'executarà en un fil
            Socket s = new Socket(address, port);

            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

            out.writeInt(4);
            out.flush();

            out.writeInt(tasca.getId());
            out.flush();


            Log.e("PROVA","LLEGIREM: ");
            int qt_entrades = ois.readInt();
            Log.e("PROVA","TASQUES: "+qt_entrades);


            for(int i =0;i<qt_entrades;i++){
                entrades.add((Entrada) ois.readObject());
            }

            entradesViewModel = new EntradesViewModel(entrades);

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

                    Log.e("PROVA",entradesViewModel.mEntrades.size()+" SIZE");
                    if(entradesViewModel!=null){
                        Log.e("PROVA","ENTRO");
                        EntradaAdapter adapter = new EntradaAdapter(entradesViewModel.mEntrades,this);
                        rcyEntrades.setAdapter(adapter);

                    }




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

        txvIdTascaEntrada = findViewById(R.id.txvIdTascaEntrada);
        txvNomTascaEntrada = findViewById(R.id.txvNomTascaEntrada);
        txvDescripcioTascaEntrada = findViewById(R.id.txvDescripcioTascaEntrada);
        txvDataCreacioTascaEntrada = findViewById(R.id.txvDataCreacioTascaEntrada);
        txvDataLimitTascaEntrada = findViewById(R.id.txvDataLimitTascaEntrada);
        txvResponsableTascaEntrada = findViewById(R.id.txvResponsableTascaEntrada);
        txvEstatTascaEntrada = findViewById(R.id.txvEstatTascaEntrada);

        btnNovaEntrada = findViewById(R.id.btnNovaEntrada);
        btnNovaEntrada.setOnClickListener(this);

        rcyEntrades = findViewById(R.id.rcyEntrades);
        rcyEntrades.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rcyEntrades.setHasFixedSize(true);
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.btnNovaEntrada){
            try{

                Intent i = new Intent(this,CrearNovaEntrada.class);
                i.putExtra("tasca",tasca);
                i.putExtra("usuari",usuari);
                i.putExtra("projecte",projecte);

                startActivity(i);

            }catch(Exception ex){

            }
        }

    }
}