import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/todos-page.component').then(m => m.TodosPageComponent)
  }
];
