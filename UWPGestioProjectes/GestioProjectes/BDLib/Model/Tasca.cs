using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Text;

namespace BDLib.Model
{
    public class Tasca
    {

        private int id;
        private DateTime dataCreacio;
        private String nom;
        private String descripcio;
        private DateTime dataLimit;
        private Usuari propietari;
        private Usuari responsable;
        private ObservableCollection<Entrada> entrades;
        private Estat estat;

        public Tasca(int id, DateTime dataCreacio, string nom, string descripcio, DateTime dataLimit, Usuari propietari, Usuari responsable, ObservableCollection<Entrada> entrades, Estat estat)
        {
            this.id = id;
            this.dataCreacio = dataCreacio;
            this.nom = nom;
            this.descripcio = descripcio;
            this.dataLimit = dataLimit;
            this.propietari = propietari;
            this.responsable = responsable;
            this.entrades = entrades;
            this.estat = estat;
        }

        public int Id { get => id; set => id = value; }
        public DateTime DataCreacio { get => dataCreacio; set => dataCreacio = value; }
        public string Nom { get => nom; set => nom = value; }
        public string Descripcio { get => descripcio; set => descripcio = value; }
        public DateTime DataLimit { get => dataLimit; set => dataLimit = value; }
        public Usuari Propietari { get => propietari; set => propietari = value; }
        public Usuari Responsable { get => responsable; set => responsable = value; }
        public ObservableCollection<Entrada> Entrades { get => entrades; set => entrades = value; }
        public Estat Estat { get => estat; set => estat = value; }
    }
}
