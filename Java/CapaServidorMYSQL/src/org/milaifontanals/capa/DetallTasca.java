/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.capa;

import java.util.List;
import org.milaifontanals.model.Entrada;
import org.milaifontanals.model.Tasca;

/**
 *
 * @author Lenovo T530
 */
public class DetallTasca {
    private Tasca tasca;
    private List<Entrada> entrades;

    public DetallTasca(Tasca tasca, List<Entrada> entrades) {
        this.tasca = tasca;
        this.entrades = entrades;
    }

    public DetallTasca(Tasca tasca) {
        this.tasca = tasca;
    }
    
    

    public Tasca getTasca() {
        return tasca;
    }

    public void setTasca(Tasca tasca) {
        this.tasca = tasca;
    }

    public List<Entrada> getEntrades() {
        return entrades;
    }

    public void setEntrades(List<Entrada> entrades) {
        this.entrades = entrades;
    }
}
