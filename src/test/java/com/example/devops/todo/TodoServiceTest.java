package com.example.devops.todo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    void create_shouldAssignIdAndReturn() {
        when(todoRepository.save(any(Todo.class))).thenAnswer(inv -> {
            Todo t = inv.getArgument(0);
            t.setId(1L);
            return t;
        });

        Todo created = todoService.create(new Todo(null, "Test", false));

        assertNotNull(created.getId());
        assertEquals("Test", created.getTitle());
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    void getAllTodos_shouldReturnList() {
        when(todoRepository.findAll()).thenReturn(Arrays.asList(
                new Todo(1L, "A", false),
                new Todo(2L, "B", true)
        ));

        List<Todo> all = todoService.getAllTodos();
        assertEquals(2, all.size());
        assertEquals("A", all.get(0).getTitle());
    }

    @Test
    void update_shouldModifyExisting() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(new Todo(1L, "Old", false)));
        when(todoRepository.save(any(Todo.class))).thenAnswer(inv -> inv.getArgument(0));

        Todo updated = todoService.update(1L, new Todo(null, "New", true));
        assertEquals(1L, updated.getId());
        assertEquals("New", updated.getTitle());
        assertTrue(updated.isCompleted());
    }

    @Test
    void delete_shouldCallRepository() {
        when(todoRepository.existsById(5L)).thenReturn(true);
        doNothing().when(todoRepository).deleteById(5L);

        todoService.delete(5L);
        verify(todoRepository, times(1)).deleteById(5L);
    }
}


