

package org.milaifontanals.capa;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.DefaultListModel;
import org.milaifontanals.IPersistence;
import org.milaifontanals.PersistenceException;
import org.milaifontanals.model.Entrada;
import org.milaifontanals.model.Projecte;
import org.milaifontanals.model.ProjecteUsuari;
import org.milaifontanals.model.Rol;
import org.milaifontanals.model.Tasca;
import org.milaifontanals.model.Usuari;


public class CapaMYSQL implements IPersistence{

    private EntityManager em;
    
    public CapaMYSQL(){
        
    }
    
    @Override
    public void obrir_connexio(String nomFitx) throws PersistenceException {
            
        EntityManagerFactory emf = null;
        Properties props = new Properties();
        try {
            
            props.load(new FileInputStream(nomFitx));
            
            String up = props.getProperty("up");
            props.remove("up");
            
            HashMap<String,String> p = new HashMap(props);
            
            em = null;
            emf = null;
            System.out.println("Intent amb " + up);
            emf = Persistence.createEntityManagerFactory(up);
            System.out.println("EntityManagerFactory creada");
            em = emf.createEntityManager();
            System.out.println();
            System.out.println("EntityManager creat");
            
            
        } catch (Exception ex) {
            
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
            
            System.out.println("Exception: " + ex.getMessage());
            System.out.print(ex.getCause() != null ? "Caused by:" + ex.getCause().getMessage() + "\n" : "");
            System.out.println("Traça:");
            ex.printStackTrace();
        }
        
    }

    @Override
    public void tancar_connexio() throws PersistenceException {
       EntityManagerFactory emf = null;
        try {
            emf = em.getEntityManagerFactory();
            em.close();
            em = null;
        } catch (Exception ex) {
 
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
    }

    @Override
    public DefaultListModel mostrar_usuaris() throws PersistenceException {
        Query q = em.createNamedQuery("foundUsuaris");

        List<Usuari> ll = (List<Usuari>)q.getResultList();
        DefaultListModel modelLlista = new DefaultListModel();

        if(ll==null){

        }else{
            System.out.println("Llista no null");
            System.out.println("SIZE: "+ll.size());
            for(Usuari u: ll){
                modelLlista.add(0, u);
            }

        }
        return modelLlista;
    }

    @Override
    public Projecte[] mostrar_projectes() throws PersistenceException {
        Query q = em.createNamedQuery("foundProjectes");

        List<Projecte> ll = (List<Projecte>)q.getResultList();
        Projecte projectes[] = new Projecte[ll.size()];
        
        for(int i=0;i<ll.size();i++){
            projectes[i] = ll.get(i);
        }

        return projectes;
    }

    @Override
    public Rol[] mostrar_rols() throws PersistenceException {
        Query q = em.createNamedQuery("foundRol");

        List<Rol> ll = (List<Rol>)q.getResultList();
        Rol rols[] = new Rol[ll.size()];
        
        for(int i=0;i<ll.size();i++){
            rols[i] = ll.get(i);
            
        }

        return rols;
    }

    @Override
    public List<ProjecteUsuari> mostrar_projecteUsuari(int identi_proj) throws PersistenceException {
        Query q = em.createNamedQuery("foundProjecteUsuari");
        q.setParameter("identi_proj", identi_proj);
        List<ProjecteUsuari> ll = (List<ProjecteUsuari>)q.getResultList();
        
        
        for(ProjecteUsuari pru : ll){
            System.out.println(pru);
        }

        return ll;
    }

    @Override
    public DefaultListModel mostrar_usuarsisPerProjecte(int id_projecte) throws PersistenceException {
        Query q = em.createNamedQuery("foundProjecteUsuariByProject");
        q.setParameter("numero", id_projecte);

        List<ProjecteUsuari> ll = (List<ProjecteUsuari>)q.getResultList();
        DefaultListModel modelLlista = new DefaultListModel();
        
        for(ProjecteUsuari pru : ll){
            modelLlista.add(0,pru.getUsuari());
            
        }

        return modelLlista;
    }

    @Override
    public DefaultListModel mostrar_usuarsisPerProjectePendent(int id_projecte) throws PersistenceException {
        Query q = em.createNamedQuery("foundUsuarisProjectPending");
        q.setParameter("iden_projecte", id_projecte);

        List<Usuari> ll = (List<Usuari>)q.getResultList();
        DefaultListModel modelLlista = new DefaultListModel();
        
        for(Usuari u : ll){
            modelLlista.add(0,u);
            
        }

        return modelLlista;
    }

    @Override
    public void commit() throws PersistenceException {
        em.getTransaction().commit();
    }

    @Override
    public void rollback() throws PersistenceException {
        em.getTransaction().rollback();
    }

    @Override
    public void guardaCanvis(Object o) throws PersistenceException {
        em.persist(o);
    }

    @Override
    public void sobreEscriureCanvis(Object o) throws PersistenceException {
        em.merge(o);
    }

    @Override
    public void esborrar(Object o) throws PersistenceException {
        em.remove(o);
    }

    @Override
    public void modificarOEsborrar(Object o) throws PersistenceException {
        em.remove(em.contains(o) ? o : em.merge(o));
    }

    @Override
    public ProjecteUsuari find(Class<ProjecteUsuari> aClass, Integer get) throws PersistenceException {
        return em.find(aClass,get);
    }

    @Override
    public void neteja() throws PersistenceException {
        em.clear();
    }

    @Override
    public void flush() throws PersistenceException {
        em.flush();
    }

    @Override
    public void comensaTransaccio() throws PersistenceException {
        em.getTransaction().begin();
    }

    @Override
    public void tancaConnexio() throws PersistenceException {
        em.close();
    }

    @Override
    public Usuari getLogin(String login, String password) throws PersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Projecte> getProjectes(int id_usuari) throws PersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Tasca> getTasques(int id_usuari, int id_projecte) throws PersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Entrada> getEntrades(int id_tasca) throws PersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Tasca> getNotificacionsPendents(int id_usuari) throws PersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean NovaEntrada(String entrada, int nova_assignacio, int escriptor, int nou_estat, int tasca_id) throws PersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Usuari> GetUsuarisProjecte(int id_projecte) throws PersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Tasca> getTasquesFiltre(String sql, String nomTasca, String descripcioTasca, boolean tascaTancada) throws PersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Projecte> getProjectesFiltre(int id_usuari, int id_projecte) throws PersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DefaultListModel getLog() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
