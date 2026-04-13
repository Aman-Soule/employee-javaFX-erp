package sn.iage.isi.employeejavafx.tools;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Utilitaire centralisé pour afficher des boîtes de dialogue JavaFX.
 *
 * <p>Évite la répétition de code dans les controllers.</p>
 */
public class AlertHelper {

    private AlertHelper() { /* Classe utilitaire — pas d'instanciation */ }

    /**
     * Affiche une alerte de succès.
     *
     * @param titre   titre de la fenêtre
     * @param message message à afficher
     */
    public static void showSuccess(String titre, String message) {
        showAlert(AlertType.INFORMATION, titre, message);
    }

    /**
     * Affiche une alerte d'erreur.
     *
     * @param titre   titre de la fenêtre
     * @param message message à afficher
     */
    public static void showError(String titre, String message) {
        showAlert(AlertType.ERROR, titre, message);
    }

    /**
     * Affiche une alerte d'avertissement.
     *
     * @param titre   titre de la fenêtre
     * @param message message à afficher
     */
    public static void showWarning(String titre, String message) {
        showAlert(AlertType.WARNING, titre, message);
    }

    /**
     * Affiche une boîte de confirmation et retourne {@code true} si l'utilisateur
     * clique sur "OK".
     *
     * @param titre   titre de la fenêtre
     * @param message question posée à l'utilisateur
     * @return {@code true} si confirmé
     */
    public static boolean showConfirmation(String titre, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    // ── Méthode interne ────────────────────────────────────────────────────────
    private static void showAlert(AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
