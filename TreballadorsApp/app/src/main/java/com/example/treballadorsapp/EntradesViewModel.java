package com.example.treballadorsapp;

import androidx.lifecycle.ViewModel;

import org.milaifontanals.model.Entrada;

import java.util.List;

public class EntradesViewModel extends ViewModel {


    public List<Entrada> mEntrades;

    public EntradesViewModel(List<Entrada> entrades){
        mEntrades = entrades;
    }




}
