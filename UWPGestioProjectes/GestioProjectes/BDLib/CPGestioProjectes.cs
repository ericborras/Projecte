using BDLib.Model;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Data;
using System.Data.Common;
using System.Text;

namespace BDLib
{
    public class CPGestioProjectes : IPersistence 
    {

        private delegate T callable_query<T>(DbCommand consulta);


        private static T launchQuery<T>(callable_query<T> cq)
        {
            using (MyDBContext context = new MyDBContext("Server=localhost;Database=gestio_projectes;UID=root;Password=")) //crea el contexte de la base de dades
            {
                using (DbConnection connection = context.Database.GetDbConnection()) //pren la conexxio de la BD
                {
                    connection.Open();
                    using (DbCommand consulta = connection.CreateCommand())
                    {
                        return cq(consulta);
                    }
                }
            }
        }




        public static ObservableCollection<Projecte> GetProjectes()
        {
            return launchQuery<ObservableCollection<Projecte>>(
                 delegate (DbCommand consulta)
                 {

                     ObservableCollection<Projecte> projectes = new ObservableCollection<Projecte>();
                   

                     consulta.CommandText = @"select p.id,p.nom, p.descripcio, u.nom as nom_cap, u.cognom1, u.cognom2,p.cap_projecte from projecte p join usuari u on p.cap_projecte=u.id";

                     DbDataReader reader = consulta.ExecuteReader(); //per cuan pot retorna mes d'una fila

                     Dictionary<string, int> ordinals = new Dictionary<string, int>();
                     string[] cols = { "id", "nom", "descripcio", "nom_cap", "cognom1", "cognom2", "cap_projecte" };
                     foreach (string c in cols)
                     {
                         ordinals[c] = reader.GetOrdinal(c);
                     }


                     while (reader.Read()) //llegeix la fila seguent, retorna true si ha pogut llegir la fila, retorna false si no hi ha mes dades per lleguir
                     {

                         int id = reader.GetInt32(ordinals["id"]);
                         string nom_projecte = reader.GetString(ordinals["nom"]);
                         string descripcio = reader.GetString(ordinals["descripcio"]);
                         string nom_cap = reader.GetString(ordinals["nom_cap"]);
                         string cognom1 = reader.GetString(ordinals["cognom1"]);
                         string cognom2 = "";

                         try
                         {
                             reader.GetString(ordinals["cognom2"]);
                         }
                         catch(Exception ex)
                         {

                         }
                         
                         int cap_projecte = reader.GetInt32(ordinals["cap_projecte"]);
                         //Montar nom complet

                         Usuari usuari = new Usuari(cap_projecte, nom_cap, cognom1, cognom2);
                         if (cognom2 != "")
                         {
                             usuari.Nomcomplet = nom_cap + " " + cognom1 + " " + cognom2;
                         }
                         else
                         {
                             usuari.Nomcomplet = nom_cap + " " + cognom1;
                         }
                         
                         Projecte projecte = new Projecte(id, nom_projecte, descripcio,usuari);

                       

                         projectes.Add(projecte);
                     }

                     return projectes;
                 });
        }


        public static ObservableCollection<Usuari> GetUsuarisProjecte(int pIdProjecte)
        {
            return launchQuery<ObservableCollection<Usuari>>(
                 delegate (DbCommand consulta)
                 {

                     ObservableCollection<Usuari> usuaris = new ObservableCollection<Usuari>();


                     //SELECT team.logo, team.caption as nomequip, division.caption as nomdivisio FROM team join division on team.current_division_id = division.id where current_division_id = (select id from division where caption like 'Atlantic');

                     consulta.CommandText = @"select pru.id_usuari, u.nom, u.cognom1, u.cognom2 
                                            from projecte_usuari pru join usuari u on pru.id_usuari=u.id
                                            where id_projecte = @id_projecte";

                     DBUtils.crearParametre(consulta, "id_projecte", pIdProjecte, System.Data.DbType.Int32);

                     DbDataReader reader = consulta.ExecuteReader(); //per cuan pot retorna mes d'una fila

                     Dictionary<string, int> ordinals = new Dictionary<string, int>();
                     string[] cols = { "id_usuari", "nom", "cognom1", "cognom2" };
                     foreach (string c in cols)
                     {
                         ordinals[c] = reader.GetOrdinal(c);
                     }


                     while (reader.Read()) //llegeix la fila seguent, retorna true si ha pogut llegir la fila, retorna false si no hi ha mes dades per lleguir
                     {

                         int id = reader.GetInt32(ordinals["id_usuari"]);
                         string nom = reader.GetString(ordinals["nom"]);
                         string cognom1 = reader.GetString(ordinals["cognom1"]);

                         string cognom2 = "";

                         try
                         {
                             reader.GetString(ordinals["cognom2"]);
                         }
                         catch (Exception ex)
                         {

                         }

                         //Montar nom complet

                         Usuari usuari = new Usuari(id, nom, cognom1, cognom2);
                         if (cognom2 != "")
                         {
                             usuari.Nomcomplet = nom + " " + cognom1 + " " + cognom2;
                         }
                         else
                         {
                             usuari.Nomcomplet = nom + " " + cognom1;
                         }




                         usuaris.Add(usuari);
                     }

                     return usuaris;
                 });
        }


        public static bool InsertTeam(Projecte p)
        {
            using (MyDBContext context = new MyDBContext("Server=localhost;Database=gestio_projectes;UID=root;Password=")) //crea el contexte de la base de dades
            {
                using (DbConnection connection = context.Database.GetDbConnection()) //pren la conexxio de la BD
                {
                    connection.Open();
                    DbTransaction transaccio = connection.BeginTransaction(); //Creacio d'una transaccio

                    using (DbCommand consulta = connection.CreateCommand())
                    {
                        consulta.Transaction = transaccio; // marques la consulta dins de la transacció


                        DBUtils.crearParametre(consulta, "nom", p.Nom, DbType.String);
                        DBUtils.crearParametre(consulta, "descripcio", p.Descripcio, DbType.String);
                        DBUtils.crearParametre(consulta, "cap_projecte", p.CapProjecte.Id, DbType.Int32);
                        

                        
                        consulta.CommandText = "insert into projecte values(NULL,@nom,@descripcio,@cap_projecte)";                   


                        int numeroDeFiles = consulta.ExecuteNonQuery(); //per fer un update o un delete
                        if (numeroDeFiles != 1)
                        {
                            //shit happens
                            transaccio.Rollback();
                            return false;
                        }
                        else
                        {
                            transaccio.Commit();
                            return true;
                        }

                    }

                }

            }

        }




        public void close()
        {
            throw new NotImplementedException();
        }

        public void commit()
        {
            throw new NotImplementedException();
        }

        public void connect(string conexioString)
        {
            
            


        }

        public void rollback()
        {
            throw new NotImplementedException();
        }
    } 
}
