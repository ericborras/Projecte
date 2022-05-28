delete from entrada;
delete from tasca;
delete from projecte_usuari;
delete from projecte;
delete from usuari;
delete from rol;
delete from estat;


insert into rol values(0,'PROGRAMADOR');
insert into rol values(1,'ANALISTA');
insert into rol values(2,'CLIENT');




insert into estat values(0,'TANCADA_SENSE_SOLUCIO');
insert into estat values(1,'TANCADA_RESOLTA');
insert into estat values(2,'TANCADA_DUPLICADA');
insert into estat values(3,'OBERTA_NO_ASSIGNADA');
insert into estat values(4,'OBERTA_ASSIGNADA');



insert into usuari values(NULL,'Emperatriz Haydée','Carreño','Moles','1958-07-23','emperatriz','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Marisela','de Reguera',NULL,'1985-08-27','marisela','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Norberto Enrique','Carmona','Solera','1972-02-08','norberto','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Fausto','Montserrat','Enríquez','1965-10-10','fausto','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Clara','Zabaleta','Zapata','1998-04-14','clara','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Tomasa','del Pomares',NULL,'1968-09-06','tomasa','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Lope','Piquer','Manso','1985-08-02','lope','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Macario','Adán',NULL,'1960-02-06','macario','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Macaria','Pinto','Cabeza','1980-02-01','macaria','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Nazario','Canales','Vazquez','2002-05-16','nazario','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Chelo','Sebastián',NULL,'1976-07-31','chelo','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Inocencio','Santana','Badía','1975-04-08','inocencio','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Pepita','Marín',NULL,'1984-08-01','pepita','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Eli','Fabregat','Cañizares','1991-02-01','eli','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Coral','Pardo',NULL,'1989-05-30','coral','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Buenaventura','Zorrilla','Cárdenas','1971-11-07','buenaventura','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'María Luisa','Diaz','Berenguer','1985-03-07','maria','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Nereida','Lara',NULL,'1975-12-20','nereida','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Dolores','Costa','Pérez','1988-08-29','dolores','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Cándido','Requena','Pallarès','1962-11-30','candido','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Ciro','Duarte',NULL,'1975-07-28','ciro','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Soraya','Palomar','Feijoo','1976-06-22','soraya','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Concha','Bárcena','Mur','1986-08-26','concha','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Duilio Lalo','Durán','Vera','1978-09-17','duilio','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Cecilia','Torrijos','Domínguez','1993-03-09','cecilia','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Montserrat','Alonso','Segovia','1997-12-17','montserrat','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Jose','Gárate','Carnero','1987-04-16','jose','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Custodia','Franco','Doménech','1961-03-27','custodia','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Odalys','Calleja',NULL,'1976-07-07','odalys','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Irene','Quintanilla','Molins','1994-05-30','irene','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Domitila','de Márquez',NULL,'1973-07-27','domitila','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Plácido','Merino','Jáuregui','1993-04-12','placido','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Lorena','Viña','Cobo','1986-08-16','lorena','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Isidoro','Benítez',NULL,'1986-07-03','isidoro','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Juanita','Falcó','Pascual','1962-12-22','juanita','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Dorotea','Porta',NULL,'1962-05-16','dorotea','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Isaura','Campos','Jara','1981-05-01','isaura','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Marcelino','Echevarría','Cárdenas','1967-09-13','marcelino','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Fabio','Arévalo','Muñoz','1971-03-20','fabio','81dc9bdb52d04dc20036dbd8313ed055');
insert into usuari values(NULL,'Saturnino','Pablo','Cepeda','1990-06-27','saturnino','81dc9bdb52d04dc20036dbd8313ed055');




insert into projecte values(NULL,'Proyecto aplicación Android','Proyecto de desarrollo de una aplicación en dispositivos móbiles Android, que permite generar informes personalizados.',(select id from usuari where login = 'placido'));
insert into projecte values(NULL,'Proyecto aplicación Java','Proyecto de desarrollo de una aplicación en Java, que permite gestionar la contabilidad.',(select id from usuari where login = 'tomasa'));
insert into projecte values(NULL,'Proyecto aplicación UWP','Proyecto de desarrollo de una aplicación en UWP, que permite gestionar todas las funcionalidades de recursos humanos.',(select id from usuari where login = 'soraya'));
insert into projecte values(NULL,'Proyecto creación de página web con PHP','Proyecto de desarrollo de una página web con PHP.',(select id from usuari where login = 'nazario'));




insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación Android'), (select id from usuari where login = 'placido'),(select id_rol from rol where nom = 'ANALISTA'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación Android'), (select id from usuari where login = 'marisela'),(select id_rol from rol where nom = 'PROGRAMADOR'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación Android'), (select id from usuari where login = 'norberto'),(select id_rol from rol where nom = 'PROGRAMADOR'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación Android'), (select id from usuari where login = 'fausto'),(select id_rol from rol where nom = 'PROGRAMADOR'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación Android'), (select id from usuari where login = 'clara'),(select id_rol from rol where nom = 'ANALISTA'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación Android'), (select id from usuari where login = 'lope'),(select id_rol from rol where nom = 'PROGRAMADOR'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación Android'), (select id from usuari where login = 'macario'),(select id_rol from rol where nom = 'ANALISTA'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación Android'), (select id from usuari where login = 'macaria'),(select id_rol from rol where nom = 'PROGRAMADOR'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación Android'), (select id from usuari where login = 'nazario'),(select id_rol from rol where nom = 'CLIENT'));

insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación Java'), (select id from usuari where login = 'tomasa'),(select id_rol from rol where nom = 'ANALISTA'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación Java'), (select id from usuari where login = 'chelo'),(select id_rol from rol where nom = 'CLIENT'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación Java'), (select id from usuari where login = 'inocencio'),(select id_rol from rol where nom = 'PROGRAMADOR'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación Java'), (select id from usuari where login = 'pepita'),(select id_rol from rol where nom = 'PROGRAMADOR'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación Java'), (select id from usuari where login = 'eli'),(select id_rol from rol where nom = 'PROGRAMADOR'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación Java'), (select id from usuari where login = 'buenaventura'),(select id_rol from rol where nom = 'PROGRAMADOR'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación Java'), (select id from usuari where login = 'maria'),(select id_rol from rol where nom = 'PROGRAMADOR'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación Java'), (select id from usuari where login = 'nereida'),(select id_rol from rol where nom = 'PROGRAMADOR'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación Java'), (select id from usuari where login = 'dolores'),(select id_rol from rol where nom = 'PROGRAMADOR'));

insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación UWP'), (select id from usuari where login = 'soraya'),(select id_rol from rol where nom = 'ANALISTA'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación UWP'), (select id from usuari where login = 'ciro'),(select id_rol from rol where nom = 'PROGRAMADOR'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación UWP'), (select id from usuari where login = 'concha'),(select id_rol from rol where nom = 'PROGRAMADOR'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación UWP'), (select id from usuari where login = 'duilio'),(select id_rol from rol where nom = 'PROGRAMADOR'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación UWP'), (select id from usuari where login = 'cecilia'),(select id_rol from rol where nom = 'PROGRAMADOR'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación UWP'), (select id from usuari where login = 'montserrat'),(select id_rol from rol where nom = 'CLIENT'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación UWP'), (select id from usuari where login = 'jose'),(select id_rol from rol where nom = 'PROGRAMADOR'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación UWP'), (select id from usuari where login = 'custodia'),(select id_rol from rol where nom = 'PROGRAMADOR'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación UWP'), (select id from usuari where login = 'odalys'),(select id_rol from rol where nom = 'PROGRAMADOR'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación UWP'), (select id from usuari where login = 'macario'),(select id_rol from rol where nom = 'ANALISTA'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto aplicación UWP'), (select id from usuari where login = 'buenaventura'),(select id_rol from rol where nom = 'PROGRAMADOR'));


insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto creación de página web con PHP'), (select id from usuari where login = 'nazario'),(select id_rol from rol where nom = 'ANALISTA'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto creación de página web con PHP'), (select id from usuari where login = 'irene'),(select id_rol from rol where nom = 'PROGRAMADOR'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto creación de página web con PHP'), (select id from usuari where login = 'lorena'),(select id_rol from rol where nom = 'PROGRAMADOR'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto creación de página web con PHP'), (select id from usuari where login = 'placido'),(select id_rol from rol where nom = 'ANALISTA'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto creación de página web con PHP'), (select id from usuari where login = 'isidoro'),(select id_rol from rol where nom = 'PROGRAMADOR'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto creación de página web con PHP'), (select id from usuari where login = 'juanita'),(select id_rol from rol where nom = 'PROGRAMADOR'));
insert into projecte_usuari values(NULL,(select id from projecte where nom = 'Proyecto creación de página web con PHP'), (select id from usuari where login = 'dorotea'),(select id_rol from rol where nom = 'PROGRAMADOR'));




insert into tasca values(NULL,'2022-05-01','Crear base de datos','Tarea que consiste en crear la base de datos','2022-05-07',(select id from usuari where login = 'marisela'),(select id from usuari where login = 'placido'),(select id_estat from estat where nom = 'TANCADA_SENSE_SOLUCIO'),(select id from projecte where nom = 'Proyecto aplicación Android'));
insert into tasca values(NULL,'2022-01-22','Crear la lista de los clientes','Tarea que consiste en crear la lista de los clientes','2022-10-12',(select id from usuari where login = 'norberto'),(select id from usuari where login = 'placido'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from projecte where nom = 'Proyecto aplicación Android'));
insert into tasca values(NULL,'2022-01-22','Crear la lista de los representantes','Tarea que consiste en crear la lista de los representantes',NULL,(select id from usuari where login = 'fausto'),(select id from usuari where login = 'norberto'),(select id_estat from estat where nom = 'TANCADA_RESOLTA'),(select id from projecte where nom = 'Proyecto aplicación Android'));
insert into tasca values(NULL,'2022-03-06','Crear pantalla almacén','Tarea que consiste en crear la pantalla del almacén','2022-03-16',(select id from usuari where login = 'lope'),(select id from usuari where login = 'placido'),(select id_estat from estat where nom = 'TANCADA_SENSE_SOLUCIO'),(select id from projecte where nom = 'Proyecto aplicación Android'));
insert into tasca values(NULL,'2022-02-22','Leer códigos de barras','Tarea que consiste en leer códigos de barras',NULL,(select id from usuari where login = 'macaria'),(select id from usuari where login = 'placido'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from projecte where nom = 'Proyecto aplicación Android'));
insert into tasca values(NULL,'2022-02-22','Hacer búsqueda','Tarea que consiste en hacer la búsqueda',NULL,(select id from usuari where login = 'macario'),(select id from usuari where login = 'placido'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from projecte where nom = 'Proyecto aplicación Android'));
insert into tasca values(NULL,'2022-02-22','Conectar con el servidor','Tarea que consiste conectar con el servidor',NULL,(select id from usuari where login = 'macario'),(select id from usuari where login = 'placido'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from projecte where nom = 'Proyecto aplicación Android'));

insert into tasca values(NULL,'2022-03-09','Crear classes','Tarea que consiste en crear classes','2022-03-10',(select id from usuari where login = 'inocencio'),(select id from usuari where login = 'tomasa'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from projecte where nom = 'Proyecto aplicación Java'));
insert into tasca values(NULL,'2022-03-30','Crear servidor','Tarea que consiste en crear el servidor',NULL,(select id from usuari where login = 'inocencio'),(select id from usuari where login = 'tomasa'),(select id_estat from estat where nom = 'OBERTA_NO_ASSIGNADA'),(select id from projecte where nom = 'Proyecto aplicación Java'));
insert into tasca values(NULL,'2022-01-02','Conectar a la base de datos','Tarea que consiste en conectar a la base de datos','2022-01-04',(select id from usuari where login = 'pepita'),(select id from usuari where login = 'nereida'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from projecte where nom = 'Proyecto aplicación Java'));
insert into tasca values(NULL,'2022-01-28','Crear pantallas con swing','Tarea que consiste en crear pantallas con swing','2022-02-28',(select id from usuari where login = 'maria'),(select id from usuari where login = 'tomasa'),(select id_estat from estat where nom = 'OBERTA_NO_ASSIGNADA'),(select id from projecte where nom = 'Proyecto aplicación Java'));
insert into tasca values(NULL,'2022-03-05','Test de funcionalidades','Tarea que consiste en test de funcionalidades',NULL,(select id from usuari where login = 'dolores'),(select id from usuari where login = 'nereida'),(select id_estat from estat where nom = 'OBERTA_NO_ASSIGNADA'),(select id from projecte where nom = 'Proyecto aplicación Java'));


insert into tasca values(NULL,'2022-03-22','Crear conexión a base de datos','Tarea que consiste en crear conexión a base de datos','2022-03-27',(select id from usuari where login = 'custodia'),(select id from usuari where login = 'soraya'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from projecte where nom = 'Proyecto aplicación UWP'));
insert into tasca values(NULL,'2022-01-26','Crear elementos interfaz','Tarea que consiste en crear elementos interfaz',NULL,(select id from usuari where login = 'concha'),(select id from usuari where login = 'soraya'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from projecte where nom = 'Proyecto aplicación UWP'));
insert into tasca values(NULL,'2022-02-19','Programar métodos de cáclulos de impuestos','Tarea que consiste en programar métodos de cáclulos de impuestos','2022-03-01',(select id from usuari where login = 'duilio'),(select id from usuari where login = 'macario'),(select id_estat from estat where nom = 'OBERTA_NO_ASSIGNADA'),(select id from projecte where nom = 'Proyecto aplicación UWP'));




insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'marisela'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from tasca where propietari = (select id from usuari where login = 'marisela')));
insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'norberto'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from tasca where propietari = (select id from usuari where login = 'norberto')));
insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'fausto'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from tasca where propietari = (select id from usuari where login = 'fausto')));
insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'lope'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from tasca where propietari = (select id from usuari where login = 'lope')));
insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'macaria'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from tasca where propietari = (select id from usuari where login = 'macaria')));
insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'macario'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from tasca where propietari = (select id from usuari where login = 'macario') and nom='Conectar con el servidor'));
insert into entrada values(NULL,'2022-05-16','Entrada',NULL,(select id from usuari where login = 'macario'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from tasca where propietari = (select id from usuari where login = 'macario') and nom='Hacer búsqueda'));
insert into entrada values(NULL,'2022-05-16','Entrada',NULL,(select id from usuari where login = 'macario'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from tasca where propietari = (select id from usuari where login = 'macario') and nom='Conectar con el servidor'));

insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'inocencio'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from tasca where propietari = (select id from usuari where login = 'inocencio') and data_creacio = '2022-03-09'));
insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'inocencio'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from tasca where propietari = (select id from usuari where login = 'inocencio') and data_creacio = '2022-03-30'));
insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'pepita'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from tasca where propietari = (select id from usuari where login = 'pepita')));
insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'maria'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from tasca where propietari = (select id from usuari where login = 'maria')));
insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'dolores'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from tasca where propietari = (select id from usuari where login = 'dolores')));insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'dolores'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from tasca where propietari = (select id from usuari where login = 'dolores')));

insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'custodia'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from tasca where propietari = (select id from usuari where login = 'custodia')));insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'dolores'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from tasca where propietari = (select id from usuari where login = 'dolores')));
insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'concha'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from tasca where propietari = (select id from usuari where login = 'concha')));insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'dolores'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from tasca where propietari = (select id from usuari where login = 'dolores')));
insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'duilio'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from tasca where propietari = (select id from usuari where login = 'duilio')));insert into entrada values(NULL,'2022-05-15','Entrada',NULL,(select id from usuari where login = 'dolores'),(select id_estat from estat where nom = 'OBERTA_ASSIGNADA'),(select id from tasca where propietari = (select id from usuari where login = 'dolores')));





