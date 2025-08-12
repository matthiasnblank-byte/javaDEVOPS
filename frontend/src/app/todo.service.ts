import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Todo {
  id?: number;
  title: string;
  completed: boolean;
}

@Injectable({ providedIn: 'root' })
export class TodoService {
  private readonly http = inject(HttpClient);

  list(): Observable<Todo[]> {
    return this.http.get<Todo[]>('/api/todos');
  }

  get(id: number): Observable<Todo> {
    return this.http.get<Todo>(`/api/todos/${id}`);
  }

  create(todo: Omit<Todo, 'id'>): Observable<Todo> {
    return this.http.post<Todo>('/api/todos', todo);
  }

  update(id: number, todo: Omit<Todo, 'id'>): Observable<Todo> {
    return this.http.put<Todo>(`/api/todos/${id}`, todo);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`/api/todos/${id}`);
  }
}

