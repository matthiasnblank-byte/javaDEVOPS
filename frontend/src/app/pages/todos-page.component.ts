import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Todo, TodoService } from '../todo.service';

@Component({
  selector: 'app-todos-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="text-gray-900" data-cy="todos-page">
      <div class="max-w-2xl mx-auto p-6">
        <h2 class="text-2xl font-semibold mb-4">Todos</h2>

        <form (ngSubmit)="addTodo()" class="flex gap-2 mb-6" data-cy="add-form">
          <input
            class="flex-1 border rounded px-3 py-2"
            type="text"
            placeholder="Neues Todo..."
            [(ngModel)]="newTitle"
            name="title"
            required
            data-cy="new-todo-input"
          />
          <button class="bg-blue-600 text-white px-4 py-2 rounded" type="submit" data-cy="add-btn">Hinzufügen</button>
        </form>

        <ul class="space-y-2" data-cy="todo-list">
          <li *ngFor="let t of todos(); trackBy: trackById" class="bg-white rounded border p-3 flex items-center justify-between" [attr.data-cy]="'todo-item-' + t.id">
            <label class="flex items-center gap-2">
              <input type="checkbox" [checked]="t.completed" (change)="toggleCompleted(t)" data-cy="toggle" />
              <span [class.line-through]="t.completed">{{ t.title }}</span>
            </label>
            <button class="text-sm text-red-600" (click)="remove(t)" data-cy="delete-btn">Löschen</button>
          </li>
        </ul>
      </div>
    </div>
  `,
})
export class TodosPageComponent {
  private readonly todoService = inject(TodoService);

  todos = signal<Todo[]>([]);
  newTitle = '';

  constructor() {
    this.load();
  }

  trackById(_: number, item: Todo): number | undefined {
    return item.id;
  }

  load(): void {
    this.todoService.list().subscribe((items) => this.todos.set(items));
  }

  addTodo(): void {
    const title = this.newTitle.trim();
    if (!title) return;
    this.todoService.create({ title, completed: false }).subscribe(() => {
      this.newTitle = '';
      this.load();
    });
  }

  toggleCompleted(todo: Todo): void {
    const updated: Omit<Todo, 'id'> = { title: todo.title, completed: !todo.completed };
    this.todoService.update(todo.id!, updated).subscribe(() => this.load());
  }

  remove(todo: Todo): void {
    this.todoService.delete(todo.id!).subscribe(() => this.load());
  }
}


