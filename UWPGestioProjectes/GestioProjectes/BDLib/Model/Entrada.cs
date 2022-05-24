using System;
using System.Collections.Generic;
using System.Text;

namespace BDLib.Model
{
    public class Entrada
    {

        private int numero;
        private DateTime dataEntrada;
        private String entrada_e;
        private Usuari escriptor;
        private Usuari novaAssignacio;
        private Estat nouEstat;

        public Entrada(int numero, DateTime dataEntrada, string entrada, Usuari escriptor, Usuari novaAssignacio, Estat nouEstat)
        {
            this.numero = numero;
            this.dataEntrada = dataEntrada;
            this.entrada_e = entrada;
            this.escriptor = escriptor;
            this.novaAssignacio = novaAssignacio;
            this.nouEstat = nouEstat;
        }

        public int Numero { get => numero; set => numero = value; }
        public DateTime DataEntrada { get => dataEntrada; set => dataEntrada = value; }
        public string Entrada_e { get => entrada_e; set => entrada_e = value; }
        public Usuari Escriptor { get => escriptor; set => escriptor = value; }
        public Usuari NovaAssignacio { get => novaAssignacio; set => novaAssignacio = value; }
        public Estat NouEstat { get => nouEstat; set => nouEstat = value; }
    }
}
