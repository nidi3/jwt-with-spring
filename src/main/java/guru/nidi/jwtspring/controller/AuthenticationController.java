package guru.nidi.jwtspring.controller;

import guru.nidi.jwtspring.config.JwtToken;
import guru.nidi.jwtspring.config.JwtTokenCodec;
import guru.nidi.jwtspring.config.UserDetailsImpl;
import guru.nidi.jwtspring.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenCodec jwtCodec;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) throws AuthenticationException {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        final User user = userDetails.getUser();
        final String token = jwtCodec.encodeToken(JwtToken.ofUser(user));
        return ResponseEntity.ok(new AuthenticationResponse(token, user));
    }
}