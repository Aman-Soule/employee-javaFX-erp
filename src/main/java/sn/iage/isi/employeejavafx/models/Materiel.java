package sn.iage.isi.employeejavafx.models;

import javafx.beans.*;
import lombok.*;
import sn.iage.isi.employeejavafx.services.CategorieService;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@ToString

public class Materiel {

    private  int id;
    private  String  nom;
    private  String  description;
    private  Integer  quantite;
    private  String localisation;
    private  Date date_acquisition;
    private  Double  valeur;
    private  String  etat;
    private  Categorie  categorie;

    // ── Constructeur sans id (création) ───────────────────────────────────────


    public Materiel(String nom, String description, Integer quantite, String localisation, Date date,  Double valeur, String etat, Categorie categorie) {
        this(0, nom, description, quantite, localisation, date,  valeur, etat, categorie );
    }


    public Materiel(int id, String nom, String description, Integer quantite,
                    String localisation, Date date, Double valeur, String etat,
                    Categorie categorie ) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.quantite = quantite;
        this.localisation = localisation;
        this.valeur = valeur;
        this.etat = etat;
        this.date_acquisition = date;
        this.categorie = categorie;

    }


    public Materiel(String nom, String description, int quantite, String localisation, Date date, double valeur, String etat) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.quantite = quantite;
        this.localisation = localisation;

        this.date_acquisition = date;
        this.valeur = valeur;
        this.etat = etat;
    }
}
