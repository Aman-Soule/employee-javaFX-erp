package sn.iage.isi.employeejavafx.services;

import sn.iage.isi.employeejavafx.config.DB;
import sn.iage.isi.employeejavafx.models.Categorie;
import sn.iage.isi.employeejavafx.models.Employee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategorieService {

    private DB db = new DB();
    private int ok;
    private ResultSet rs;

    public boolean createCategorie(Categorie categorie) {
        String sql = """
                INSERT INTO categorie (nom, description)
                VALUES (?, ?)
                """;

        try{
            db.initPrepar(sql);
            db.getPstm().setString(1, String.valueOf(categorie.getNom()));
            db.getPstm().setString(2, String.valueOf(categorie.getDescription()));
            int rowsAffected = db.executeMaj();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur création categorie : " + e.getMessage());
            return false;
        }
    }

    public List<Categorie> findAllCategories() {
        List<Categorie> categories = new ArrayList<>();
        String sql = "SELECT * FROM categorie ORDER BY nom";
        try{
            db.initPrepar(sql);
            rs = db.executeSelect();
            while (rs.next()){
                Categorie categorie = new Categorie();
                categorie.setId(rs.getInt(1));
                categorie.setNom(rs.getString("nom"));
                categorie.setDescription(rs.getString("description"));

                categories.add(categorie);
            }
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }


    public Categorie findCategorieById(int id) {
        Categorie categorie = null;
        String sql = "SELECT * FROM categorie WHERE id = ?";
        try{
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            rs = db.executeSelect();
            if (rs.next()){
                categorie = new Categorie();
                categorie.setId(rs.getInt("id"));
                categorie.setNom(rs.getString("nom"));
                categorie.setDescription(rs.getString("description"));
            }
            db.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        return categorie;
    }

    public boolean updateCategorie(Categorie categorie) {
        String sql = """
                UPDATE categorie
                SET nom = ?, description = ?
                WHERE id = ?
                """;
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, categorie.getNom());
            db.getPstm().setString(2, categorie.getDescription());
            db.getPstm().setInt(3, categorie.getId());
            int rowsAffected = db.executeMaj();
            db.closeConnection();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("❌ Erreur mise à jour Categorie : " + e.getMessage());
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
        String sql = "DELETE FROM categorie WHERE id = ?";
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
}
