// login.component.ts
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, HttpClientModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  form: FormGroup;

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
    const { email, password } = this.form.value;
    this.authService.login(email, password).subscribe({
      next: (token: string) => {
        if (token) {
          localStorage.setItem('token', token);
          this.router.navigate(['/']);
        }
      },
      error: (err) => {
        console.error('Error al iniciar sesión:', err);
        alert('Credenciales inválidas');
      }
    });
  }
}
