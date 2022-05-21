
package org.milaifontanals.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class Tasca implements Serializable{
    
    private int id;
    private Date dataCreacio;
    private String nom;
    private String descripcio;
    private Date dataLimit;
    private Usuari propietari;
    private Usuari responsable;
    private List<Entrada> entrades;
    private Estat estat;

    public Tasca(int id, Date dataCreacio, String nom, String descripcio, Date dataLimit, Usuari propietari, Usuari responsable, List<Entrada> entrades, Estat estat) {
        this.id = id;
        this.dataCreacio = dataCreacio;
        this.nom = nom;
        this.descripcio = descripcio;
        this.dataLimit = dataLimit;
        this.propietari = propietari;
        this.responsable = responsable;
        this.entrades = entrades;
        this.estat = estat;
    }

    public Tasca(int id, String nom, String descripcio, Usuari propietari, Estat estat) {
        this.id = id;
        this.nom = nom;
        this.descripcio = descripcio;
        this.propietari = propietari;
        this.estat = estat;
    }
    
    

    public Tasca(int id, Date dataCreacio, String nom, String descripcio) {
        this.id = id;
        this.dataCreacio = dataCreacio;
        this.nom = nom;
        this.descripcio = descripcio;
    }

    public Tasca(int id, Date dataCreacio, String nom, String descripcio, Date dataLimit, Usuari responsable) {
        this.id = id;
        this.dataCreacio = dataCreacio;
        this.nom = nom;
        this.descripcio = descripcio;
        this.dataLimit = dataLimit;
        this.responsable = responsable;
    }
    
    
    

    

    
    
    
    protected Tasca(){
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDataCreacio() {
        return dataCreacio;
    }

    public void setDataCreacio(Date dataCreacio) {
        this.dataCreacio = dataCreacio;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public Date getDataLimit() {
        return dataLimit;
    }

    public void setDataLimit(Date dataLimit) {
        this.dataLimit = dataLimit;
    }

    public Usuari getPropietari() {
        return propietari;
    }

    public void setPropietari(Usuari propietari) {
        this.propietari = propietari;
    }

    public Usuari getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuari responsable) {
        this.responsable = responsable;
    }

    public List<Entrada> getEntrades() {
        return entrades;
    }

    public void setEntrades(List<Entrada> entrades) {
        this.entrades = entrades;
    }

    public Estat getEstat() {
        return estat;
    }

    public void setEstat(Estat estat) {
        this.estat = estat;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tasca other = (Tasca) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tasca{" + "id=" + id + ", dataCreacio=" + dataCreacio + ", nom=" + nom + ", descripcio=" + descripcio + ", dataLimit=" + dataLimit + ", propietari=" + propietari + ", responsable=" + responsable + ", entrades=" + entrades + ", estat=" + estat + '}';
    }
    
    
}
