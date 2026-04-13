package sn.iage.isi.employeejavafx.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sn.iage.isi.employeejavafx.models.Facture;
import sn.iage.isi.employeejavafx.models.Materiel;
import sn.iage.isi.employeejavafx.services.FactureService;
import sn.iage.isi.employeejavafx.services.MaterielService;
import sn.iage.isi.employeejavafx.tools.StageHelper;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FactureController implements Initializable {


    @FXML
    private TableColumn<Facture, String> colDate;

    @FXML
    private TableColumn<Facture, String> colEmployee;

    @FXML
    private TableColumn<Facture, String> colFileName;

    @FXML
    private TableColumn<Facture, String > colId;

    @FXML
    private TableColumn<Facture, String> colMontant;

    @FXML
    private Button deleteFButton;

    @FXML
    private TableView<Facture> factureTable;

    @FXML
    private TextField searchField;


    private final FactureService factureService = new FactureService();
    private ObservableList<Facture> factureList = FXCollections.observableArrayList();

    private static final String INFOS_FXML = "/sn/iage/isi/employeejavafx/views/infos_facture.fxml";



    @FXML
    void handleDelete(ActionEvent event) {
        Facture selectedFacture = factureTable.getSelectionModel().getSelectedItem();
        if (selectedFacture != null) {
            try {
                factureService.delete(selectedFacture.getId());
                factureList.remove(selectedFacture);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Facture supprimée avec succès.");
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de la suppression.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une facture à supprimer.");
            alert.showAndWait();
        }
    }

    @FXML
    void handleOpenPdf(ActionEvent event) {
        Facture selectedFacture = factureTable.getSelectionModel().getSelectedItem();
        if (selectedFacture != null) {
            try {
                // Construire le chemin absolu vers le fichier
                String projectRoot = System.getProperty("user.dir"); // racine du projet
                String filePath = projectRoot + "/factures/" + selectedFacture.getFile_name();

                java.io.File pdfFile = new java.io.File(filePath);

                if (pdfFile.exists()) {
                    java.awt.Desktop.getDesktop().open(pdfFile);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Le fichier n’existe pas : " + filePath);
                    alert.showAndWait();
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Impossible d’ouvrir le fichier PDF.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une facture à ouvrir.");
            alert.showAndWait();
        }
    }


    @FXML
    void handleRefresh(ActionEvent event) {
        loadFacture();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Liste des factures actualisée.");
        alert.showAndWait();
    }

    @FXML
    private void getData(MouseEvent event) {
        if (event.getClickCount() == 2) { // double-clic
            Facture selectedFacture = factureTable.getSelectionModel().getSelectedItem();
            if (selectedFacture != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(INFOS_FXML));
                    Parent root = loader.load();

                    // Récupérer le contrôleur et injecter la facture
                    InfosFactureController controller = loader.getController();
                    controller.setFacture(selectedFacture);

                    Stage stage = new Stage();
                    stage.setTitle("Infos Facture");
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initOwner(factureTable.getScene().getWindow());
                    stage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }




    @FXML
    void loadFacture() {
        factureList = FXCollections.observableArrayList(factureService.findAllFactures());
        factureTable.setItems(factureList);
    }

    private void setUpColumns() {
        List<Facture> factures = factureService.findAllFactures();
        ObservableList<Facture> items = FXCollections.observableArrayList(factures);
        factureTable.setItems(items);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFileName.setCellValueFactory(new PropertyValueFactory<>("file_name"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date_generation"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        colEmployee.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployee().getNom()));

        factureTable.setItems(factureList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
        loadFacture();
    }
}
