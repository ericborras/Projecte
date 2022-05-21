
package org.milaifontanals.model;

import java.io.Serializable;
import java.util.Date;


public class Entrada implements Serializable{
    
    private int numero;
    private Date dataEntrada;
    private String entrada;
    private Usuari escriptor;
    private Usuari novaAssignacio;
    private Estat nouEstat;

    public Entrada(int numero, Date dataEntrada, String entrada, Usuari escriptor, Usuari novaAssignacio, Estat nouEstat) {
        this.numero = numero;
        this.dataEntrada = dataEntrada;
        this.entrada = entrada;
        this.escriptor = escriptor;
        this.novaAssignacio = novaAssignacio;
        this.nouEstat = nouEstat;
    }

    public Entrada(int numero, Date dataEntrada, String entrada, Usuari escriptor, Usuari novaAssignacio) {
        this.numero = numero;
        this.dataEntrada = dataEntrada;
        this.entrada = entrada;
        this.escriptor = escriptor;
        this.novaAssignacio = novaAssignacio;
    }
    
    
    
    protected Entrada(){
        
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Date getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public String getEntrada() {
        return entrada;
    }

    public void setEntrada(String entrada) {
        this.entrada = entrada;
    }

    public Usuari getEscriptor() {
        return escriptor;
    }

    public void setEscriptor(Usuari escriptor) {
        this.escriptor = escriptor;
    }

    public Usuari getNovaAssignacio() {
        return novaAssignacio;
    }

    public void setNovaAssignacio(Usuari novaAssignacio) {
        this.novaAssignacio = novaAssignacio;
    }

    public Estat getNouEstat() {
        return nouEstat;
    }

    public void setNouEstat(Estat nouEstat) {
        this.nouEstat = nouEstat;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this.numero;
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
        final Entrada other = (Entrada) obj;
        if (this.numero != other.numero) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entrada{" + "numero=" + numero + ", dataEntrada=" + dataEntrada + ", entrada=" + entrada + ", escriptor=" + escriptor + ", novaAssignacio=" + novaAssignacio + ", nouEstat=" + nouEstat + '}';
    }
    
    
    
    
    
}
