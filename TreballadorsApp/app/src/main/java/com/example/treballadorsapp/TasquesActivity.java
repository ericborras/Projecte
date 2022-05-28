package com.example.treballadorsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.treballadorsapp.adapter.ProjecteAdapter;
import com.example.treballadorsapp.adapter.TascaAdapter;

import org.milaifontanals.model.Projecte;
import org.milaifontanals.model.Tasca;
import org.milaifontanals.model.Usuari;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TasquesActivity extends AppCompatActivity implements View.OnClickListener{

    Usuari usuari;
    Projecte projecte_selected;

    List<Projecte> projectes = new ArrayList<>();
    List<Tasca> tasques = new ArrayList<>();

    private static final int port = 44444;
    //private static final String address = "10.132.0.120";
    private static final String address = "192.168.0.11";


    private TasquesViewModel tasquesViewModel;
    private ProjectesViewModel projectesViewModel;

    private EditText edtSearchTitol;
    private EditText edtSearchDescripcio;
    private Spinner spnProjecte;
    private Spinner spnEstatTasca;
    private CheckBox chkTasquesTancades;
    private Button btnEsborraFiltres;
    private Button btnCerca;
    private RecyclerView rcyTasques;
    private RecyclerView rcyProjectes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasques);

        inicialitzar_ui();

        recive_data();


        getProjectes();









    }

    private void getProjectes() {

        Observable.fromCallable(() -> {
            //---------------- START OF THREAD ------------------------------------
            // Això és el codi que s'executarà en un fil
            Socket s = new Socket(address, port);

            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

            out.writeInt(2);
            out.flush();

            out.writeInt(usuari.getId());
            out.flush();

            Log.e("PROVA","LLEGIREM: ");
            int qt_projectes = ois.readInt();
            Log.e("PROVA","PROJECTES: "+qt_projectes);


            for(int i =0;i<qt_projectes;i++){
                projectes.add((Projecte) ois.readObject());
            }

            projectesViewModel = new ProjectesViewModel(projectes);

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

                    Log.e("PROVA",projectesViewModel.mProjectes.size()+"");
                    if(projectesViewModel!=null){
                        Log.e("PROVA","ENTRO");
                        ProjecteAdapter adapter = new ProjecteAdapter(projectesViewModel.mProjectes,this);
                        rcyProjectes.setAdapter(adapter);

                    }




                    //-------------  END OF UI THREAD ---------------------------------------
                });



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
        rcyTasques.setLayoutManager(new LinearLayoutManager(this));
        rcyTasques.setHasFixedSize(true);

        rcyProjectes = findViewById(R.id.rcyProjectes);
        rcyProjectes.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        rcyProjectes.setHasFixedSize(true);

        btnCerca.setOnClickListener(this);
        btnEsborraFiltres.setOnClickListener(this);




    }

    private void recive_data() {

        if(getIntent().getExtras()!=null){
            usuari = (Usuari) getIntent().getSerializableExtra("usuari");
        }else{

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

    public void onProjecteSelected(Projecte projecte) {

        projecte_selected = projecte;

        Log.e("PROVA","PROJECTE SELECTED: "+projecte.getNom());
        try{

            Observable.fromCallable(() -> {
                //---------------- START OF THREAD ------------------------------------
                // Això és el codi que s'executarà en un fil
                Socket s = new Socket(address, port);

                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

                out.writeInt(3);
                out.flush();

                out.writeInt(projecte.getId());
                out.flush();

                out.writeInt(usuari.getId());
                out.flush();

                Log.e("PROVA","LLEGIREM: ");
                int qt_tasques = ois.readInt();
                Log.e("PROVA","TASQUES: "+qt_tasques);


                for(int i =0;i<qt_tasques;i++){
                    tasques.add((Tasca) ois.readObject());
                }

                tasquesViewModel = new TasquesViewModel(tasques);

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

                        Log.e("PROVA",tasquesViewModel.mTasca.size()+" SIZE");
                        if(tasquesViewModel!=null){
                            Log.e("PROVA","ENTRO");
                            TascaAdapter adapter = new TascaAdapter(tasquesViewModel.mTasca,this);
                            rcyTasques.setAdapter(adapter);

                        }




                        //-------------  END OF UI THREAD ---------------------------------------
                    });

        }catch(Exception ex){

        }


    }

    public void onTascaSelected(Tasca tasca) {

        try{

            Intent i = new Intent(this,EntradesActivity.class);
            i.putExtra("tasca",tasca);
            i.putExtra("usuari",usuari);
            i.putExtra("projecte", projecte_selected);

            startActivity(i);

        }catch(Exception ex){

        }

    }
}