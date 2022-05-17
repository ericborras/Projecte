package org.milaifontanals.model;

import java.io.Serializable;
import java.util.List;


public class Projecte implements Serializable{
    
    
    private int id;
    private String nom;
    private String descripcio;
    private Usuari capProjecte;
    private List<Tasca> tasques;
    private List<ProjecteUsuari> projectes_usuaris;
    
    
    public Projecte(int id, String nom, String descripcio, Usuari capProjecte, List<Tasca> tasques, List<ProjecteUsuari> projectes_usuaris ) {
        this.id = id;
        this.nom = nom;
        this.descripcio = descripcio;
        this.capProjecte = capProjecte;
        this.tasques = tasques;
        this.projectes_usuaris = projectes_usuaris;
    }
    
    protected Projecte(){
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Usuari getCapProjecte() {
        return capProjecte;
    }

    public void setCapProjecte(Usuari capProjecte) {
        this.capProjecte = capProjecte;
    }

    public List<Tasca> getTasques() {
        return tasques;
    }

    public void setTasques(List<Tasca> tasques) {
        this.tasques = tasques;
    }
    public List<ProjecteUsuari> usuaris(){
        return projectes_usuaris;
    }
    
    public void setUsuaris(List<ProjecteUsuari> usuaris){
        this.projectes_usuaris = usuaris;
    }
    

    @Override
    public int hashCode() {
        int hash = 5;
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
        final Projecte other = (Projecte) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Projecte{" + "id=" + id + ", nom=" + nom + ", descripcio=" + descripcio + ", capProjecte=" + capProjecte + ", tasques=" + tasques + ", usuaris=" + projectes_usuaris + '}';
    }


    
    
    
    
    
    
    
    
    
}
