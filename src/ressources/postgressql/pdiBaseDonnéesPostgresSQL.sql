

CREATE TABLE Entreprise (
    IdEts SERIAL PRIMARY KEY,
    NomEts VARCHAR(255),
    LocalisationEts VARCHAR(255)
);

CREATE TABLE Employé (
    IdEmploye SERIAL PRIMARY KEY,
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
    IdConge SERIAL PRIMARY KEY,
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
    IdBonus SERIAL PRIMARY KEY,
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
    IdSalaire SERIAL PRIMARY KEY,
    MontantSalaire DECIMAL(10, 2),
    DateVersementSalaire DATE,
    IdEmploye INT,
    FOREIGN KEY (IdEmploye) REFERENCES Employé(IdEmploye)
); 