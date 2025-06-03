import { Component } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [RouterLink, FormsModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  showPassword = false;
  formData = {
    nombre: '',
    apellido: '',
    dni: '',
    telefono: '',
    email: '',
    password: '',
    terminos: false
  };

  // Mensajes de error
  errorMessages = {
    nombre: {
      required: 'El nombre es obligatorio',
      format: 'El nombre solo puede contener letras'
    },
    apellido: {
      required: 'El apellido es obligatorio',
      format: 'El apellido solo puede contener letras'
    },
    email: {
      required: 'El email es obligatorio',
      format: 'El email debe tener un formato válido',
      duplicate: 'Este email ya está registrado'
    },
    telefono: {
      required: 'El teléfono es obligatorio',
      format: 'El teléfono debe tener exactamente 9 números'
    },
    dni: {
      required: 'El DNI es obligatorio',
      format: 'El DNI debe tener un formato válido y la letra en mayúsculas',
      duplicate: 'Este DNI ya está registrado'
    },
    password: {
      required: 'La contraseña es obligatoria',
      format: 'La contraseña debe tener al menos 8 caracteres, una mayúscula y un número'
    },
    terminos: {
      required: 'Debes aceptar los términos y la política de privacidad'
    }
  };

  fieldErrors: { [key: string]: string | null } = {};
  isSubmitting = false;
  serverError: string | null = null;

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  // Método para validar un campo específico
  validateField(field: string, value: any): string | null {
    switch (field) {
      case 'nombre':
      case 'apellido':
        if (!value || value.trim().length === 0) {
          return this.errorMessages[field].required;
        }
        const textRegex = /^[a-zA-ZÀ-ÿ\u00f1\u00d1\s]+$/;
        return textRegex.test(value) ? null : this.errorMessages[field].format;

      case 'dni':
        if (!value || value.trim().length === 0) {
          return this.errorMessages.dni.required;
        }
        const dniRegex = /^[0-9]{8}[A-Z]$/;
        if (!dniRegex.test(value)) {
          return this.errorMessages.dni.format;
        }
        // Validar la letra del DNI
        const numero = value.substring(0, 8);
        const letra = value.charAt(8).toUpperCase();
        if (this.calcularLetraDNI(numero) !== letra) {
          return this.errorMessages.dni.format;
        }
        return null;

      case 'email':
        if (!value || value.trim().length === 0) {
          return this.errorMessages.email.required;
        }
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(value)) {
          return this.errorMessages.email.format;
        }
        return null;

      case 'telefono':
        if (!value || value.trim().length === 0) {
          return this.errorMessages.telefono.required;
        }
        const numeroLimpio = value.replaceAll("[\\s\\-()+]", "");
        if (!numeroLimpio.match(/^\d{9}$/)) {
          return this.errorMessages.telefono.format;
        }
        return null;

      case 'password':
        if (!value || value.trim().length === 0) {
          return this.errorMessages.password.required;
        }
        // Verificar longitud mínima de 8 caracteres
        if (value.length < 8) {
          return this.errorMessages.password.format;
        }
        // Verificar que contenga al menos una mayúscula
        if (!value.match(/.*[A-Z].*/)) {
          return this.errorMessages.password.format;
        }
        // Verificar que contenga al menos un número
        if (!value.match(/.*\d.*/)) {
          return this.errorMessages.password.format;
        }
        return null;

      default:
        return null;
    }
  }

  // Método para calcular la letra del DNI
  private calcularLetraDNI(numero: string): string {
    const LETRAS = "TRWAGMYFPDXBNJZSQVHLCKE";
    const numeroDNI = parseInt(numero);
    return LETRAS.charAt(numeroDNI % 23);
  }

  // Método para validar todos los campos
  validateForm(): boolean {
    let isValid = true;
    // Guardar los errores de DNI y email si existen
    const dniError = this.fieldErrors['dni'];
    const emailError = this.fieldErrors['email'];
    this.fieldErrors = {};

    // Validar todos los campos excepto términos
    Object.keys(this.formData).forEach(field => {
      if (field !== 'terminos') {
        const value = this.formData[field as keyof typeof this.formData];
        const error = this.validateField(field, value);
        if (error) {
          this.fieldErrors[field] = error;
          isValid = false;
        }
      }
    });

    // Solo validar términos si no hay otros errores
    if (isValid && !this.formData.terminos) {
      this.fieldErrors['terminos'] = this.errorMessages.terminos.required;
      isValid = false;
    }

    // Restaurar los errores de DNI y email si existían
    if (dniError) {
      this.fieldErrors['dni'] = dniError;
      isValid = false;
    }
    if (emailError) {
      this.fieldErrors['email'] = emailError;
      isValid = false;
    }

    return isValid;
  }

  onSubmit() {
    console.log('Iniciando validación del formulario');
    this.isSubmitting = true;
    this.serverError = null;

    // Primero validar todos los campos
    if (!this.validateForm()) {
      this.isSubmitting = false;
      return;
    }

    // Verificar duplicación de DNI y email antes de proceder
    if (typeof this.formData.dni === 'string' && typeof this.formData.email === 'string') {
      // Primero verificar el DNI
      this.authService.checkDniExists(this.formData.dni).subscribe({
        next: (dniExists) => {
          if (dniExists) {
            this.fieldErrors['dni'] = this.errorMessages.dni.duplicate;
            this.isSubmitting = false;
            return;
          }
          // Si el DNI no está duplicado, verificar el email
          this.authService.checkEmailExists(this.formData.email).subscribe({
            next: (emailExists) => {
              if (emailExists) {
                this.fieldErrors['email'] = this.errorMessages.email.duplicate;
                this.isSubmitting = false;
                return;
              }
              // Si ni el DNI ni el email están duplicados, sigue con el registro
              this.proceedWithRegistration();
            },
            error: (error) => {
              console.error('Error al verificar email:', error);
              this.isSubmitting = false;
              this.serverError = 'Error al verificar el email. Por favor, inténtalo de nuevo.';
            }
          });
        },
        error: (error) => {
          console.error('Error al verificar DNI:', error);
          this.isSubmitting = false;
          this.serverError = 'Error al verificar el DNI. Por favor, inténtalo de nuevo.';
        }
      });
    }
  }

  private proceedWithRegistration() {
    const userData = {
      nombre: this.capitalizeFirstLetter(this.formData.nombre),
      apellido: this.capitalizeFirstLetter(this.formData.apellido),
      dni: this.formData.dni,
      telefono: this.formData.telefono,
      email: this.formData.email.toLowerCase(),
      password: this.formData.password
    };

    this.authService.register(userData).subscribe({
      next: (response) => {
        console.log('Usuario registrado exitosamente:', response);
        this.router.navigate(['/login']);
      },
      error: (error) => {
        console.error('Error en el registro:', error);
        this.isSubmitting = false;
        if (error.status === 409) {
          if (error.error?.message?.includes('DNI')) {
            this.fieldErrors['dni'] = this.errorMessages.dni.duplicate;
          } else if (error.error?.message?.includes('email')) {
            this.fieldErrors['email'] = this.errorMessages.email.duplicate;
          }
        } else {
          this.serverError = 'Error al registrar el usuario. Por favor, inténtalo de nuevo.';
        }
      }
    });
  }

  // Método para poner en mayúscula la primera letra de cada palabra
  private capitalizeFirstLetter(text: string): string {
    if (!text) return '';

    return text
      .toLowerCase()
      .split(' ')
      .map(word => word.charAt(0).toUpperCase() + word.slice(1))
      .join(' ');
  }

  // Método para validar campos individuales en tiempo real
  validateFieldOnBlur(field: string) {
    const value = this.formData[field as keyof typeof this.formData];
    const error = this.validateField(field, value);

    if (!error) {
      // Si no hay error de formato, verificar duplicación
      if (field === 'dni' && typeof value === 'string') {
        this.authService.checkDniExists(value).subscribe({
          next: (exists) => {
            if (exists) {
              this.fieldErrors[field] = this.errorMessages.dni.duplicate;
            } else {
              // Si el DNI no está duplicado, limpiar el error
              this.fieldErrors[field] = null;
            }
          },
          error: (error) => {
            console.error('Error al verificar DNI:', error);
            this.fieldErrors[field] = null;
            this.serverError = 'Error al verificar el DNI. Por favor, inténtalo de nuevo.';
          }
        });
      } else if (field === 'email' && typeof value === 'string') {
        this.authService.checkEmailExists(value).subscribe({
          next: (exists) => {
            if (exists) {
              this.fieldErrors[field] = this.errorMessages.email.duplicate;
            } else {
              this.fieldErrors[field] = null;
            }
          },
          error: (error) => {
            console.error('Error al verificar email:', error);
            if (error.status === 500) {
              this.fieldErrors[field] = this.errorMessages.email.duplicate;
            } else {
              this.fieldErrors[field] = null;
            }
          }
        });
      }
    } else {
      this.fieldErrors[field] = error;
    }
  }
}
