package sn.iage.isi.employeejavafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sn.iage.isi.employeejavafx.models.Employee;
import sn.iage.isi.employeejavafx.services.EmployeeService;
import sn.iage.isi.employeejavafx.tools.AlertHelper;
import sn.iage.isi.employeejavafx.tools.ValidationHelper;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller de la vue {@code addEmployee.fxml}.
 *
 * <p>Gère la saisie et la validation du formulaire d'ajout d'un employé,
 * puis déclenche la persistance via {@link EmployeeService}.</p>
 */
public class AddEmployeeController implements Initializable {

    // ── Champs du formulaire ───────────────────────────────────────────────────
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField telephoneField;
    @FXML private TextField posteField;
    @FXML private TextField salaireField;

    // ── Boutons ────────────────────────────────────────────────────────────────
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    // ── Label d'erreur inline ──────────────────────────────────────────────────
    @FXML private Label formErrorLabel;

    // ── Service ───────────────────────────────────────────────────────────────
    private final EmployeeService employeeService = new EmployeeService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        formErrorLabel.setVisible(false);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  ACTIONS FXML
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Valide le formulaire et enregistre le nouvel employé en base.
     * Appelé par {@code onAction="#handleSave"}.
     */
    @FXML
    private void handleSave(ActionEvent event) {
        String nom       = nomField.getText().trim();
        String prenom    = prenomField.getText().trim();
        String email     = emailField.getText().trim();
        String telephone = telephoneField.getText().trim();
        String poste     = posteField.getText().trim();
        String salaire   = salaireField.getText().trim();

        // Validation
        String erreur = ValidationHelper.validateEmployeeForm(
                nom, prenom, email, telephone, poste, salaire
        );
        if (erreur != null) {
            formErrorLabel.setText(erreur);
            formErrorLabel.setVisible(true);
            return;
        }

        Employee employee = new Employee(
                nom, prenom, email, telephone, poste,
                Double.parseDouble(salaire)
        );

        boolean success = employeeService.create(employee);
        if (success) {
            AlertHelper.showSuccess("Enregistrement réussi",
                    "L'employé " + prenom + " " + nom + " a été ajouté.");
            closeWindow();
        } else {
            AlertHelper.showError("Erreur d'enregistrement",
                    "L'ajout de l'employé a échoué. Vérifiez les données.");
        }
    }

    /** Ferme la fenêtre modale sans enregistrer. */
    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  HELPERS
    // ═══════════════════════════════════════════════════════════════════════════

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
