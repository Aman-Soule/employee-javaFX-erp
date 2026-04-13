package sn.iage.isi.employeejavafx.models;

import javafx.beans.property.*;
import lombok.NoArgsConstructor;

/**
 * Modèle représentant un employé.
 * Utilise les {@link javafx.beans.property.Property} JavaFX pour
 * la liaison bidirectionnelle avec les TableView et les contrôles FXML.
 *
 * Lombok génère automatiquement le constructeur sans argument.
 */
@NoArgsConstructor
public class Employee {

    private final IntegerProperty id         = new SimpleIntegerProperty();
    private final StringProperty  nom        = new SimpleStringProperty();
    private final StringProperty  prenom     = new SimpleStringProperty();
    private final StringProperty  email      = new SimpleStringProperty();
    private final StringProperty  telephone  = new SimpleStringProperty();
    private final StringProperty  poste      = new SimpleStringProperty();
    private final DoubleProperty  salaire    = new SimpleDoubleProperty();

    // ── Constructeur complet ───────────────────────────────────────────────────
    public Employee(int id, String nom, String prenom, String email,
                    String telephone, String poste, double salaire) {
        this.id.set(id);
        this.nom.set(nom);
        this.prenom.set(prenom);
        this.email.set(email);
        this.telephone.set(telephone);
        this.poste.set(poste);
        this.salaire.set(salaire);
    }

    // ── Constructeur sans id (création) ───────────────────────────────────────
    public Employee(String nom, String prenom, String email,
                    String telephone, String poste, double salaire) {
        this(0, nom, prenom, email, telephone, poste, salaire);
    }

    // ── Getters / Setters classiques ───────────────────────────────────────────
    public int    getId()        { return id.get(); }
    public String getNom()       { return nom.get(); }
    public String getPrenom()    { return prenom.get(); }
    public String getEmail()     { return email.get(); }
    public String getTelephone() { return telephone.get(); }
    public String getPoste()     { return poste.get(); }
    public double getSalaire()   { return salaire.get(); }

    public void setId(int id)            { this.id.set(id); }
    public void setNom(String nom)       { this.nom.set(nom); }
    public void setPrenom(String prenom) { this.prenom.set(prenom); }
    public void setEmail(String email)   { this.email.set(email); }
    public void setTelephone(String t)   { this.telephone.set(t); }
    public void setPoste(String poste)   { this.poste.set(poste); }
    public void setSalaire(double s)     { this.salaire.set(s); }

    // ── Property accessors (requis pour TableView binding) ─────────────────────
    public IntegerProperty idProperty()        { return id; }
    public StringProperty  nomProperty()       { return nom; }
    public StringProperty  prenomProperty()    { return prenom; }
    public StringProperty  emailProperty()     { return email; }
    public StringProperty  telephoneProperty() { return telephone; }
    public StringProperty  posteProperty()     { return poste; }
    public DoubleProperty  salaireProperty()   { return salaire; }

    @Override
    public String toString() {
        return prenom.get() + " " + nom.get() + " (" + poste.get() + ")";
    }
}
