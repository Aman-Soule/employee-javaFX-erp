package sn.iage.isi.employeejavafx.tools;

import org.mindrot.jbcrypt.BCrypt;

public class Utils {

    public static String hashPassword(String passwordTextPlain) {
        return BCrypt.hashpw(passwordTextPlain, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
