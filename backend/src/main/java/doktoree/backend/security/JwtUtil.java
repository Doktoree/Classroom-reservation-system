package doktoree.backend.security;

import doktoree.backend.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private String jwtSecret = "5582d647e1705bc25701002965913b7f6"
        + "a1ef9b557a074d75613377c9e55dcea0861"
        + "8cc72c65888b6aafbe34ea78ab2dfe1b6f51"
        + "96b9ba9d68c4b19296835e342f3b33a515c64"
        + "435f9380f9024b869a9495cb37c1dc860d6b9d"
        + "258917b3af14fa7a95eec80f68b63032724405"
        + "88823fb83d235c9f32f53ef347f79d60e5fbae0bf"
        + "96b229cc9e21aa8e7f33d9936d83296b2f5ae3d8"
        + "d2b47482a21cade48ce61f95daf49da3bfc7056e64"
        + "679a287b2f6422c2a0626f95fa887d5a211158230"
        + "430f8f5cbed3195c3c37d4c15cf97d362817d543225"
        + "2458622360d7e6d9e2431f2ae453c4c45995b702ce5"
        + "0cc9b60b398bde04e5edbcfe299fe692c5d2d61cdd00e";

    public String generateToken(User user) {

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", "ROLE_" + user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 60))
                .signWith(Keys.hmacShaKeyFor(
                    jwtSecret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();

    }


    public String extractUsername(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

    }

    public boolean isTokenExpired(String token) {

        Date date = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return date.before(new Date());



    }

    public boolean validateToken(String token, String username) {

        return extractUsername(token).equals(username) && !isTokenExpired(token);

    }


}
