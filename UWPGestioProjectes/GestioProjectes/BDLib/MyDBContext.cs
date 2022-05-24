using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Text;

namespace BDLib
{
    public class MyDBContext : DbContext
    {

        private string cadena_connexio;

        public MyDBContext(string cadena_connexio)
        {
            this.cadena_connexio = cadena_connexio;
        }


        protected override void OnConfiguring(DbContextOptionsBuilder optionBuilder)
        {

            optionBuilder.UseMySQL(cadena_connexio);

        }
    }
}
