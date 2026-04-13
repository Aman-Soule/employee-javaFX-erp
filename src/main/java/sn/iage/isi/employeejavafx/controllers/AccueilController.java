package sn.iage.isi.employeejavafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sn.iage.isi.employeejavafx.models.User;
import sn.iage.isi.employeejavafx.tools.AlertHelper;
import sn.iage.isi.employeejavafx.tools.SessionManager;
import sn.iage.isi.employeejavafx.tools.StageHelper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller de la vue principale {@code accueil.fxml}.
 *
 * <p>Joue le rôle de "shell" : la barre de menu en haut permet de charger
 * différentes vues dans la zone centrale {@link #contentPane} sans fermer
 * la fenêtre principale.</p>
 */
public class AccueilController implements Initializable {


    // ── Conteneur central — les vues y sont injectées dynamiquement ────────────
    @FXML private AnchorPane contentPane;

    @FXML private Menu userMenu;
    // ── Chemins des vues ────────────────────────────────────────────────────────
    private static final String BASE           = "/sn/iage/isi/employeejavafx/views/";
    private static final String DASHBOARD_FXML = BASE + "dashboard.fxml";
    private static final String RESSOURCES_FXML = BASE + "ressources.fxml";
    private static final String MATERIELS_FXML = BASE + "materiels.fxml";
    private static final String PROFILE_FXML   = BASE + "profile.fxml";
    private static final String LOGIN_FXML     = BASE + "login.fxml";
    private static final String FACTURE_FXML = BASE + "facture.fxml";
    private static final String UTILISATEUR_FXML = BASE + "utilisateurs.fxml";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Charger le dashboard par défaut au démarrage
        loadView(RESSOURCES_FXML);

        userMenu.setVisible(false);

        // Récupérer l'utilisateur en session
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null && "ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            userMenu.setVisible(true);
        }
    }



    // ═════════════════════════════════════════════════════════════════════════
    //  ACTIONS FXML (Menu items)
    // ═══════════════════════════════════════════════════════════════════════════

    /** Charge le dashboard dans la zone centrale. */
    @FXML
    private void dashboard(ActionEvent event) {
        loadView(DASHBOARD_FXML);
    }

    @FXML
    private void ressources(ActionEvent event) {
        loadView(RESSOURCES_FXML);
    }

    @FXML
    public void materiels(ActionEvent event) {
        loadView(MATERIELS_FXML);
    }

    @FXML
    public void factures(ActionEvent event) {
        loadView(FACTURE_FXML);
    }
    @FXML
    public void utilisateurs(ActionEvent event) {
        loadView(UTILISATEUR_FXML);
    }
    /** Charge la vue profil dans la zone centrale. */
    @FXML
    private void profile(ActionEvent event) {
        loadView(PROFILE_FXML);
    }

    /** Déconnecte l'utilisateur et retourne sur l'écran de connexion. */
    @FXML
    private void logout(ActionEvent event) {
        boolean confirmed = AlertHelper.showConfirmation(
                "Déconnexion",
                "Voulez-vous vraiment vous déconnecter ?"
        );
        if (confirmed) {
            SessionManager.getInstance().clear();
            Stage stage = (Stage) contentPane.getScene().getWindow();
            StageHelper.navigateTo(stage, LOGIN_FXML, "Employee Manager — Connexion");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  MÉTHODE CENTRALE : injection de vue dans contentPane
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Charge un FXML et l'affiche dans {@link #contentPane}.
     * Le nœud chargé est étiré pour remplir tout l'espace disponible.
     *
     * @param fxmlPath chemin absolu du fichier FXML à charger
     */
    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();

            // Remplir tout le contentPane
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            AlertHelper.showError("Erreur de navigation",
                    "Impossible de charger la vue : " + fxmlPath + "\n" + e.getMessage());
        }
    }


    public void quitApp(ActionEvent event) {
        javafx.application.Platform.exit();
        System.exit(0);
    }
}
