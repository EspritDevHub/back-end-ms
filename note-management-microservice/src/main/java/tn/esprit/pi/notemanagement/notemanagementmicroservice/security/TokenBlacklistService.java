package tn.esprit.pi.notemanagement.notemanagementmicroservice.security;

import java.util.HashSet;
import java.util.Set;

public class TokenBlacklistService {
    private static final Set<String> blacklistedTokens = new HashSet<>();

    public static void addToBlacklist(String token) {
        blacklistedTokens.add(token);
    }

    public static boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
