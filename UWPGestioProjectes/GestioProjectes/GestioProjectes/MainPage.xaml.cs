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
        private bool modeEditProjectes = false;
        private int idxCapProjecte = -1;
        private Projecte projecte;

        public MainPage()
        {
            this.InitializeComponent();
        }

        private void dtgProjectes_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {

            btnEliminaProjecte.IsEnabled = true;
            if (dtgProjectes.SelectedItem != null)
            {
                projecte = (Projecte)dtgProjectes.SelectedItem;
                txbNomProjecte.Text = projecte.Nom;
                txbDescProjecte.Text = projecte.Descripcio;


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
            if (modeAltaProjectes)
            {
                //Insert

                if (txbNomProjecte.Text.Trim().Length > 0 && txbDescProjecte.Text.Trim().Length > 0 && cboCapProjecte.SelectedIndex!= -1)
                {
                    Usuari capProjecte = (Usuari)cboCapProjecte.SelectedValue;
                    Projecte projecte = new Projecte(txbNomProjecte.Text.Trim(), txbDescProjecte.Text.Trim(), capProjecte);
                    if (CPGestioProjectes.InsertProjecte(projecte))
                    {
                        //Afegir a la llista en memòria
                        projectes.Add(projecte);
                        dtgProjectes.ItemsSource = null;
                        dtgProjectes.ItemsSource = projectes;
                        mostraMissatge("Inserción", "Se ha insertado el proyecto correctamente", "OK");
                    }
                    else
                    {
                        mostraMissatge("Error", "Ha habido un error al insertar el proyecto", "OK");
                    }

                }
                else
                {
                    mostraMissatge("Error", "No puedes insertar un proyecto con algún campo vacío", "OK");
                }
            }
            else
            {
                //Mode edit
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

        private void btnEliminaProjecte_Click(object sender, RoutedEventArgs e)
        {
            btnEliminaProjecte.IsEnabled = false;
        }
    }
}
