// login.component.ts
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, HttpClientModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  form: FormGroup;
  serverError: string | null = null;
  isSubmitting = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      email: [''],
      password: ['']
    });
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.isSubmitting = true;
      this.serverError = null;

      const { email, password } = this.form.value;

      this.authService.login(email, password).subscribe({
        next: (response) => {
          const returnUrl = this.router.getCurrentNavigation()?.extras?.state?.['returnUrl'] || '/';
          this.router.navigate([returnUrl]);
        },
        error: (error) => {
          console.error('Error en el login:', error);
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Credenciales inválidas. Por favor, inténtalo de nuevo.',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#3085d6'
          });
          this.isSubmitting = false;
        }
      });
    }
  }
}
