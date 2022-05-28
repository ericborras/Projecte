using System;
using System.Collections.Generic;
using System.Text;

namespace BDLib.Model
{
    public class Estat
    {
        private int codi_estat;
        private string nom_estat;

        public Estat(int codi_estat, string nom_estat)
        {
            this.codi_estat = codi_estat;
            this.nom_estat = nom_estat;
        }

        public int Codi_estat { get => codi_estat; set => codi_estat = value; }
        public string Nom_estat { get => nom_estat; set => nom_estat = value; }
    }
}
