package tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.security;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatorService {

    private final GoogleAuthenticator gAuth;

    public AuthenticatorService() {
        this.gAuth = new GoogleAuthenticator();
    }

    public String generateSecretKey() {
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }

    public boolean verifySecretKey(String secretKey, int otp) {
        int generatedOtp = gAuth.getTotpPassword(secretKey);
        return generatedOtp == otp;
    }
}
