package com.example.devops.web;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "devops-todo");
        info.put("message", "Willkommen zur Todo-API. Siehe /api/todos");
        info.put("endpoints", new String[]{"GET /api/todos", "POST /api/todos", "GET /api/todos/{id}", "PUT /api/todos/{id}", "DELETE /api/todos/{id}"});
        return info;
    }
}


