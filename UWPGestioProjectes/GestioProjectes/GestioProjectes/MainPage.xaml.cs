using BDLib;
using BDLib.Model;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;

// La plantilla de elemento Página en blanco está documentada en https://go.microsoft.com/fwlink/?LinkId=402352&clcid=0xc0a

namespace GestioProjectes
{
    /// <summary>
    /// Página vacía que se puede usar de forma independiente o a la que se puede navegar dentro de un objeto Frame.
    /// </summary>
    public sealed partial class MainPage : Page
    {


        private ObservableCollection<Projecte> projectes = new ObservableCollection<Projecte>();
        private ObservableCollection<Usuari> usuaris_projecte = new ObservableCollection<Usuari>();
        private ObservableCollection<Usuari> usuaris = new ObservableCollection<Usuari>();
        private bool modeAltaProjectes = false;
        private bool modeEditProjectes = true;
        private int idxCapProjecte = -1;
        private Projecte projecte;

        private int cap_projecte_id, projecte_id;


        private ObservableCollection<Tasca> tasques = new ObservableCollection<Tasca>();


        private Tasca tasca;
        private bool modeAltaTasques = false;
        private bool modeEditTasques = true;
        private int idxPropietariTasca = -1;
        private int idxResponsableTasca = -1;
        private int idxEstatTasca = -1;

        private ObservableCollection<Estat> estats = new ObservableCollection<Estat>();



        public MainPage()
        {
            this.InitializeComponent();
        }

        private void dtgProjectes_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            modeAltaProjectes = false;
            modeEditProjectes = true;

            btnEliminaProjecte.IsEnabled = true;
            if (dtgProjectes.SelectedItem != null)
            {



                projecte = (Projecte)dtgProjectes.SelectedItem;
                txbNomProjecte.Text = projecte.Nom;
                txbDescProjecte.Text = projecte.Descripcio;

                Debug.WriteLine("PROJECTE: " +projecte.Id);
                usuaris_projecte = CPGestioProjectes.GetUsuarisProjecte(projecte.Id);
                cboCapProjecte.ItemsSource = usuaris_projecte;
                cboCapProjecte.DisplayMemberPath = "Nomcomplet";
                             
                for(int i = 0; i < usuaris_projecte.Count; i++)
                {
                    if (usuaris_projecte[i].Id == projecte.CapProjecte.Id)
                    {
                        idxCapProjecte = i;
                    }
                }
                cboCapProjecte.SelectedIndex = idxCapProjecte;


                tasques = CPGestioProjectes.GetUsuarisTasquesProjecte(projecte.Id);
                dtgTasques.ItemsSource = tasques;
                

            }
        }

        private void cboCapProjecte_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {

        }

        private void btnNouProjecte_Click(object sender, RoutedEventArgs e)
        {
            modeAltaProjectes = true;
            modeEditProjectes = false;

            txbNomProjecte.Text = "";
            txbDescProjecte.Text = "";
            cboCapProjecte.SelectedIndex = -1;

            btnEliminaProjecte.IsEnabled = false;

            //Carregar tota la llista d'usuaris
            usuaris = CPGestioProjectes.GetUsuaris();
            cboCapProjecte.ItemsSource = usuaris;
            cboCapProjecte.DisplayMemberPath = "Nomcomplet";


        }

        private void btnCancelProjecte_Click(object sender, RoutedEventArgs e)
        {
            modeAltaProjectes = false;
            modeEditProjectes = true;

            if (projecte != null)
            {
                txbNomProjecte.Text = projecte.Nom;
                txbDescProjecte.Text = projecte.Descripcio;
                cboCapProjecte.SelectedIndex = idxCapProjecte;
            }




        }

        private void btnSaveProjecte_Click(object sender, RoutedEventArgs e)
        {

            if (txbNomProjecte.Text.Trim().Length > 0 && txbDescProjecte.Text.Trim().Length > 0 && cboCapProjecte.SelectedIndex != -1)
            {
                

                if (modeAltaProjectes)
                {
                    Usuari capProjecte = (Usuari)cboCapProjecte.SelectedValue;
                    Projecte projecte = new Projecte(txbNomProjecte.Text.Trim(), txbDescProjecte.Text.Trim(), capProjecte);


                    if (CPGestioProjectes.InsertProjecte(projecte))
                    {

                        //Afegir a la llista en memòria
                        projectes = CPGestioProjectes.GetProjectes();
                        dtgProjectes.ItemsSource = null;
                        dtgProjectes.ItemsSource = projectes;

                        foreach(Projecte proj in projectes)
                        {
                            if (proj.Nom.Equals(projecte.Nom))
                            {
                                cap_projecte_id = proj.CapProjecte.Id;
                                projecte_id = proj.Id;
                            }
                        }


                        if(CPGestioProjectes.InsertCapProjecte(cap_projecte_id, projecte_id))
                        {
                            usuaris_projecte.Add(capProjecte);
                            mostraMissatge("Inserción", "Se ha insertado el proyecto correctamente", "OK");
                        }
                        else
                        {
                            mostraMissatge("Inserción", "No se ha podido insertar el jefe del proyecto correctamente", "OK");
                        }
                        


                       




                    }
                    else
                    {
                        mostraMissatge("Error", "Ha habido un error al insertar el proyecto", "OK");
                    }
                }
                else
                {
                    Console.WriteLine("PROJECTE: " + projecte.Id);
                    projecte.Nom = txbNomProjecte.Text.Trim();
                    projecte.Descripcio = txbDescProjecte.Text.Trim();
                    projecte.CapProjecte = (Usuari)cboCapProjecte.SelectedItem;
                     
                    //Mode edit
                    if (CPGestioProjectes.updateProjecte(projecte))
                    {
                        //Reemplaçar objecte de la llista en memòria
                        for(int i = 0; i < projectes.Count; i++)
                        {
                            if (projectes[i].Id == projecte.Id)
                            {
                                projectes[i].Nom = projecte.Nom;
                                projectes[i].Descripcio = projecte.Descripcio;
                                projectes[i].CapProjecte = projecte.CapProjecte;
                            }
                        }

                        dtgProjectes.ItemsSource = null;
                        dtgProjectes.ItemsSource = projectes;
                        mostraMissatge("Modificación", "Se ha modificado el proyecto correctamente", "OK");
                    }
                    else
                    {
                        mostraMissatge("Modificación", "No se ha podido modificar el proyecto", "OK");
                    }
                }



            }
            else
            {
                mostraMissatge("Error", "No puede haber ningun campo vacío", "OK");
            }

        }

        private async void mostraMissatge(string title, string content, string contentButton)
        {
            
                ContentDialog deleteFileDialog = new ContentDialog
                {
                    Title = title,
                    Content = content,
                    PrimaryButtonText = contentButton
                };

                ContentDialogResult result = await deleteFileDialog.ShowAsync();

                // Delete the file if the user clicked the primary button.
                /// Otherwise, do nothing.
                if (result == ContentDialogResult.Primary)
                {

                }
                else
                {
                    // The user clicked the CLoseButton, pressed ESC, Gamepad B, or the system back button.
                    // Do nothing.
                }
            
        }

        private void Page_Loaded(object sender, RoutedEventArgs e)
        {
            inicialitzaDatagridProjecte();
        }

        private void inicialitzaDatagridProjecte()
        {
            projectes = CPGestioProjectes.GetProjectes();
            dtgProjectes.ItemsSource = projectes;
        }

        private void btnNouTasca_Click(object sender, RoutedEventArgs e)
        {
            modeAltaTasques = true;
            modeEditTasques = false;

            txbNomTasca.Text = "";
            txbDescripcioTasca.Text = "";
            //txbDataLimitTasca.SelectedDate = null;//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

            cboEstadoTasca.SelectedIndex = -1;
            cboPropietariTasca.SelectedIndex = -1;
            cboResponsableTasca.SelectedIndex = -1;
           
        }

        private void btnSaveTasca_Click(object sender, RoutedEventArgs e)
        {

            if (txbNomTasca.Text.Trim().Length > 0 && txbDescripcioTasca.Text.Trim().Length > 0 && cboPropietariTasca.SelectedIndex!= -1 && cboEstadoTasca.SelectedIndex!= -1)
            {

                Tasca tasc = new Tasca(txbNomTasca.Text.Trim(), txbDescripcioTasca.Text.Trim());
                tasc.DataCreacio = DateTime.Now;
                tasc.Propietari = (Usuari)cboPropietariTasca.SelectedItem;


                tasc.Estat = (Estat)cboEstadoTasca.SelectedItem;

                if (cboResponsableTasca.SelectedIndex != -1)
                {
                    tasc.Responsable = (Usuari)cboResponsableTasca.SelectedItem;
                }
                /*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                if (txbDataLimitTasca.SelectedDate != null)
                {
                    tasc.DataLimit = txbDataLimitTasca.Date.DateTime;
                }
                */
                if (modeAltaTasques)
                {
                    

                    if(CPGestioProjectes.InsertTasca(tasc, projecte.Id))
                    {

                        tasques = CPGestioProjectes.GetUsuarisTasquesProjecte(projecte.Id);
                        dtgTasques.ItemsSource = null;
                        dtgTasques.ItemsSource = tasques;

                        mostraMissatge("Inserción", "Se ha insertado la tarea correctamente", "OK");
                    }
                    else
                    {
                        mostraMissatge("Error", "Error al insertar la tarea", "OK");
                    }

                }
                else
                {
                    //Mode edició

                    if (CPGestioProjectes.updateTasca(tasc, projecte.Id))
                    {
                        tasques = CPGestioProjectes.GetUsuarisTasquesProjecte(projecte.Id);
                        dtgTasques.ItemsSource = null;
                        dtgTasques.ItemsSource = tasques;

                        mostraMissatge("Inserción", "Se ha actualizado la tarea correctamente", "OK");
                    }
                    else
                    {
                        mostraMissatge("Error", "Error al actualizar la tarea", "OK");
                    } 





                }

            }
            else
            {
                mostraMissatge("Error", "Los campos nombre, descripción, propietario, estado son obligatorios", "OK");
            }




        }

        private void btnCancelTasca_Click(object sender, RoutedEventArgs e)
        {
            modeAltaTasques = false;
            modeEditTasques = true;

            if (tasca != null)
            {
                txbNomTasca.Text = tasca.Nom;
                txbDescripcioTasca.Text = tasca.Descripcio;
                txbDataLimitTasca.Date = tasca.DataLimit;

                cboEstadoTasca.SelectedIndex = idxEstatTasca;
                cboPropietariTasca.SelectedIndex = idxPropietariTasca;
                cboResponsableTasca.SelectedIndex = idxResponsableTasca;
            }

        }

        private void btnEliminaTasca_Click(object sender, RoutedEventArgs e)
        {

        }

        private void dtgTasques_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {

            modeEditTasques = true;
            modeAltaTasques = false;


            tasca = (Tasca)dtgTasques.SelectedItem;
            

            if(tasca != null)
            {
                txbNomTasca.Text = tasca.Nom;
                txbDescripcioTasca.Text = tasca.Descripcio;
                try
                {
                    txbDataLimitTasca.Date = tasca.DataLimit;
                }
                catch { }



                cboPropietariTasca.ItemsSource = usuaris_projecte;
                cboPropietariTasca.DisplayMemberPath = "Nomcomplet";

                for (int i = 0; i < usuaris_projecte.Count; i++)
                {
                    if (usuaris_projecte[i].Id == tasca.Propietari.Id)
                    {
                        idxPropietariTasca = i;
                    }
                }
                cboPropietariTasca.SelectedIndex = idxPropietariTasca;


                cboResponsableTasca.ItemsSource = usuaris_projecte;
                cboResponsableTasca.DisplayMemberPath = "Nomcomplet";

                for(int i = 0;i< usuaris_projecte.Count; i++)
                {
                    if(usuaris_projecte[i].Id == tasca.Responsable.Id)
                    {
                        idxResponsableTasca = i;
                    }
                }
                cboResponsableTasca.SelectedIndex = idxResponsableTasca;



                estats = CPGestioProjectes.GetUsuarisTasquesEstats();
                cboEstadoTasca.ItemsSource = estats;
                cboEstadoTasca.DisplayMemberPath = "Nom_estat";


                for (int i = 0; i < estats.Count; i++)
                {
                    if (estats[i].Codi_estat == tasca.Estat.Codi_estat)
                    {
                        idxEstatTasca = i;
                    }
                }
                cboEstadoTasca.SelectedIndex = idxEstatTasca;


            }









        }

        private void dtgEntrada_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {

        }

        private void btnNouEntrada_Click(object sender, RoutedEventArgs e)
        {

        }

        private void btnSaveEntrada_Click(object sender, RoutedEventArgs e)
        {

        }

        private void btnCancelEntrada_Click(object sender, RoutedEventArgs e)
        {

        }

        private void btnEliminaEntrada_Click(object sender, RoutedEventArgs e)
        {

        }

        private void btnEliminaProjecte_Click(object sender, RoutedEventArgs e)
        {

            //TODO: BORRAR EL PROYECTO PARA LO ÚLTIMO


            btnEliminaProjecte.IsEnabled = false;
        }
    }
}
