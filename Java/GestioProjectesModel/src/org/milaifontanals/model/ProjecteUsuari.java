
package org.milaifontanals.model;

import java.io.Serializable;
import java.util.Objects;


public class ProjecteUsuari implements Serializable{
    
    private Integer id;
    private Projecte projecte;
    private Usuari usuari;
    private Rol rol;

    protected ProjecteUsuari(){
        
    }
    
    public ProjecteUsuari(Projecte projecte, Usuari usuari, Rol rol) {
        this.projecte = projecte;
        this.usuari = usuari;
        this.rol = rol;
    }

    public ProjecteUsuari(Integer id, Projecte projecte, Usuari usuari, Rol rol) {
        this.id = id;
        this.projecte = projecte;
        this.usuari = usuari;
        this.rol = rol;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    
    public Projecte getProjecte() {
        return projecte;
    }

    public void setProjecte(Projecte projecte) {
        this.projecte = projecte;
    }

    public Usuari getUsuari() {
        return usuari;
    }

    public void setUsuari(Usuari usuari) {
        this.usuari = usuari;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.projecte);
        hash = 67 * hash + Objects.hashCode(this.usuari);
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
        final ProjecteUsuari other = (ProjecteUsuari) obj;
        if (!Objects.equals(this.projecte, other.projecte)) {
            return false;
        }
        if (!Objects.equals(this.usuari, other.usuari)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProjecteUsuari{" + "id=" + id + ", projecte=" + projecte + ", usuari=" + usuari + ", rol=" + rol + '}';
    }


    
    
    
}
