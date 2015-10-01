package um;

import java.util.List;

import domain.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;

/**
 * Test the endpoints
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Configuration.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0", "management.port=0"})
@DirtiesContext
public class ConfigurationTests {
    public static final String DH_GMAIL_COM = "dh@gmail.com";

    private ResponseEntity<User> testUser;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Value("${local.server.port}")
    private int port;

    @Value("${local.management.port}")
    private int mgt;

    @Before
    public void setup() {
        postTestUser();
    }

    @Test
    public void testUsers() throws Exception {
        @SuppressWarnings("rawtypes")
        ResponseEntity<List> entity = restTemplate.getForEntity(
                "http://localhost:" + this.port + "/api/users", List.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    public void testGetSpecificUser() throws Exception {
        ResponseEntity<User> user = findTestUser();
        assertEquals("Couldn't find user with ID \"" + DH_GMAIL_COM + "\"", HttpStatus.OK, user.getStatusCode());
    }

    //FIXME: Delete isn't working
    @Ignore("Delete isn't working")
    @Test
    public void testDeleteSpecificUser() throws Exception {
        restTemplate.delete(getUrl() + DH_GMAIL_COM);
        final ResponseEntity<User> testUser = findTestUser();
        assertEquals("Found user with ID \"" + DH_GMAIL_COM + "\"", HttpStatus.NOT_FOUND, testUser.getStatusCode());
    }

    //public void test

    private ResponseEntity<User> findTestUser() {
        return restTemplate.getForEntity(
                getUrl() + DH_GMAIL_COM + "/", User.class);
    }

    private String getUrl() {
        return "http://localhost:" + this.port + "/api/users/";
    }


    private void postTestUser() {
        if (null != testUser) {
            return;
        }

        testUser = restTemplate.postForEntity(
                "http://localhost:" + this.port + "/api/users", new User("Don Herkimer", DH_GMAIL_COM, "xyz"), User.class);
    }

}
