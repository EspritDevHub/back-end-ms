package tn.esprit.pi.usermanagement.suermanagementmicroservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import tn.esprit.pi.usermanagement.suermanagementmicroservice.Entities.User;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    private static final String SECRET_KEY_BASE64 = "zFrD2/7VyW+yPzM9RkU7/UcT6Ttz+7Dc2Qh5X9uNzlg=";
    private static final byte[] SECRET_KEY = Base64.getDecoder().decode(SECRET_KEY_BASE64);
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .addClaims(Map.of(
                        "roles", user.getRole(),
                        "id", user.getId(),
                        "email", user.getEmail())
                )
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public Claims getClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY);

        Jws<Claims> jws = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);

        return jws.getPayload();
    }
}


