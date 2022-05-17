/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.persistence;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.DefaultListModel;
import org.milaifontanals.model.Usuari;

/**
 *
 * @author Usuari
 */
public class Persistencia implements IPersistence{

    EntityManager em = null;
    
    public Persistencia(){
        
    }
    
    
    @Override
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

    @Override
    public void tancar_connexio() {
        
    }

    @Override
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
    
    
    
    
    
}
