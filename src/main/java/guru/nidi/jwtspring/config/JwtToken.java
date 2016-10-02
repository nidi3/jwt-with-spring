package guru.nidi.jwtspring.config;

import guru.nidi.jwtspring.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JwtToken {
    private static class Key {
        public static final String COMPANY_ID = "cmp";
        public static final String ROLES = "rol";
    }

    private final Claims claims;

    public JwtToken(Claims claims) {
        this.claims = claims;
    }

    public static JwtToken ofUser(User user) {
        final Claims claims = Jwts.claims();
        claims.put(JwtToken.Key.COMPANY_ID, user.getCompany() == null ? null : user.getCompany().getId());
        claims.put(JwtToken.Key.ROLES, user.getRoles().stream().collect(Collectors.joining(",")));
        return new JwtToken(claims);
    }

    public Claims getClaims() {
        return claims;
    }

    public String getCompanyId() {
        return claims.get(Key.COMPANY_ID, String.class);
    }

    public List<String> getRoles() {
        return Arrays.asList(claims.get(Key.ROLES, String.class).split(","));
    }

    public String getUsername() {
        return claims.getSubject();
    }

    public boolean isExpired() {
        return claims.getExpiration().before(new Date());
    }
}