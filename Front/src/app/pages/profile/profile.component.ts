import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ProfileService, ProfileData } from '../../services/profile.service';
import { Router } from '@angular/router';

interface Reserva {
  id: number;
  camper: string;
  estado: string;
  fecha: string;
  ubicacion: string;
  personas: number;
  precio: number;
}

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    HttpClientModule
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent implements OnInit {
  user: ProfileData | null = null;
  isLoading = true;
  error: string | null = null;
  isEditingPersonal = false;
  fechaActual: string = new Date().toISOString().split('T')[0];
  fechaMinima: string = (() => {
    const fecha = new Date();
    fecha.setFullYear(fecha.getFullYear() - 100);
    return fecha.toISOString().split('T')[0];
  })();
  personalInfo: ProfileData = {
    nombre: '',
    apellido: '',
    email: '',
    telefono: '',
    fechaNacimiento: '',
    direccion: '',
    dni: '',
    numeroLicencia: '',
    fechaExpedicion: '',
    fechaCaducidad: '',
    anosExperiencia: 0
  };
  reservas: Reserva[] = [];
  alertMessage: string | null = null;
  alertType: 'success' | 'error' | null = null;
  alertTimeout: any = null;

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
      format: 'El email debe tener un formato válido'
    },
    telefono: {
      required: 'El teléfono es obligatorio',
      format: 'El teléfono debe tener 9 números'
    },
    dni: {
      required: 'El DNI es obligatorio',
      format: 'El DNI debe tener un formato válido'
    },
    fechaNacimiento: 'La fecha de nacimiento debe tener un formato válido',
    direccion: 'La dirección debe tener un formato válido'
  };

  fieldErrors: { [key: string]: string | null } = {};

  constructor(
    private profileService: ProfileService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadProfile();
  }

  // Método para formatear la fecha ISO a formato europeo 
  formatFechaEuropea(fechaIso: string | null | undefined): string {
    if (!fechaIso) return '';
    const date = new Date(fechaIso);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
  }

  // Método para convertir la fecha ISO a formato europeo en el input type="date"
  get fechaNacimientoInput(): string {
    if (!this.personalInfo.fechaNacimiento) return '';
    return this.personalInfo.fechaNacimiento.split('T')[0];
  }

  // Método para manejar el cambio de fecha en el input
  onFechaNacimientoChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.value) {
      // Convertir la fecha del input a formato ISO
      const date = new Date(input.value);
      this.personalInfo.fechaNacimiento = date.toISOString();
    } else {
      this.personalInfo.fechaNacimiento = '';
    }
  }

  loadProfile(): void {
    this.isLoading = true;
    this.error = null;

    this.profileService.getProfileData().subscribe({
      next: (data: ProfileData) => {
        this.user = data;
        this.personalInfo = { ...data };
        this.loadReservations();
        this.isLoading = false;
      },
      error: (error: any) => {
        console.error('Error completo al cargar el perfil:', error);
        this.error = 'Error al cargar el perfil. Por favor, intenta de nuevo.';
        this.isLoading = false;
      }
    });
  }

  toggleEdit(section: string): void {
    if (section === 'personal') {
      this.isEditingPersonal = !this.isEditingPersonal;
    }
  }

  // Función para calcular la letra correcta del DNI
  private calcularLetraDNI(numero: string): string {
    const letras = 'TRWAGMYFPDXBNJZSQVHLCKE';
    return letras[parseInt(numero, 10) % 23];
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
      case 'direccion':
        if (!value) return null; // Dirección es opcional
        return value.trim().length > 0 ? null : this.errorMessages.direccion;
      case 'email':
        if (!value || value.trim().length === 0) {
          return this.errorMessages.email.required;
        }
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(value) ? null : this.errorMessages.email.format;
      case 'telefono':
        if (!value || value.trim().length === 0) {
          return this.errorMessages.telefono.required;
        }
        const phoneRegex = /^[0-9]{9}$/;
        return phoneRegex.test(value) ? null : this.errorMessages.telefono.format;
      case 'fechaNacimiento':
        if (!value) return null; // Fecha de nacimiento es opcional
        const date = new Date(value);
        return !isNaN(date.getTime()) ? null : this.errorMessages.fechaNacimiento;
      default:
        return null;
    }
  }

  // Método para validar todos los campos
  validateForm(): boolean {
    let isValid = true;
    this.fieldErrors = {};

    // Validar campos obligatorios
    Object.keys(this.errorMessages).forEach(field => {
      const value = this.personalInfo[field as keyof ProfileData];
      const error = this.validateField(field, value);
      if (error) {
        this.fieldErrors[field] = error;
        isValid = false;
      }
    });

    return isValid;
  }

  savePersonalInfo(): void {
    if (!this.validateForm()) {
      this.showAlert('Por favor, corrige los errores en el formulario', 'error');
      return;
    }

    this.isLoading = true;
    this.error = null;

    this.profileService.updatePersonalInfo(this.personalInfo).subscribe({
      next: (response: ProfileData) => {
        this.user = response;
        this.personalInfo = { ...response };
        this.isEditingPersonal = false;
        this.isLoading = false;
        this.showAlert('Perfil actualizado correctamente', 'success');
      },
      error: (error: any) => {
        console.error('Error al actualizar el perfil:', error);
        this.isLoading = false;

        if (error.status === 400) {
          // Error de validación del servidor
          const errorMessage = error.error?.message || error.error;
          if (typeof errorMessage === 'string') {
            if (errorMessage.toLowerCase().includes('dni')) {
              this.fieldErrors['dni'] = this.errorMessages.dni.format;
              this.showAlert('Por favor, corrige los errores en el formulario', 'error');
            } else {
              this.error = errorMessage;
              this.showAlert(errorMessage, 'error');
            }
          } else {
            this.error = 'Error de validación en el servidor';
            this.showAlert('Error de validación en el servidor', 'error');
          }
        } else if (error.status === 500) {
          this.error = 'Error interno del servidor. Por favor, intenta de nuevo más tarde.';
          this.showAlert(this.error, 'error');
        } else {
          this.error = error.message || 'Error al actualizar el perfil';
          this.showAlert(this.error || 'Error al actualizar el perfil', 'error');
        }
      }
    });
  }

  loadReservations(): void {
    this.profileService.getReservations().subscribe({
      next: (data: Reserva[]) => {
        this.reservas = data;
      },
      error: (error: any) => {
        console.error('Error al cargar las reservas:', error);
      }
    });
  }

  confirmDelete(): void {
    if (confirm('¿Estás seguro de que quieres eliminar tu cuenta? Esta acción no se puede deshacer.')) {
      this.deleteAccount();
    }
  }

  deleteAccount(): void {
    this.profileService.deleteAccount().subscribe({
      next: () => {
        this.showAlert('Cuenta eliminada correctamente', 'success');
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 1500);
      },
      error: (error: any) => {
        console.error('Error al eliminar la cuenta:', error);
        this.showAlert('Error al eliminar la cuenta', 'error');
      }
    });
  }

  showAlert(message: string, type: 'success' | 'error') {
    this.alertMessage = message;
    this.alertType = type;
    if (this.alertTimeout) {
      clearTimeout(this.alertTimeout);
    }
    this.alertTimeout = setTimeout(() => {
      this.alertMessage = null;
      this.alertType = null;
    }, 3000);
  }
}
