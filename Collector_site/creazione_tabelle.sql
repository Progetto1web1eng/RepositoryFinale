# CREAZIONE DEL DATABASE
drop schema if exists collector_site;
create schema collector_site;

use collector_site;


# CREAZIOEN TABELLE CHE CONTENGONO VALORI PREDEFINITI
create table genere(
	ID smallint primary key auto_increment,
    nome varchar(50)
);

create table statoDisco(
	ID smallint primary key auto_increment,
    nome varchar(50)
);

create table ruolo(
	ID smallint primary key auto_increment,
    nome varchar(50)
);

create table tipo (
	ID smallint primary key auto_increment,
    nome varchar(50)
);


# CREAZIONE DELLE TABELLE
create table collezionista (
	ID smallint primary key auto_increment,
    nickname varchar(50) unique not null, 
    email varchar(50) unique not null, 
    username varchar(50) not null, 
    `password` varchar(50) not null ,
    cellulare varchar(15) unique
);

create table disco (
	ID smallint primary key auto_increment,
    nomeDisco varchar(50) not null, 
    barcode char(12), 
    IDgenere smallint not null,
    genere varchar(50) not null,
    anno smallint not null,
    etichetta varchar(50) not null,
	IDtipo smallint not null,
    tipo varchar(50) not null,
    fulltext (nomeDisco, barcode), # necessario per la ricerca globale
	foreign key (IDgenere) references genere(ID),
	foreign key (IDtipo) references tipo(ID)
);

create table immagine(
	ID smallint primary key auto_increment,
	# nome che il client ha assegnato all'immagine
    nomeImmagine varchar(255) not null, 
    dimensioneImmagine int(11) not null, 
    # nome con il quale viene effettivamente salvata l'immagine nel server
    filename varchar(255) not null, 
    # le immagini devono avere formato .jpeg o .png 
    imgType varchar(10) not null, 
    # in IDdisco non ci sta il vincolo "not null" perché nella tabella "immagine" ci sono anche immagini che sono associate alle 
    # collezioni e non ai dischi
    # CHECK da controllare il not null
    IDdisco smallint not null,
	`digest` varchar(255) not null,
    `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    foreign key (IDdisco) references disco(ID) on delete cascade,
    constraint pathUnivoco unique (filename, nomeImmagine)
); 

create table collezione (
	ID smallint primary key auto_increment,
    nomeCollezione varchar(50) not null, 
    IDcollezionista smallint not null,
    pubblico bool not null default false,
    fulltext(nomeCollezione),
    foreign key (IDcollezionista) references collezionista(ID) on delete cascade,
    # uno stesso collezionista non può creare più di una collezione con il medesimo nome
    constraint nome_collezione_univoco_per_collezionista unique (nomeCollezione, IDcollezionista)
);

create table traccia (
	ID smallint primary key auto_increment,
    titolo varchar(50) not null, 
    durata time not null, 
    IDdisco smallint not null,
    foreign key (IDdisco) references disco(ID) on delete cascade
);

create table artista (
	ID smallint primary key auto_increment,
    nomeDarte varchar(50) not null, 
    # il "ruolo" non può essere not null perché se l'artista in questione è un gruppo musicale, quest'ultimo non ha associato un ruolo
    IDruolo smallint,
    ruolo varchar(50),
    IDgruppoMusicale smallint,
    foreign key (IDgruppoMusicale) references artista(ID),
	foreign key (IDruolo) references ruolo(ID)
);

create table condivide (
    IDcollezionista smallint not null, 
    IDcollezione smallint not null,
    primary key(IDcollezionista, IDcollezione),
    foreign key (IDcollezionista) references collezionista(ID) on delete cascade,
    foreign key (IDcollezione) references collezione(ID) on delete cascade
);

create table colleziona (
	numCopieDisco smallint not null default 0, 
    IDstatoDisco smallint not null,
    statoDisco varchar(50) not null,
    IDcollezionista smallint not null,
    IDdisco smallint not null,
    primary key (IDcollezionista, IDdisco, statoDisco),
    foreign key (IDcollezionista) references collezionista(ID) on delete cascade,
    foreign key (IDdisco) references disco(ID) on delete cascade,
	foreign key (IDstatoDisco) references statoDisco(ID)
);

create table incide (
	IDdisco smallint not null, 
    IDartista smallint not null,
    primary key(IDdisco, IDartista),
    foreign key (IDdisco) references disco(ID),
    foreign key (IDartista) references artista(ID)
);

create table crea (
    IDartista smallint not null,
    IDtraccia smallint not null,
    primary key (IDartista, IDtraccia),
    foreign key (IDartista) references artista(ID),
    foreign key (IDtraccia) references traccia(ID)
);

create table racchiude (
    IDcollezione smallint,
    IDdisco smallint,
    primary key (IDcollezione, IDdisco),
    foreign key (IDcollezione) references collezione(ID) on delete cascade,
    foreign key (IDdisco) references disco(ID) on delete cascade
);


# INSERIMENTO VALORI PREDEFINITI NELLE TABELLE
insert into genere values(1,"Blues");
insert into genere values(2,"Classico");
insert into genere values(3,"Country");
insert into genere values(4,"Dance");
insert into genere values(5,"Folk");
insert into genere values(6,"Rock");
insert into genere values(7,"House");
insert into genere values(8,"Indie");
insert into genere values(9,"Jazz");
insert into genere values(10,"Latino");
insert into genere values(11,"Metal");
insert into genere values(12,"Pop");

insert into statoDisco values(1,"Nuovo");
insert into statoDisco values(2,"Usato");
insert into statoDisco values(3,"Aperto - mai usato");

insert into ruolo values(1,"Voce");
insert into ruolo values(2,"Chitarra");
insert into ruolo values(3,"Basso");
insert into ruolo values(4,"Violino");
insert into ruolo values(5,"Percussioni");
insert into ruolo values(6,"Tastiera");
insert into ruolo values(7,"Pianoforte");
insert into ruolo values(8,"DJ");
insert into ruolo values(9,"Fiato");

insert into tipo values(1,"CD");
insert into tipo values(2,"vinile");
insert into tipo values(3,"MP3");
insert into tipo values(4,"audiocassetta");


# INSERIMENTO DATI DI PROVA
insert into artista values (1, "Metallica", null, null, null);
insert into artista values (2,"James Hetfield", 1, "Voce", 1);

insert into collezionista values (1, "Stefano", "stefano@gmail.com", "stefa", "stefa", "3880581680");
insert into collezionista values (2, "Fabrizio", "fabrizio@gmail.com", "fabri", "fabri", "3880581670");
insert into collezionista values (3, "Maurizio", "maurizio@gmail.com", "mauri", "mauri", "3880581660");

insert into collezione values (1, "anni 80", 1, true); 
insert into collezione values (2, "euro disco", 1, true);
insert into collezione values (3, "anni 90", 1, true);
insert into collezione values (4, "chill music", 1, true);
insert into collezione values (5, "heavy", 2, true);

insert into disco values (1, "Black Album", "47957", 11, "Metal", 1857, "Metal Studio", 1, "CD");
insert into disco values (2, "Master of Puppets", "47956", 11, "Metal", 1958, "Metal Studio", 2, "vinile");
insert into disco values (3, "bianco", "47956", 11, "Metal", 1978, "Metal Studio", 1, "CD");
insert into disco values (4, "Master of Puppets", "47956", 11, "Metal", 1958, "Metal Studio", 4, "audiocassetta");

insert into immagine(nomeImmagine,imgType,IDdisco,dimensioneImmagine,filename,digest,updated) VALUES("foto_black_album", "jpg", 1,200,"c:/", "jgfjjvhvhvh5456", CURRENT_TIMESTAMP);

insert into traccia values (1, "Enter Sandman", 0, 1); 

insert into racchiude values (3, 1);
insert into racchiude values (1, 1);
insert into racchiude values (2, 2);
insert into racchiude values (5, 2);

insert into colleziona values (2, 1, "Nuovo", 1, 1);

insert into crea values (1, 1);


# GESTIONE UTENZA
drop user if exists 'collector'@'localhost';
create user 'collector'@'localhost' identified by '1234';
grant all on collector_site.* to 'collector'@'localhost';








