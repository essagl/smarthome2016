package org.openhapi.smarthome2016.server;

import com.fasterxml.jackson.datatype.joda.JodaMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.*;
import org.openhapi.smarthome2016.server.core.BoardData;
import org.openhpi.smarthome2016.ServiceMockImpl;
import org.openhapi.smarthome2016.server.core.User;
import org.openhapi.smarthome2016.server.resources.AbstractResourceTest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import java.io.File;
import java.io.IOException;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static org.assertj.core.api.Assertions.assertThat;

public class SmarthomeBoardIntegrationTest {

    private static final String TMP_FILE = createTempFile();
    private static final String CONFIG_PATH = resourceFilePath("test-smarthome-board.yml");


    /**
     * configure application and start test server
     */
    @ClassRule
    public static final DropwizardAppRule<SmarthomeConfiguration> RULE =
            new DropwizardAppRule<SmarthomeConfiguration>(SmarthomeApplication.class,
                    CONFIG_PATH,
                    ConfigOverride.config("database.url", "jdbc:h2:" + TMP_FILE));


    private Client client;

    @BeforeClass
    public static void migrateDb() throws Exception {
        RULE.getApplication().run("db", "migrate", CONFIG_PATH);

    }

    @Before
    public void setUp() throws Exception {
        RULE.getObjectMapper().registerModule(new JodaModule());
        client = ClientBuilder.newClient();

    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }


    @Test
    public void testGetAllValues() throws Exception {
        ServiceMockImpl serviceMock = new ServiceMockImpl();
        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        provider.setMapper(new JodaMapper());

        final BoardData messuredValues = client.target("http://localhost:" + RULE.getLocalPort() + "/board")
                .register(provider)
                .request()
                .get(BoardData.class);

        assertThat(messuredValues.getHumidity()).isEqualTo(serviceMock.getHumidity());
    }


    @Test
    public void testUsersAccess() throws Exception {
        String authHeaderValue = AbstractResourceTest.getAuthorizationHeaderValue("admin","secret");
        User hans = new User("Hans","secret");
        final User storedUser = client.target("http://localhost:" + RULE.getLocalPort() + "/user")
                .request()
                .header(HttpHeaders.AUTHORIZATION, authHeaderValue)
                .post(Entity.entity(hans, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(User.class);
        assertThat(storedUser.getId()).isNotNull();
        assertThat(hans).isEqualTo(storedUser);
    }


    private static String createTempFile() {
        try {
            return File.createTempFile("test-smarthome", null).getAbsolutePath();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
