package sn.iage.isi.employeejavafx.controllers;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sn.iage.isi.employeejavafx.models.Employee;
import sn.iage.isi.employeejavafx.models.Facture;
import sn.iage.isi.employeejavafx.services.EmployeeService;
import sn.iage.isi.employeejavafx.services.FactureService;
import sn.iage.isi.employeejavafx.tools.AlertHelper;
import sn.iage.isi.employeejavafx.tools.SessionManager;
import sn.iage.isi.employeejavafx.tools.StageHelper;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

/**
 * Controller de la vue {@code dashboard.fxml}.
 *
 * <p>Gère l'affichage de la liste des employés dans un {@link TableView},
 * la sélection d'une ligne, et le déclenchement des actions CRUD
 * (ajout, modification, suppression).</p>
 *
 * <p>La {@link TableView} est liée aux colonnes via {@link PropertyValueFactory},
 * qui résout automatiquement les getters de {@link Employee}.</p>
 */
public class DashboardController implements Initializable {

    // ── TableView & Colonnes ───────────────────────────────────────────────────
    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, Integer> idColumn;
    @FXML
    private TableColumn<Employee, String> nomColumn;
    @FXML
    private TableColumn<Employee, String> prenomColumn;
    @FXML
    private TableColumn<Employee, String> emailColumn;
    @FXML
    private TableColumn<Employee, String> telephoneColumn;
    @FXML
    private TableColumn<Employee, String> posteColumn;
    @FXML
    private TableColumn<Employee, Double> salaireColumn;

    // ── Barre de recherche ────────────────────────────────────────────────────
    @FXML
    private TextField searchField;

    // ── Boutons d'action ──────────────────────────────────────────────────────
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button factButton;


    // ── Étiquette de bienvenue ─────────────────────────────────────────────────
    @FXML
    private Label welcomeLabel;

    // ── Compteur d'employés ────────────────────────────────────────────────────
    @FXML
    private Label countLabel;

    // ── Services & données ────────────────────────────────────────────────────
    private final EmployeeService employeeService = new EmployeeService();
    private final ObservableList<Employee> employeeData = FXCollections.observableArrayList();

    // ── Chemins FXML ──────────────────────────────────────────────────────────
    private static final String ADD_FXML = "/sn/iage/isi/employeejavafx/views/addEmployee.fxml";
    private static final String EDIT_FXML = "/sn/iage/isi/employeejavafx/views/editEmployee.fxml";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupColumns();
        setupSearchListener();
        loadEmployees();
        updateWelcomeLabel();

        // Désactiver modifier/supprimer si rien n'est sélectionné
        updateButton.setDisable(true);
        deleteButton.setDisable(true);

        employeeTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, selected) -> {
                    boolean hasSelection = selected != null;
                    updateButton.setDisable(!hasSelection);
                    deleteButton.setDisable(!hasSelection);
                    factButton.setDisable(!hasSelection);
                }
        );
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  SETUP
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Lie chaque colonne à la propriété correspondante du modèle {@link Employee}.
     */
    private void setupColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        posteColumn.setCellValueFactory(new PropertyValueFactory<>("poste"));
        salaireColumn.setCellValueFactory(new PropertyValueFactory<>("salaire"));

        // Formatter le salaire (ex: "350 000.00")
        salaireColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null
                        : String.format("%,.2f FCFA", value));
            }
        });

        employeeTable.setItems(employeeData);
    }

    /**
     * Écoute les modifications dans le champ de recherche et filtre en temps réel.
     */
    private void setupSearchListener() {
        searchField.textProperty().addListener((obs, old, keyword) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                loadEmployees();
            } else {
                List<Employee> results = employeeService.search(keyword.trim());
                employeeData.setAll(results);
                updateCountLabel(results.size());
            }
        });
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  CHARGEMENT DES DONNÉES
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Recharge tous les employés depuis la base et rafraîchit la table.
     */
    private void loadEmployees() {
        List<Employee> employees = employeeService.findAll();
        employeeData.setAll(employees);
        updateCountLabel(employees.size());
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  ACTIONS FXML
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Ouvre la vue d'ajout d'employé en modal.
     * La table est rechargée à la fermeture de la fenêtre modale.
     */
    @FXML
    private void handleAdd(ActionEvent event) {
        Stage owner = (Stage) addButton.getScene().getWindow();
        StageHelper.openModal(owner, ADD_FXML, "Ajouter un employé");
        loadEmployees(); // Rafraîchir après fermeture du modal
    }

    /**
     * Ouvre la vue de modification de l'employé sélectionné.
     * Transmet l'objet {@link Employee} au controller de la vue d'édition.
     */
    @FXML
    private void handleUpdate(ActionEvent event) {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showWarning("Sélection requise",
                    "Veuillez sélectionner un employé à modifier.");
            return;
        }
        Stage owner = (Stage) updateButton.getScene().getWindow();
        StageHelper.openModal(
                owner, EDIT_FXML, "Modifier l'employé",
                (EditEmployeeController ctrl) -> ctrl.setEmployee(selected)
        );
        loadEmployees();
    }

    /**
     * Supprime l'employé sélectionné après confirmation.
     */
    @FXML
    private void handleDelete(ActionEvent event) {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showWarning("Sélection requise",
                    "Veuillez sélectionner un employé à supprimer.");
            return;
        }
        boolean confirmed = AlertHelper.showConfirmation(
                "Supprimer l'employé",
                "Êtes-vous sûr de vouloir supprimer " +
                        selected.getPrenom() + " " + selected.getNom() + " ?"
        );
        if (confirmed) {
            boolean success = employeeService.delete(selected.getId());
            if (success) {
                AlertHelper.showSuccess("Suppression réussie",
                        "L'employé a été supprimé avec succès.");
                loadEmployees();
            } else {
                AlertHelper.showError("Erreur",
                        "La suppression a échoué. Veuillez réessayer.");
            }
        }
    }

    /**
     * Récupère les données d'une ligne cliquée dans la table.
     * Peut être utilisé pour pré-remplir des champs de détail.
     */
    @FXML
    private void getData(MouseEvent event) {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected != null && event.getClickCount() == 2) {
            // Double-clic : ouvrir l'édition directement
            handleUpdate(null);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  HELPERS
    // ═══════════════════════════════════════════════════════════════════════════

    private void updateWelcomeLabel() {
        String username = SessionManager.getInstance().isLoggedIn()
                ? SessionManager.getInstance().getCurrentUser().getUsername()
                : "Invité";
        welcomeLabel.setText("Bienvenue, " + username + " !");
    }

    private void updateCountLabel(int count) {
        countLabel.setText(count + " employé(s) trouvé(s)");
    }

    @FXML
    public void handleFacture(ActionEvent event) {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showWarning("Sélection requise",
                    "Veuillez sélectionner un employé pour générer une facture.");
            return;
        }

        try {
            // Répertoire local "factures" dans le projet
            File facturesDir = new File(System.getProperty("user.dir"), "factures");
            if (!facturesDir.exists()) {
                facturesDir.mkdirs(); // crée le dossier si absent
            }

            // Nom unique du fichier
            String fileName = "facture_" + selected.getId() + "_" + System.currentTimeMillis() + ".pdf";
            String dest = new File(facturesDir, fileName).getAbsolutePath();

            // Génération du PDF
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Titre centré et stylisé
            Paragraph titre = new Paragraph("FACTURE")
                    .setBold()
                    .setFontSize(20)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER);
            document.add(titre);
            // Informations générales
            document.add(new Paragraph("Date : " + java.time.LocalDate.now())
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
            document.add(new Paragraph("\n"));

            // Tableau des informations de l’employé
            float[] columnWidths = {150F, 300F};
            Table table = new Table(columnWidths);

            table.addCell("Nom");
            table.addCell(selected.getNom());

            table.addCell("Prénom");
            table.addCell(selected.getPrenom());

            table.addCell("Email");
            table.addCell(selected.getEmail());

            table.addCell("Téléphone");
            table.addCell(selected.getTelephone());

            table.addCell("Poste");
            table.addCell(selected.getPoste());

            table.addCell("Salaire");
            table.addCell(String.format("%,.2f FCFA", selected.getSalaire()));

            document.add(table);

            // Ajout d’un espace et signature
            document.add(new Paragraph("\n\nSignature: _____________________")
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));

            document.close();


            // Création de l’objet Facture
            Facture facture = new Facture();
            facture.setEmployee(selected);
            facture.setFile_name(fileName);
            facture.setDate_generation(java.time.LocalDate.now());
            facture.setMontant(selected.getSalaire());

            // Sauvegarde en base
            FactureService factureService = new FactureService();
            boolean success = factureService.createFacture(facture);

            if (success) {
                AlertHelper.showSuccess("Facture générée",
                        "La facture a été créée dans le dossier 'factures' du projet : " + fileName);
            } else {
                AlertHelper.showError("Erreur",
                        "La facture a été générée mais l’enregistrement en base a échoué.");
            }

        } catch (Exception e) {
            AlertHelper.showError("Erreur",
                    "Impossible de générer la facture : " + e.getMessage());
        }
    }


}
