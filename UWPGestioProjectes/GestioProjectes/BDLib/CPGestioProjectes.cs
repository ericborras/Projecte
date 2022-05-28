using BDLib.Model;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Data;
using System.Data.Common;
using System.Diagnostics;
using System.Text;

namespace BDLib
{
    public class CPGestioProjectes : IPersistence 
    {

        private static string stringConn= "Server=localhost;Database=gestio_projectes;UID=root;Password=";

        private delegate T callable_query<T>(DbCommand consulta);


        private static T launchQuery<T>(callable_query<T> cq)
        {
            using (MyDBContext context = new MyDBContext(stringConn)) //crea el contexte de la base de dades
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

                     consulta.CommandText = @"select pru.id_usuari, u.nom, u.cognom1, u.cognom2, u.login 
                                            from projecte_usuari pru join usuari u on pru.id_usuari=u.id
                                            where id_projecte = @id_projecte";

                     DBUtils.crearParametre(consulta, "id_projecte", pIdProjecte, System.Data.DbType.Int32);

                     DbDataReader reader = consulta.ExecuteReader(); //per cuan pot retorna mes d'una fila

                     Dictionary<string, int> ordinals = new Dictionary<string, int>();
                     string[] cols = { "id_usuari", "nom", "cognom1", "cognom2", "login" };
                     foreach (string c in cols)
                     {
                         ordinals[c] = reader.GetOrdinal(c);
                     }


                     while (reader.Read()) //llegeix la fila seguent, retorna true si ha pogut llegir la fila, retorna false si no hi ha mes dades per lleguir
                     {

                         int id = reader.GetInt32(ordinals["id_usuari"]);
                         string nom = reader.GetString(ordinals["nom"]);
                         string cognom1 = reader.GetString(ordinals["cognom1"]);
                         string login = reader.GetString(ordinals["login"]);

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
                         usuari.Login = login;
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


        public static bool InsertProjecte(Projecte p)
        {
            using (MyDBContext context = new MyDBContext(stringConn)) //crea el contexte de la base de dades
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

                        int numeroDeFiles = -1;

                        try
                        {
                            numeroDeFiles = consulta.ExecuteNonQuery(); //per fer un update o un delete
                        }
                        catch{}
                        
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


        public static ObservableCollection<Usuari> GetUsuaris()
        {
            return launchQuery<ObservableCollection<Usuari>>(
                 delegate (DbCommand consulta)
                 {

                     ObservableCollection<Usuari> usuaris = new ObservableCollection<Usuari>();


                     //SELECT team.logo, team.caption as nomequip, division.caption as nomdivisio FROM team join division on team.current_division_id = division.id where current_division_id = (select id from division where caption like 'Atlantic');

                     consulta.CommandText = @"select id, nom, cognom1, cognom2 from usuari order by cognom1";


                     DbDataReader reader = consulta.ExecuteReader(); //per cuan pot retorna mes d'una fila

                     Dictionary<string, int> ordinals = new Dictionary<string, int>();
                     string[] cols = { "id", "nom", "cognom1", "cognom2" };
                     foreach (string c in cols)
                     {
                         ordinals[c] = reader.GetOrdinal(c);
                     }


                     while (reader.Read()) //llegeix la fila seguent, retorna true si ha pogut llegir la fila, retorna false si no hi ha mes dades per lleguir
                     {

                         int id = reader.GetInt32(ordinals["id"]);
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



        public static bool updateProjecte(Projecte p)
        {
            using (MyDBContext context = new MyDBContext(stringConn)) //crea el contexte de la base de dades
            {
                using (DbConnection connection = context.Database.GetDbConnection()) //pren la conexxio de la BD
                {
                    connection.Open();
                    DbTransaction transaccio = connection.BeginTransaction(); //Creacio d'una transaccio

                    using (DbCommand consulta = connection.CreateCommand())
                    {
                        consulta.Transaction = transaccio; // marques la consulta dins de la transacció

                        DBUtils.crearParametre(consulta, "id", p.Id, DbType.Int32);
                        DBUtils.crearParametre(consulta, "nom", p.Nom, DbType.String);
                        DBUtils.crearParametre(consulta, "descripcio", p.Descripcio, DbType.String);
                        DBUtils.crearParametre(consulta, "cap_projecte", p.CapProjecte.Id, DbType.Int32);

                        consulta.CommandText = "update projecte set nom = @nom, descripcio = @descripcio, cap_projecte = @cap_projecte where id = @id";

                        int numeroDeFiles = -1;

                        try
                        {
                            numeroDeFiles = consulta.ExecuteNonQuery(); //per fer un update o un delete
                        }
                        catch{}
                        

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


        public static bool InsertCapProjecte(int id_usuari, int id_projecte)
        {
            using (MyDBContext context = new MyDBContext(stringConn)) //crea el contexte de la base de dades
            {
                using (DbConnection connection = context.Database.GetDbConnection()) //pren la conexxio de la BD
                {
                    connection.Open();
                    DbTransaction transaccio = connection.BeginTransaction(); //Creacio d'una transaccio

                    using (DbCommand consulta = connection.CreateCommand())
                    {
                        consulta.Transaction = transaccio; // marques la consulta dins de la transacció

                        DBUtils.crearParametre(consulta, "id_usuari", id_usuari, DbType.Int32);
                        DBUtils.crearParametre(consulta, "id_projecte", id_projecte, DbType.Int32);



                        consulta.CommandText = "insert into projecte_usuari values(NULL,@id_projecte,@id_usuari,1)";

                        int numeroDeFiles = -1;
                        try
                        {
                            numeroDeFiles = consulta.ExecuteNonQuery(); //per fer un update o un delete
                        }
                        catch { }
                        
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



        public static ObservableCollection<Tasca> GetUsuarisTasquesProjecte(int pIdProjecte)
        {
            return launchQuery<ObservableCollection<Tasca>>(
                 delegate (DbCommand consulta)
                 {

                     ObservableCollection<Tasca> tasques = new ObservableCollection<Tasca>();


                     //SELECT team.logo, team.caption as nomequip, division.caption as nomdivisio FROM team join division on team.current_division_id = division.id where current_division_id = (select id from division where caption like 'Atlantic');

                     consulta.CommandText = @"select t.id, t.nom, t.descripcio, t.data_creacio, t.data_limit, u.id as propietari_id, u.nom as propietari_nom, u.cognom1 as propietari_cognom1, u.cognom2 as propietari_cognom2, us.id as responsable_id, us.nom as responsable_nom, us.cognom1 as responsable_cognom1, us.cognom2 as responsable_cognom2, t.id_estat, e.nom as nom_estat
                                            from tasca t join usuari u on t.propietari = u.id
			                                             left join usuari us on t.responsable = us.id
                                                         join estat e on t.id_estat = e.id_estat
                                            where t.projecte_id = @id_projecte";

                     DBUtils.crearParametre(consulta, "id_projecte", pIdProjecte, System.Data.DbType.Int32);

                     DbDataReader reader = consulta.ExecuteReader(); //per cuan pot retorna mes d'una fila

                     Dictionary<string, int> ordinals = new Dictionary<string, int>();
                     string[] cols = { "id", "nom", "descripcio", "data_creacio", "data_limit", "propietari_id", "propietari_nom", "propietari_cognom1", "propietari_cognom2", "responsable_id", "responsable_nom", "responsable_cognom1", "responsable_cognom2", "id_estat", "nom_estat" };
                     foreach (string c in cols)
                     {
                         ordinals[c] = reader.GetOrdinal(c);
                     }


                     while (reader.Read()) //llegeix la fila seguent, retorna true si ha pogut llegir la fila, retorna false si no hi ha mes dades per lleguir
                     {

                         int id = reader.GetInt32(ordinals["id"]);
                         string nom = reader.GetString(ordinals["nom"]);
                         string descripcio = reader.GetString(ordinals["descripcio"]);
                         DateTime data_creacio = reader.GetDateTime(ordinals["data_creacio"]);

                         bool dataLimitNula = true;
                         DateTime data_limit = new DateTime(1,1,1);
                         try
                         {
                             data_limit = reader.GetDateTime(ordinals["data_limit"]);
                         }catch {
                             dataLimitNula = false;
                         }

                         Tasca tasca;
                         if (dataLimitNula)
                         {
                             tasca = new Tasca(id, data_creacio, nom, descripcio, data_limit);
                         }
                         else
                         {
                             tasca = new Tasca(id, data_creacio, nom, descripcio);
                         }


                         int propietari_id = reader.GetInt32(ordinals["propietari_id"]);
                         string propietari_nom = reader.GetString(ordinals["propietari_nom"]);
                         string propietari_cognom1 = reader.GetString(ordinals["propietari_cognom1"]);

                         string propietari_cognom2 = "";
                         try
                         {
                             propietari_cognom2 = reader.GetString(ordinals["propietari_cognom2"]);
                         }
                         catch { }
                         tasca.Propietari = new Usuari(propietari_id, propietari_nom, propietari_cognom1, propietari_cognom2);


                         if (propietari_cognom2 != "")
                         {
                             tasca.Propietari.Nomcomplet = propietari_nom + " " + propietari_cognom1 + " " + propietari_cognom2;
                         }
                         else
                         {
                             tasca.Propietari.Nomcomplet = propietari_nom + " " + propietari_cognom1;
                         }

                         int responsable_id = -1;
                         string responsable_nom="", responsable_cognom1="";
                         bool esEmplenat = false;
                         try
                         {
                             responsable_id = reader.GetInt32(ordinals["responsable_id"]);
                             responsable_nom = reader.GetString(ordinals["responsable_nom"]);
                             responsable_cognom1 = reader.GetString(ordinals["responsable_cognom1"]);
                         }
                         catch {
                             esEmplenat = true;
                         }


                         if (!esEmplenat)
                         {
                             string responsable_cognom2 = "";

                             try
                             {
                                 responsable_cognom2 = reader.GetString(ordinals["responsable_cognom2"]);
                             }
                             catch { }


                             Usuari responsable = new Usuari(responsable_id, responsable_nom, responsable_cognom1, responsable_cognom2);
                             tasca.Responsable = responsable;

                             if (responsable_cognom2 != "")
                             {
                                 tasca.Responsable.Nomcomplet = responsable_nom + " " + responsable_cognom1 + " " + responsable_cognom2;
                             }
                             else
                             {
                                 tasca.Responsable.Nomcomplet = responsable_nom + " " + responsable_cognom1;
                             }

                         }


                         int id_estat;
                         string nom_estat;

                         id_estat = reader.GetInt32(ordinals["id_estat"]);
                         nom_estat = reader.GetString(ordinals["nom_estat"]);

                         tasca.Estat = new Estat(id_estat,nom_estat);

                         tasques.Add(tasca);
                     }

                     return tasques;
                 });
        }


        public static ObservableCollection<Estat> GetUsuarisTasquesEstats()
        {
            return launchQuery<ObservableCollection<Estat>>(
                 delegate (DbCommand consulta)
                 {

                     ObservableCollection<Estat> estats = new ObservableCollection<Estat>();


                     //SELECT team.logo, team.caption as nomequip, division.caption as nomdivisio FROM team join division on team.current_division_id = division.id where current_division_id = (select id from division where caption like 'Atlantic');

                     consulta.CommandText = @"select id_estat, nom from estat";


                     DbDataReader reader = consulta.ExecuteReader(); //per cuan pot retorna mes d'una fila

                     Dictionary<string, int> ordinals = new Dictionary<string, int>();
                     string[] cols = { "id_estat", "nom"};
                     foreach (string c in cols)
                     {
                         ordinals[c] = reader.GetOrdinal(c);
                     }


                     while (reader.Read()) //llegeix la fila seguent, retorna true si ha pogut llegir la fila, retorna false si no hi ha mes dades per lleguir
                     {

                         int id = reader.GetInt32(ordinals["id_estat"]);
                         string nom = reader.GetString(ordinals["nom"]);

                         Estat estat = new Estat(id, nom);

                        

                         estats.Add(estat);
                     }

                     return estats;
                 });
        }


        public static bool InsertTasca(Tasca t, int projecte_id)
        {
            using (MyDBContext context = new MyDBContext(stringConn)) //crea el contexte de la base de dades
            {
                using (DbConnection connection = context.Database.GetDbConnection()) //pren la conexxio de la BD
                {
                    connection.Open();
                    DbTransaction transaccio = connection.BeginTransaction(); //Creacio d'una transaccio

                    using (DbCommand consulta = connection.CreateCommand())
                    {
                        consulta.Transaction = transaccio; // marques la consulta dins de la transacció

                        DBUtils.crearParametre(consulta, "data_creacio", t.DataCreacio, DbType.DateTime);
                        DBUtils.crearParametre(consulta, "nom", t.Nom, DbType.String);
                        DBUtils.crearParametre(consulta, "descripcio", t.Descripcio, DbType.String);
                        DBUtils.crearParametre(consulta, "data_limit", t.DataLimit, DbType.DateTime);
                        DBUtils.crearParametre(consulta, "propietari", t.Propietari.Id, DbType.Int32);
                        DBUtils.crearParametre(consulta, "responsable", t.Responsable.Id, DbType.Int32);
                        DBUtils.crearParametre(consulta, "id_estat", t.Estat.Codi_estat, DbType.Int32);
                        DBUtils.crearParametre(consulta, "projecte_id", projecte_id, DbType.Int32);

                        Debug.WriteLine("DATA CREACIO " + t.DataCreacio + " NOM " + t.Nom + " DESCRIPCIO " + t.Descripcio + " DATA LIMIT " + t.DataLimit + " PROPIETARI" + t.Propietari.Id + " RESPONSABLE " + t.Responsable.Id + " ESTAT " + t.Estat.Codi_estat + " PROJECTE " + projecte_id);

                        consulta.CommandText = "insert into tasca values(NULL, @data_creacio, @nom, @descripcio, @data_limit, @propietari, @responsable, @id_estat, @projecte_id)";

                        int numeroDeFiles = -1;

                        try
                        {
                            numeroDeFiles = consulta.ExecuteNonQuery(); //per fer un update o un delete
                        }
                        catch { }

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



        public static bool updateTasca(Tasca t, int projecte_id)
        {
            using (MyDBContext context = new MyDBContext(stringConn)) //crea el contexte de la base de dades
            {
                using (DbConnection connection = context.Database.GetDbConnection()) //pren la conexxio de la BD
                {
                    connection.Open();
                    DbTransaction transaccio = connection.BeginTransaction(); //Creacio d'una transaccio

                    using (DbCommand consulta = connection.CreateCommand())
                    {
                        consulta.Transaction = transaccio; // marques la consulta dins de la transacció

                        DBUtils.crearParametre(consulta, "id", t.Id, DbType.Int32);
                        DBUtils.crearParametre(consulta, "nom", t.Nom, DbType.String);
                        DBUtils.crearParametre(consulta, "descripcio", t.Descripcio, DbType.String);
                        DBUtils.crearParametre(consulta, "data_limit", t.DataLimit, DbType.DateTime);
                        DBUtils.crearParametre(consulta, "propietari", t.Propietari.Id, DbType.Int32);
                        DBUtils.crearParametre(consulta, "responsable", t.Responsable.Id, DbType.Int32);
                        DBUtils.crearParametre(consulta, "id_estat", t.Estat.Codi_estat, DbType.Int32);

                        consulta.CommandText = "update tasca set nom = @nom, descripcio = @descripcio, data_limit = @data_limit, propietari = @propietari, responsable = @responsable, id_estat = @id_estat where id = @id";
                        Debug.WriteLine(" NOM " + t.Nom + " DESCRIPCIO " + t.Descripcio + " DATA LIMIT " + t.DataLimit + " PROPIETARI" + t.Propietari.Id + " RESPONSABLE " + t.Responsable.Id + " ESTAT " + t.Estat.Codi_estat + " PROJECTE " + projecte_id);

                        int numeroDeFiles = -1;

                        try
                        {
                            numeroDeFiles = consulta.ExecuteNonQuery(); //per fer un update o un delete
                        }
                        catch { }


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



        public static ObservableCollection<Entrada> GetEntradesTasca(int id_tasca)
        {
            return launchQuery<ObservableCollection<Entrada>>(
                 delegate (DbCommand consulta)
                 {

                     ObservableCollection<Entrada> entrades = new ObservableCollection<Entrada>();


                     //SELECT team.logo, team.caption as nomequip, division.caption as nomdivisio FROM team join division on team.current_division_id = division.id where current_division_id = (select id from division where caption like 'Atlantic');

                     consulta.CommandText = @"select e.numero, e.data_entrada, e.entrada, ua.id as novass_id, ua.nom as novass_nom, ua.cognom1 as novass_cognom1, ua.cognom2 as novass_cognom2, es.id as escriptor_id, es.nom as escriptor_nom, es.cognom1 as escriptor_cognom1, es.cognom2 as escriptor_cognom2, est.id_estat as estat_id, est.nom as estat_nom
                                            from entrada e left join usuari ua on e.nova_assignacio = ua.id
					                                            join usuari es on e.escriptor = es.id
                                                           left join estat est on e.nou_estat = est.id_estat
                                            where e.tasca_id = @tasca_id";

                     DBUtils.crearParametre(consulta, "tasca_id", id_tasca, System.Data.DbType.Int32);

                     DbDataReader reader = consulta.ExecuteReader(); //per cuan pot retorna mes d'una fila

                     Dictionary<string, int> ordinals = new Dictionary<string, int>();
                     string[] cols = { "numero", "data_entrada", "entrada", "novass_id", "novass_nom", "novass_cognom1", "novass_cognom2", "escriptor_id", "escriptor_nom", "escriptor_cognom1", "escriptor_cognom2", "estat_id", "estat_nom" };
                     foreach (string c in cols)
                     {
                         ordinals[c] = reader.GetOrdinal(c);
                     }


                     while (reader.Read()) //llegeix la fila seguent, retorna true si ha pogut llegir la fila, retorna false si no hi ha mes dades per lleguir
                     {

                         int numero = reader.GetInt32(ordinals["numero"]);
                         DateTime data_entrada = reader.GetDateTime(ordinals["data_entrada"]);
                         String ent = reader.GetString(ordinals["entrada"]);



                         Entrada entrada = new Entrada(numero,data_entrada,ent);







                         entrades.Add(entrada);
                     }

                     return entrades;
                 });
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
