/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.persistence;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.DefaultListModel;
import org.milaifontanals.IPersistence;
import org.milaifontanals.model.Projecte;
import org.milaifontanals.model.ProjecteUsuari;
import org.milaifontanals.model.Rol;
import org.milaifontanals.model.Usuari;

/**
 *
 * @author Usuari
 */
public class Persistencia{

    EntityManager em = null;
    
    public Persistencia(){
        
    }
    
    
    //@Override
    public EntityManager obrir_connexio() {
        
        String up = "UP-MYSQL";
        EntityManagerFactory emf = null;
        em = null;
        try {
            em = null;
            emf = null;
            System.out.println("Intent amb " + up);
            emf = Persistence.createEntityManagerFactory(up);
            System.out.println("EntityManagerFactory creada");
            em = emf.createEntityManager();
            System.out.println();
            System.out.println("EntityManager creat");
            
            return em;
            
            
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
            
            return null;
        }
        
        
    }

    //@Override
    public void tancar_connexio() {
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

    //@Override
    public DefaultListModel mostrar_usuaris() {
        
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

    //@Override
    public Projecte[] mostrar_projectes() {
        Query q = em.createNamedQuery("foundProjectes");

        List<Projecte> ll = (List<Projecte>)q.getResultList();
        Projecte projectes[] = new Projecte[ll.size()];
        
        for(int i=0;i<ll.size();i++){
            projectes[i] = ll.get(i);
        }

        return projectes;
    }

    //@Override
    public Rol[] mostrar_rols() {
        
        Query q = em.createNamedQuery("foundRol");

        List<Rol> ll = (List<Rol>)q.getResultList();
        Rol rols[] = new Rol[ll.size()];
        
        for(int i=0;i<ll.size();i++){
            rols[i] = ll.get(i);
            
        }

        return rols;
        
        
    }
    
    public List<ProjecteUsuari> mostrar_projecteUsuari(int identi_proj){
        Query q = em.createNamedQuery("foundProjecteUsuari");
        q.setParameter("identi_proj", identi_proj);
        List<ProjecteUsuari> ll = (List<ProjecteUsuari>)q.getResultList();
        
        
        for(ProjecteUsuari pru : ll){
            System.out.println(pru);
        }

        return ll;
    }
    
    public DefaultListModel mostrar_usuarsisPerProjecte(int id_projecte){
        Query q = em.createNamedQuery("foundProjecteUsuariByProject");
        q.setParameter("numero", id_projecte);

        List<ProjecteUsuari> ll = (List<ProjecteUsuari>)q.getResultList();
        DefaultListModel modelLlista = new DefaultListModel();
        
        for(ProjecteUsuari pru : ll){
            modelLlista.add(0,pru.getUsuari());
            
        }

        return modelLlista;
    }
    
    public DefaultListModel mostrar_usuarsisPerProjectePendent(int id_projecte){
        Query q = em.createNamedQuery("foundUsuarisProjectPending");
        q.setParameter("iden_projecte", id_projecte);

        List<Usuari> ll = (List<Usuari>)q.getResultList();
        DefaultListModel modelLlista = new DefaultListModel();
        
        for(Usuari u : ll){
            modelLlista.add(0,u);
            
        }

        return modelLlista;
    }
    


    
    
    
    
    
}
