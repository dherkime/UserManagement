package um;

import java.util.List;
import java.util.Map;

import domain.User;
import org.junit.After;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
    public static final String DH_GMAIL_COM_TRAILING_SLASH = DH_GMAIL_COM + "/";

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

    @After
    public void teardown() {
        restTemplate.delete(getUrl() + DH_GMAIL_COM_TRAILING_SLASH);
    }

    @Test
    public void testGetUsers() throws Exception {
        @SuppressWarnings("rawtypes")
        final ResponseEntity<List> entity = restTemplate.getForEntity(
                "http://localhost:" + this.port + "/api/users", List.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    public void testGetUser() throws Exception {
        final ResponseEntity<User> user = findTestUser();
        assertEquals("Couldn't find user with ID \"" + DH_GMAIL_COM + "\"!", HttpStatus.OK, user.getStatusCode());
    }

    @Test
    public void testDeleteUser() throws Exception {
        restTemplate.delete(getUrl() + DH_GMAIL_COM_TRAILING_SLASH);
        final ResponseEntity<User> testUser = findTestUser();
        assertEquals("Found user with ID \"" + DH_GMAIL_COM + "\"!", HttpStatus.NOT_FOUND, testUser.getStatusCode());
    }

    @Test
    public void testUpdateUser() {
        final User user = testUser.getBody();
        final String updatedName = "Donald Herkimer";
        user.setName(updatedName);
        restTemplate.put(getUrl(), user);
        ResponseEntity<User> updatedUser = findTestUser();
        assertEquals("The user's name wasn't updated!", updatedName, updatedUser.getBody().getName());
    }

    @Test
    public void testLoginUser() {
        final User user = testUser.getBody();
        assertNull("The last login is not null!", user.getLastLogin());
        restTemplate.getForEntity(getUrl() + "/login?emailAddress={email}&password={pwd}", Map.class, user.getEmailAddress(), user.getPassword());
        ResponseEntity<Map> loggedInUser = findTestUserMap();
        assertNotNull("The user's last login was null!", loggedInUser.getBody().get("lastLogin"));
    }

    private ResponseEntity<User> findTestUser() {
        return restTemplate.getForEntity(
                getUrl() + DH_GMAIL_COM_TRAILING_SLASH, User.class);
    }

    private ResponseEntity<Map> findTestUserMap() {
        return restTemplate.getForEntity(
                getUrl() + DH_GMAIL_COM_TRAILING_SLASH, Map.class);
    }

    private String getUrl() {
        return "http://localhost:" + this.port + "/api/users/";
    }


    private void postTestUser() {
        if (null != testUser) {
            return;
        }

        testUser = restTemplate.postForEntity("http://localhost:" + this.port + "/api/users", new User("Don Herkimer", DH_GMAIL_COM, "xyz"), User.class);
    }

}
