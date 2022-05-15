create table rol(
	
	id_rol integer(2) PRIMARY KEY
);

create table estat(

	id_estat integer(2) PRIMARY KEY
	
);

create table usuari(

	id integer(6) AUTO_INCREMENT PRIMARY KEY,
	nom varchar(60) NOT NULL,
	cognom1 varchar(80) NOT NULL,
	cognom2 varchar(80),
	data_naix DATE NOT NULL,
	login varchar(20) NOT NULL,
	passwd_hash varchar(200) NOT NULL,
	
	CONSTRAINT USUSARI_UN_LOGIN unique(login)

);

create table projecte(

	id integer(6) AUTO_INCREMENT PRIMARY KEY,
	nom varchar(100) NOT NULL,
	descripcio TEXT NOT NULL,
	cap_projecte integer(6) NOT NULL, 
	
	CONSTRAINT FK_PROJECTE_USUARI FOREIGN KEY(cap_projecte) REFERENCES usuari(id),
	CONSTRAINT PROJECTE_UN_NOM unique(nom)
	
	

);


create table projecte_usuari(

	id_projecte integer(6),
	id_usuari integer(6),
	id_rol integer(2) NOT NULL,
	PRIMARY KEY(id_projecte,id_usuari),
	CONSTRAINT FK_PROJECTEUSUARI_PROJECTE FOREIGN KEY(id_projecte) REFERENCES projecte(id),
	CONSTRAINT FK_PROJECTEUSUARI_USUARI FOREIGN KEY(id_usuari) REFERENCES usuari(id),
	CONSTRAINT FK_PROJECTEUSUARI_ROL FOREIGN KEY(id_rol) REFERENCES rol(id_rol)

);



create table tasca(

	id integer(6) AUTO_INCREMENT PRIMARY KEY,
	data_creacio date NOT NULL,
	nom varchar(80) NOT NULL,
	descripcio varchar(300) NOT NULL,
	data_limit date,
	propietari integer(6) NOT NULL,
	responsable integer(6),
	id_estat integer(2) NOT NULL,
	projecte_id integer(2), 
	
	
	
	CONSTRAINT FK_TASCA_USUARI FOREIGN KEY(propietari) REFERENCES usuari(id),
	CONSTRAINT FK_TASCARESPONSABLE_USUARI FOREIGN KEY(responsable) REFERENCES usuari(id),
	CONSTRAINT FK_TASCA_ESTAT FOREIGN KEY(id_estat) REFERENCES estat(id_estat),
	CONSTRAINT FK_TASCA_PROJECTE FOREIGN KEY(projecte_id) REFERENCES projecte(id)

);


create table entrada(

	numero integer(6) AUTO_INCREMENT PRIMARY KEY,
	data_entrada date NOT NULL,
	entrada varchar(300) NOT NULL,
	nova_assignacio integer(6),
	escriptor integer(6) NOT NULL,
	nou_estat integer(2),
	tasca_id integer(2), 
	
	CONSTRAINT FK_ENTRADANOVAASSIGN_USUARI FOREIGN KEY(nova_assignacio) REFERENCES usuari(id),
	CONSTRAINT FK_ENTRADAESCRIPTOR_USUARI FOREIGN KEY(escriptor) REFERENCES usuari(id),
	CONSTRAINT FK_ENTRADA_ESTAT FOREIGN KEY(nou_estat) REFERENCES estat(id_estat),
	CONSTRAINT FK_ENTRADA_TASCA FOREIGN KEY(tasca_id) REFERENCES tasca(id)
	
	


);