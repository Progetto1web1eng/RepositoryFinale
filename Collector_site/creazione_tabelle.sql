# CREAZIONE DEL DATABASE
drop schema if exists collector_site;
create schema collector_site;
use collector_site;

# CREAZIONE TABELLE CHE CONTENGONO VALORI PREDEFINITI
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
    barcode varchar(50), 
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
    # è UNIQUE perché l'istanza di di un Disco è personale
    # constraint unicoDisco unique (IDdisco,IDstatoDisco)
); 

create table incide (
    IDdisco smallint not null, 
    IDartista smallint not null,
    primary key(IDdisco, IDartista),
    # CHECK RECENTE
    foreign key (IDdisco) references disco(ID) on delete cascade,
    foreign key (IDartista) references artista(ID)
);

create table crea (
    IDartista smallint not null,
    IDtraccia smallint not null,
    primary key (IDartista, IDtraccia),
    foreign key (IDartista) references artista(ID),
    # CHECK RECENT
    foreign key (IDtraccia) references traccia(ID) on delete cascade
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
# insert into statoDisco values(3,"Aperto - mai usato");


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
# gruppo musicale 1
insert into artista values (1, "Metallica", null, null, null);
insert into artista values (2,"James Hetfield", 1, "Voce", 1);
insert into artista values (3,"Lars Ulrich", 5, "Percussioni",1);
# gruppo musicale 2
insert into artista values (4,"Queen",null,null,null);
insert into artista values (5,"Freddy Mercury",1, "Voce",4);
# singoli artisti
insert into artista values (6,"Richard Benson",null, null, null);
insert into artista values (7,"Axl Rose", null, null, null);
insert into artista values (8,"Davide Guetta", null, null, null);
insert into collezionista values (1, "stefa", "stefano@gmail.com", "", "stefa", "3880581680");
insert into collezionista values (2, "fabri", "fabrizio@gmail.com", "", "fabri", "3880581670");
insert into collezionista values (3, "mauri", "maurizio@gmail.com", "", "mauri", "3880581660");


# collezioni create da Stefano
insert into collezione values (1, "anni 80", 1, true); 
insert into collezione values (2, "euro disco", 1, true);
insert into collezione values (3, "anni 90", 1, true);
insert into collezione values (4, "chill music", 1, false);
# collezioni create da Fabrizio
insert into collezione values (5, "heavy", 2, true);
insert into collezione values (6, "newCollection", 2, false);
insert into collezione values (7, "HI-FI Rock", 2, false);


insert into disco values (1, "Black Album", "47957", 11, "Metal", 1857, "Metal Studio", 1, "CD");
insert into disco values (2, "Master of Puppets", "47956", 11, "Metal", 1958, "Metal Studio", 2, "vinile");
insert into disco values (3, "Bianco", "47956", 1, "Metal", 1978, "Metal Studio", 1, "CD");
# insert into disco values (4, "Master of Puppets", "47956", 11, "Metal", 1958, "Metal Studio", 4, "audiocassetta");
insert into disco values (5, "The Dark Side of the Moon", "3282", 6, "Rock", 1970, "Rock Studio", 2, "vinile"); 
# dischi recenti
insert into disco values (6, "DiscoRock1", "3283", 6, "Rock", 1972, "Rock Studio", 2, "vinile"); 
insert into disco values (7, "DiscoRock2", "3284", 6, "Rock", 1973, "Rock Studio", 2, "vinile"); 
insert into disco values (8, "DiscoRock3", "3285", 6, "Rock", 1970, "Rock Studio", 2, "vinile"); 
insert into disco values (9, "DiscoPop1", "3286", 12, "Pop", 1970, "Pop Studio", 2, "vinile"); 
insert into disco values (10, "DiscoPop2", "3287", 12, "Pop", 1970, "Blues Studio", 2, "vinile"); 
insert into disco values (11, "DiscoBlues", "3288", 1, "Blues", 1970, "Blues Studio", 2, "vinile"); 
insert into disco values (12, "DiscoBlues1", "3289", 1, "Blues", 1970, "Blues Studio", 2, "vinile"); 

# insert into immagine(nomeImmagine,imgType,IDdisco,dimensioneImmagine,filename,digest,updated) VALUES("foto_black_album", "jpg", 1,200,"c:/", "jgfjjvhvhvh5456", CURRENT_TIMESTAMP);


insert into traccia values (1, "Enter Sandman", 0251, 1);  
insert into traccia values (2, "Sad butTrue",0 , 1);


insert into racchiude values (1, 3);
# insert into racchiude values (1, 4);
insert into racchiude values (1, 2);
insert into racchiude values (1, 5);
insert into racchiude values (1, 1);
insert into racchiude values (3, 1);
insert into racchiude values (2, 2);
insert into racchiude values (5, 2);
# tuple recenti della tabella "racchiude"
insert into racchiude values (1, 6);
insert into racchiude values (2, 7);
insert into racchiude values (3, 8);
insert into racchiude values (4, 9);
insert into racchiude values (1, 10);
insert into racchiude values (2, 11);


# dischi di Stefano
insert into colleziona values (1, 1, "Nuovo", 1, 1);
insert into colleziona values (0, 2, "Usato", 1, 1);

insert into colleziona values (0, 1, "Nuovo", 1, 2);
insert into colleziona values (2, 2, "Usato", 1, 2);

insert into colleziona values (1, 1, "Nuovo", 1, 3);
insert into colleziona values (0, 2, "Usato", 1, 3);

insert into colleziona values (5, 1, "Nuovo", 1, 5);
insert into colleziona values (0, 2, "Usato", 1, 5);

insert into colleziona values (1, 1, "Nuovo", 1, 6);
insert into colleziona values (0, 2, "Usato", 1, 6);

insert into colleziona values (1, 1, "Nuovo", 1, 7);
insert into colleziona values (0, 2, "Usato", 1, 7);

insert into colleziona values (1, 1, "Nuovo", 1, 8);
insert into colleziona values (0, 2, "Usato", 1, 8);

insert into colleziona values (1, 1, "Nuovo", 1, 9);
insert into colleziona values (0, 2, "Usato", 1, 9);

insert into colleziona values (1, 1, "Nuovo", 1, 10);
insert into colleziona values (0, 2, "Usato", 1, 10);

insert into colleziona values (1, 1, "Nuovo", 1, 11);
insert into colleziona values (1, 2, "Usato", 1, 11);

insert into colleziona values (2, 1, "Nuovo", 2, 2); 
insert into colleziona values (5, 2, "Usato", 2, 2); 
# insert into colleziona values (1, 1, "Nuovo", 1, 4);


insert into crea values (1, 1);


insert into incide values(1, 1);
insert into incide values(2, 1);
insert into incide values(3, 4);
# insert into incide values(4, 1);
insert into incide values(5, 7);
insert into incide values(6, 7);
insert into incide values(7, 7);
insert into incide values(8, 7);
insert into incide values(9, 7);
insert into incide values(10, 7);
insert into incide values(11, 7);
insert into incide values(12, 8);


insert into condivide values(1, 7);


# GESTIONE UTENZA
drop user if exists 'collector'@'localhost';
create user 'collector'@'localhost' identified by '1234';
grant all on collector_site.* to 'collector'@'localhost';