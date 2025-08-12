package com.example.devops.todo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoE2ETest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    private String baseUrl(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void todoCrudFlow_shouldWork() {
        // Create
        Todo toCreate = new Todo(null, "First", false);
        ResponseEntity<Todo> createResp = restTemplate.postForEntity(baseUrl("/api/todos"), toCreate, Todo.class);
        assertEquals(HttpStatus.CREATED, createResp.getStatusCode());
        assertNotNull(createResp.getBody());
        Long id = createResp.getBody().getId();
        assertNotNull(id);

        // List
        ResponseEntity<Todo[]> listResp = restTemplate.getForEntity(baseUrl("/api/todos"), Todo[].class);
        assertEquals(HttpStatus.OK, listResp.getStatusCode());
        assertNotNull(listResp.getBody());
        assertTrue(listResp.getBody().length >= 1);

        // Get single
        ResponseEntity<Todo> getResp = restTemplate.getForEntity(baseUrl("/api/todos/" + id), Todo.class);
        assertEquals(HttpStatus.OK, getResp.getStatusCode());
        assertNotNull(getResp.getBody());
        assertEquals("First", getResp.getBody().getTitle());

        // Update
        Todo updated = new Todo(null, "Updated Title", true);
        ResponseEntity<Todo> updateResp = restTemplate.exchange(
                baseUrl("/api/todos/" + id), HttpMethod.PUT, new HttpEntity<>(updated), Todo.class);
        assertEquals(HttpStatus.OK, updateResp.getStatusCode());
        assertNotNull(updateResp.getBody());
        assertEquals("Updated Title", updateResp.getBody().getTitle());
        assertTrue(updateResp.getBody().isCompleted());

        // Delete
        restTemplate.delete(baseUrl("/api/todos/" + id));

        // Verify 404 after delete
        ResponseEntity<Todo> afterDelete = restTemplate.getForEntity(baseUrl("/api/todos/" + id), Todo.class);
        assertEquals(HttpStatus.NOT_FOUND, afterDelete.getStatusCode());
    }
}


