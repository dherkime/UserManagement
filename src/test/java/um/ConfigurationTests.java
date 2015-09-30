package um;

import java.util.List;
import java.util.Map;

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

}
