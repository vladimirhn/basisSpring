package jwtsecurity.jwt;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtProvider {

//    @Value("$(jwt.secret)")
    private String jwtSecret = "secret";

    public String generateToken(String login, Date date) {

        return Jwts.builder()
                .setSubject(login)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
//            log.severe("Token expired");
        } catch (UnsupportedJwtException unsEx) {
//            log.severe("Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
//            log.severe("Malformed jwt");
        } catch (SignatureException sEx) {
//            log.severe("Invalid signature");
        } catch (Exception e) {
//            log.severe("invalid token");
        }
        return false;
    }

    public Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }
}
