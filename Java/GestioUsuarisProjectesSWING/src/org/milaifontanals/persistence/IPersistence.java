/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.persistence;

import java.util.List;
import javax.persistence.EntityManager;
import javax.swing.DefaultListModel;
import org.milaifontanals.model.Usuari;

/**
 *
 * @author Usuari
 */
public interface IPersistence {
    
    EntityManager obrir_connexio();
    void tancar_connexio();
    DefaultListModel mostrar_usuaris();
    
}
