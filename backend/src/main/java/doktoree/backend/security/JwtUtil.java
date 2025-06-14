package doktoree.backend.security;

import doktoree.backend.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private String jwtSecret = "5582d647e1705bc25701002965913b7f6a1ef9b557a074d75613377c9e55dcea08618cc72c65888b6aafbe34ea78ab2dfe1b6f5196b9ba9d68c4b19296835e342f3b33a515c64435f9380f9024b869a9495cb37c1dc860d6b9d258917b3af14fa7a95eec80f68b6303272440588823fb83d235c9f32f53ef347f79d60e5fbae0bf96b229cc9e21aa8e7f33d9936d83296b2f5ae3d8d2b47482a21cade48ce61f95daf49da3bfc7056e64679a287b2f6422c2a0626f95fa887d5a211158230430f8f5cbed3195c3c37d4c15cf97d362817d5432252458622360d7e6d9e2431f2ae453c4c45995b702ce50cc9b60b398bde04e5edbcfe299fe692c5d2d61cdd00e";

    public String generateToken(User user){

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 *60))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()),SignatureAlgorithm.HS256)
                .compact();

    }


    public String extractUsername(String token){

        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

    }

    public boolean isTokenExpired(String token){

        Date date = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return date.before(new Date());



    }

    public boolean validateToken(String token, String username){

        System.out.println(extractUsername(token).equals(username) && !isTokenExpired(token));
        return extractUsername(token).equals(username) && !isTokenExpired(token);

    }


}
