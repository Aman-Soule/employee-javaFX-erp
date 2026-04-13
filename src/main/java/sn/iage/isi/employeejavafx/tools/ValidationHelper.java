package sn.iage.isi.employeejavafx.tools;

import java.util.regex.Pattern;

/**
 * Utilitaire de validation des saisies dans les formulaires.
 *
 * <p>Toutes les méthodes sont statiques. Retournent {@code true} si la valeur
 * est valide, {@code false} sinon.</p>
 */
public class ValidationHelper {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^(\\+221|00221)?[0-9]{9}$");

    private ValidationHelper() {}

    /** Vérifie qu'une chaîne n'est ni nulle ni vide. */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /** Vérifie qu'une adresse email est syntaxiquement correcte. */
    public static boolean isValidEmail(String email) {
        return isNotEmpty(email) && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /** Vérifie qu'un numéro de téléphone respecte le format sénégalais ou international. */
    public static boolean isValidPhone(String phone) {
        return isNotEmpty(phone) && PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /** Vérifie qu'un mot de passe contient au moins 6 caractères. */
    public static boolean isValidPassword(String password) {
        return isNotEmpty(password) && password.length() >= 6;
    }

    /**
     * Vérifie qu'une chaîne représente un nombre décimal positif.
     *
     * @param value la chaîne à valider
     * @return {@code true} si la valeur est un double positif
     */
    public static boolean isPositiveDouble(String value) {
        try {
            return isNotEmpty(value) && Double.parseDouble(value) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public static boolean isPositiveInteger(String value) {
        try {
            return isNotEmpty(value) && Integer.parseInt(value) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Retourne un message d'erreur de validation pour un formulaire employé.
     * Retourne {@code null} si tout est valide.
     */
    public static String validateEmployeeForm(String nom, String prenom,
                                               String email, String telephone,
                                               String poste, String salaire) {
        if (!isNotEmpty(nom))       return "Le nom est obligatoire.";
        if (!isNotEmpty(prenom))    return "Le prénom est obligatoire.";
        if (!isValidEmail(email))   return "L'adresse email est invalide.";
        if (!isValidPhone(telephone)) return "Le numéro de téléphone est invalide.";
        if (!isNotEmpty(poste))     return "Le poste est obligatoire.";
        if (!isPositiveDouble(salaire)) return "Le salaire doit être un nombre positif.";
        return null; // Tout est valide
    }

    public static String validateMaterielForm(String nom, String description,
                                              String quantite, String valeur,
                                              String localisation, String etat ) {
        if (!isNotEmpty(nom))            return "Le nom du matériel est obligatoire.";
        if (!isNotEmpty(description))    return "La description est obligatoire.";
        if (!isPositiveInteger(quantite)) return "La quantité doit être un entier positif.";
        if (!isPositiveDouble(valeur))   return "La valeur doit être un nombre positif.";
        if (!isNotEmpty(localisation))   return "La localisation est obligatoire.";
        if (!isNotEmpty(etat))           return "L'état du matériel est obligatoire.";
        return null; // Tout est valide

    }

}
