package sn.iage.isi.employeejavafx.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sn.iage.isi.employeejavafx.models.Materiel;
import sn.iage.isi.employeejavafx.models.User;
import sn.iage.isi.employeejavafx.services.MaterielService;
import sn.iage.isi.employeejavafx.services.UserService;
import sn.iage.isi.employeejavafx.tools.AlertHelper;
import sn.iage.isi.employeejavafx.tools.StageHelper;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class UtilisateurController implements Initializable {


    public Button addUButton;
    public Button updateUButton;
    public Button deleteUButton;
    @FXML
    private TableColumn<Materiel, String> colLocalisation;
    @FXML private TableColumn<Materiel, String> colEtat;
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> colId;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colRole;


    private static final String BASE             = "/sn/iage/isi/employeejavafx/views/";
    private static final String ADD_USER_FXML = BASE + "addUser.fxml";
    private static final String EDIT_USER_FXML = BASE + "editUser.fxml";

    @FXML private TextField searchField;

    private UserService userService = new UserService();
    private ObservableList<User> userList;

    public void getData(MouseEvent mouseEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
         loadUtilisateur();
         setUpColumns();
    }

    private void loadUtilisateur() {
        userList = FXCollections.observableArrayList(userService.findAll());
        userTable.setItems(userList);
    }
    private void setUpColumns() {
        List<User> users = userService.findAll();
        ObservableList<User> items = FXCollections.observableArrayList(users);
        userTable.setItems(items);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        userTable.setItems(userList);
    }

    public void handleAdd(ActionEvent event) {
        Stage owner = (Stage) addUButton.getScene().getWindow();
        StageHelper.openModal(owner, ADD_USER_FXML, "Créer un utilisateur");
    }

    public void handleUpdate(ActionEvent event) {
    }

    public void handleDelete(ActionEvent event) {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showWarning("Sélection requise",
                    "Veuillez sélectionner un utilisateur à supprimer.");
            return;
        }
        boolean confirmed = AlertHelper.showConfirmation(
                "Supprimer le materiel",
                "Êtes-vous sûr de vouloir supprimer " +
                        selected.getUsername() + " " + selected.getRole() + " ?"
        );
        if (confirmed) {
            boolean success = userService.delete(selected.getId());
            if (success) {
                AlertHelper.showSuccess("Suppression réussie",
                        "Le materiel a été supprimé avec succès.");
                loadUtilisateur();
            } else {
                AlertHelper.showError("Erreur",
                        "La suppression a échoué. Veuillez réessayer.");
            }
        }
    }

    public void handleRefresh(ActionEvent event) {
    }
}
