package um;

import java.util.List;

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
@IntegrationTest({ "server.port=0", "management.port=0" })
@DirtiesContext
public class ConfigurationTests {
	public static final User SPECIFIC_USER = new User("Don Herkimer", "dh@gmail.com", "xyz");

    private ResponseEntity<User> postedUser;

	@Value("${local.server.port}")
	private int port;

	@Value("${local.management.port}")
	private int mgt;

	@Test
	public void testUsers() throws Exception {
		@SuppressWarnings("rawtypes")
		ResponseEntity<List> entity = new TestRestTemplate().getForEntity(
				"http://localhost:" + this.port + "/api/users", List.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
	}

	@Test
	public void testGetSpecificUser() throws Exception {
        final ResponseEntity<User> postedUser = postSpecificUser();
        String emailAddress = postedUser.getBody().getEmailAddress();
        ResponseEntity<User> user = new TestRestTemplate().getForEntity(
				"http://localhost:" + this.port + "/api/users/" + emailAddress + "/", User.class);
		assertEquals("Couldn't find user with ID \"" + emailAddress + "\"", HttpStatus.OK, user.getStatusCode());
	}

    private ResponseEntity<User> postSpecificUser() {
        if (null != postedUser) {
            return postedUser;
        }
        postedUser = new TestRestTemplate().postForEntity(
                "http://localhost:" + this.port + "/api/users/" + SPECIFIC_USER.getEmailAddress(), SPECIFIC_USER, User.class);
        return postedUser;
    }

}
