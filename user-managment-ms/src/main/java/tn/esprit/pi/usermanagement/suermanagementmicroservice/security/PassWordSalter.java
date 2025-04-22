package tn.esprit.pi.usermanagement.suermanagementmicroservice.security;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class PassWordSalter {
    public String saltPassWord(String passWord, String salt) {
        return passWord + salt;
    }

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();

        byte[] saltBytes = new byte[25];
        random.nextBytes(saltBytes);

        return Base64.getEncoder().encodeToString(saltBytes);
    }
}
