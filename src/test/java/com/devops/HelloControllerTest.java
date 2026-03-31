package com.devops;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testHelloEndpoint() {
        String response = this.restTemplate.getForObject(
                "http://localhost:" + port + "/hello",
                String.class
        );

        assertThat(response).contains("Hello World!");
    }

    @Test
    public void testEndpoint() {
        String response = this.restTemplate.getForObject(
            "http://localhost:" + port + "/", 
            String.class
        );

        assertThat(response).contains("What are you trying to do ...");
    }
}