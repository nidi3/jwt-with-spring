package guru.nidi.jwtspring;

import com.jayway.jsonpath.JsonPath;
import guru.nidi.jwtspring.controller.AuthenticationRequest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext
public class AuthenticationTest extends AbstractMvcTest {
    @Override
    protected void doInit() throws Exception {
        registerUser("name", "pass").andExpect(status().isCreated());
    }

    @Test
    public void userRepositoryWithoutTokenIsForbidden() throws Exception {
        mockMvc.perform(get("/api/users")).andExpect(status().isForbidden());
    }

    @Test
    public void userRepositoryWithTokenIsAllowed() throws Exception {
        final String token = extractToken(login("name", "pass").andReturn());
        mockMvc.perform(get("/api/users").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    public void loginOk() throws Exception {
        login("name", "pass")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.user.username", equalTo("name")))
                .andExpect(jsonPath("$.user.password").doesNotExist())
                .andReturn();
    }

    @Test
    public void loginNok() throws Exception {
        login("name", "wrong").andExpect(status().isForbidden());
    }


    private ResultActions registerUser(String username, String password) throws Exception {
        return mockMvc.perform(
                post("/api/users")
                        .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"));
    }

    private ResultActions login(String username, String password) throws Exception {
        final AuthenticationRequest auth = new AuthenticationRequest();
        auth.setUsername(username);
        auth.setPassword(password);
        return mockMvc.perform(
                post("/api/public/login")
                        .content(json(auth))
                        .contentType(MediaType.APPLICATION_JSON));
    }

    private String extractToken(MvcResult result) throws UnsupportedEncodingException {
        return JsonPath.read(result.getResponse().getContentAsString(), "$.token");
    }
}
