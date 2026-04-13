package sn.iage.isi.employeejavafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sn.iage.isi.employeejavafx.models.Categorie;
import sn.iage.isi.employeejavafx.models.Employee;
import sn.iage.isi.employeejavafx.models.Materiel;
import sn.iage.isi.employeejavafx.services.CategorieService;
import sn.iage.isi.employeejavafx.services.EmployeeService;
import sn.iage.isi.employeejavafx.services.MaterielService;
import sn.iage.isi.employeejavafx.tools.AlertHelper;
import sn.iage.isi.employeejavafx.tools.ValidationHelper;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AddMaterielController implements Initializable {
    public Button cancelButton;
    public Button saveButton;
    public TextField nomField;
    public TextField descriptionField;
    public TextField quantiteField;
    public TextField valeurField;
    public TextField localisationField;
    public ChoiceBox etatChoiceBox;
    public Label formErrorLabel;

    // ── Service ───────────────────────────────────────────────────────────────
    private final MaterielService materielService = new MaterielService();

    private final CategorieService categorieService = new CategorieService();

    @FXML
    private ChoiceBox<Categorie> categorieChoiceBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCategories();
        formErrorLabel.setVisible(false);
    }

    public void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private void loadCategories() {
        List<Categorie> categories = categorieService.findAllCategories();
        categorieChoiceBox.getItems().addAll(categories);

        // Pour afficher le nom au lieu de l'objet complet
        categorieChoiceBox.setConverter(new StringConverter<Categorie>() {
            @Override
            public String toString(Categorie categorie) {
                return (categorie != null) ? categorie.getNom() : "-- Selectionner une categorie --";
            }

            @Override
            public Categorie fromString(String string) {
                return null; // inutile ici
            }
        });
    }




    public void handleSave(ActionEvent event) {
        String nom       = nomField.getText().trim();
        String description    = descriptionField.getText().trim();
        String quantite     = quantiteField.getText().trim();
        String valeur = valeurField.getText().trim();
        String localisation     = localisationField.getText().trim();
        String etat   = (String) etatChoiceBox.getValue();
        Categorie categorie = categorieChoiceBox.getValue();
        Date date = new Date();

        // Validation
        String erreur = ValidationHelper.validateMaterielForm(
                nom, description, quantite, valeur, localisation, etat
        );
        if (erreur != null) {
            formErrorLabel.setText(erreur);
            formErrorLabel.setVisible(true);
            return;
        }

        Materiel materiel = new Materiel(
                nom, description, Integer.parseInt(quantite), localisation, date, Double.parseDouble(valeur), etat,categorie
        );

        boolean success = materielService.createMateriel(materiel);
        if (success) {
            AlertHelper.showSuccess("Enregistrement réussi",
                    "Le materiel de nom " + nom + " a été ajouté.");
            closeWindow();
        } else {
            AlertHelper.showError("Erreur d'enregistrement",
                    "L'ajout du materiel a échoué. Vérifiez les données.");
        }
    }



    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
