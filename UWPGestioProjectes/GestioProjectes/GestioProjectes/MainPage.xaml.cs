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


        private ObservableCollection<Entrada> entrades = new ObservableCollection<Entrada>();
        private Entrada entrada_seleccionada;
        private bool modeAltaEntrades = false;
        private bool modeEditEntrades = true;
        private int idxNovaAssignEntrada = -1;
        private int idxEscriptorEntrada = -1;
        private int idxEstatEntrada = -1;



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

                cboPropietariTasca.ItemsSource = usuaris_projecte;
                cboPropietariTasca.DisplayMemberPath = "Nomcomplet";

                cboResponsableTasca.ItemsSource = usuaris_projecte;
                cboResponsableTasca.DisplayMemberPath = "Nomcomplet";

                cboNovaAssign.ItemsSource = usuaris_projecte;
                cboNovaAssign.DisplayMemberPath = "Nomcomplet";

                cboEscriptor.ItemsSource = usuaris_projecte;
                cboEscriptor.DisplayMemberPath = "Nomcomplet";


                for (int i = 0; i < usuaris_projecte.Count; i++)
                {
                    if (usuaris_projecte[i].Id == projecte.CapProjecte.Id)
                    {
                        idxCapProjecte = i;
                    }
                }
                cboCapProjecte.SelectedIndex = idxCapProjecte;


                estats = CPGestioProjectes.GetUsuarisTasquesEstats();
                cboEstadoTasca.ItemsSource = estats;
                cboEstadoTasca.DisplayMemberPath = "Nom_estat";

                cboNouEstat.ItemsSource = estats;
                cboNouEstat.DisplayMemberPath = "Nom_estat";

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
            txbDataLimitTasca.SelectedDate = null;

            cboEstadoTasca.SelectedIndex = -1;
            cboPropietariTasca.SelectedIndex = -1;
            cboResponsableTasca.SelectedIndex = -1;
           
        }

        private void btnSaveTasca_Click(object sender, RoutedEventArgs e)
        {


            if (projecte != null)
            {
                if (txbNomTasca.Text.Trim().Length > 0 && txbDescripcioTasca.Text.Trim().Length > 0 && cboPropietariTasca.SelectedIndex != -1 && cboEstadoTasca.SelectedIndex != -1)
                {

                    Tasca tasc = new Tasca(txbNomTasca.Text.Trim(), txbDescripcioTasca.Text.Trim());
                    tasc.DataCreacio = DateTime.Now;
                    tasc.Propietari = (Usuari)cboPropietariTasca.SelectedItem;
                    


                    tasc.Estat = (Estat)cboEstadoTasca.SelectedItem;

                    if (cboResponsableTasca.SelectedIndex != -1)
                    {
                        tasc.Responsable = (Usuari)cboResponsableTasca.SelectedItem;
                    }

                    if (txbDataLimitTasca.SelectedDate != null)
                    {
                        tasc.DataLimit = txbDataLimitTasca.Date.DateTime;
                    }

                    if (modeAltaTasques)
                    {


                        if (CPGestioProjectes.InsertTasca(tasc, projecte.Id))
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
                        Tasca taux = (Tasca)dtgTasques.SelectedItem;
                        tasc.Id = taux.Id;

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
            else
            {
                mostraMissatge("Error", "Debes seleccionar el proyecto primero", "OK");
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
            btnEliminaTasca.IsEnabled = false;
            DisplayDeleteTasca();
        }

        private void dtgTasques_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {

            modeEditTasques = true;
            modeAltaTasques = false;

            btnEliminaTasca.IsEnabled = true;


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
                    if (tasca.Responsable != null)
                    {
                        if (usuaris_projecte[i].Id == tasca.Responsable.Id)
                        {
                            idxResponsableTasca = i;
                        }
                    }
                    else
                    {
                        idxResponsableTasca = -1;
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






                //Mostrar info de les entrades de la tasca
                entrades = CPGestioProjectes.GetEntradesTasca(tasca.Id);
                dtgEntrada.ItemsSource = entrades;

            }












        }

        private void dtgEntrada_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {

            modeAltaEntrades = false;
            modeEditEntrades = true;

            entrada_seleccionada = (Entrada)dtgEntrada.SelectedItem;

            if (entrada_seleccionada != null)
            {
                btnEliminaEntrada.IsEnabled = true;

                txbEntrada.Text = entrada_seleccionada.Entrada_e;


                cboNovaAssign.ItemsSource = usuaris_projecte;
                cboNovaAssign.DisplayMemberPath = "Nomcomplet";
                if (entrada_seleccionada.NovaAssignacio != null) {


                    for (int i = 0; i < usuaris_projecte.Count; i++)
                    {
                        if (usuaris_projecte[i].Id == entrada_seleccionada.NovaAssignacio.Id)
                        {
                            idxNovaAssignEntrada = i;
                        }
                    }
                    cboNovaAssign.SelectedIndex = idxNovaAssignEntrada;
                }


                cboEscriptor.ItemsSource = usuaris_projecte;
                cboEscriptor.DisplayMemberPath = "Nomcomplet";
                for (int i = 0; i < usuaris_projecte.Count; i++)
                {
                    if (usuaris_projecte[i].Id == entrada_seleccionada.Escriptor.Id)
                    {
                        idxEscriptorEntrada = i;
                    }
                }
                cboEscriptor.SelectedIndex = idxEscriptorEntrada;



                estats = CPGestioProjectes.GetUsuarisTasquesEstats();
                cboNouEstat.ItemsSource = estats;
                cboNouEstat.DisplayMemberPath = "Nom_estat";
                for (int i = 0; i < estats.Count; i++)
                {
                    if (estats[i].Codi_estat == entrada_seleccionada.NouEstat.Codi_estat)
                    {
                        idxEstatEntrada = i;
                    }
                }
                cboNouEstat.SelectedIndex = idxEstatEntrada;


            }


        }

        private void btnNouEntrada_Click(object sender, RoutedEventArgs e)
        {
            modeAltaEntrades = true;
            modeEditEntrades = false;

            txbEntrada.Text = "";
            cboNovaAssign.SelectedIndex = -1;
            cboEscriptor.SelectedIndex = -1;
            cboNouEstat.SelectedIndex = -1;


        }

        private void btnSaveEntrada_Click(object sender, RoutedEventArgs e)
        {


            if (tasca != null)
            {
                if (txbEntrada.Text.Trim().Length == 0 || cboEscriptor.SelectedIndex == -1)
                {
                    mostraMissatge("Error", "Debes ponerle un nombre a la entrada y debe tener un escritor", "OK");
                }
                else
                {

                    Entrada entrada = new Entrada(DateTime.Now, txbEntrada.Text.Trim());

                    if (cboNovaAssign.SelectedIndex != -1)
                    {
                        Usuari novaAssignacio = (Usuari)cboNovaAssign.SelectedItem;
                        entrada.NovaAssignacio = novaAssignacio;
                    }

                    Usuari escriptor = (Usuari)cboEscriptor.SelectedItem;
                    entrada.Escriptor = escriptor;

                    if (cboNouEstat.SelectedIndex != -1)
                    {
                        Estat estat = (Estat)cboNouEstat.SelectedItem;
                        entrada.NouEstat = estat;
                    }

                    if (modeAltaEntrades)
                    {
                        if (CPGestioProjectes.InsertEntrada(entrada, tasca.Id))
                        {
                            entrades = CPGestioProjectes.GetEntradesTasca(tasca.Id);
                            dtgEntrada.ItemsSource = null;
                            dtgEntrada.ItemsSource = entrades;

                            mostraMissatge("Inserción", "Se ha insertado la entrada correctamente", "OK");
                        }
                        else
                        {
                            mostraMissatge("Error", "No se ha podido insertar la entrada", "OK");
                        }


                    }
                    else
                    {
                        entrada.Numero = entrada_seleccionada.Numero;
                        //Mode edició
                        if (CPGestioProjectes.updateEntrada(entrada))
                        {
                            entrades = CPGestioProjectes.GetEntradesTasca(tasca.Id);
                            dtgEntrada.ItemsSource = null;
                            dtgEntrada.ItemsSource = entrades;

                            mostraMissatge("Actualización", "Se ha actualizado la entrada correctamente", "OK");
                        }
                        else
                        {
                            mostraMissatge("Error", "No se ha podido actualizar la entrada", "OK");
                        }
                    }


                }
            }
            else
            {
                mostraMissatge("Error", "Tienes que seleccionar a que tarea pertenece", "OK");
            }

        }




        private async void DisplayDeleteTasca()
        {
            ContentDialog deleteFileDialog = new ContentDialog
            {
                Title = "Eliminar permanentemente",
                Content = "¿Estás seguro de querer eliminar la tarea? Se eliminarán TODAS las entradas asociadas",
                PrimaryButtonText = "Eliminar",
                CloseButtonText = "Cancelar"
            };

            ContentDialogResult result = await deleteFileDialog.ShowAsync();

            // Delete the file if the user clicked the primary button.
            /// Otherwise, do nothing.
            if (result == ContentDialogResult.Primary)
            {
                bool esborrats = true;

                foreach(Entrada entrada in entrades)
                {
                    if (!CPGestioProjectes.deleteEntrada(entrada.Numero))
                    {
                        esborrats = false;
                    }
                }

                if (esborrats)
                {
                    //En cas que s'hagin esborrat correctament les entrades corresponents, eliminar la tasca



                    if (CPGestioProjectes.deleteTasca(tasca.Id))
                    {


                        dtgEntrada.ItemsSource = null;

                        tasques.Remove(tasca);
                        dtgTasques.ItemsSource = null;
                        dtgTasques.ItemsSource = tasques;


                        txbNomTasca.Text = "";
                        txbDescripcioTasca.Text = "";
                        txbDataLimitTasca.SelectedDate = null;
                        cboPropietariTasca.SelectedIndex = -1;
                        cboResponsableTasca.SelectedIndex = -1;
                        cboEstadoTasca.SelectedIndex = -1;


                        txbEntrada.Text = "";
                        cboEscriptor.SelectedIndex = -1;
                        cboNouEstat.SelectedIndex = -1;
                        cboNovaAssign.SelectedIndex = -1;



                        mostraMissatge("Eliminar", "Se ha eliminado correctamente la tarea y todas las entradas relacionadas", "OK");
                    }
                    else
                    {
                        mostraMissatge("Error", "No se ha podido eliminar la tarea", "OK");
                    }


                }
                else
                {
                    mostraMissatge("Error", "No se ha podido eliminar la tarea porque alguna entrada no se ha eliminado correctamente", "OK");
                }
                

                

            }
            else
            {
                // The user clicked the CLoseButton, pressed ESC, Gamepad B, or the system back button.
                // Do nothing.

                btnEliminaTasca.IsEnabled = true;
            }
        }



        private async void DisplayDeleteEntrada()
        {
            ContentDialog deleteFileDialog = new ContentDialog
            {
                Title = "Eliminar permanentemente",
                Content = "¿Estás seguro de querer eliminar la entrada?",
                PrimaryButtonText = "Eliminar",
                CloseButtonText = "Cancelar"
            };

            ContentDialogResult result = await deleteFileDialog.ShowAsync();

            // Delete the file if the user clicked the primary button.
            /// Otherwise, do nothing.
            if (result == ContentDialogResult.Primary)
            {

                if (CPGestioProjectes.deleteEntrada(entrada_seleccionada.Numero))
                {
                    entrades = CPGestioProjectes.GetEntradesTasca(tasca.Id);
                    dtgEntrada.ItemsSource = null;
                    dtgEntrada.ItemsSource = entrades;

                    txbEntrada.Text = "";
                    cboEscriptor.SelectedIndex = -1;
                    cboNouEstat.SelectedIndex = -1;
                    cboNovaAssign.SelectedIndex = -1;

                    mostraMissatge("Eliminar", "Se ha eliminado la entrada correctamente", "OK");
                }
                else
                {
                    mostraMissatge("Error", "No se ha podido eliminar la entrada", "OK");
                }

            }
            else
            {
                // The user clicked the CLoseButton, pressed ESC, Gamepad B, or the system back button.
                // Do nothing.

                btnEliminaEntrada.IsEnabled = true;
            }
        }





        private void btnCancelEntrada_Click(object sender, RoutedEventArgs e)
        {

            if (entrada_seleccionada != null)
            {
                txbEntrada.Text = entrada_seleccionada.Entrada_e;


                if (entrada_seleccionada.NovaAssignacio != null)
                {
                    for (int i = 0; i < usuaris_projecte.Count; i++)
                    {
                        if (usuaris_projecte[i].Id == entrada_seleccionada.NovaAssignacio.Id)
                        {
                            idxNovaAssignEntrada = i;
                        }
                    }
                    cboPropietariTasca.SelectedIndex = idxNovaAssignEntrada;
                }


                for (int i = 0; i < usuaris_projecte.Count; i++)
                {
                    if (usuaris_projecte[i].Id == entrada_seleccionada.Escriptor.Id)
                    {
                        idxEscriptorEntrada = i;
                    }
                }
                cboEscriptor.SelectedIndex = idxEscriptorEntrada;


                if (entrada_seleccionada.NouEstat != null)
                {
                    for (int i = 0; i < estats.Count; i++)
                    {
                        if (estats[i].Codi_estat == entrada_seleccionada.NouEstat.Codi_estat)
                        {
                            idxEstatEntrada = i;
                        }
                    }
                    cboNouEstat.SelectedIndex = idxEstatEntrada;
                }



            }



        }

        private void btnEliminaEntrada_Click(object sender, RoutedEventArgs e)
        {
            btnEliminaEntrada.IsEnabled = false;
            DisplayDeleteEntrada();
        }

        private void btnEliminaProjecte_Click(object sender, RoutedEventArgs e)
        {

            //TODO: BORRAR EL PROYECTO PARA LO ÚLTIMO


            btnEliminaProjecte.IsEnabled = false;

            DisplayDeleteProjecte();



        }


        private void btnGoReport_Click(object sender, RoutedEventArgs e)
        {
            this.Frame.Navigate(typeof(Report));
        }

        private async void DisplayDeleteProjecte()
        {
            ContentDialog deleteFileDialog = new ContentDialog
            {
                Title = "Eliminar permanentemente",
                Content = "¿Estás seguro de querer eliminar el proyecto? Se eliminarán TODAS las tareas y entradas asociadas",
                PrimaryButtonText = "Eliminar",
                CloseButtonText = "Cancelar"
            };

            ContentDialogResult result = await deleteFileDialog.ShowAsync();

            // Delete the file if the user clicked the primary button.
            /// Otherwise, do nothing.
            if (result == ContentDialogResult.Primary)
            {
                bool esborrats = true;

                foreach (Tasca t in tasques)
                {
                    if (!CPGestioProjectes.deleteEntradaCascade(t.Id))
                    {
                        esborrats = false;
                    }
                }
                
                if (esborrats)
                {
                    //En cas que s'hagin esborrat correctament les entrades corresponents, eliminar la tasca
                    Debug.WriteLine("SE HAN ELIMINADO TODAS LAS ENTRADAS!!!");
                    if (tasques.Count > 0)
                    {
                        if (!CPGestioProjectes.deleteTascaCascade(projecte.Id))
                        {
                            esborrats = false;
                        }
                    }




                    if (esborrats)
                    {
                        Debug.WriteLine("SE HAN ELIMINADO TODAS LAS TASCAS!!!");

                        
                        if (!CPGestioProjectes.deleteProjecteUsuari(projecte.Id))
                        {
                            esborrats = false;
                        }


                        if (esborrats)
                        {
                            if (CPGestioProjectes.deleteProjecte(projecte.Id))
                            {
                                dtgEntrada.ItemsSource = null;

                                tasques.Remove(tasca);
                                dtgTasques.ItemsSource = null;


                                txbNomTasca.Text = "";
                                txbDescripcioTasca.Text = "";
                                txbDataLimitTasca.SelectedDate = null;
                                cboPropietariTasca.SelectedIndex = -1;
                                cboResponsableTasca.SelectedIndex = -1;
                                cboEstadoTasca.SelectedIndex = -1;


                                txbEntrada.Text = "";
                                cboEscriptor.SelectedIndex = -1;
                                cboNouEstat.SelectedIndex = -1;
                                cboNovaAssign.SelectedIndex = -1;

                                dtgProjectes.ItemsSource = null;
                                projectes.Remove(projecte);
                                dtgProjectes.ItemsSource = projectes;

                                txbNomProjecte.Text = "";
                                txbDescProjecte.Text = "";
                                cboCapProjecte.SelectedIndex = -1;

                                mostraMissatge("Eliminar", "Se ha eliminado el proyecto correctamente", "OK");

                            }
                            else
                            {
                                mostraMissatge("Error", "No se ha podido eliminar el proyecto", "OK");
                            }


                        }
                        else
                        {
                            mostraMissatge("Error", "No se ha podido eliminar el proyecto porque algun proyecto-usuario no se ha borrado", "OK");
                        }

                        


                       

                    }
                    else
                    {
                        mostraMissatge("Error", "No se ha podido eliminar el proyecto porque alguna tarea no se ha eliminado correctamente", "OK");
                    }




                }
                else
                {
                    mostraMissatge("Error", "No se ha podido eliminar el proyecto porque alguna entrada no se ha eliminado correctamente", "OK");
                }




            }
            else
            {
                // The user clicked the CLoseButton, pressed ESC, Gamepad B, or the system back button.
                // Do nothing.

                btnEliminaProjecte.IsEnabled = true;
            }
        }
    }
}
