using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Text;

namespace BDLib.Model
{
    public class Projecte
    {

        private int id;
        private String nom;
        private String descripcio;
        private Usuari capProjecte;
        private ObservableCollection<Tasca> tasques;

        public Projecte(int id, string nom, string descripcio, Usuari capProjecte, ObservableCollection<Tasca> tasques)
        {
            this.id = id;
            this.nom = nom;
            this.descripcio = descripcio;
            this.capProjecte = capProjecte;
            this.tasques = tasques;
        }

        public Projecte(int id, string nom, string descripcio, Usuari capProjecte)
        {
            this.id = id;
            this.nom = nom;
            this.descripcio = descripcio;
            this.capProjecte = capProjecte;
        }

        public int Id { get => id; set => id = value; }
        public string Nom { get => nom; set => nom = value; }
        public string Descripcio { get => descripcio; set => descripcio = value; }
        public Usuari CapProjecte { get => capProjecte; set => capProjecte = value; }
        public ObservableCollection<Tasca> Tasques { get => tasques; set => tasques = value; }
    }
}
