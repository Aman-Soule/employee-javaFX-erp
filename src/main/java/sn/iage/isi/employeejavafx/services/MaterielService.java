package sn.iage.isi.employeejavafx.services;

import sn.iage.isi.employeejavafx.config.DB;
import sn.iage.isi.employeejavafx.models.Categorie;
import sn.iage.isi.employeejavafx.models.Employee;
import sn.iage.isi.employeejavafx.models.Materiel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MaterielService {

    private DB db = new DB();
    private ResultSet rs;

    CategorieService categorieService = new CategorieService();

    // ═══════════════════════════════════════════════════════════════════════════
    //  CREATE
    // ═══════════════════════════════════════════════════════════════════════════
    public boolean createMateriel(Materiel materiel) {
        String sql = """
                INSERT INTO materiel (nom, description, quantite, localisation,  date_acquisition, valeur, etat,  categorie_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, materiel.getNom());
            db.getPstm().setString(2, materiel.getDescription());
            db.getPstm().setInt(3, materiel.getQuantite());
            db.getPstm().setString(4, materiel.getLocalisation());
            db.getPstm().setDate(5, new java.sql.Date(materiel.getDate_acquisition().getTime()));
            db.getPstm().setDouble(6, materiel.getValeur());
            db.getPstm().setString(7, materiel.getEtat());
            db.getPstm().setInt(8, materiel.getCategorie().getId());

            int rowsAffected = db.executeMaj();
            db.closeConnection();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("❌ Erreur création Materiel : " + e.getMessage());
            return false;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  READ - Tous les matériels
    // ═══════════════════════════════════════════════════════════════════════════
    public List<Materiel> findAllMateriels() {
        List<Materiel> materiels = new ArrayList<>();
        String sql = "SELECT * FROM materiel ORDER BY nom";
        try {
            db.initPrepar(sql);
            rs = db.executeSelect();
            while (rs.next()) {
                Materiel materiel = new Materiel();
                materiel.setId(rs.getInt("id"));
                materiel.setNom(rs.getString("nom"));
                materiel.setDescription(rs.getString("description"));
                materiel.setQuantite(rs.getInt("quantite"));
                materiel.setDate_acquisition(rs.getDate("date_acquisition"));
                materiel.setValeur(rs.getDouble("valeur"));
                materiel.setLocalisation(rs.getString("localisation"));
                materiel.setEtat(rs.getString("etat"));
                materiel.setCategorie(categorieService.findCategorieById(rs.getInt("categorie_id")));
                materiels.add(materiel);
            }
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return materiels;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  READ - Un matériel par ID
    // ═══════════════════════════════════════════════════════════════════════════
    public Optional<Materiel> findMaterielById(int id) {
        Materiel materiel = null;
        String sql = "SELECT * FROM materiel WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            rs = db.executeSelect();
            if (rs.next()) {
                materiel = new Materiel();
                materiel.setId(rs.getInt("id"));
                materiel.setNom(rs.getString("nom"));
                materiel.setDescription(rs.getString("description"));
                materiel.setQuantite(rs.getInt("quantite"));
                materiel.setDate_acquisition(rs.getDate("date_acquisition"));
                materiel.setValeur(rs.getDouble("valeur"));

                Categorie categorie = new Categorie();
                categorie.setId(rs.getInt("categorie_id"));
                materiel.setCategorie(categorie);
            }
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(materiel);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  UPDATE
    // ═══════════════════════════════════════════════════════════════════════════
    public boolean updateMateriel(Materiel materiel) {
        String sql = """
                UPDATE materiel
                SET nom = ?, description = ?, quantite = ?, date_acquisition = ?,  valeur = ?, localisation = ? , categorie_id = ?
                WHERE id = ?
                """;
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, materiel.getNom());
            db.getPstm().setString(2, materiel.getDescription());
            db.getPstm().setInt(3, materiel.getQuantite());
            db.getPstm().setDate(4, new java.sql.Date(materiel.getDate_acquisition().getTime()));
            db.getPstm().setDouble(5, materiel.getValeur());
            db.getPstm().setString(6, materiel.getLocalisation());
            db.getPstm().setInt(7, materiel.getCategorie().getId());
            db.getPstm().setInt(8, materiel.getId());

            int rowsAffected = db.executeMaj();
            db.closeConnection();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour Materiel : " + e.getMessage());
            return false;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  DELETE
    // ═══════════════════════════════════════════════════════════════════════════
    public boolean deleteMateriel(int id) {
        String sql = "DELETE FROM materiel WHERE id = ?";
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

    public List<Materiel> search(String keyword) {
        List<Materiel> results = new ArrayList<>();
        String sql = """
                SELECT * FROM materiel
                WHERE nom LIKE ? OR description LIKE ? OR localisation LIKE ?
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
            System.err.println("Erreur recherche materiel : " + e.getMessage());
        }
        return results;
    }

    private Materiel mapResultSet(ResultSet rs) throws SQLException {
        return new Materiel(
                rs.getString("nom"),
                rs.getString("description"),
                rs.getInt("quantite"),
                rs.getString("localisation"),
                rs.getDate("date_acquisition"),
                rs.getDouble("valeur"),
                rs.getString("etat")
        );
    }

    public int countAll() {
        int count = 0;
        String sql = "SELECT COUNT(*) as total FROM materiel";
        try {
            db.initPrepar(sql);
            rs = db.executeSelect();
            if (rs.next()) {
                count = rs.getInt("total");
            }
            db.closeConnection();
        } catch (SQLException e) {
            System.err.println("Erreur comptage materiel : " + e.getMessage());
        }
        return count;
    }

    public double sumValeur() {
        double total = 0;
        String sql = "SELECT SUM(valeur) AS valeur FROM materiel";
        try {
            db.initPrepar(sql);
            rs = db.executeSelect();
            if (rs.next()) {
                total = rs.getDouble("valeur");
            }
            db.closeConnection();
        } catch (SQLException e) {
            System.err.println("Erreur calcul somme valeur : " + e.getMessage());
        }
        return total;
    }
}
