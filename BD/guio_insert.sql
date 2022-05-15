delete from entrada;
delete from tasca;
delete from projecte_usuari;
delete from projecte;
delete from usuari;
delete from rol;
delete from estat;


insert into rol values(0);
insert into rol values(1);
insert into rol values(2);




insert into estat values(0);
insert into estat values(1);
insert into estat values(2);
insert into estat values(3);
insert into estat values(4);



insert into usuari values(NULL,'Emperatriz Haydée','Carreño','Moles','1958-07-23','emperatriz','');
insert into usuari values(NULL,'Marisela','de Reguera',NULL,'1985-08-27','marisela','');
insert into usuari values(NULL,'Norberto Enrique','Carmona','Solera','1972-02-08','norberto','');
insert into usuari values(NULL,'Fausto','Montserrat','Enríquez','1965-10-10','fausto','');
insert into usuari values(NULL,'Clara','Zabaleta','Zapata','1998-04-14','clara','');
insert into usuari values(NULL,'Tomasa','del Pomares',NULL,'1968-09-06','tomasa','');
insert into usuari values(NULL,'Lope','Piquer','Manso','1985-08-02','lope','');
insert into usuari values(NULL,'Macario','Adán',NULL,'1960-02-06','macario','');
insert into usuari values(NULL,'Macaria','Pinto','Cabeza','1980-02-01','macaria','');
insert into usuari values(NULL,'Nazario','Canales','Vazquez','2002-05-16','nazario','');
insert into usuari values(NULL,'Chelo','Sebastián',NULL,'1976-07-31','chelo','');
insert into usuari values(NULL,'Inocencio','Santana','Badía','1975-04-08','inocencio','');
insert into usuari values(NULL,'Pepita','Marín',NULL,'1984-08-01','pepita','');
insert into usuari values(NULL,'Eli','Fabregat','Cañizares','1991-02-01','eli','');
insert into usuari values(NULL,'Coral','Pardo',NULL,'1989-05-30','coral','');
insert into usuari values(NULL,'Buenaventura','Zorrilla','Cárdenas','1971-11-07','buenaventura','');
insert into usuari values(NULL,'María Luisa','Diaz','Berenguer','1985-03-07','maria','');
insert into usuari values(NULL,'Nereida','Lara',NULL,'1975-12-20','nereida','');
insert into usuari values(NULL,'Dolores','Costa','Pérez','1988-08-29','dolores','');
insert into usuari values(NULL,'Cándido','Requena','Pallarès','1962-11-30','candido','');
insert into usuari values(NULL,'Ciro','Duarte',NULL,'1975-07-28','ciro','');
insert into usuari values(NULL,'Soraya','Palomar','Feijoo','1976-06-22','soraya','');
insert into usuari values(NULL,'Concha','Bárcena','Mur','1986-08-26','concha','');
insert into usuari values(NULL,'Duilio Lalo','Durán','Vera','1978-09-17','duilio','');
insert into usuari values(NULL,'Cecilia','Torrijos','Domínguez','1993-03-09','cecilia','');
insert into usuari values(NULL,'Montserrat','Alonso','Segovia','1997-12-17','montserrat','');
insert into usuari values(NULL,'Jose','Gárate','Carnero','1987-04-16','jose','');
insert into usuari values(NULL,'Custodia','Franco','Doménech','1961-03-27','custodia','');
insert into usuari values(NULL,'Odalys','Calleja',NULL,'1976-07-07','odalys','');
insert into usuari values(NULL,'Irene','Quintanilla','Molins','1994-05-30','irene','');
insert into usuari values(NULL,'Domitila','de Márquez',NULL,'1973-07-27','domitila','');
insert into usuari values(NULL,'Plácido','Merino','Jáuregui','1993-04-12','placido','');
insert into usuari values(NULL,'Lorena','Viña','Cobo','1986-08-16','lorena','');
insert into usuari values(NULL,'Isidoro','Benítez',NULL,'1986-07-03','isidoro','');
insert into usuari values(NULL,'Juanita','Falcó','Pascual','1962-12-22','juanita','');
insert into usuari values(NULL,'Dorotea','Porta',NULL,'1962-05-16','dorotea','');
insert into usuari values(NULL,'Isaura','Campos','Jara','1981-05-01','isaura','');
insert into usuari values(NULL,'Marcelino','Echevarría','Cárdenas','1967-09-13','marcelino','');
insert into usuari values(NULL,'Fabio','Arévalo','Muñoz','1971-03-20','fabio','');
insert into usuari values(NULL,'Saturnino','Pablo','Cepeda','1990-06-27','saturnino','');




insert into projecte values(NULL,'Proyecto aplicación Android','Proyecto de desarrollo de una aplicación en dispositivos móbiles Android, que permite generar informes personalizados.',(select id from usuari where login = 'placido'));
insert into projecte values(NULL,'Proyecto aplicación Java','Proyecto de desarrollo de una aplicación en Java, que permite gestionar la contabilidad.',(select id from usuari where login = 'tomasa'));
insert into projecte values(NULL,'Proyecto aplicación UWP','Proyecto de desarrollo de una aplicación en UWP, que permite gestionar todas las funcionalidades de recursos humanos.',(select id from usuari where login = 'soraya'));
insert into projecte values(NULL,'Proyecto creación de página web con PHP','Proyecto de desarrollo de una página web con PHP.',(select id from usuari where login = 'nazario'));




insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación Android'), (select id from usuari where login = 'placido'),1);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación Android'), (select id from usuari where login = 'marisela'),0);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación Android'), (select id from usuari where login = 'norberto'),0);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación Android'), (select id from usuari where login = 'fausto'),0);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación Android'), (select id from usuari where login = 'clara'),1);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación Android'), (select id from usuari where login = 'lope'),0);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación Android'), (select id from usuari where login = 'macario'),1);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación Android'), (select id from usuari where login = 'macaria'),0);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación Android'), (select id from usuari where login = 'nazario'),2);

insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación Java'), (select id from usuari where login = 'tomasa'),1);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación Java'), (select id from usuari where login = 'chelo'),2);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación Java'), (select id from usuari where login = 'inocencio'),0);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación Java'), (select id from usuari where login = 'pepita'),0);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación Java'), (select id from usuari where login = 'eli'),0);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación Java'), (select id from usuari where login = 'buenaventura'),0);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación Java'), (select id from usuari where login = 'maria'),0);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación Java'), (select id from usuari where login = 'nereida'),0);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación Java'), (select id from usuari where login = 'dolores'),0);

insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación UWP'), (select id from usuari where login = 'soraya'),1);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación UWP'), (select id from usuari where login = 'ciro'),0);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación UWP'), (select id from usuari where login = 'concha'),0);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación UWP'), (select id from usuari where login = 'duilio'),0);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación UWP'), (select id from usuari where login = 'cecilia'),0);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación UWP'), (select id from usuari where login = 'montserrat'),2);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación UWP'), (select id from usuari where login = 'jose'),0);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación UWP'), (select id from usuari where login = 'custodia'),0);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación UWP'), (select id from usuari where login = 'odalys'),0);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación UWP'), (select id from usuari where login = 'macario'),1);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto aplicación UWP'), (select id from usuari where login = 'buenaventura'),0);


insert into projecte_usuari values((select id from projecte where nom = 'Proyecto creación de página web con PHP'), (select id from usuari where login = 'nazario'),1);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto creación de página web con PHP'), (select id from usuari where login = 'irene'),0);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto creación de página web con PHP'), (select id from usuari where login = 'lorena'),0);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto creación de página web con PHP'), (select id from usuari where login = 'placido'),1);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto creación de página web con PHP'), (select id from usuari where login = 'isidoro'),0);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto creación de página web con PHP'), (select id from usuari where login = 'juanita'),0);
insert into projecte_usuari values((select id from projecte where nom = 'Proyecto creación de página web con PHP'), (select id from usuari where login = 'dorotea'),0);




insert into tasca values(NULL,'2022-05-01','Crear base de datos','Tarea que consiste en crear la base de datos','2022-05-07',(select id from usuari where login = 'marisela'),(select id from usuari where login = 'placido'),0,(select id from projecte where nom = 'Proyecto aplicación Android'));
insert into tasca values(NULL,'2022-01-22','Crear la lista de los clientes','Tarea que consiste en crear la lista de los clientes','2022-10-12',(select id from usuari where login = 'norberto'),(select id from usuari where login = 'placido'),2,(select id from projecte where nom = 'Proyecto aplicación Android'));
insert into tasca values(NULL,'2022-01-22','Crear la lista de los representantes','Tarea que consiste en crear la lista de los representantes',NULL,(select id from usuari where login = 'fausto'),(select id from usuari where login = 'norberto'),1,(select id from projecte where nom = 'Proyecto aplicación Android'));
insert into tasca values(NULL,'2022-03-06','Crear pantalla almacén','Tarea que consiste en crear la pantalla del almacén','2022-03-16',(select id from usuari where login = 'lope'),(select id from usuari where login = 'placido'),0,(select id from projecte where nom = 'Proyecto aplicación Android'));
insert into tasca values(NULL,'2022-02-22','Leer códigos de barras','Tarea que consiste en leer códigos de barras',NULL,(select id from usuari where login = 'macaria'),(select id from usuari where login = 'placido'),3,(select id from projecte where nom = 'Proyecto aplicación Android'));


insert into tasca values(NULL,'2022-03-09','Crear classes','Tarea que consiste en crear classes','2022-03-10',(select id from usuari where login = 'inocencio'),(select id from usuari where login = 'tomasa'),3,(select id from projecte where nom = 'Proyecto aplicación Java'));
insert into tasca values(NULL,'2022-03-30','Crear servidor','Tarea que consiste en crear el servidor',NULL,(select id from usuari where login = 'inocencio'),(select id from usuari where login = 'tomasa'),3,(select id from projecte where nom = 'Proyecto aplicación Java'));
insert into tasca values(NULL,'2022-01-02','Conectar a la base de datos','Tarea que consiste en conectar a la base de datos','2022-01-04',(select id from usuari where login = 'pepita'),(select id from usuari where login = 'nereida'),3,(select id from projecte where nom = 'Proyecto aplicación Java'));
insert into tasca values(NULL,'2022-01-28','Crear pantallas con swing','Tarea que consiste en crear pantallas con swing','2022-02-28',(select id from usuari where login = 'maria'),(select id from usuari where login = 'tomasa'),3,(select id from projecte where nom = 'Proyecto aplicación Java'));
insert into tasca values(NULL,'2022-03-05','Test de funcionalidades','Tarea que consiste en test de funcionalidades',NULL,(select id from usuari where login = 'dolores'),(select id from usuari where login = 'nereida'),3,(select id from projecte where nom = 'Proyecto aplicación Java'));


insert into tasca values(NULL,'2022-03-22','Crear conexión a base de datos','Tarea que consiste en crear conexión a base de datos','2022-03-27',(select id from usuari where login = 'custodia'),(select id from usuari where login = 'soraya'),0,(select id from projecte where nom = 'Proyecto aplicación UWP'));
insert into tasca values(NULL,'2022-01-26','Crear elementos interfaz','Tarea que consiste en crear elementos interfaz',NULL,(select id from usuari where login = 'concha'),(select id from usuari where login = 'soraya'),3,(select id from projecte where nom = 'Proyecto aplicación UWP'));
insert into tasca values(NULL,'2022-02-19','Programar métodos de cáclulos de impuestos','Tarea que consiste en programar métodos de cáclulos de impuestos','2022-03-01',(select id from usuari where login = 'duilio'),(select id from usuari where login = 'macario'),3,(select id from projecte where nom = 'Proyecto aplicación UWP'));


insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'marisela'),4,(select id from tasca where propietari = (select id from usuari where login = 'marisela')));
insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'norberto'),4,(select id from tasca where propietari = (select id from usuari where login = 'norberto')));
insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'fausto'),4,(select id from tasca where propietari = (select id from usuari where login = 'fausto')));
insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'lope'),4,(select id from tasca where propietari = (select id from usuari where login = 'lope')));
insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'macaria'),4,(select id from tasca where propietari = (select id from usuari where login = 'macaria')));

insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'inocencio'),4,(select id from tasca where propietari = (select id from usuari where login = 'inocencio') and data_creacio = '2022-03-09'));
insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'inocencio'),4,(select id from tasca where propietari = (select id from usuari where login = 'inocencio') and data_creacio = '2022-03-30'));
insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'pepita'),4,(select id from tasca where propietari = (select id from usuari where login = 'pepita')));
insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'maria'),4,(select id from tasca where propietari = (select id from usuari where login = 'maria')));
insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'dolores'),4,(select id from tasca where propietari = (select id from usuari where login = 'dolores')));insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'dolores'),4,(select id from tasca where propietari = (select id from usuari where login = 'dolores')));

insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'custodia'),4,(select id from tasca where propietari = (select id from usuari where login = 'custodia')));insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'dolores'),4,(select id from tasca where propietari = (select id from usuari where login = 'dolores')));
insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'concha'),4,(select id from tasca where propietari = (select id from usuari where login = 'concha')));insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'dolores'),4,(select id from tasca where propietari = (select id from usuari where login = 'dolores')));
insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'duilio'),4,(select id from tasca where propietari = (select id from usuari where login = 'duilio')));insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'dolores'),4,(select id from tasca where propietari = (select id from usuari where login = 'dolores')));





