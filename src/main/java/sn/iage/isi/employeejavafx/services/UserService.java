package sn.iage.isi.employeejavafx.services;

import org.mindrot.jbcrypt.BCrypt;
import sn.iage.isi.employeejavafx.config.DB;
import sn.iage.isi.employeejavafx.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {

    private DB db = new DB();
    private ResultSet rs;

    // ═══════════════════════════════════════════════════════════════════════════
    //  AUTHENTIFICATION
    // ═══════════════════════════════════════════════════════════════════════════
    public Optional<User> authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, username);
            rs = db.executeSelect();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if (BCrypt.checkpw(password, hashedPassword)) {
                    return Optional.of(mapResultSet(rs));
                }
            }
            db.closeConnection();
        } catch (SQLException e) {
            System.err.println("Erreur authentification : " + e.getMessage());
        }
        return Optional.empty();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  CREATE
    // ═══════════════════════════════════════════════════════════════════════════
    public boolean create(User user) {
        if (usernameExists(user.getUsername())) {
            System.err.println("Nom d'utilisateur déjà utilisé : " + user.getUsername());
            return false;
        }
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, user.getUsername());
            db.getPstm().setString(2, hashedPassword);
            db.getPstm().setString(3, user.getRole());

            int rowsAffected = db.executeMaj();
            db.closeConnection();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur création utilisateur : " + e.getMessage());
            return false;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  READ ALL
    // ═══════════════════════════════════════════════════════════════════════════
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY username ASC";
        try {
            db.initPrepar(sql);
            rs = db.executeSelect();
            while (rs.next()) {
                users.add(mapResultSet(rs));
            }
            db.closeConnection();
        } catch (SQLException e) {
            System.err.println("Erreur récupération utilisateurs : " + e.getMessage());
        }
        return users;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  READ BY ID
    // ═══════════════════════════════════════════════════════════════════════════
    public Optional<User> findById(int id) {
        User user = null;
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            rs = db.executeSelect();
            if (rs.next()) {
                user = mapResultSet(rs);
            }
            db.closeConnection();
        } catch (SQLException e) {
            System.err.println("Erreur recherche utilisateur par ID : " + e.getMessage());
        }
        return Optional.ofNullable(user);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  UPDATE
    // ═══════════════════════════════════════════════════════════════════════════
    public boolean update(User user) {
        String sql = "UPDATE users SET username = ?, role = ? WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, user.getUsername());
            db.getPstm().setString(2, user.getRole());
            db.getPstm().setInt(3, user.getId());

            int rowsAffected = db.executeMaj();
            db.closeConnection();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour utilisateur : " + e.getMessage());
            return false;
        }
    }

    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        String hashed = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, hashed);
            db.getPstm().setInt(2, userId);

            int rowsAffected = db.executeMaj();
            db.closeConnection();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour mot de passe : " + e.getMessage());
            return false;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  DELETE
    // ═══════════════════════════════════════════════════════════════════════════
    public boolean delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);

            int rowsAffected = db.executeMaj();
            db.closeConnection();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur suppression utilisateur : " + e.getMessage());
            return false;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  HELPERS
    // ═══════════════════════════════════════════════════════════════════════════
    private boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) AS total FROM users WHERE username = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, username);
            rs = db.executeSelect();
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
            db.closeConnection();
        } catch (SQLException e) {
            System.err.println("Erreur vérification username : " + e.getMessage());
        }
        return false;
    }

    private User mapResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password")); // stocké hashé
        user.setRole(rs.getString("role"));
        return user;
    }
}
