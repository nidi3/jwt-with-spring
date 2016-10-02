package guru.nidi.jwtspring.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenCodec {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public JwtToken decodeToken(String token) {
        return new JwtToken(Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody());
    }

    private Date generateExpirationDate(Date issuedAt) {
        return new Date(issuedAt.getTime() + expiration * 1000);
    }

    public String encodeToken(JwtToken token) {
        return Jwts.builder()
                .setClaims(token.getClaims())
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate(new Date()))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}