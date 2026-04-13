package sn.iage.isi.employeejavafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sn.iage.isi.employeejavafx.models.User;
import sn.iage.isi.employeejavafx.services.UserService;
import sn.iage.isi.employeejavafx.tools.AlertHelper;
import sn.iage.isi.employeejavafx.tools.SessionManager;
import sn.iage.isi.employeejavafx.tools.StageHelper;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller de la vue {@code login.fxml}.
 *
 * <p>Gère la saisie des identifiants, l'authentification via {@link UserService},
 * la mise en session via {@link SessionManager}, et la navigation vers
 * {@code accueil.fxml} en cas de succès.</p>
 */
public class LoginController implements Initializable {

    // ── Champs FXML ────────────────────────────────────────────────────────────
    @FXML private TextField     usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button        loginButton;
    @FXML private Label         errorLabel;
    @FXML private ProgressIndicator loadingIndicator;

    // ── Services ───────────────────────────────────────────────────────────────
    private final UserService userService = new UserService();

    // ── Chemins FXML ───────────────────────────────────────────────────────────
    private static final String ACCUEIL_FXML =
            "/sn/iage/isi/employeejavafx/views/accueil.fxml";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorLabel.setVisible(false);
        loadingIndicator.setVisible(false);

        // Permettre la connexion avec la touche Entrée
        passwordField.setOnAction(this::login);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  ACTIONS FXML
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Tente d'authentifier l'utilisateur avec les identifiants saisis.
     * Appelé par {@code onAction="#login"} dans login.fxml.
     */
    @FXML
    private void login(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // Validation de la saisie
        if (username.isEmpty() || password.isEmpty()) {
            showError("Veuillez renseigner tous les champs.");
            return;
        }

        // Indicateur de chargement
        setLoading(true);

        Optional<User> result = userService.authenticate(username, password);

        setLoading(false);

        if (result.isPresent()) {
            // Stocker l'utilisateur en session
            SessionManager.getInstance().setCurrentUser(result.get());

            // Naviguer vers l'accueil
            Stage stage = (Stage) loginButton.getScene().getWindow();


            StageHelper.navigateTo(stage, ACCUEIL_FXML, "Employee Manager — Accueil");

        } else {
            showError("Nom d'utilisateur ou mot de passe incorrect.");
            passwordField.clear();
            passwordField.requestFocus();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  MÉTHODES PRIVÉES
    // ═══════════════════════════════════════════════════════════════════════════

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void setLoading(boolean loading) {
        loadingIndicator.setVisible(loading);
        loginButton.setDisable(loading);
    }
}
