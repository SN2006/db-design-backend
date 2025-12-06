package com.example.app.dbdesignbackend.util;

import com.example.app.dbdesignbackend.dto.CredentialDTO;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BasicAuthTokenUtil {

    private static final String BASIC_PREFIX = "Basic ";
    private static final String SEPARATOR = ":";

    private BasicAuthTokenUtil() {}

    public static String generateToken(CredentialDTO credential) {
        if (credential == null || credential.getEmail() == null || credential.getPassword() == null) {
            throw new IllegalArgumentException("Credentials cannot be null");
        }

        String authPair = credential.getEmail() + SEPARATOR + credential.getPassword();

        String encodedAuth = Base64.getEncoder()
                .encodeToString(authPair.getBytes(StandardCharsets.UTF_8));

        return BASIC_PREFIX + encodedAuth;
    }

    public static CredentialDTO parseToken(String token) {
        if (token == null || !token.startsWith(BASIC_PREFIX)) {
            throw new IllegalArgumentException("Invalid Basic Authentication token");
        }

        String base64Credentials = token.substring(BASIC_PREFIX.length());

        byte[] decodedBytes;
        try {
            decodedBytes = Base64.getDecoder().decode(base64Credentials);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to decode Base64 token", e);
        }

        String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);

        String[] parts = decodedString.split(SEPARATOR, 2);

        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid format within Basic Auth token. Expected 'username:password'");
        }

        return new CredentialDTO(parts[0], parts[1]);
    }


}
