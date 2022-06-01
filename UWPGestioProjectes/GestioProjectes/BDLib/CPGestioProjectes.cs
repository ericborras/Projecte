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

        private static string stringConn= "Server=51.68.224.27;Database=dam2_eborras;UID=dam2-eborras;Password=3483W";

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
                        if (t.Responsable != null)
                        {
                            DBUtils.crearParametre(consulta, "responsable", t.Responsable.Id, DbType.Int32);
                        }
                        else
                        {
                            DBUtils.crearParametre(consulta, "responsable", null, DbType.Int32);
                        }
                        
                        DBUtils.crearParametre(consulta, "id_estat", t.Estat.Codi_estat, DbType.Int32);
                        DBUtils.crearParametre(consulta, "projecte_id", projecte_id, DbType.Int32);

                        //Debug.WriteLine("DATA CREACIO " + t.DataCreacio + " NOM " + t.Nom + " DESCRIPCIO " + t.Descripcio + " DATA LIMIT " + t.DataLimit + " PROPIETARI" + t.Propietari.Id + " RESPONSABLE " + t.Responsable.Id + " ESTAT " + t.Estat.Codi_estat + " PROJECTE " + projecte_id);

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

                         try
                         {
                             int id_nova_assign = reader.GetInt32(ordinals["novass_id"]);
                             String novass_nom = reader.GetString(ordinals["novass_nom"]);
                             String novass_cognom1 = reader.GetString(ordinals["novass_cognom1"]);

                             String novass_cognom2 = null;
                             try
                             {
                                 novass_cognom2 = reader.GetString(ordinals["novass_cognom2"]);
                             }
                             catch { }
                             
                             Usuari nova_assignacio;
                             if (novass_cognom2 != null)
                             {
                                 nova_assignacio = new Usuari(id_nova_assign, novass_nom, novass_cognom1, novass_cognom2);
                                 nova_assignacio.Nomcomplet = novass_nom + " " + novass_cognom1 + " " + novass_cognom2;
                             }
                             else
                             {
                                 nova_assignacio = new Usuari(id_nova_assign, novass_nom, novass_cognom1, null);
                                 nova_assignacio.Nomcomplet = novass_nom + " " + novass_cognom1;
                             }

                             entrada.NovaAssignacio = nova_assignacio;

                         }
                         catch
                         {}



                             




                         int id_escriptor = reader.GetInt32(ordinals["escriptor_id"]);
                         String nom_escriptor = reader.GetString(ordinals["escriptor_nom"]);
                         String cognom1_escriptor = reader.GetString(ordinals["escriptor_cognom1"]);

                         Usuari escriptor;

                         String cognom2_escriptor = null;
                         try
                         {
                             cognom2_escriptor = reader.GetString(ordinals["escriptor_cognom2"]);
                         }
                         catch { }
                         
                         if (cognom2_escriptor != null)
                         {
                             escriptor = new Usuari(id_escriptor,nom_escriptor,cognom1_escriptor,cognom2_escriptor);
                             escriptor.Nomcomplet = nom_escriptor + " " + cognom1_escriptor + " " + cognom2_escriptor;
                         }
                         else
                         {
                             escriptor = new Usuari(id_escriptor, nom_escriptor, cognom1_escriptor, null);
                             escriptor.Nomcomplet = nom_escriptor + " " + cognom1_escriptor;
                         }
                         entrada.Escriptor = escriptor;

                         int id_estat = reader.GetInt32(ordinals["estat_id"]);
                         String nom_estat = reader.GetString(ordinals["estat_nom"]);

                         if (nom_estat != null)
                         {
                             Estat estat = new Estat(id_estat, nom_estat);
                             entrada.NouEstat = estat;
                         }





                         entrades.Add(entrada);
                     }

                     return entrades;
                 });
        }



        public static bool InsertEntrada(Entrada entrada, int tasca_id)
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

                        DBUtils.crearParametre(consulta, "data_entrada", entrada.DataEntrada, DbType.DateTime);
                        DBUtils.crearParametre(consulta, "entrada", entrada.Entrada_e, DbType.String);
                        DBUtils.crearParametre(consulta, "escriptor", entrada.Escriptor.Id, DbType.Int32);
                        DBUtils.crearParametre(consulta, "nou_estat", entrada.NouEstat.Codi_estat, DbType.Int32);
                        DBUtils.crearParametre(consulta, "nova_assignacio", entrada.NovaAssignacio.Id, DbType.Int32);
                        DBUtils.crearParametre(consulta, "tasca_id", tasca_id, DbType.Int32);

                       

                        consulta.CommandText = "insert into entrada(numero,data_entrada,entrada,escriptor,nou_estat,nova_assignacio,tasca_id) values(null,@data_entrada,@entrada,@escriptor,@nou_estat,@nova_assignacio,@tasca_id)";

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



        public static bool updateEntrada(Entrada e)
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

                        DBUtils.crearParametre(consulta, "numero", e.Numero, DbType.Int32);
                        DBUtils.crearParametre(consulta, "entrada", e.Entrada_e, DbType.String);
                        DBUtils.crearParametre(consulta, "escriptor", e.Escriptor.Id, DbType.Int32);
                        DBUtils.crearParametre(consulta, "nou_estat", e.NouEstat.Codi_estat, DbType.Int32);
                        DBUtils.crearParametre(consulta, "nova_assignacio", e.NovaAssignacio.Id, DbType.Int32);

                        Debug.WriteLine("NUMERO: " + e.Numero + " ENTRADA: " + e.Entrada_e + " ESCRITOR: " + e.Escriptor.Nom + " ESTADO: " + e.NouEstat.Nom_estat + " ASSIGNACION: " + e.NovaAssignacio.Nom);
                        consulta.CommandText = "update entrada set entrada = @entrada, escriptor = @escriptor, nou_estat = @nou_estat, nova_assignacio = @nova_assignacio where numero = @numero";
                       

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



        public static bool deleteEntrada(int id_entrada)
        {
            bool haAnatBe = true;
            using (MyDBContext context = new MyDBContext(stringConn)) //crea el contexte de la base de dades
            {
                using (DbConnection connection = context.Database.GetDbConnection()) //pren la conexxio de la BD
                {
                    connection.Open();
                    DbTransaction transaccio = connection.BeginTransaction(); //Creacio d'una transaccio

                    using (DbCommand consulta = connection.CreateCommand())
                    {
                        consulta.Transaction = transaccio; // marques la consulta dins de la transacció

                        Debug.WriteLine("ID ENTRADA: " + id_entrada);
                        DBUtils.crearParametre(consulta, "numero", id_entrada, DbType.Int32);

                        consulta.CommandText = "delete from entrada where numero = @numero ";


                        int numDeleted = consulta.ExecuteNonQuery();
                        Debug.WriteLine("NUMDELETED ENTRADA: " + numDeleted);
                        if (numDeleted != 1)
                        {
                            transaccio.Rollback();
                            haAnatBe = false;
                        }
                        transaccio.Commit();
                        return haAnatBe;
                    }
                }

            }
        }



        public static bool deleteTasca(int id_tasca)
        {
            bool haAnatBe = true;
            using (MyDBContext context = new MyDBContext(stringConn)) //crea el contexte de la base de dades
            {
                using (DbConnection connection = context.Database.GetDbConnection()) //pren la conexxio de la BD
                {
                    connection.Open();
                    DbTransaction transaccio = connection.BeginTransaction(); //Creacio d'una transaccio

                    using (DbCommand consulta = connection.CreateCommand())
                    {
                        consulta.Transaction = transaccio; // marques la consulta dins de la transacció


                        DBUtils.crearParametre(consulta, "id_tasca", id_tasca, DbType.Int32);

                        consulta.CommandText = "delete from tasca where id = @id_tasca ";


                        int numDeleted = -1;
                        try
                        {
                            numDeleted = consulta.ExecuteNonQuery();
                        }
                        catch(Exception ex)
                        {
                            Debug.WriteLine("ERRORRRRR: " + ex.Message);
                        }

                        
                           
                        if (numDeleted != 1)
                        {
                            transaccio.Rollback();
                            haAnatBe = false;
                        }
                        else
                        {
                            transaccio.Commit();
                        }
                        
                        return haAnatBe;
                    }
                }

            }
        }


        public static bool deleteProjecte(int id_projecte)
        {
            bool haAnatBe = true;
            using (MyDBContext context = new MyDBContext(stringConn)) //crea el contexte de la base de dades
            {
                using (DbConnection connection = context.Database.GetDbConnection()) //pren la conexxio de la BD
                {
                    connection.Open();
                    DbTransaction transaccio = connection.BeginTransaction(); //Creacio d'una transaccio

                    using (DbCommand consulta = connection.CreateCommand())
                    {
                        consulta.Transaction = transaccio; // marques la consulta dins de la transacció

                        Debug.WriteLine("ID PROJECTE: "+id_projecte);
                        DBUtils.crearParametre(consulta, "id_projecte", id_projecte, DbType.Int32);

                        consulta.CommandText = "delete from projecte where id = @id_projecte ";


                        int numDeleted = consulta.ExecuteNonQuery();
                        if (numDeleted != 1)
                        {
                            transaccio.Rollback();
                            haAnatBe = false;
                        }
                        transaccio.Commit();
                        return haAnatBe;
                    }
                }

            }
        }


        public static bool deleteProjecteUsuari(int id_projecte)
        {
            bool haAnatBe = true;
            using (MyDBContext context = new MyDBContext(stringConn)) //crea el contexte de la base de dades
            {
                using (DbConnection connection = context.Database.GetDbConnection()) //pren la conexxio de la BD
                {
                    connection.Open();
                    DbTransaction transaccio = connection.BeginTransaction(); //Creacio d'una transaccio

                    using (DbCommand consulta = connection.CreateCommand())
                    {
                        consulta.Transaction = transaccio; // marques la consulta dins de la transacció

                        Debug.WriteLine("ID PROJECTE: " + id_projecte);
                        DBUtils.crearParametre(consulta, "id_projecte", id_projecte, DbType.Int32);

                        consulta.CommandText = "delete from projecte_usuari where id_projecte = @id_projecte ";


                        int numDeleted = consulta.ExecuteNonQuery();
                        if (numDeleted == 0)
                        {
                            transaccio.Rollback();
                            haAnatBe = false;
                        }
                        transaccio.Commit();
                        return haAnatBe;
                    }
                }

            }
        }



        public static bool deleteTascaCascade(int id_projecte)
        {
            bool haAnatBe = true;
            using (MyDBContext context = new MyDBContext(stringConn)) //crea el contexte de la base de dades
            {
                using (DbConnection connection = context.Database.GetDbConnection()) //pren la conexxio de la BD
                {
                    connection.Open();
                    DbTransaction transaccio = connection.BeginTransaction(); //Creacio d'una transaccio

                    using (DbCommand consulta = connection.CreateCommand())
                    {
                        consulta.Transaction = transaccio; // marques la consulta dins de la transacció


                        DBUtils.crearParametre(consulta, "id_projecte", id_projecte, DbType.Int32);

                        consulta.CommandText = "delete from tasca where projecte_id = @id_projecte";


                        int numDeleted = -1;
                        try
                        {
                            numDeleted = consulta.ExecuteNonQuery();
                        }
                        catch { }

                        Debug.WriteLine("NUMDELETED TASCA CASCADE: " + numDeleted);
                        if (numDeleted == 0)
                        {
                            transaccio.Rollback();
                            haAnatBe = false;
                        }
                        else
                        {
                            transaccio.Commit();
                        }

                        return haAnatBe;
                    }
                }

            }
        }


        public static bool deleteEntradaCascade(int id_tasca)
        {
            bool haAnatBe = true;
            using (MyDBContext context = new MyDBContext(stringConn)) //crea el contexte de la base de dades
            {
                using (DbConnection connection = context.Database.GetDbConnection()) //pren la conexxio de la BD
                {
                    connection.Open();
                    DbTransaction transaccio = connection.BeginTransaction(); //Creacio d'una transaccio

                    using (DbCommand consulta = connection.CreateCommand())
                    {
                        consulta.Transaction = transaccio; // marques la consulta dins de la transacció

                        DBUtils.crearParametre(consulta, "numero", id_tasca, DbType.Int32);

                        consulta.CommandText = "delete from entrada where tasca_id = @numero ";


                        int numDeleted = consulta.ExecuteNonQuery();
                        Debug.WriteLine("NUMDELETED ENTRADA: " + numDeleted);
                        if (numDeleted == 0)
                        {
                            transaccio.Rollback();
                            haAnatBe = false;
                        }
                        else
                        {
                            transaccio.Commit();
                        }
                        
                        return haAnatBe;
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
