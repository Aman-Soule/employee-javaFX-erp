package sn.iage.isi.employeejavafx.tools;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sn.iage.isi.employeejavafx.controllers.InfosFactureController;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

/**
 * Utilitaire de navigation entre les vues FXML.
 *
 * <p>Centralise le chargement des FXML, la création de {@link Stage},
 * et la transmission de données vers les controllers cibles.</p>
 */
public class StageHelper {

    // Chemin unique vers le CSS global — ne jamais le dupliquer ailleurs
    private static final String CSS_PATH =
            "/sn/iage/isi/employeejavafx/css/style.css";

    private StageHelper() { /* Classe utilitaire */ }

    /**
     * Charge un FXML et remplace la scène de la fenêtre courante.
     *
     * @param currentStage  la fenêtre courante à réutiliser
     * @param fxmlPath      chemin du FXML (ex: "/sn/.../views/dashboard.fxml")
     * @param titre         nouveau titre de la fenêtre
     */
    public static void navigateTo(Stage currentStage, String fxmlPath, String titre) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    StageHelper.class.getResource(fxmlPath)
            );
            Parent root = loader.load();
            Scene scene = new Scene(root);
            applyGlobalCss(scene);              // ← CSS appliqué ici
            currentStage.setTitle(titre);
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            AlertHelper.showError("Erreur de navigation",
                    "Impossible de charger la vue : " + fxmlPath + "\n" + e.getMessage());
        }
    }

    /**
     * Charge un FXML, transmet des données au controller, puis remplace la scène.
     *
     * @param <C>           type du controller cible
     * @param currentStage  la fenêtre courante
     * @param fxmlPath      chemin du FXML
     * @param titre         nouveau titre
     * @param controllerConsumer lambda pour initialiser le controller
     *                           (ex: {@code ctrl -> ctrl.setUser(user)})
     */
    public static <C> void navigateTo(Stage currentStage, String fxmlPath,
                                      String titre, Consumer<C> controllerConsumer) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    StageHelper.class.getResource(fxmlPath)
            );
            Parent root = loader.load();
            C controller = loader.getController();
            controllerConsumer.accept(controller);
            Scene scene = new Scene(root);
            applyGlobalCss(scene);              // ← CSS appliqué ici
            currentStage.setTitle(titre);
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            AlertHelper.showError("Erreur de navigation",
                    "Impossible de charger la vue : " + fxmlPath + "\n" + e.getMessage());
        }
    }

    /**
     * Ouvre un FXML dans une nouvelle fenêtre modale (bloque la fenêtre parent).
     *
     * @param parentStage fenêtre parente
     * @param fxmlPath    chemin du FXML
     * @param titre       titre de la nouvelle fenêtre
     * @return
     */
    public static InfosFactureController openModal(Stage parentStage, String fxmlPath, String titre) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    StageHelper.class.getResource(fxmlPath)
            );
            Parent root = loader.load();
            Scene scene = new Scene(root);
            applyGlobalCss(scene);              // ← CSS appliqué ici
            Stage modal = new Stage();
            modal.initOwner(parentStage);
            modal.initModality(Modality.WINDOW_MODAL);
            modal.setTitle(titre);
            modal.setScene(scene);
            modal.showAndWait();
        } catch (IOException e) {
            AlertHelper.showError("Erreur", "Impossible d'ouvrir : " + fxmlPath + "\n" + e.getMessage());
        }
        return null;
    }

    /**
     * Ouvre un FXML dans une nouvelle fenêtre modale avec transmission de données.
     *
     * @param <C>                type du controller cible
     * @param parentStage        fenêtre parente
     * @param fxmlPath           chemin du FXML
     * @param titre              titre de la nouvelle fenêtre
     * @param controllerConsumer lambda pour initialiser le controller
     */
    public static <C> void openModal(Stage parentStage, String fxmlPath,
                                     String titre, Consumer<C> controllerConsumer) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    StageHelper.class.getResource(fxmlPath)
            );
            Parent root = loader.load();
            C controller = loader.getController();
            controllerConsumer.accept(controller);
            Scene scene = new Scene(root);
            applyGlobalCss(scene);              // ← CSS appliqué ici
            Stage modal = new Stage();
            modal.initOwner(parentStage);
            modal.initModality(Modality.WINDOW_MODAL);
            modal.setTitle(titre);
            modal.setScene(scene);
            modal.showAndWait();
        } catch (IOException e) {
            AlertHelper.showError("Erreur", "Impossible d'ouvrir : " + fxmlPath + "\n" + e.getMessage());
        }
    }

    /**
     * Ferme la fenêtre fournie.
     *
     * @param stage la fenêtre à fermer
     */
    public static void close(Stage stage) {
        if (stage != null) stage.close();
    }

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Applique le CSS global sur une {@link Scene}.
     * Appelé automatiquement dans toutes les méthodes ci-dessus.
     * Exposé en {@code public} pour {@code App.java} (première scène).
     */
    public static void applyGlobalCss(Scene scene) {
        URL css = StageHelper.class.getResource(CSS_PATH);
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        } else {
            System.err.println("⚠️  CSS introuvable : " + CSS_PATH);
        }
    }

    /**
     * Applique le CSS global sur un nœud {@link Parent} (vue injectée dans un conteneur).
     * Utilisé dans {@code AccueilController.loadView()} pour les vues chargées
     * dynamiquement dans le {@code contentPane}, avant qu'elles soient rattachées
     * à la scène parente.
     */
    public static void applyGlobalCss(Parent node) {
        URL css = StageHelper.class.getResource(CSS_PATH);
        if (css != null) {
            String cssPath = css.toExternalForm();
            if (!node.getStylesheets().contains(cssPath)) {
                node.getStylesheets().add(cssPath);
            }
        } else {
            System.err.println("⚠️  CSS introuvable : " + CSS_PATH);
        }
    }
}