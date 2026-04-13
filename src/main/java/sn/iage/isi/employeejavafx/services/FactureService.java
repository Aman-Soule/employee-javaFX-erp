package sn.iage.isi.employeejavafx.services;

import sn.iage.isi.employeejavafx.config.DB;
import sn.iage.isi.employeejavafx.models.Employee;
import sn.iage.isi.employeejavafx.models.Facture;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FactureService {

    private DB db = new DB();
    private ResultSet rs;

    EmployeeService employeeService = new EmployeeService();

    // ── CREATE ───────────────────────────────────────────────
    public boolean createFacture(Facture facture) {
        String sql = """
            INSERT INTO facture (employee_id, file_name, date_generation, montant)
            VALUES (?, ?, ?, ?)
            """;
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, facture.getEmployee().getId());
            db.getPstm().setString(2, facture.getFile_name());
            db.getPstm().setDate(3, java.sql.Date.valueOf(String.valueOf(facture.getDate_generation()))); // si LocalDate
            db.getPstm().setDouble(4, facture.getMontant());
            int rowsAffected = db.executeMaj();
            db.closeConnection();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur création facture : " + e.getMessage());
            return false;
        }
    }


    // ── READ ALL ─────────────────────────────────────────────
    public List<Facture> findAllFactures() {
        List<Facture> factures = new ArrayList<>();
        String sql = "SELECT * FROM facture ORDER BY date_generation DESC";
        try {
            db.initPrepar(sql);
            rs = db.executeSelect();
            while (rs.next()) {
                Facture facture = new Facture();
                facture.setId(rs.getInt("id"));
                facture.setFile_name(rs.getString("file_name"));
                facture.setDate_generation(rs.getDate("date_generation").toLocalDate());
                facture.setMontant(rs.getDouble("montant"));
                facture.setEmployee(employeeService.findById(rs.getInt("employee_id")));

                factures.add(facture);
            }
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return factures;
    }

    // ── READ BY ID ───────────────────────────────────────────
    public Facture findFactureById(int id) {
        Facture facture = null;
        String sql = "SELECT * FROM facture WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            rs = db.executeSelect();
            if (rs.next()) {
                facture = new Facture();
                facture.setId(rs.getInt("id"));
                facture.setFile_name(rs.getString("file_name"));
                facture.setDate_generation(rs.getDate("date_generation").toLocalDate());
                facture.setMontant(rs.getDouble("montant"));

                EmployeeService employeeService = new EmployeeService();
                Employee emp = employeeService.findById(rs.getInt("employee_id"));
                facture.setEmployee(emp);
            }
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return facture;
    }

    // ── UPDATE ───────────────────────────────────────────────
    public boolean updateFacture(Facture facture) {
        String sql = """
                UPDATE facture
                SET employee_id = ?, file_name = ?, date_generation = ?, montant = ?
                WHERE id = ?
                """;
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, facture.getEmployee().getId());
            db.getPstm().setString(2, facture.getFile_name());
            db.getPstm().setDate(3, Date.valueOf(facture.getDate_generation()));
            db.getPstm().setDouble(4, facture.getMontant());
            db.getPstm().setInt(5, facture.getId());
            int rowsAffected = db.executeMaj();
            db.closeConnection();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour facture : " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ───────────────────────────────────────────────
    public boolean delete(int id) {
        String sql = "DELETE FROM facture WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            int rowsAffected = db.executeMaj();
            db.closeConnection();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
