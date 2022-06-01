/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals;

import javax.persistence.EntityManager;
import javax.swing.DefaultListModel;
import org.milaifontanals.model.Projecte;
import org.milaifontanals.model.Rol;

/**
 *
 * @author Lenovo T530
 */
public interface IPersistence {
    
    /*
    EntityManager obrir_connexio();
    void tancar_connexio();
    DefaultListModel mostrar_usuaris();
    Projecte[] mostrar_projectes();
    Rol[] mostrar_rols();
    */
    
    void connect(String nomFitx) throws PersistenceException;
    void close() throws PersistenceException;
    void commit() throws PersistenceException;
    void rollback() throws PersistenceException;
    
    
    
}
