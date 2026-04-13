package sn.iage.isi.employeejavafx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sn.iage.isi.employeejavafx.models.User;
import sn.iage.isi.employeejavafx.services.UserService;
import sn.iage.isi.employeejavafx.tools.AlertHelper;
import sn.iage.isi.employeejavafx.tools.SessionManager;
import sn.iage.isi.employeejavafx.tools.ValidationHelper;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller de la vue {@code profile.fxml}.
 *
 * <p>Affiche les informations de l'utilisateur connecté et permet
 * la modification de son mot de passe.</p>
 */
public class ProfileController implements Initializable {

    // ── Affichage des infos ────────────────────────────────────────────────────
    @FXML private Label usernameLabel;
    @FXML private Label roleLabel;

    // ── Changement de mot de passe ─────────────────────────────────────────────
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button        changePasswordButton;
    @FXML private Label         passwordErrorLabel;

    // ── Service ───────────────────────────────────────────────────────────────
    private final UserService userService = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        passwordErrorLabel.setVisible(false);
        loadCurrentUser();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  ACTIONS FXML
    // ═══════════════════════════════════════════════════════════════════════════

    /** Valide et met à jour le mot de passe de l'utilisateur connecté. */
    @FXML
    private void handleChangePassword() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) return;

        String currentPwd = currentPasswordField.getText();
        String newPwd     = newPasswordField.getText();
        String confirmPwd = confirmPasswordField.getText();

        // Vérification du mot de passe actuel
        if (userService.authenticate(currentUser.getUsername(), currentPwd).isEmpty()) {
            showPasswordError("Mot de passe actuel incorrect.");
            return;
        }
        if (!ValidationHelper.isValidPassword(newPwd)) {
            showPasswordError("Le nouveau mot de passe doit contenir au moins 6 caractères.");
            return;
        }
        if (!newPwd.equals(confirmPwd)) {
            showPasswordError("Les nouveaux mots de passe ne correspondent pas.");
            return;
        }

        boolean success = userService.updatePassword(currentUser.getId(), newPwd);
        if (success) {
            AlertHelper.showSuccess("Succès", "Mot de passe modifié avec succès.");
            clearPasswordFields();
        } else {
            AlertHelper.showError("Erreur", "La modification du mot de passe a échoué.");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  HELPERS
    // ═══════════════════════════════════════════════════════════════════════════

    private void loadCurrentUser() {
        User user = SessionManager.getInstance().getCurrentUser();
        if (user != null) {
            usernameLabel.setText(user.getUsername());
            roleLabel.setText(user.getRole());
        }
    }

    private void showPasswordError(String message) {
        passwordErrorLabel.setText(message);
        passwordErrorLabel.setVisible(true);
    }

    private void clearPasswordFields() {
        currentPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
        passwordErrorLabel.setVisible(false);
    }
}
