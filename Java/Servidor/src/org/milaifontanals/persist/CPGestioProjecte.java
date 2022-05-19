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
    PreparedStatement psLogin, psCapProjecte;
    PreparedStatement psProjectes, psTasquesAssignades;
    
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
        
        Statement stCapProjecte = null;
        ResultSet rsCapProjecte = null;
        try {
            stProjectes = con.createStatement();
            
            psProjectes.setInt(1, id_usuari);
            
            rsProjectes = psProjectes.executeQuery();
            System.out.println("CONSULTA: "+psProjectes.toString());
            while(rsProjectes.next()){
                projecte = new Projecte(rsProjectes.getInt("id"),rsProjectes.getString("nom"),rsProjectes.getString("descripcio"));              
                stCapProjecte = con.createStatement();
            
                psCapProjecte.setInt(1, rsProjectes.getInt("cap_projecte"));
                rsCapProjecte = psCapProjecte.executeQuery();
                System.out.println("CONSULTA: "+psCapProjecte.toString());
                while(rsCapProjecte.next()){
                    capProjecte = new Usuari(rsCapProjecte.getInt("id"),rsCapProjecte.getString("nom"),
                            rsCapProjecte.getString("cognom1"),rsCapProjecte.getDate("data_naix"), rsCapProjecte.getString("login"), 
                            rsCapProjecte.getString("passwd_hash"));
                }
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
    
    private void prepararStatements() throws SQLException {
        
        //Consulta login
        //select * from usuari where login = 'marisela' and passwd_hash = '81dc9bdb52d04dc20036dbd8313ed055';
        psLogin = con.prepareStatement("select * from usuari where login = ? and passwd_hash = ?");
        
        //Consulta Projectes que està associat l'usuari
        psProjectes = con.prepareStatement("select p.* from projecte p join projecte_usuari pr on p.id = pr.id_projecte where pr.id_usuari = ?");
        //Consulta per agafar el cap del projecte
        psCapProjecte = con.prepareStatement("select * from usuari where id = ?");
        
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
    }
    
}