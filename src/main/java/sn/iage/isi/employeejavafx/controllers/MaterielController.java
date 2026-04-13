package sn.iage.isi.employeejavafx.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import sn.iage.isi.employeejavafx.models.Employee;
import sn.iage.isi.employeejavafx.models.Materiel;
import sn.iage.isi.employeejavafx.models.Categorie;
import sn.iage.isi.employeejavafx.services.MaterielService;
import sn.iage.isi.employeejavafx.tools.AlertHelper;
import sn.iage.isi.employeejavafx.tools.StageHelper;

import java.util.List;
import java.util.Optional;

public class MaterielController {

    public Button addMButton;
    public Button updateMButton;
    public Button deleteMButton;
    @FXML private TableColumn<Materiel, String> colLocalisation;
    @FXML private TableColumn<Materiel, String> colEtat;
    @FXML private TableView<Materiel> materielTable;
    @FXML private TableColumn<Materiel, Integer> colId;
    @FXML private TableColumn<Materiel, String> colNom;
    @FXML private TableColumn<Materiel, String> colDescription;
    @FXML private TableColumn<Materiel, Integer> colQuantite;
    @FXML private TableColumn<Materiel, java.util.Date> colDate;
    @FXML private TableColumn<Materiel, Double> colValeur;
    @FXML private TableColumn<Materiel, String> colCategorie;

    @FXML private TextField searchField;


    // ── Chemins FXML ──────────────────────────────────────────────────────────
    private static final String ADD_MATERIEL_FXML    = "/sn/iage/isi/employeejavafx/views/addMateriel.fxml";
    private static final String EDIT_MATERIEL_FXML   = "/sn/iage/isi/employeejavafx/views/editMateriel.fxml";

    private MaterielService materielService = new MaterielService();
    private ObservableList<Materiel> materielList;


    private final ObservableList<Materiel> materielData = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        setUpColumns();
        setupSearchListener();
        loadMateriels();
    }

    private void loadMateriels() {
        materielList = FXCollections.observableArrayList(materielService.findAllMateriels());
        materielTable.setItems(materielList);
    }

    private void setUpColumns() {
        List<Materiel> materiels = materielService.findAllMateriels();
        ObservableList<Materiel> items = FXCollections.observableArrayList(materiels);
        materielTable.setItems(items);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date_acquisition"));
        colValeur.setCellValueFactory(new PropertyValueFactory<>("valeur"));
        colLocalisation.setCellValueFactory(new PropertyValueFactory<>("localisation"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));
        colCategorie.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategorie().getNom()));

        materielTable.setItems(materielList);
    }


    // ═══════════════════════════════════════════════════════════════════════════
    //  HANDLE ADD
    // ═══════════════════════════════════════════════════════════════════════════
    @FXML
    private void handleAdd() {
        Stage owner = (Stage) addMButton.getScene().getWindow();
        StageHelper.openModal(owner, ADD_MATERIEL_FXML, "Ajouter un materiel");
        loadMateriels(); // Rafraîchir après fermeture du modal
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  HANDLE UPDATE
    // ═══════════════════════════════════════════════════════════════════════════
    @FXML
    private void handleUpdate(ActionEvent event) {
        Materiel selected = materielTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showWarning("Sélection requise",
                    "Veuillez sélectionner un materiel à modifier.");
            return;
        }
        Stage owner = (Stage) updateMButton.getScene().getWindow();
        StageHelper.openModal(
                owner, EDIT_MATERIEL_FXML, "Modifier les informatiosn du materiel",
                (EditMaterielController ctrl) -> ctrl.setMateriel(selected)
        );
        loadMateriels();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  HANDLE DELETE
    // ═══════════════════════════════════════════════════════════════════════════
    @FXML
    private void handleDelete() {
        Materiel selected = materielTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showWarning("Sélection requise",
                    "Veuillez sélectionner un materiel à supprimer.");
            return;
        }
        boolean confirmed = AlertHelper.showConfirmation(
                "Supprimer le materiel",
                "Êtes-vous sûr de vouloir supprimer " +
                        selected.getNom() + " " + selected.getCategorie() + " ?"
        );
        if (confirmed) {
            boolean success = materielService.deleteMateriel(selected.getId());
            if (success) {
                AlertHelper.showSuccess("Suppression réussie",
                        "Le materiel a été supprimé avec succès.");
                loadMateriels();
            } else {
                AlertHelper.showError("Erreur",
                        "La suppression a échoué. Veuillez réessayer.");
            }
        }
    }

    /** Écoute les modifications dans le champ de recherche et filtre en temps réel. */
    private void setupSearchListener() {
        searchField.textProperty().addListener((obs, old, keyword) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                loadMateriels();
            } else {
                List<Materiel> results = materielService.search(keyword.trim());
                materielData.setAll(results);
            }
        });
    }

    @FXML
    private void getData(MouseEvent event) {
        Materiel selected = materielTable.getSelectionModel().getSelectedItem();
        if (selected != null && event.getClickCount() == 2) {
            // Double-clic : ouvrir l'édition directement
            handleUpdate(null);
        }
    }


    // ═══════════════════════════════════════════════════════════════════════════
    //  HANDLE REFRESH
    // ═══════════════════════════════════════════════════════════════════════════
    @FXML
    private void handleRefresh() {
        loadMateriels();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  HANDLE SEARCH
    // ═══════════════════════════════════════════════════════════════════════════
//    @FXML
//    private void handleSearch() {
//        String keyword = searchField.getText().trim().toLowerCase();
//        if (keyword.isEmpty()) {
//            loadMateriels();
//        } else {
//            ObservableList<Materiel> filtered = materielList.filtered(m ->
//                    m.getNom().toLowerCase().contains(keyword) ||
//                            m.getDescription().toLowerCase().contains(keyword));
//            materielTable.setItems(filtered);
//        }
//    }


    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        alert.showAndWait();
    }
}
