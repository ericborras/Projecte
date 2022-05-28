package com.example.treballadorsapp;

import androidx.lifecycle.ViewModel;

import org.milaifontanals.model.Projecte;

import java.util.List;

public class ProjectesViewModel extends ViewModel {

    public List<Projecte> mProjectes;

    public ProjectesViewModel(List<Projecte> projectes){
        mProjectes = projectes;
    }


}
