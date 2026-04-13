package sn.iage.isi.employeejavafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sn.iage.isi.employeejavafx.models.User;
import sn.iage.isi.employeejavafx.services.UserService;
import sn.iage.isi.employeejavafx.tools.AlertHelper;
import sn.iage.isi.employeejavafx.tools.ValidationHelper;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller de la vue {@code addUser.fxml}.
 *
 * <p>Formulaire de création d'un nouvel utilisateur (espace admin).
 * Correspond à l'ancienne classe AddController de votre projet original.</p>
 */
public class AddController implements Initializable {

    // ── Champs du formulaire ───────────────────────────────────────────────────
    @FXML private TextField     usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ComboBox<String> roleComboBox;

    // ── Boutons ────────────────────────────────────────────────────────────────
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    // ── Erreur ────────────────────────────────────────────────────────────────
    @FXML private Label errorLabel;

    // ── Service ───────────────────────────────────────────────────────────────
    private final UserService userService = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorLabel.setVisible(false);
        roleComboBox.getItems().addAll("EMPLOYE", "ADMIN", "RH");
        roleComboBox.setValue("EMPLOYE");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  ACTIONS FXML
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Valide et enregistre le nouvel utilisateur.
     * Appelé par {@code onAction="#handleSave"} dans addUser.fxml.
     */
    @FXML
    private void handleSave(ActionEvent event) {
        String username        = usernameField.getText().trim();
        String password        = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role            = roleComboBox.getValue();

        if (!ValidationHelper.isNotEmpty(username)) {
            showError("Le nom d'utilisateur est obligatoire.");
            return;
        }
        if (!ValidationHelper.isValidPassword(password)) {
            showError("Le mot de passe doit contenir au moins 6 caractères.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showError("Les mots de passe ne correspondent pas.");
            return;
        }

        User user = new User(username, password, role);
        boolean success = userService.create(user);

        if (success) {
            AlertHelper.showSuccess("Utilisateur créé",
                    "L'utilisateur " + username + " a été créé avec le rôle " + role + ".");
            closeWindow();
        } else {
            showError("Échec de la création. Le nom d'utilisateur existe peut-être déjà.");
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

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
