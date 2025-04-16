package tne.sprit.gestion_user.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        //return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getExpiration().before(new Date());
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
    // Added method to generate a password reset token
    public String generatePasswordResetToken(Long userId) {
        return Jwts.builder()
                .setSubject(userId.toString()) // Use user ID as the subject
                .claim("type", "password-reset") // Add a claim to differentiate from auth tokens
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    // Added method to validate a password reset token
    public boolean validatePasswordResetToken(String token) {
        try {
            var claims = Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            // Check if the token is of type "password-reset"
            String type = claims.get("type", String.class);
            if (!"password-reset".equals(type)) {
                return false;
            }
            // Check if the token is expired
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false; // Token is invalid (e.g., tampered, malformed, or signature invalid)
        }
    }

    // Added method to extract user ID from a password reset token
    public Long extractUserId(String token) {
        String userIdStr = Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return Long.parseLong(userIdStr);
    }

}

