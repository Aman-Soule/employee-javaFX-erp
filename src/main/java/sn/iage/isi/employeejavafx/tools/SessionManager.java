package sn.iage.isi.employeejavafx.tools;

import sn.iage.isi.employeejavafx.models.User;

/**
 * Gestionnaire de session applicative (Singleton).
 *
 * <p>Stocke l'utilisateur connecté pour le rendre accessible dans tous les
 * controllers sans avoir à le transmettre manuellement à chaque navigation.</p>
 *
 * <p>Usage :</p>
 * <pre>
 *   // Après connexion :
 *   SessionManager.getInstance().setCurrentUser(user);
 *
 *   // Depuis n'importe quel controller :
 *   User me = SessionManager.getInstance().getCurrentUser();
 *
 *   // À la déconnexion :
 *   SessionManager.getInstance().clear();
 * </pre>
 */
public class SessionManager {

    private static SessionManager instance;
    private User currentUser;

    private SessionManager() {}

    /** Retourne l'instance unique du gestionnaire de session. */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /** Retourne l'utilisateur actuellement connecté, ou {@code null} si non connecté. */
    public User getCurrentUser() {
        return currentUser;
    }

    /** Définit l'utilisateur connecté. */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /** Vérifie si une session est active. */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /** Vérifie si l'utilisateur connecté a le rôle ADMIN. */
    public boolean isAdmin() {
        return isLoggedIn() && "ADMIN".equalsIgnoreCase(currentUser.getRole());
    }

    /** Efface la session courante (déconnexion). */
    public void clear() {
        this.currentUser = null;
    }
}
