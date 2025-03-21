import { Routes } from '@angular/router';
import { BooksListComponent } from './components/books-list/books-list.component';
import { AddComponent } from './components/add/add.component';
import { RegisterComponent } from './components/register/register.component';

export const routes: Routes = [
  { path: '', component: BooksListComponent },
  { path: 'add', component: AddComponent },
  { path: 'register', component: RegisterComponent}
];
