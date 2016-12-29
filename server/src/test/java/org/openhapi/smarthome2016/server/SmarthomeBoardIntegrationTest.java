package org.openhapi.smarthome2016.server;

import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.*;
import org.openhapi.smarthome2016.server.api.MessuredValues;
import org.openhapi.smarthome2016.server.board.ServiceMockImpl;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static org.assertj.core.api.Assertions.assertThat;

public class SmarthomeBoardIntegrationTest {

    /**
     * configure application and start test server
     */
    @ClassRule
    public static final DropwizardAppRule<SmarthomeConfiguration> RULE =
            new DropwizardAppRule<SmarthomeConfiguration>(SmarthomeApplication.class,
                    resourceFilePath("test-smarthome-board.yml"));


    private Client client;


    @Before
    public void setUp() throws Exception {
        client = ClientBuilder.newClient();

    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }


    @Test
    public void testGetAllValues() throws Exception {
        ServiceMockImpl serviceMock = new ServiceMockImpl();
        final MessuredValues messuredValues = client.target("http://localhost:" + RULE.getLocalPort() + "/board")
                .request()
                .get(MessuredValues.class);

        assertThat(messuredValues.getHumidity()).isEqualTo(serviceMock.getHumidity());
    }


}
