package org.milaifontanals.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class Usuari implements Serializable{
    
    private int id;
    private String nom;
    private String cognom1;
    private String cognom2;
    private Date dataNaixement;
    private String login;
    private String passwdHash;
    private List<Tasca> tasques;
    private List<ProjecteUsuari> projectes_usuaris;

    public Usuari(int id, String nom, String cognom1, String cognom2, Date dataNaixement, String login, String passwdHash, List<Tasca> tasques, List<ProjecteUsuari> projectes_usuaris) {
        this.id = id;
        this.nom = nom;
        this.cognom1 = cognom1;
        this.cognom2 = cognom2;
        this.dataNaixement = dataNaixement;
        this.login = login;
        this.passwdHash = passwdHash;
        this.tasques = tasques;
        this.projectes_usuaris = projectes_usuaris;
    }

    public Usuari(String nom, String cognom1, Date dataNaixement, String login, String passwdHash) {
        this.nom = nom;
        this.cognom1 = cognom1;
        this.dataNaixement = dataNaixement;
        this.login = login;
        this.passwdHash = passwdHash;
    }

    public Usuari(int id, String nom, String cognom1, Date dataNaixement, String login, String passwdHash) {
        this.id = id;
        this.nom = nom;
        this.cognom1 = cognom1;
        this.dataNaixement = dataNaixement;
        this.login = login;
        this.passwdHash = passwdHash;
    }

    public Usuari(int id, String nom, String cognom1) {
        this.id = id;
        this.nom = nom;
        this.cognom1 = cognom1;
    }


    
    
    
    protected Usuari(){
        
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

    public String getCognom1() {
        return cognom1;
    }

    public void setCognom1(String cognom1) {
        this.cognom1 = cognom1;
    }

    public String getCognom2() {
        return cognom2;
    }

    public void setCognom2(String cognom2) {
        this.cognom2 = cognom2;
    }

    public Date getDataNaixement() {
        return dataNaixement;
    }

    public void setDataNaixement(Date dataNaixement) {
        this.dataNaixement = dataNaixement;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswdHash() {
        return passwdHash;
    }

    public void setPasswdHash(String passwdHash) {
        this.passwdHash = passwdHash;
    }

    public List<Tasca> getTasques() {
        return tasques;
    }

    public void setTasques(List<Tasca> tasques) {
        this.tasques = tasques;
    }
    
    public List<ProjecteUsuari> getProjectes(){
        return this.projectes_usuaris;
    }
    
    public void setProjectes(List<ProjecteUsuari> projecetes){
        this.projectes_usuaris = projectes_usuaris;
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
        final Usuari other = (Usuari) obj;
        if (!Objects.equals(this.login, other.login)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        String out ="";
        out += this.id+": "+this.nom+" "+this.cognom1+" ";
        if(this.cognom2!=null){
         out += this.cognom2+" ";   
        }
        return out;
                
    }


    
    
}
