package RomainParedes.sport_team_manager;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class TokenValidator {

    private static final Logger logger =
            LoggerFactory.getLogger(TokenValidator.class);

    private final SecretKey key;

    public TokenValidator(
            @Value("${r5a05.app.jwtSecret}") String jwtSecret) {

        if (jwtSecret == null || jwtSecret.isBlank()) {
            throw new IllegalArgumentException(
                    "La clé JWT ne peut pas être vide");
        }

        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);

        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUserNameFromJwtToken(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {

        try {

            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(authToken);

            return true;

        } catch (SignatureException e) {

            logger.error("Signature JWT invalide : {}", e.getMessage());

        } catch (MalformedJwtException e) {

            logger.error("Token JWT mal formé : {}", e.getMessage());

        } catch (ExpiredJwtException e) {

            logger.error("Token JWT expiré : {}", e.getMessage());

        } catch (UnsupportedJwtException e) {

            logger.error("Token JWT non supporté : {}", e.getMessage());

        } catch (IllegalArgumentException e) {

            logger.error("Token JWT vide ou invalide : {}", e.getMessage());

        } catch (JwtException e) {

            logger.error("Erreur JWT : {}", e.getMessage());
        }

        return false;
    }
}