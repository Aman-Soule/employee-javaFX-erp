package sn.iage.isi.employeejavafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sn.iage.isi.employeejavafx.models.Employee;
import sn.iage.isi.employeejavafx.models.Facture;
import sn.iage.isi.employeejavafx.services.FactureService;

public class InfosFactureController {

    @FXML private Label idLabel;
    @FXML private Label fileNameLabel;
    @FXML private Label dateLabel;
    @FXML private Label montantLabel;
    @FXML private Label employeeLabel;

    private Facture facture;
    private final FactureService factureService = new FactureService();

    // Méthode pour injecter la facture depuis l'appelant
    public void setFacture(Facture facture) {
        this.facture = facture;
        populateForm(facture);
    }

    @FXML
    public void handleClose(ActionEvent event) {
        Stage stage = (Stage) idLabel.getScene().getWindow();
        stage.close();
    }



    @FXML
    private void populateForm(Facture facture) {
        idLabel.setText(String.valueOf(facture.getId()));
        fileNameLabel.setText(facture.getFile_name());
        dateLabel.setText(facture.getDate_generation().toString());
        montantLabel.setText(facture.getMontant().toString());
        employeeLabel.setText(facture.getEmployee().getNom());
    }
}
