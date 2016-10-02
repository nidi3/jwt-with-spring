package guru.nidi.jwtspring.repository;

import guru.nidi.jwtspring.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RepositoryEventHandler(User.class)
public class UserEventHandler {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @HandleBeforeCreate
    @HandleBeforeSave
    public void beforeSave(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("ROLE_USER"));
    }

}