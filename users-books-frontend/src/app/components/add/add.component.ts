import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

// Services
import { BooksService, Book } from '../../services/books.service';

@Component({
  selector: 'app-add',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.scss']
})
export class AddComponent {

  bookForm = this.fb.group({
    titulo: ['', Validators.required],
    autor: ['', Validators.required],
    genero: ['', Validators.required],
    disponibilidad: [true, Validators.required]
  });

  constructor(
     private fb: FormBuilder,
     private router: Router,
     private booksService: BooksService
    ) {}

  onSave() {
    if (this.bookForm.valid) {
      const formValue = this.bookForm.value;
      const newBook: Book = {
        titulo: formValue.titulo!,
        autor: formValue.autor!,
        genero: formValue.genero!,
        disponibilidad: formValue.disponibilidad!
      };
      this.booksService.addBook(newBook).subscribe({
        next: (book) => {
          console.log('Libro agregado:', book);
          this.router.navigate(['/']);
        },
        error: (error) => {
          console.error('Error al agregar el libro:', error);
        }
      });
    }
  }

  onCancel() {
    this.router.navigate(['/']);
  }

}
