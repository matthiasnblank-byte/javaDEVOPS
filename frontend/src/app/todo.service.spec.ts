import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { TodoService, Todo } from './todo.service';

describe('TodoService', () => {
  let service: TodoService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(TodoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should list todos', () => {
    const mock: Todo[] = [{ id: 1, title: 'A', completed: false }];
    service.list().subscribe((todos) => {
      expect(todos.length).toBe(1);
      expect(todos[0].title).toBe('A');
    });
    const req = httpMock.expectOne('/api/todos');
    expect(req.request.method).toBe('GET');
    req.flush(mock);
  });

  it('should create todo', () => {
    const payload = { title: 'A', completed: false };
    const created: Todo = { id: 1, ...payload };
    service.create(payload).subscribe((todo) => {
      expect(todo.id).toBe(1);
    });
    const req = httpMock.expectOne('/api/todos');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);
    req.flush(created);
  });
});


