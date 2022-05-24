using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Text;

namespace BDLib.Model
{
    public class Usuari
    {

        private int id;
        private String nom;
        private String cognom1;
        private String cognom2;
        private DateTime dataNaixement;
        private String login;
        private String passwdHash;
        private ObservableCollection<Tasca> tasques;

        private String nomcomplet;

        public Usuari(int id, string nom, string cognom1, string cognom2, DateTime dataNaixement, string login, string passwdHash, ObservableCollection<Tasca> tasques)
        {
            this.id = id;
            this.nom = nom;
            this.cognom1 = cognom1;
            this.cognom2 = cognom2;
            this.dataNaixement = dataNaixement;
            this.login = login;
            this.passwdHash = passwdHash;
            this.tasques = tasques;
        }

        public Usuari(int id, string nom, string cognom1, string cognom2)
        {
            this.id = id;
            this.nom = nom;
            this.cognom1 = cognom1;
            this.cognom2 = cognom2;
        }

        public int Id { get => id; set => id = value; }
        public string Nom { get => nom; set => nom = value; }
        public string Cognom1 { get => cognom1; set => cognom1 = value; }
        public string Cognom2 { get => cognom2; set => cognom2 = value; }
        public DateTime DataNaixement { get => dataNaixement; set => dataNaixement = value; }
        public string Login { get => login; set => login = value; }
        public string PasswdHash { get => passwdHash; set => passwdHash = value; }
        public ObservableCollection<Tasca> Tasques { get => tasques; set => tasques = value; }
        public string Nomcomplet { get => nomcomplet; set => nomcomplet = value; }
    }
}
