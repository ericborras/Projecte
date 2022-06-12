/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals;

import java.util.List;
import javax.persistence.EntityManager;
import javax.swing.DefaultListModel;
import org.milaifontanals.model.Entrada;
import org.milaifontanals.model.Projecte;
import org.milaifontanals.model.ProjecteUsuari;
import org.milaifontanals.model.Rol;
import org.milaifontanals.model.Tasca;
import org.milaifontanals.model.Usuari;

/**
 *
 * @author Lenovo T530
 */
public interface IPersistence {
       
    public void obrir_connexio(String nomFitx) throws PersistenceException;
    public void tancar_connexio() throws PersistenceException;
    public DefaultListModel mostrar_usuaris() throws PersistenceException;
    public Projecte[] mostrar_projectes() throws PersistenceException;
    public Rol[] mostrar_rols() throws PersistenceException;
    public List<ProjecteUsuari> mostrar_projecteUsuari(int identi_proj) throws PersistenceException;
    public DefaultListModel mostrar_usuarsisPerProjecte(int id_projecte) throws PersistenceException;
    public DefaultListModel mostrar_usuarsisPerProjectePendent(int id_projecte) throws PersistenceException;
    public void commit() throws PersistenceException;
    public void rollback() throws PersistenceException;
    public void guardaCanvis(Object o) throws PersistenceException;
    public void sobreEscriureCanvis(Object o) throws PersistenceException;
    public void esborrar(Object o) throws PersistenceException;
    public void modificarOEsborrar(Object o) throws PersistenceException;
    public ProjecteUsuari find(Class<ProjecteUsuari> aClass, Integer get) throws PersistenceException;
    public void neteja() throws PersistenceException;
    public void flush() throws PersistenceException;
    public void comensaTransaccio() throws PersistenceException;
    public void tancaConnexio() throws PersistenceException;
    
    public Usuari getLogin(String login, String password) throws PersistenceException;
    public List<Projecte> getProjectes(int id_usuari) throws PersistenceException;
    public List<Tasca> getTasques(int id_usuari, int id_projecte) throws PersistenceException;
    public List<Entrada> getEntrades(int id_tasca) throws PersistenceException;
    public List<Tasca> getNotificacionsPendents(int id_usuari) throws PersistenceException;
    public boolean NovaEntrada(String entrada, int nova_assignacio, int escriptor, int nou_estat, int tasca_id) throws PersistenceException;
    public List<Usuari> GetUsuarisProjecte(int id_projecte) throws PersistenceException;
    public List<Tasca> getTasquesFiltre(String sql, String nomTasca, String descripcioTasca, boolean tascaTancada) throws PersistenceException;
    public List<Projecte> getProjectesFiltre(int id_usuari, int id_projecte) throws PersistenceException;
    public DefaultListModel getLog();
    
    
}
