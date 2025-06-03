// login.component.ts
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';

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

  onSubmit() {
    this.isSubmitting = true;
    this.serverError = null;

    const { email, password } = this.form.value;

    // Solo si ambos campos tienen contenido
    if (email && password) {
      this.authService.login(email, password).subscribe({
        next: (response) => {
          if (response && response.token) {
            this.router.navigate(['/']);
          }
        },
        error: (err) => {
          console.error('Error al iniciar sesión:', err);
          this.serverError = 'Credenciales inválidas';
          this.isSubmitting = false;
        }
      });
    } else {
      this.isSubmitting = false;
    }
  }
}
