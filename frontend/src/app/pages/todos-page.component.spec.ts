import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TodosPageComponent } from './todos-page.component';
import { TodoService } from '../todo.service';
import { of } from 'rxjs';

describe('TodosPageComponent', () => {
  let fixture: ComponentFixture<TodosPageComponent>;
  let component: TodosPageComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TodosPageComponent],
      providers: [
        {
          provide: TodoService,
          useValue: {
            list: () => of([{ id: 1, title: 'X', completed: false }]),
            create: () => of({ id: 2, title: 'Y', completed: false }),
            update: () => of({ id: 1, title: 'X', completed: true }),
            delete: () => of(void 0),
          },
        },
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(TodosPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should render initial list', () => {
    const list = fixture.nativeElement.querySelector('[data-cy="todo-list"]');
    expect(list).toBeTruthy();
  });
});


