package guru.nidi.jwtspring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 *
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource(properties = {"spring.data.mongodb.port=27018"})
public class AbstractMvcTest {
    protected MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();
    private boolean isInited = false;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Before
    public void init() throws Exception {
        if (!isInited) {
            doInit();
            isInited = true;
        }
    }

    protected void doInit() throws Exception {
    }

    protected String json(Object o) throws IOException {
        return mapper.writeValueAsString(o);
    }
}
