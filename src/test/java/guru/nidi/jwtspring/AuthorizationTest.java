package guru.nidi.jwtspring;

import com.jayway.jsonpath.JsonPath;
import guru.nidi.jwtspring.domain.Company;
import guru.nidi.jwtspring.domain.User;
import guru.nidi.jwtspring.repository.CompanyRepository;
import guru.nidi.jwtspring.repository.UserRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthorizationTest extends AbstractMvcTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static String companyAid, companyBid;

    public void doInit() throws Exception {
        final Company companyA = new Company();
        companyA.setName("a");
        companyAid = companyRepository.save(companyA).getId();

        final Company companyB = new Company();
        companyB.setName("b");
        companyBid = companyRepository.save(companyB).getId();

        final User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("pass"));
        user.setRoles(Arrays.asList("ROLE_USER"));
        user.setCompany(companyA);
        userRepository.save(user);
        final User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("pass"));
        admin.setRoles(Arrays.asList("ROLE_ADMIN"));
        admin.setCompany(companyB);
        userRepository.save(admin);
    }

    @Test
    public void userCannotAccessListOfCompanies() throws Exception {
        final String token = extractToken(login("user", "pass").andReturn());
        mockMvc.perform(get("/api/companies").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    public void adminCanAccessListOfCompanies() throws Exception {
        final String token = extractToken(login("admin", "pass").andReturn());
        mockMvc.perform(get("/api/companies").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    public void userCanAccessOwnCompany() throws Exception {
        final String token = extractToken(login("user", "pass").andReturn());
        mockMvc.perform(get("/api/companies/" + companyAid).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    public void userCannotAccessForeignCompany() throws Exception {
        final String token = extractToken(login("user", "pass").andReturn());
        mockMvc.perform(get("/api/companies/" + companyBid).header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    public void userCannotAccessForeignCompanyViaUser() throws Exception {
        final String token = extractToken(login("user", "pass").andReturn());
        final MvcResult authorization = mockMvc.perform(get("/api/users/search/findByUsername?username=admin").header("Authorization", "Bearer " + token)).andReturn();
        final String c = JsonPath.read(authorization.getResponse().getContentAsString(), "$._links.company.href");

        mockMvc.perform(get(c).header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    public void adminCanAccessOwnCompany() throws Exception {
        final String token = extractToken(login("admin", "pass").andReturn());
        mockMvc.perform(get("/api/companies/" + companyAid).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    public void adminCanAccessForeignCompany() throws Exception {
        final String token = extractToken(login("admin", "pass").andReturn());
        mockMvc.perform(get("/api/companies/" + companyBid).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

}
