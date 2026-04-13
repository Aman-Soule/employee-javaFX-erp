package sn.iage.isi.employeejavafx.models;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

/**
 * Modèle représentant un utilisateur de l'application (authentification).
 * Distinct du modèle {@link Employee} qui représente les données RH.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Facture {

    private int id;
    private String file_name;
    private LocalDate date_generation;
    private Double montant;
    private Employee employee;

    // ── Constructeur complet ───────────────────────────────────────────────────
    public Facture(int id, String file_name, LocalDate date_generation, Double montant, Employee employee) {
        this.id = id;
        this.file_name = file_name;
        this.date_generation = date_generation;
        this.montant = montant;
        this.employee = employee;

    }

    // ── Constructeur création (sans id) ───────────────────────────────────────
    public Facture(String file_name, LocalDate date_generation, Double montant, Employee employee)
    {
        this.file_name = file_name;
        this.date_generation = date_generation;
        this.montant = montant;
        this.employee = employee;
    }


}