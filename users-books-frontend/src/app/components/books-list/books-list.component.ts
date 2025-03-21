import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';

// Services
import { BooksService, Book } from '../../services/books.service';

// Dialog
import { MatDialog} from '@angular/material/dialog';
import { EditBookDialogComponent } from '../edit-book-dialog/edit-book-dialog.component';

@Component({
  selector: 'app-books-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatInputModule,
    MatButtonModule,
    MatTableModule,
    MatFormFieldModule,
    MatIconModule
  ],
  templateUrl: './books-list.component.html',
  styleUrls: ['./books-list.component.scss']
})
export class BooksListComponent implements OnInit {

  searchTerm: string = '';
  errorMessage: string = '';

  books: Book[] = [];
  filteredBooks: Book[] = [];

  constructor(private booksService: BooksService,  private dialog: MatDialog) { }



  ngOnInit() {
    this.loadBooks();
  }

  loadBooks() {
    this.booksService.getBooks().subscribe({
      next: (data) => {
        this.books = data;
        this.filteredBooks = data;
      },
      error: () => {
        this.errorMessage = 'Error al cargar los libros';
      }
    });
  }

  searchBooks() {
    if (this.searchTerm.trim() === '') {
      this.filteredBooks = this.books;
      this.errorMessage = '';
    } else {
      const term = this.searchTerm.toLowerCase();
      const filtered = this.books.filter(book => book.genero.toLowerCase().includes(term));
      if (filtered.length === 0) {
        this.errorMessage = 'No books of that genre were found.';
      } else {
        this.errorMessage = '';
      }
      this.filteredBooks = filtered;
    }
  }

  editBook(book: Book) {
    const dialogRef = this.dialog.open(EditBookDialogComponent, {
      width: '400px',
      data: { book }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.booksService.updateBook(result).subscribe({
          next: updatedBook => {
            console.log('Libro actualizado:', updatedBook);
            this.books = this.books.map(b => b.id === updatedBook.id ? updatedBook : b);
            this.filteredBooks = [...this.books];
          },
          error: () => {
            console.error('Error al actualizar el libro');
          }
        });
      }
    });
  }

  deleteBook(book: Book) {
    if (book.id) {
      this.booksService.deleteBook(book.id).subscribe({
        next: () => {
          this.books = this.books.filter(b => b.id !== book.id);
          this.filteredBooks = this.books;
        },
        error: () => {
          console.error('Error eliminando el libro');
        }
      });
    }
  }
}
