package sn.iage.isi.employeejavafx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sn.iage.isi.employeejavafx.models.Employee;
import sn.iage.isi.employeejavafx.services.EmployeeService;
import sn.iage.isi.employeejavafx.services.MaterielService;
import sn.iage.isi.employeejavafx.tools.SessionManager;
import sn.iage.isi.employeejavafx.tools.StageHelper;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller de la vue {@code ressources.fxml}.
 *
 * <p>Gère les clics sur les cartes de navigation et charge
 * la vue correspondante dans le {@code contentPane} de l'accueil,
 * via {@link AccueilController} — ou directement dans la scène
 * si la vue est ouverte en standalone.</p>
 *
 * <p>Chaque méthode {@code goToXxx} est liée à un {@code onMouseClicked}
 * dans le FXML. La navigation délègue à l'AccueilController parent
 * via l'interface {@link ViewLoader} pour rester découplée.</p>
 */
public class RessourcesController implements Initializable {

    public Label prctSalaire;
    public Label cptEmployee;
    public Label masseSalaire;
    public VBox cardMateriel;
    public Label cptMateriel;
    public Label sommeMateriel;
    // ── IDs FXML ─────────────────────────────────────────────────────────────
    @FXML private Label pageTitle;
    @FXML private VBox  cardEmployees;
    @FXML private VBox  cardAddEmployee;
    @FXML private VBox  cardUsers;
    @FXML private VBox  cardMasse;

    // ── Chemins des vues ──────────────────────────────────────────────────────
    private static final String BASE             = "/sn/iage/isi/employeejavafx/views/";
    private static final String DASHBOARD_FXML   = BASE + "dashboard.fxml";
    private static final String ADD_EMPLOYEE_FXML = BASE + "addEmployee.fxml";
    private static final String ADD_USER_FXML    = BASE + "addUser.fxml";
    private static final String PROFILE_FXML     = BASE + "profile.fxml";


    private final EmployeeService employeeService = new EmployeeService();
    private final ObservableList<Employee> employeeData = FXCollections.observableArrayList();


    private final MaterielService materielService = new MaterielService();

    private void countMateriel() {
        int count = materielService.countAll();
        cptMateriel.setText(count + " matériel(s) enregistré(s)");
    }

    private void sommeMateriel() {
        sommeMateriel.setText(String.valueOf(materielService.sumValeur()) + " FCFA de valeur materiel");

    }

    @FXML
    private void goToMateriel(MouseEvent event) {
        Stage owner = (Stage) cardMateriel.getScene().getWindow();
        StageHelper.openModal(owner, BASE + "materiel.fxml", "Gestion du matériel");
    }


    private void countEmployees() {
        List<Employee> employees = employeeService.findAll();
        employeeData.setAll(employees);
        updateCountLabel(employees.size());
    }
    private void updateCountLabel(int count) {
        cptEmployee.setText(count + " employé(s) trouvé(s)");
    }
    private void sommeSalaires(){
        masseSalaire.setText(String.valueOf(employeeService.sumSalaries()) + " FCFA de masse salariale");
    }
    private void poucentageSalaire(){
        double percentage = employeeService.percentageAbove500k();
        prctSalaire.setText(String.format("%.2f%% de super salariés", percentage));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        countEmployees();
        sommeSalaires();
        poucentageSalaire();
        countMateriel();
        sommeMateriel();
        // Personnaliser le titre avec le nom de l'utilisateur connecté
        if (SessionManager.getInstance().isLoggedIn()) {
            String username = SessionManager.getInstance().getCurrentUser().getUsername();
            pageTitle.setText("Bonjour, " + username + " 👋");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  ACTIONS FXML — clic sur les cartes
    // ═══════════════════════════════════════════════════════════════════════════

    /** Ouvre le formulaire d'ajout d'un employé en modal. */
    @FXML
    private void goToAddEmployee(MouseEvent event) {
        Stage owner = (Stage) cardAddEmployee.getScene().getWindow();
        StageHelper.openModal(owner, ADD_EMPLOYEE_FXML, "Ajouter un employé");
    }

    /** Ouvre le formulaire de création d'un utilisateur en modal. */
    @FXML
    private void goToUsers(MouseEvent event) {
        Stage owner = (Stage) cardUsers.getScene().getWindow();
        StageHelper.openModal(owner, ADD_USER_FXML, "Créer un utilisateur");
    }


}