package sn.iage.isi.employeejavafx.models;

import javafx.beans.property.*;
import lombok.NoArgsConstructor;

/**
 * Modèle représentant un utilisateur de l'application (authentification).
 * Distinct du modèle {@link Employee} qui représente les données RH.
 */

@NoArgsConstructor
public class User {

    private final IntegerProperty id       = new SimpleIntegerProperty();
    private final StringProperty  username = new SimpleStringProperty();
    private final StringProperty  password = new SimpleStringProperty();
    private final StringProperty  role     = new SimpleStringProperty("EMPLOYE");

    // ── Constructeur complet ───────────────────────────────────────────────────
    public User(int id, String username, String password, String role) {
        this.id.set(id);
        this.username.set(username);
        this.password.set(password);
        this.role.set(role);
    }

    // ── Constructeur création (sans id) ───────────────────────────────────────
    public User(String username, String password, String role) {
        this(0, username, password, role);
    }

    // ── Getters / Setters ──────────────────────────────────────────────────────
    public int    getId()       { return id.get(); }
    public String getUsername() { return username.get(); }
    public String getPassword() { return password.get(); }
    public String getRole()     { return role.get(); }

    public void setId(int id)             { this.id.set(id); }
    public void setUsername(String u)     { this.username.set(u); }
    public void setPassword(String p)     { this.password.set(p); }
    public void setRole(String r)         { this.role.set(r); }

    // ── Properties ────────────────────────────────────────────────────────────
    public IntegerProperty idProperty()       { return id; }
    public StringProperty  usernameProperty() { return username; }
    public StringProperty  passwordProperty() { return password; }
    public StringProperty  roleProperty()     { return role; }

    @Override
    public String toString() {
        return username.get() + " [" + role.get() + "]";
    }
}
