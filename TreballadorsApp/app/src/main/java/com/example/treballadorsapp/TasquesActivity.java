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

import org.milaifontanals.model.Estat;
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
    private static final String address = "10.132.0.120";
    //private static final String address = "192.168.0.11";


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

    ArrayList<String> projectes_s;
    List<Projecte> projectes_llist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasques);


        inicialitzar_ui();

        recive_data();
        projectes_s = new ArrayList<>();

        getProjectes();


        ArrayAdapter<Estat> adapterSpnEstats = new ArrayAdapter<Estat>(this,R.layout.support_simple_spinner_dropdown_item,Estat.values());
        spnEstatTasca.setAdapter(adapterSpnEstats);









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


            projectes.clear();
            for(int i =0;i<qt_projectes;i++){
                projectes.add((Projecte) ois.readObject());
            }

            projectesViewModel = new ProjectesViewModel(projectes);

            projectes_s.clear();
            projectes_s.add("Elige proyecto");
            for(Projecte p : projectesViewModel.mProjectes){
                projectes_s.add(p.getNom());
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

                    ArrayAdapter<String> adapterProjectes = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,projectes_s);
                    spnProjecte.setAdapter(adapterProjectes);

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


            try{

                Observable.fromCallable(() -> {
                    //---------------- START OF THREAD ------------------------------------
                    // Això és el codi que s'executarà en un fil
                    Socket s = new Socket(address, port);

                    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                    ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

                    out.writeInt(3);
                    out.flush();

                    out.writeInt(projecte_selected.getId());
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

        }else if(view.getId()==R.id.btnCerca){

            Estat estat = (Estat) spnEstatTasca.getSelectedItem();
            String projecte = spnProjecte.getSelectedItem().toString();


            if(!projecte.equals("Elige proyecto")){

                int id_projecte = -1;
                for(Projecte p : projectes){
                    if(p.getNom().equals(projecte)){
                        id_projecte = p.getId();
                    }
                }

                if(id_projecte!=-1){
                    try{
                        int finalId_projecte = id_projecte;
                        Observable.fromCallable(() -> {
                            //---------------- START OF THREAD ------------------------------------
                            // Això és el codi que s'executarà en un fil
                            Socket s = new Socket(address, port);

                            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

                            out.writeInt(8);
                            out.flush();

                            out.writeInt(usuari.getId());
                            Log.e("PROVA","USUARI: "+usuari.getId());
                            out.flush();

                            out.writeInt(finalId_projecte);
                            Log.e("PROVA","PROJECTE ID: "+finalId_projecte);
                            out.flush();


                            projectes_llist.clear();
                            projectes_llist.add((Projecte) ois.readObject());
                            //projectes.clear();
                            //projectes.add((Projecte) ois.readObject());
                            Log.e("PROVA","PROJECTE : "+projectes.get(0).getNom());



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

                                    Log.e("PROVA",tasques.size()+" SIZE");

                                    //if(tasquesViewModel!=null){
                                    Log.e("PROVA","ENTRO");
                                    for(Tasca t : tasques){
                                        Log.e("PROVA", "TASCA: "+t.getNom());
                                    }
                                    ProjecteAdapter adapter = new ProjecteAdapter(projectes_llist,this);
                                    rcyProjectes.setAdapter(adapter);

                                    //}




                                    //-------------  END OF UI THREAD ---------------------------------------
                                });

                    }catch(Exception ex){

                    }
                }

            }else{
                getProjectes();
            }


            Log.e("PROVA","ESTAT Y PROJECTE: "+estat+" "+projecte);

            String nom_tasca = edtSearchTitol.getText().toString().trim();
            String descripcio_tasca = edtSearchDescripcio.getText().toString().trim();
            boolean tasques_tancades = chkTasquesTancades.isChecked();

            if(nom_tasca.length()>0 || descripcio_tasca.length()>0 || tasques_tancades){

                if(tasques.size()>0){

                    //Montar la query i la enviarem per xarxa
                    String consulta = "select t.id, t.data_creacio, t.nom, t.descripcio, t.data_limit, u.id as responsable_id, u.nom as responsable_nom, u.cognom1 as responsable_cognom1 \n" +
                                      "from tasca t join usuari u on t.responsable=u.id\n" +
                                       " where t.propietari="+usuari.getId();
                    if(nom_tasca.length()>0){
                        consulta += " and lower(t.nom) like lower('"+nom_tasca+"%')";
                    }

                    if(descripcio_tasca.length()>0){
                        consulta += " and lower(t.descripcio) like lower('"+descripcio_tasca+"%')";
                    }

                    if(chkTasquesTancades.isChecked()){
                        consulta += " and (t.id_estat=0 or t.id_estat=1 or t.id_estat=2)";
                    }else{
                        consulta += " and (t.id_estat=3 or t.id_estat=4)";
                    }

                    Log.e("PROVA"," CONSULTA: "+consulta);


                    try{

                        String finalConsulta = consulta;
                        Observable.fromCallable(() -> {
                            //---------------- START OF THREAD ------------------------------------
                            // Això és el codi que s'executarà en un fil
                            Socket s = new Socket(address, port);

                            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

                            out.writeInt(7);
                            out.flush();

                            out.writeUTF(finalConsulta);
                            out.flush();

                            if(nom_tasca.length()>0){
                                out.writeUTF(nom_tasca);
                                out.flush();
                            }else{
                                out.writeUTF("");
                                out.flush();
                            }

                            if(descripcio_tasca.length()>0){
                                out.writeUTF(descripcio_tasca);
                                out.flush();
                            }else{
                                out.writeUTF("");
                                out.flush();
                            }

                            if(chkTasquesTancades.isChecked()){
                                out.writeBoolean(true);
                                out.flush();
                            }else{
                                out.writeBoolean(false);
                                out.flush();
                            }

                            int qt_tasques = ois.readInt();

                            tasques.clear();
                            for(int i=0;i<qt_tasques;i++){
                                tasques.add((Tasca) ois.readObject());
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

                                    Log.e("PROVA",tasques.size()+" SIZE");

                                    //if(tasquesViewModel!=null){
                                        Log.e("PROVA","ENTRO");
                                        for(Tasca t : tasques){
                                            Log.e("PROVA", "TASCA: "+t.getNom());
                                        }
                                        TascaAdapter adapter = new TascaAdapter(tasques,this);
                                        rcyTasques.setAdapter(adapter);

                                    //}




                                    //-------------  END OF UI THREAD ---------------------------------------
                                });

                    }catch(Exception ex){

                    }
                }

            }


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

                tasques.clear();
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