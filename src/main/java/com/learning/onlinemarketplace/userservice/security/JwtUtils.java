package com.learning.onlinemarketplace.userservice.security;

import com.learning.onlinemarketplace.userservice.model.UserAccount;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenValidityInMilliseconds;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenValidityInMilliseconds;

    public String generateAccessToken(UserAccount user) {
        return generateToken(user.getEmail(), accessTokenValidityInMilliseconds);
    }

    public String generateRefreshToken(UserAccount user) {
        return generateToken(user.getEmail(), refreshTokenValidityInMilliseconds);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException ex) {
            log.info("JWT signature không hợp lệ: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.info("Token không đúng định dạng: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.info("Token đã hết hạn: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.info("Token không được hỗ trợ: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.info("Token rỗng hoặc không hợp lệ: {}", ex.getMessage());
        }
        return false;
    }

    public Instant getExpirationDateFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        var claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration().toInstant();
    }

    private String generateToken(String userName, long expirationMs) {
        try
        {
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS512).build();
            var now = Instant.now();
            var expirationTime = now.plusMillis(expirationMs);
            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(userName)
                    .issuer("OnlineMarketplace")
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(expirationTime))
                    .claim("role", "USER")
                    .build();

            var payload = new Payload(jwtClaimsSet.toJSONObject());
            var jwsObject = new JWSObject(header, payload);

            jwsObject.sign(new MACSigner(secretKey));

            return jwsObject.serialize();
        }
        catch (Exception e)
        {
            log.error("Failed to generate token", e);
            throw new RuntimeException("Failed to generate token with message: " + e.getMessage());
        }
    }
}
