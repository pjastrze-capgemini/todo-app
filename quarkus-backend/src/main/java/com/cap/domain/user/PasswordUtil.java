package com.cap.domain.user;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verifyPassword(String inputPassword, String databaseHash) {
        return BCrypt.checkpw(inputPassword, databaseHash);
    }
}
