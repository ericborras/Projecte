package org.milaifontanals.persist;


import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import org.milaifontanals.IPersistence;
import org.milaifontanals.exception.ServidorException;
import org.milaifontanals.model.Entrada;
import org.milaifontanals.model.Estat;
import org.milaifontanals.model.Projecte;
import org.milaifontanals.model.Tasca;
import org.milaifontanals.model.Usuari;
import org.milaifontanals.utils.MD5Utils;
import static org.milaifontanals.utils.MD5Utils.bytesToHex;


public class CPGestioProjecte implements IPersistence{

    private Connection con;  
    //LLISTA QUE CONTÉ TOT EL QUE VA PASSANT
    public DefaultListModel llista_log = new DefaultListModel();
    
    
    //PreparedStatements
    PreparedStatement psLogin;
    PreparedStatement psProjectes, psTasquesAssignades;
    PreparedStatement psTasca, psEntradaTasca;
    PreparedStatement psNotificacionsPendents;
    PreparedStatement psNovaEntrada;
    
    @Override
    public void connect(String nomFitx) {
        
        Properties p = new Properties();
        try {
            p.load(new FileReader(nomFitx));
        } catch (IOException ex) {
            llista_log.add(0,"Problemes en carregar el fitxer de configuració. Més info: "+ex.getMessage());
            throw new ServidorException("Problemes en carregar el fitxer de configuració. Més info: " + ex.getMessage(), ex);
        }

        String url = p.getProperty("url");
        String usu = p.getProperty("usuari");
        String pwd = p.getProperty("contrasenya");
        if (url == null || usu == null || pwd == null) {
            llista_log.add(0,"Manca alguna de les propietats: url, usuari, contrasenya");
            throw new ServidorException("Manca alguna de les propietats: url, usuari, contrasenya");
        }
        // Ja tenim les 3 propietats
        con = null;
        try {
            con = DriverManager.getConnection(url, usu, pwd);
            con.setAutoCommit(false);//Evitar autocommit
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            llista_log.add(0,"Connectat a la base de dades correctament");
            System.out.println("Connectat a la base de dades correctament");
            prepararStatements();
        } catch (SQLException ex) {
            llista_log.add(0,"Error en establir connexió amb la BD. Més info: "+ex.getMessage());
            throw new ServidorException("Error en establir connexió amb la BD", ex);
        }
    }

    @Override
    public void close() {
        try {
            if (con != null) {
                rollback(); // Si no es fa commit o rollback casca... Excepció
                con.close();
                llista_log.add(0,"Connexió amb la base de dades tancada correctament");
                System.out.println("Connexió amb la base de dades tancada correctament");
            }
        } catch (SQLException ex) {
            llista_log.add(0,"Error en tancar connexió amb la BD: "+ex.getMessage());
            throw new ServidorException("Error en tancar connexió amb la BD", ex);
        }
    }

    @Override
    public void commit() {
        try {
            con.commit();
        } catch (SQLException ex) {
            llista_log.add(0,"Error en realitzar commit: "+ex.getMessage());
            throw new ServidorException("Error en realitzar commit",ex);
        }
    }

    @Override
    public void rollback() {
        try {
            con.rollback();
        } catch (SQLException ex) {
            llista_log.add(0,"Error en realitzar rollback: "+ex.getMessage());
            throw new ServidorException("Error en realitzar rollback",ex);
        }
    }

    
    public Usuari getLogin(String login, String password){
        
        Usuari usuari = null;
        
        //Passar la password a hash
        MD5Utils md5 = new MD5Utils();
        byte[] md5InBytes = md5.digest(password.getBytes(StandardCharsets.UTF_8));
        String hash = bytesToHex(md5InBytes);
        
        Statement stLogin = null;
        ResultSet rsLogin = null;
        try {
            stLogin = con.createStatement();
            
            psLogin.setString(1, login);
            psLogin.setString(2, hash);
            
            rsLogin = psLogin.executeQuery();
            System.out.println("CONSULTA: "+psLogin.toString());
            while(rsLogin.next()){
                usuari = new Usuari(rsLogin.getInt("id"),rsLogin.getString("nom"),rsLogin.getString("cognom1"),rsLogin.getDate("data_naix"),
                                    rsLogin.getString("login"), rsLogin.getString("passwd_hash"));              
                
                String cognom2 = rsLogin.getString("cognom2");              
                if(cognom2!=null){
                    usuari.setCognom2(cognom2);
                }               
            }                   
        } catch (SQLException ex) {
            throw new ServidorException("Error: ",ex);
        }
        
        return usuari;
        
        
        
    } 
    
    public List<Projecte> getProjectes(int id_usuari){
        List<Projecte> projectes = new ArrayList();
        
        Projecte projecte = null;
        Usuari capProjecte = null;
        
        Statement stProjectes = null;
        ResultSet rsProjectes = null;

        try {
            stProjectes = con.createStatement();
            
            psProjectes.setInt(1, id_usuari);
            
            rsProjectes = psProjectes.executeQuery();
            System.out.println("CONSULTA: "+psProjectes.toString());
            while(rsProjectes.next()){
                projecte = new Projecte(rsProjectes.getInt("id"),rsProjectes.getString("nom"),rsProjectes.getString("descripcio"));              
                
                capProjecte = new Usuari(rsProjectes.getInt("id_cap_projecte"),rsProjectes.getString("nom_cap_projecte"),rsProjectes.getString("cognom1"));
                projecte.setCapProjecte(capProjecte);
                
                
                projectes.add(projecte);
            }                   
        } catch (SQLException ex) {
            throw new ServidorException("Error: ",ex);
        }
        
        
        return projectes;
    }
    
    public List<Tasca> getTasques(int id_usuari){
        List<Tasca> tasques = new ArrayList();
        
        Tasca tasca = null;
        
        Usuari propietari = null;
        Usuari responsable = null;
        
                
        Statement stTasques = null;
        ResultSet rsTasques = null;
        try {
            stTasques = con.createStatement();
            
            psTasquesAssignades.setInt(1, id_usuari);
            
            rsTasques = psTasquesAssignades.executeQuery();
            System.out.println("CONSULTA: "+psTasquesAssignades.toString());
            while(rsTasques.next()){
                tasca = new Tasca(rsTasques.getInt("id"),rsTasques.getDate("data_creacio"),rsTasques.getString("nom"),rsTasques.getString("descripcio")); 
                if(rsTasques.getDate("data_limit")!=null){
                    tasca.setDataCreacio(rsTasques.getDate("data_limit"));
                }
                //ESTAT
                switch(rsTasques.getInt("id_estat")){
                    
                    case 0:
                        tasca.setEstat(Estat.TANCADA_SENSE_SOLUCIO);
                        break;
                        
                    case 1:
                        tasca.setEstat(Estat.TANCADA_RESOLTA);
                        break;
                        
                    case 2:
                        tasca.setEstat(Estat.TANCADA_DUPLICADA);
                        break;
                        
                    case 3:
                        tasca.setEstat(Estat.OBERTA_NO_ASSIGNADA);
                        break;
                        
                    case 4:
                        tasca.setEstat(Estat.OBERTA_ASSIGNADA);
                        break;
                    
                }
                
                //PROPIETARI
                propietari = new Usuari(rsTasques.getInt("id_propietari"), rsTasques.getString("nom_propietari"), rsTasques.getString("cognom1_propietari"));
                tasca.setPropietari(propietari);
                
                //RESPONSABLE (NULLABLE)
                if(rsTasques.getString("nom_responsable")!=null){
                    responsable = new Usuari(rsTasques.getInt("id_responsable"), rsTasques.getString("nom_responsable"), rsTasques.getString("cognom1_responsable"));
                }
                
                

                
                tasques.add(tasca);
            }                   
        } catch (SQLException ex) {
            throw new ServidorException("Error: ",ex);
        }
        
        return tasques;
    }
    
    public DetallTasca getDetallTasca(int id_usuari, int id_tasca){
        DetallTasca detallTasca = null;
        Tasca tasca = null;
        List<Entrada> entrades = new ArrayList();
        
        
        Statement stTasca = null;
        ResultSet rsTasca = null;
        
        Statement stEntrada = null;
        ResultSet rsEntrada = null;
        
        try {
            stTasca = con.createStatement();
            
            psTasca.setInt(1, id_usuari);
            psTasca.setInt(2, id_tasca);
            
            rsTasca = psTasca.executeQuery();
            while(rsTasca.next()){
                //public Tasca(int id, Date dataCreacio, String nom, String descripcio, Date dataLimit, Usuari responsable, Estat estat) {
                tasca = new Tasca(rsTasca.getInt("id"),rsTasca.getDate("data_creacio"),rsTasca.getString("nom"),rsTasca.getString("descripcio"),rsTasca.getDate("data_limit"),new Usuari(rsTasca.getInt("id_responsable"),rsTasca.getString("nom_responsable"),rsTasca.getString("cognom1_responsable")));  
                
            
                //ESTAT
                switch(rsTasca.getInt("id_estat")){
                    
                    case 0:
                        tasca.setEstat(Estat.TANCADA_SENSE_SOLUCIO);
                        break;
                        
                    case 1:
                        tasca.setEstat(Estat.TANCADA_RESOLTA);
                        break;
                        
                    case 2:
                        tasca.setEstat(Estat.TANCADA_DUPLICADA);
                        break;
                        
                    case 3:
                        tasca.setEstat(Estat.OBERTA_NO_ASSIGNADA);
                        break;
                        
                    case 4:
                        tasca.setEstat(Estat.OBERTA_ASSIGNADA);
                        break;
                    
                }
            }
            
            
            //Buscar les entrades de la tasca
            stEntrada = con.createStatement();
            
            psEntradaTasca.setInt(1, id_tasca);
            
            rsTasca = psEntradaTasca.executeQuery();
            
            while(rsTasca.next()){
                Entrada entrada = new Entrada(rsTasca.getInt("numero"),rsTasca.getDate("data_entrada"),rsTasca.getString("entrada"),new Usuari(rsTasca.getInt("escriptor_id"),rsTasca.getString("escriptor_nom"),rsTasca.getString("escriptor_cognom1")),new Usuari(rsTasca.getInt("assignacio_id"),rsTasca.getString("assignacio_nom"),rsTasca.getString("assignacio_cognom1")));                             
                
                 //ESTAT
                switch(rsTasca.getInt("nou_estat")){
                    
                    case 0:
                        entrada.setNouEstat(Estat.TANCADA_SENSE_SOLUCIO);
                        break;
                        
                    case 1:
                        entrada.setNouEstat(Estat.TANCADA_RESOLTA);
                        break;
                        
                    case 2:
                        entrada.setNouEstat(Estat.TANCADA_DUPLICADA);
                        break;
                        
                    case 3:
                        entrada.setNouEstat(Estat.OBERTA_NO_ASSIGNADA);
                        break;
                        
                    case 4:
                        entrada.setNouEstat(Estat.OBERTA_ASSIGNADA);
                        break;
                    
                }
                
                entrades.add(entrada);
                detallTasca = new DetallTasca(tasca,entrades);
                
            }
            
            
        } catch (SQLException ex) {
            throw new ServidorException("Error: ",ex);
        }
        
        return detallTasca;
        
    }
    
    
    public List<Tasca> getNotificacionsPendents(int id_usuari){
        
        List<Tasca> tasques = new ArrayList();
        
        Statement stNotificacions = null;
        ResultSet rsNotificacions = null;
        
        try {
            stNotificacions = con.createStatement();
            
            psNotificacionsPendents.setInt(1, id_usuari);
            
            rsNotificacions = psNotificacionsPendents.executeQuery();
            //public Tasca(int id, Date dataCreacio, String nom, String descripcio, Date dataLimit, Usuari responsable) {
            while(rsNotificacions.next()){
                tasques.add(new Tasca(rsNotificacions.getInt("id"),rsNotificacions.getDate("data_creacio"),rsNotificacions.getString("nom"),
                                    rsNotificacions.getString("descripcio"), rsNotificacions.getDate("data_limit"),new Usuari(rsNotificacions.getInt("responsable_id"),
                                    rsNotificacions.getString("responsable_nom"), rsNotificacions.getString("responsable_cognom1"))));
                
            }
            
            
        } catch (SQLException ex) {
            throw new ServidorException("Error: ",ex);
        }
        
        
        
        return tasques;
        
    }
    
    
    private void prepararStatements() throws SQLException {
        
        //Consulta login
        //select * from usuari where login = 'marisela' and passwd_hash = '81dc9bdb52d04dc20036dbd8313ed055';
        psLogin = con.prepareStatement("select * from usuari where login = ? and passwd_hash = ?");
        
        //Consulta Projectes que està associat l'usuari
        psProjectes = con.prepareStatement("select p.id, p.nom, p.descripcio, u.nom as nom_cap_projecte, u.cognom1, u.id as id_cap_projecte \n" +
                                            "from projecte p join projecte_usuari pr on p.id = pr.id_projecte \n" +
                                            "join usuari u on u.id = p.cap_projecte\n" +
                                            "where pr.id_usuari = ?");
        

        
        //Consulta tasques OBERTES assignades a usuari
        //select t.id, t.data_creacio, t.nom, t.descripcio, t.data_limit, u.id as id_propietari,u.nom as nom_propietari, u.cognom1 as cognom1_propietari, us.id as id_responsable,us.nom as nom_responsable, us.cognom1 as cognom1_responsable, p.id as id_projecte, p.nom as nom_projecte, p.descripcio as descripcio_projecte 
//        from tasca t join usuari u on t.propietari = u.id
//                                 join usuari us on t.responsable = us.id
//                     join projecte p on t.projecte_id = p.id
//        where t.propietari = 22 and t.id_estat = 4 or t.id_estat = 3;
        psTasquesAssignades = con.prepareStatement("select t.id, t.data_creacio, t.nom, t.descripcio, t.data_limit,t.id_estat, u.id as id_propietari,u.nom as nom_propietari, u.cognom1 as cognom1_propietari, us.id as id_responsable,us.nom as nom_responsable, us.cognom1 as cognom1_responsable, p.id as id_projecte, p.nom as nom_projecte, p.descripcio as descripcio_projecte \n" +
                                                    "from tasca t join usuari u on t.propietari = u.id\n" +
                                                    "			 join usuari us on t.responsable = us.id\n" +
                                                    "             join projecte p on t.projecte_id = p.id\n" +
                                                    "where t.propietari = ? and t.id_estat = 4");
        
        
        psTasca = con.prepareStatement("select t.id, t.data_creacio, t.nom, t.descripcio, t.data_limit, u.id as id_responsable, u.nom as nom_responsable, u.cognom1 as cognom1_responsable, e.id_estat as id_estat,e.nom as nom_estat, p.id as id_projecte, p.nom as nom_projecte, p.descripcio as descripcio_projecte  \n" +
                                            "from tasca t join usuari u on t.responsable = u.id\n" +
                                            "	 		 join estat e on t.id_estat = e.id_estat\n" +
                                            "             join projecte p on t.projecte_id = p.id\n" +
                                            "where t.propietari = ? and t.id=?");
        
        psEntradaTasca = con.prepareStatement("select e.numero, e.data_entrada, e.entrada, ue.id as escriptor_id, ue.nom as escriptor_nom, ue.cognom1 as escriptor_cognom1, ua.id as assignacio_id, ua.nom as assignacio_nom, ua.cognom1 as assignacio_cognom1, e.nou_estat from "
                                                + "entrada e join usuari ue on e.escriptor = ue.id left join usuari ua on e.nova_assignacio = ua.id where tasca_id = ?");
    
        psNotificacionsPendents = con.prepareStatement("select t.id, t.data_creacio, t.nom, t.descripcio, t.data_limit, u.id as responsable_id, u.nom as responsable_nom, u.cognom1 as responsable_cognom1\n" +
                                                        "from tasca t join usuari u on t.responsable=u.id\n" +
                                                        "where (t.id_estat=4 or t.id_estat=3) and t.propietari=?");
        
        psNovaEntrada = con.prepareStatement("insert into entrada(numero,data_entrada,entrada,nova_assignacio,escriptor,nou_estat,tasca_id) \n" +
"  values(NULL,?,?,?,?,?,?);");
    
    }
    
}