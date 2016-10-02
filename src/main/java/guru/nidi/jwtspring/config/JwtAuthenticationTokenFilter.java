package guru.nidi.jwtspring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

public class JwtAuthenticationTokenFilter extends GenericFilterBean {
    @Autowired
    private JwtTokenCodec jwtTokenCodec;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String header = httpRequest.getHeader("Authorization");
        final SecurityContext context = SecurityContextHolder.getContext();
        if (header != null && context.getAuthentication() == null) {
            final String tokenStr = header.substring("Bearer ".length());
            final JwtToken token = jwtTokenCodec.decodeToken(tokenStr);
            if (!token.isExpired()) {
                final PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(token, "n/a", token.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                context.setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}