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
 * Controller de la vue {@code editEmployee.fxml}.
 *
 * <p>Reçoit un objet {@link Employee} via {@link #setEmployee(Employee)},
 * pré-remplit le formulaire, et enregistre les modifications en base.</p>
 *
 * <p>Le controller est initialisé depuis {@link DashboardController} via
 * {@code StageHelper.openModal(..., ctrl -> ctrl.setEmployee(selected))}.</p>
 */
public class EditEmployeeController implements Initializable {

    // ── Champs du formulaire ───────────────────────────────────────────────────
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField telephoneField;
    @FXML private TextField posteField;
    @FXML private TextField salaireField;
    @FXML private Label     idLabel;       // Affichage read-only de l'ID

    // ── Boutons ────────────────────────────────────────────────────────────────
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    // ── Erreur inline ──────────────────────────────────────────────────────────
    @FXML private Label formErrorLabel;

    // ── Données & Service ─────────────────────────────────────────────────────
    private Employee currentEmployee;
    private final EmployeeService employeeService = new EmployeeService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        formErrorLabel.setVisible(false);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  INJECTION DE L'EMPLOYÉ (appelé par DashboardController)
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Injecte l'employé à modifier et pré-remplit les champs du formulaire.
     * Cette méthode DOIT être appelée avant que la fenêtre ne soit affichée.
     *
     * @param employee l'employé sélectionné dans le dashboard
     */
    public void setEmployee(Employee employee) {
        this.currentEmployee = employee;
        populateForm(employee);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  ACTIONS FXML
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Valide et enregistre les modifications de l'employé.
     * Appelé par {@code onAction="#handleSave"}.
     */
    @FXML
    private void handleSave(ActionEvent event) {
        if (currentEmployee == null) return;

        String nom       = nomField.getText().trim();
        String prenom    = prenomField.getText().trim();
        String email     = emailField.getText().trim();
        String telephone = telephoneField.getText().trim();
        String poste     = posteField.getText().trim();
        String salaire   = salaireField.getText().trim();

        String erreur = ValidationHelper.validateEmployeeForm(
                nom, prenom, email, telephone, poste, salaire
        );
        if (erreur != null) {
            formErrorLabel.setText(erreur);
            formErrorLabel.setVisible(true);
            return;
        }

        currentEmployee.setNom(nom);
        currentEmployee.setPrenom(prenom);
        currentEmployee.setEmail(email);
        currentEmployee.setTelephone(telephone);
        currentEmployee.setPoste(poste);
        currentEmployee.setSalaire(Double.parseDouble(salaire));

        boolean success = employeeService.update(currentEmployee);
        if (success) {
            AlertHelper.showSuccess("Modification réussie",
                    "L'employé a été mis à jour avec succès.");
            closeWindow();
        } else {
            AlertHelper.showError("Erreur", "La modification a échoué.");
        }
    }

    /** Ferme la fenêtre sans enregistrer. */
    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  HELPERS
    // ═══════════════════════════════════════════════════════════════════════════

    /** Remplit les champs du formulaire avec les valeurs de l'employé. */
    private void populateForm(Employee employee) {
        idLabel.setText("ID : " + employee.getId());
        nomField.setText(employee.getNom());
        prenomField.setText(employee.getPrenom());
        emailField.setText(employee.getEmail());
        telephoneField.setText(employee.getTelephone());
        posteField.setText(employee.getPoste());
        salaireField.setText(String.valueOf(employee.getSalaire()));
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
