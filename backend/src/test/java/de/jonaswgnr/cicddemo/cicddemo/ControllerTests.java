package de.jonaswgnr.cicddemo.cicddemo;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = {"controller.untested.enabled=false"})
public class ControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testedEndpointShouldReturnDemoMessage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/demo/tested",
                String.class)).contains("Demo Application for CI/CD Pipeline");
    }
}