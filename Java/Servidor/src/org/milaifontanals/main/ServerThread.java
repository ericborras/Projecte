
package org.milaifontanals.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.milaifontanals.IPersistence;
import org.milaifontanals.PersistenceException;
import org.milaifontanals.model.Entrada;
import org.milaifontanals.model.Projecte;
import org.milaifontanals.model.Tasca;
import org.milaifontanals.model.Usuari;


public class ServerThread extends Thread{
    
    private Socket socket;
    private String arxiu;
 
    public ServerThread(Socket socket, String nomFitxer) {
        this.socket = socket;
        this.arxiu = arxiu;
    }
 
    public void run() {
        
        IPersistence capa_pers;
        
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                       
            try {
            Properties props = new Properties();
            props.load(new FileInputStream(arxiu));
            String nom_capa = props.getProperty("capa");

            // des de fitxer de propietats o via args. Mai per codi
            capa_pers = (IPersistence) Class.forName(nom_capa).newInstance();
            System.out.println("Capa carregada i en funcionament");
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            System.out.println("No es pot carregar capa per motiu.....");
            return;
        } catch (PersistenceException ex) {
            System.out.println(ex.getMessage());
            return;
        } catch (IOException ex) {
            System.out.println("No es pot trobar el fitxer de propietats.....");
            return;
        }
            

        capa_pers.obrir_connexio(arxiu);

        System.out.println("Abans tipus operacio");
        int tipus_operacio = ois.readInt();
        System.out.println("Tipus operaci??: "+tipus_operacio);
        switch(tipus_operacio){

            //Login
            case 1:
                String usu,contrasenya;
                //Convertir ObjectInputStream to String                                  
                usu = (String) ois.readObject();
                contrasenya = (String) ois.readObject();

                Usuari usuari = capa_pers.getLogin(usu, contrasenya); 
                System.out.println(usuari);
                out.writeObject(usuari);

                if(ois.readInt()==0){
                    System.out.println("LOGIN HA ANAT B??");
                }else{
                    System.out.println("LOGIN NO HA ANAT B??");
                }



                break;  

            //
            case 2:
                System.out.println("ENTRO");

                int id_usuari = ois.readInt();

                List<Projecte> projectes = capa_pers.getProjectes(id_usuari);
                //Hem de passar la quantitat de projectes, per tal de passar-li 
                out.writeInt(projectes.size());


                for(Projecte p : projectes){
                    out.writeObject(p);
                }



                if(ois.readInt()==0){
                    System.out.println("PROJECTES HA ANAT B??");
                }else{
                    System.out.println("PROJECTES NO HA ANAT B??");
                }



                break;


            case 3:

                int id_projecte = ois.readInt();
                id_usuari = ois.readInt();
                List<Tasca> tasques = capa_pers.getTasques(id_usuari,id_projecte);

                out.writeInt(tasques.size());
                out.flush();


                for(Tasca t : tasques){
                    out.writeObject(t);
                    out.flush();
                }



                if(ois.readInt()==0){
                    System.out.println("TASQUES HA ANAT B??");
                }else{
                    System.out.println("TASQUES NO HA ANAT B??");
                }

                break;


            case 4:

                int id_tasca = ois.readInt();
                List<Entrada> entrades = capa_pers.getEntrades(id_tasca);

                out.writeInt(entrades.size());
                out.flush();

                for(Entrada entrada : entrades){
                    out.writeObject(entrada);
                    out.flush();
                }

                if(ois.readInt()==0){
                    System.out.println("ENTARDES HA ANAT B??");
                }else{
                    System.out.println("ENTRADES NO HA ANAT B??");
                }

                break;

            case 5:

                String entrada = ois.readUTF();
                int nova_assignacio = ois.readInt();
                int escriptor = ois.readInt();
                int nou_estat = ois.readInt();
                int tasca_id = ois.readInt();

                if(capa_pers.NovaEntrada(entrada, nova_assignacio, escriptor, nou_estat, tasca_id)){
                    out.writeInt(1);
                    out.flush();
                }else{
                    out.writeInt(-1);
                    out.flush();
                }


                break;

            case 6:

                id_projecte = ois.readInt();
                System.out.println("ID PROJECTE: "+id_projecte);
                List<Usuari> usuaris = capa_pers.GetUsuarisProjecte(id_projecte);

                out.writeInt(usuaris.size());
                out.flush();

                for(Usuari u : usuaris){
                    out.writeObject(u);
                    out.flush();
                }

                if(ois.readInt()==0){
                    System.out.println("PROJECTE USUARI HA ANAT B??");
                }else{
                    System.out.println("PROJECTE USUARI NO HA ANAT B??");
                }

                break;

            case 7:

                //Cas de consulta personalitzada de la cerca
                String consulta = ois.readUTF();

                System.out.println("CONSULTA: "+consulta);

                String nomTasca = ois.readUTF();
                String descripcioTasca = ois.readUTF();
                boolean tascaTancada = ois.readBoolean();

                List<Tasca> tasques_filtre = capa_pers.getTasquesFiltre(consulta, nomTasca, descripcioTasca, tascaTancada);


                out.writeInt(tasques_filtre.size());
                out.flush();

                for(Tasca t : tasques_filtre){
                    out.writeObject(t);
                    out.flush();
                }

                if(ois.readInt()==0){
                    System.out.println("FILTRE TASQUES HA ANAT B??");
                }else{
                    System.out.println("FILTRE TASQUES NO HA ANAT B??");
                }

                break;

            case 8:

                id_usuari = ois.readInt();
                id_projecte = ois.readInt();

                List<Projecte> projectes_filtre = capa_pers.getProjectesFiltre(id_usuari, id_projecte);

                out.writeObject(projectes_filtre.get(0));
                out.flush();

                if(ois.readInt()==0){
                    System.out.println("FILTRE PROJECTES HA ANAT B??");
                }else{
                    System.out.println("FILTRE PROJECTES NO HA ANAT B??");
                }

                break;
        }

        out.close();
        ois.close();







            
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println("Errorrr al tancar el socket: "+ex.getMessage());
            }
            
            System.out.println("Hem tancat aquest socket");
        }
    }
    
}
