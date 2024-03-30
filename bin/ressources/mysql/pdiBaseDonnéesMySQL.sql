CREATE TABLE Entreprise (
    IdEnt INT PRIMARY KEY,
    NomEnt VARCHAR(255),
    Localisation VARCHAR(255)
);


CREATE TABLE Employe (
    IDEmploye INT PRIMARY KEY,
    NomE VARCHAR(255),
    PrenomE VARCHAR(255),
    Salaire DECIMAL(10, 2),
    Age INT,
    Tel VARCHAR(20),
    IdEnt INT,
    FOREIGN KEY (IdEnt) REFERENCES Entreprise(IdEnt)
);

CREATE TABLE Conges (
    IDC INT PRIMARY KEY,
    DatedebC DATE,
    DatefinC DATE,
    RaisonC VARCHAR(255),
    IDEmploye INT,
    FOREIGN KEY (IDEmploye) REFERENCES Employe(IDEmploye)
);


CREATE TABLE Prendre (
    IDEmploye INT,
    IDC INT,
    PRIMARY KEY (IDEmploye, IDC),
    FOREIGN KEY (IDEmploye) REFERENCES Employe(IDEmploye),
    FOREIGN KEY (IDC) REFERENCES Conges(IDC)
);

CREATE TABLE Bonus (
    IDB INT PRIMARY KEY,
    MontantB DECIMAL(10, 2),
    RaisonB VARCHAR(255),
    DateB DATE,
      IDEmploye INT,
    FOREIGN KEY (IDEmploye) REFERENCES Employe(IDEmploye),
);

CREATE TABLE Obtenir (
    IDEmploye INT,
    IDB INT,
    PRIMARY KEY (IDEmploye, IDB),
    FOREIGN KEY (IDEmploye) REFERENCES Employe(IDEmploye),
    FOREIGN KEY (IDB) REFERENCES Bonus(IDB)
);

CREATE TABLE Salaire (
    IDP INT PRIMARY KEY,
    Montant DECIMAL(10, 2),
    DateVersement DATE,
    IDEmploye INT,
    FOREIGN KEY (IDEmploye) REFERENCES Employe(IDEmploye)
);
























































/*
//ANCIEN CODE 






CREATE TABLE Entreprise (
    IdEts INT AUTO_INCREMENT PRIMARY KEY, 
    NomEts VARCHAR(255),                 
    LocalisationEts VARCHAR(255)            
);

CREATE TABLE Employé (
    IdEmploye INT AUTO_INCREMENT PRIMARY KEY, 
    NomEmploye VARCHAR(255), 
    PrenomEmploye VARCHAR(255),  
    SalaireEmploye DECIMAL(10, 2), 
    AgeEmploye INT,              
    TelEmploye VARCHAR(20),        
    IdEts INT,           
    FOREIGN KEY (IdEts) REFERENCES Entreprise(IdEts)
);

CREATE TABLE Appartient(
    IdEmploye INT, 
    IdEts INT, 
    DateDebAffectation DATE, 
    DateFinAffectation DATE, 
    FOREIGN KEY (IdEmploye) REFERENCES Employé(IdEmploye), 
    FOREIGN KEY (IdEts) REFERENCES Entreprise(IdEts), 
    PRIMARY KEY (IdEmploye, IdEts)
);


CREATE TABLE Congés (
    IdConge INT AUTO_INCREMENT PRIMARY KEY,    
    DateDebConge DATE,                
    DateFinConge DATE,             
    RaisonConge VARCHAR(255)           
);

CREATE TABLE prendre (
    IdEmploye INT,
    IdConge INT,
    FOREIGN KEY (IdEmploye) REFERENCES Employé(IdEmploye),
    FOREIGN KEY (IdConge) REFERENCES Congés(IdConge),
    PRIMARY KEY (IdEmploye, IdConge)
);

CREATE TABLE Bonus (
    IdBonus INT AUTO_INCREMENT PRIMARY KEY,
    MontantBonus DECIMAL(10, 2),
    RaisonBonus VARCHAR(255),
    DateBonus DATE
);

CREATE TABLE Obtenir (
    IdEmploye INT,
    IdBonus INT, 
    FOREIGN KEY (IdEmploye) REFERENCES Employé(IdEmploye),
    FOREIGN KEY (IdBonus) REFERENCES Bonus(IdBonus),
    PRIMARY KEY (IdEmploye, IdBonus)
);

CREATE TABLE salaire (
    IdSalaire INT AUTO_INCREMENT PRIMARY KEY,
    MontantSalaire DECIMAL(10, 2),
    DateVersementSalaire DATE,
    IdEmploye INT,
    FOREIGN KEY (IdEmploye) REFERENCES Employé(IdEmploye)
);