package sn.iage.isi.employeejavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sn.iage.isi.employeejavafx.models.User;
import sn.iage.isi.employeejavafx.services.UserService;

import java.io.IOException;

/**
 * Point d'entrée principal de l'application JavaFX Employee Manager.
 * Lance la fenêtre de connexion au démarrage.
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                App.class.getResource("/sn/iage/isi/employeejavafx/views/login.fxml")
        );
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(
                App.class.getResource("/sn/iage/isi/employeejavafx/css/style.css").toExternalForm()
        );

        Image logo = new Image(getClass().getResourceAsStream("/images/logo_empl.png"));
        primaryStage.getIcons().add(logo);

        primaryStage.setTitle("Employee Manager — Connexion");
        primaryStage.setScene(scene);
//        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        //Super User
        UserService userService = new UserService();
        User user = new User();

        user.setUsername("admin");
        user.setPassword("admin");
        user.setRole("admin");

        userService.create(user);
        launch(args);
    }
}
