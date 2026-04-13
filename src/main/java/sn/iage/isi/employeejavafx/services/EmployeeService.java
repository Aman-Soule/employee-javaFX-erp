package sn.iage.isi.employeejavafx.services;

import sn.iage.isi.employeejavafx.config.DB;
import sn.iage.isi.employeejavafx.models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service gérant toutes les opérations CRUD sur la table {@code employees}.
 *
 * <p>Toutes les méthodes utilisent des {@link PreparedStatement} pour
 * prévenir les injections SQL.</p>
 */
public class EmployeeService {

    private DB db = new DB();
    private int ok;
    private ResultSet rs;

    // ═══════════════════════════════════════════════════════════════════════════
    //  CREATE
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Insère un nouvel employé en base de données.
     *
     * @param employee l'employé à insérer (id ignoré, auto-incrémenté)
     * @return {@code true} si l'insertion a réussi, {@code false} sinon
     */
    public boolean create(Employee employee) {
        String sql = """
                INSERT INTO employees (nom, prenom, email, telephone, poste, salaire)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try{
            db.initPrepar(sql);
            db.getPstm().setString(1, employee.getNom());
            db.getPstm().setString(2, employee.getPrenom());
            db.getPstm().setString(3, employee.getEmail());
            db.getPstm().setString(4, employee.getTelephone());
            db.getPstm().setString(5, employee.getPoste());
            db.getPstm().setDouble(6, employee.getSalaire());
            int rowsAffected = db.executeMaj();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("❌ Erreur création employé : " + e.getMessage());
            return false;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  READ ALL
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Récupère la liste complète des employés triés par nom.
     *
     * @return liste (potentiellement vide) d'employés
     */
    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        try{
            db.initPrepar(sql);
            rs = db.executeSelect();
            while (rs.next()){
                Employee employee = new Employee();
                employee.setId(rs.getInt(1));
                employee.setNom(rs.getString("nom"));
                employee.setPrenom(rs.getString("prenom"));
                employee.setEmail(rs.getString("email"));
                employee.setTelephone(rs.getString("telephone"));
                employee.setPoste(rs.getString("poste"));
                employee.setSalaire(rs.getDouble("salaire"));

                employees.add(employee);
            }
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employees;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  READ BY ID
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Recherche un employé par son identifiant.
     *
     * @param id identifiant de l'employé
     * @return un {@link Optional} contenant l'employé, ou vide si non trouvé
     */
    public Employee findById(int id) {
        Employee employee = null;
        String sql = "SELECT * FROM employees WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            rs = db.executeSelect();
            if (rs.next()) {
                employee = new Employee();
                employee.setId(rs.getInt("id"));
                employee.setNom(rs.getString("nom"));
                employee.setPrenom(rs.getString("prenom"));
                employee.setEmail(rs.getString("email"));
                employee.setTelephone(rs.getString("telephone"));
                employee.setPoste(rs.getString("poste"));
                employee.setSalaire(rs.getDouble("salaire"));
            }
            db.closeConnection();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return employee;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  SEARCH
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Recherche des employés dont le nom ou le prénom contient le mot-clé.
     *
     * @param keyword terme de recherche (insensible à la casse)
     * @return liste des employés correspondants
     */
    public List<Employee> search(String keyword) {
        List<Employee> results = new ArrayList<>();
        String sql = """
                SELECT * FROM employees
                WHERE nom LIKE ? OR prenom LIKE ? OR poste LIKE ?
                ORDER BY nom ASC
                """;
        String pattern = "%" + keyword + "%";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, pattern);
            db.getPstm().setString(2, pattern);
            db.getPstm().setString(3, pattern);
            try (ResultSet rs = db.executeSelect()) {
                while (rs.next()) {
                    results.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur recherche employés : " + e.getMessage());
        }
        return results;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  UPDATE
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Met à jour les informations d'un employé existant.
     *
     * @param employee employé avec les nouvelles valeurs (id obligatoire)
     * @return {@code true} si la mise à jour a réussi
     */
    public boolean update(Employee employee) {
        String sql = """
                UPDATE employees
                SET nom = ?, prenom = ?, email = ?, telephone = ?, poste = ?, salaire = ?
                WHERE id = ?
                """;
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, employee.getNom());
            db.getPstm().setString(2, employee.getPrenom());
            db.getPstm().setString(3, employee.getEmail());
            db.getPstm().setString(4, employee.getTelephone());
            db.getPstm().setString(5, employee.getPoste());
            db.getPstm().setDouble(6, employee.getSalaire());
            db.getPstm().setInt(7, employee.getId());
            int rowsAffected = db.executeMaj();
            db.closeConnection();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour employé : " + e.getMessage());
            return false;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  DELETE
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Supprime un employé par son identifiant.
     *
     * @param id identifiant de l'employé à supprimer
     * @return {@code true} si la suppression a réussi
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            int rowsAffected = db.executeMaj();
            db.closeConnection();
            return rowsAffected > 0;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  COUNT
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Retourne le nombre total d'employés en base.
     *
     * @return nombre d'employés
     */
    public int count() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM employees";
        try {
            db.initPrepar(sql);
            rs = db.executeSelect();
            if (rs.next()) {
                count = rs.getInt("total");
            }
            db.closeConnection();
        } catch (SQLException e) {
            System.err.println("Erreur comptage employés : " + e.getMessage());
        }
        return count;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  MAPPER PRIVÉ
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Mappe une ligne de {@link ResultSet} vers un objet {@link Employee}.
     */
    private Employee mapResultSet(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("email"),
                rs.getString("telephone"),
                rs.getString("poste"),
                rs.getDouble("salaire")
        );
    }

    // 1) Masse salariale totale
    public double sumSalaries() {
        double total = 0;
        String sql = "SELECT SUM(salaire) AS masse FROM employees";
        try {
            db.initPrepar(sql);
            rs = db.executeSelect();
            if (rs.next()) {
                total = rs.getDouble("masse");
            }
            db.closeConnection();
        } catch (SQLException e) {
            System.err.println("Erreur calcul masse salariale : " + e.getMessage());
        }
        return total;
    }

    // 2) Pourcentage des employés avec salaire > 500000
    public double percentageAbove500k() {
        double percentage = 0;
        String sqlTotal = "SELECT COUNT(*) AS total FROM employees";
        String sqlAbove = "SELECT COUNT(*) AS above FROM employees WHERE salaire >= 500000";
        try {
            // Total employés
            db.initPrepar(sqlTotal);
            rs = db.executeSelect();
            int total = 0;
            if (rs.next()) {
                total = rs.getInt("total");
            }
            db.closeConnection();

            // Employés > 200000
            db.initPrepar(sqlAbove);
            rs = db.executeSelect();
            int above = 0;
            if (rs.next()) {
                above = rs.getInt("above");
            }
            db.closeConnection();

            if (total > 0) {
                percentage = (above * 100.0) / total;
            }
        } catch (SQLException e) {
            System.err.println("Erreur calcul pourcentage : " + e.getMessage());
        }
        return percentage;
    }

}
