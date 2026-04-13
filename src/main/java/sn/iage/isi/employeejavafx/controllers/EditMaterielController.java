package sn.iage.isi.employeejavafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sn.iage.isi.employeejavafx.models.Categorie;
import sn.iage.isi.employeejavafx.models.Materiel;
import sn.iage.isi.employeejavafx.services.CategorieService;
import sn.iage.isi.employeejavafx.services.MaterielService;
import sn.iage.isi.employeejavafx.tools.AlertHelper;
import sn.iage.isi.employeejavafx.tools.ValidationHelper;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditMaterielController implements Initializable {

    // Champs du formulaire
    @FXML private TextField nomField;
    @FXML private TextField descriptionField;
    @FXML private TextField quantiteField;
    @FXML private TextField valeurField;
    @FXML private TextField localisationField;
    @FXML private ChoiceBox<String> etatChoiceBox;
    @FXML private ChoiceBox<Categorie> categorieChoiceBox;
    @FXML private Label idLabel;

    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Label formErrorLabel;

    // Données & Services
    private Materiel currentMateriel;
    private final MaterielService materielService = new MaterielService();
    private final CategorieService categorieService = new CategorieService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        formErrorLabel.setVisible(false);
        etatChoiceBox.getItems().addAll("Disponible", "Indisponible");

        // Charger les catégories
        List<Categorie> categories = categorieService.findAllCategories();
        categorieChoiceBox.getItems().addAll(categories);

        // Afficher le nom dans la ChoiceBox
        categorieChoiceBox.setConverter(new javafx.util.StringConverter<Categorie>() {
            @Override
            public String toString(Categorie categorie) {
                return (categorie != null) ? categorie.getNom() : "";
            }
            @Override
            public Categorie fromString(String string) {
                return null;
            }
        });
    }

    // Injection du matériel à modifier
    public void setMateriel(Materiel materiel) {
        this.currentMateriel = materiel;
        populateForm(materiel);
    }

    private void populateForm(Materiel materiel) {
        idLabel.setText("ID : " + materiel.getId());
        nomField.setText(materiel.getNom());
        descriptionField.setText(materiel.getDescription());
        quantiteField.setText(String.valueOf(materiel.getQuantite()));
        valeurField.setText(String.valueOf(materiel.getValeur()));
        localisationField.setText(materiel.getLocalisation());
        etatChoiceBox.setValue(materiel.getEtat());
        categorieChoiceBox.setValue(materiel.getCategorie());
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (currentMateriel == null) return;

        String nom          = nomField.getText().trim();
        String description  = descriptionField.getText().trim();
        String quantite     = quantiteField.getText().trim();
        String valeur       = valeurField.getText().trim();
        String localisation = localisationField.getText().trim();
        String etat         = etatChoiceBox.getValue();
        Categorie categorie = categorieChoiceBox.getValue();

        String erreur = ValidationHelper.validateMaterielForm(
                nom, description, quantite, valeur, localisation, etat
        );
        if (erreur != null) {
            formErrorLabel.setText(erreur);
            formErrorLabel.setVisible(true);
            return;
        }

        currentMateriel.setNom(nom);
        currentMateriel.setDescription(description);
        currentMateriel.setQuantite(Integer.parseInt(quantite));
        currentMateriel.setValeur(Double.parseDouble(valeur));
        currentMateriel.setLocalisation(localisation);
        currentMateriel.setEtat(etat);
        currentMateriel.setCategorie(categorie);

        boolean success = materielService.updateMateriel(currentMateriel);
        if (success) {
            AlertHelper.showSuccess("Modification réussie",
                    "Le matériel a été mis à jour avec succès.");
            closeWindow();
        } else {
            AlertHelper.showError("Erreur", "La modification a échoué.");
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }
}
