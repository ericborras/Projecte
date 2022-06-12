package org.milaifontanals.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
        setId(id);
        setNom(nom);
        setCognom1(cognom1);
        setCognom2(cognom2);
        setDataNaixement(dataNaixement);
        setLogin(login);
        setPasswdHash(passwdHash);
        setTasques(tasques);
        setProjectes(projectes_usuaris);
    }

    public Usuari(String nom, String cognom1, Date dataNaixement, String login, String passwdHash) {
        setNom(nom);
        setCognom1(cognom1);
        setDataNaixement(dataNaixement);
        setLogin(login);
        setPasswdHash(passwdHash);
    }

    public Usuari(int id, String nom, String cognom1, Date dataNaixement, String login, String passwdHash) {
        setId(id);
        setNom(nom);
        setCognom1(cognom1);
        setDataNaixement(dataNaixement);
        setLogin(login);
        setPasswdHash(passwdHash);
    }

    public Usuari(int id, String nom, String cognom1) {
        setId(id);
        setNom(nom);
        setCognom1(cognom1);
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
        
        Pattern pattern = Pattern
                .compile("^[\\p{L} .'-]+$");
        
        Matcher valida = pattern.matcher(nom);
        
        if(!valida.matches()){
            throw new RuntimeException("El nombre sólo puede contener carácteres válidos");
        }
        
        this.nom = nom;
    }

    public String getCognom1() {
        return cognom1;
    }

    public void setCognom1(String cognom1) {
        
        Pattern pattern = Pattern
                .compile("^[\\p{L} .'-]+$");
        
        Matcher valida = pattern.matcher(cognom1);
        
        if(!valida.matches()){
            throw new RuntimeException("El primer apellido sólo puede contener carácteres válidos");
        }
        
        this.cognom1 = cognom1;
    }

    public String getCognom2() {
        return cognom2;
    }

    public void setCognom2(String cognom2) {
        
        if(cognom2!=null){
            Pattern pattern = Pattern
                .compile("^[\\p{L} .'-]+$");
        
            Matcher valida = pattern.matcher(cognom2);

            if(!valida.matches()){
                throw new RuntimeException("El segundo apellido sólo puede contener carácteres válidos");
            }
        }        
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
