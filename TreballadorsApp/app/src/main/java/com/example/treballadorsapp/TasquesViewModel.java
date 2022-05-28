package com.example.treballadorsapp;

import androidx.lifecycle.ViewModel;

import org.milaifontanals.model.Projecte;
import org.milaifontanals.model.Tasca;

import java.util.List;

public class TasquesViewModel extends ViewModel {

    public List<Tasca> mTasca;

    public TasquesViewModel(List<Tasca> projectes){
        mTasca = projectes;
    }

}
