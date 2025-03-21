import { Component } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Router } from '@angular/router';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';


@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {

   loading: boolean = false;

   registerForm = this.fb.group({
     fullName: ['', Validators.required],
     email: ['', [Validators.required, Validators.email]],
     loanDate: ['', Validators.required],
     returnDate: ['', Validators.required],
     acceptRules: [false, Validators.requiredTrue]
   });

   constructor(
    private fb: FormBuilder,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

   onRegister() {
    if (this.registerForm.valid) {
      this.loading = true;

      setTimeout(() => {
        this.loading = false;
        this.snackBar.open('User successfully registered', 'Cerrar', { duration: 3000 });
        this.router.navigate(['/']);
      }, 2000);
    }
  }
}
