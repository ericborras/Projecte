
package org.milaifontanals.model;

import java.io.Serializable;


public class Rol implements Serializable{
    
    private int id;
    private String nom;
    
    protected Rol(){
        
    }

    public Rol(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final Rol other = (Rol) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.nom;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }
    
    
    
    
    
    
}
