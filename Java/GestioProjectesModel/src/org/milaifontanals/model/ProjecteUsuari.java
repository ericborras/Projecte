
package org.milaifontanals.model;

import java.util.Objects;


public class ProjecteUsuari {
    
    private Usuari usuari;
    private Projecte projecte;
    private Rol rol;

    public ProjecteUsuari(Usuari usuari, Projecte projecte, Rol rol) {
        this.usuari = usuari;
        this.projecte = projecte;
        this.rol = rol;
    }

    public Usuari getUsuari() {
        return usuari;
    }

    public void setUsuari(Usuari usuari) {
        this.usuari = usuari;
    }

    public Projecte getProjecte() {
        return projecte;
    }

    public void setProjecte(Projecte projecte) {
        this.projecte = projecte;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.usuari);
        hash = 67 * hash + Objects.hashCode(this.projecte);
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
        if (!Objects.equals(this.usuari, other.usuari)) {
            return false;
        }
        if (!Objects.equals(this.projecte, other.projecte)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProjecteUsuari{" + "usuari=" + usuari + ", projecte=" + projecte + ", rol=" + rol + '}';
    }
    
    
    
}
