package com.example.devops.todo;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    public Optional<Todo> getTodoById(Long id) {
        return todoRepository.findById(id);
    }

    public Todo create(Todo toCreate) {
        toCreate.setId(null);
        return todoRepository.save(toCreate);
    }

    public Todo update(Long id, Todo updated) {
        Todo existing = todoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Todo not found: " + id));
        existing.setTitle(updated.getTitle());
        existing.setCompleted(updated.isCompleted());
        return todoRepository.save(existing);
    }

    public void delete(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new NoSuchElementException("Todo not found: " + id);
        }
        todoRepository.deleteById(id);
    }
}


