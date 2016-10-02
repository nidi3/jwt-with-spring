package guru.nidi.jwtspring.controller;

import guru.nidi.jwtspring.domain.User;

public class AuthenticationResponse {
    private final String token;
    private final User user;

    public AuthenticationResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return this.token;
    }
    
    public User getUser() {
        return this.user;
    }
}