import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HttpErrorResponse } from '@angular/common/http';
import { ProfileService, ProfileData } from '../../services/profile.service';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ImageService } from '../../services/image.service';
import Swal from 'sweetalert2';
import { environment } from '../../../environments/environment';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { ReviewService } from '../../services/review.service';

interface Sede {
  idSed: number;
  direccion: string;
  ciudad: string;
  telefono: string;
  imagen: string;
}

interface Vehiculo {
  idVeh: number;
  modelo: string;
  marca: string;
  imagen: string;
  camper: string;
}

interface Reserva {
  idReser: number;
  camper: string;
  estado: string;
  inicio: string;
  fin: string;
  idSed_Salid: Sede;
  idSed_Lleg: Sede;
  total: number;
  vehiculo: Vehiculo;
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
    fecha.setFullYear(fecha.getFullYear() - 18);
    return fecha.toISOString().split('T')[0];
  })();
  fechaMaxima: string = (() => {
    const fecha = new Date();
    fecha.setFullYear(fecha.getFullYear() - 100);
    return fecha.toISOString().split('T')[0];
  })();
  personalInfo: ProfileData = {
    idUsu: 0,
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
    anosExperiencia: 0,
    rol: 'CLIENTE',
    reservas: []
  };
  reservas: Reserva[] = [];
  reservasEnCurso: Reserva[] = [];
  reservasReservadas: Reserva[] = [];
  reservasFinalizadas: Reserva[] = [];
  alertMessage: string | null = null;
  alertType: 'success' | 'error' | null = null;
  alertTimeout: any = null;
  imageCache: { [key: string]: SafeUrl } = {};

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
    @Inject(PLATFORM_ID) private platformId: Object,
    private profileService: ProfileService,
    private router: Router,
    private authService: AuthService,
    private imageService: ImageService,
    private sanitizer: DomSanitizer,
    private reviewService: ReviewService
  ) { }

  ngOnInit(): void {
    this.loadProfile();
  }

  // Método para formatear la fecha ISO a formato europeo 
  formatFechaEuropea(fechaIso: string | undefined): string {
    if (!fechaIso) return '';
    const date = new Date(fechaIso);
    if (isNaN(date.getTime())) return '';
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

  get isClient(): boolean {
    return this.user?.rol === 'CLIENTE';
  }

  get isAdmin(): boolean {
    return this.user?.rol === 'ADMINISTRADOR';
  }

  loadProfile(): void {
    this.isLoading = true;
    this.error = null;

    this.profileService.getProfileData().subscribe({
      next: (data: ProfileData) => {
        this.user = data;
        this.personalInfo = { ...data };
        if (this.isClient) {
          this.loadReservations();
        }
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
      if (this.isEditingPersonal) {
        // Si se cancela la edición, restaurar los datos originales
        if (this.user) {
          this.personalInfo = { ...this.user };
          this.fieldErrors = {};
        }
      }
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
        if (isNaN(date.getTime())) {
          return this.errorMessages.fechaNacimiento;
        }
        // Validar que tenga al menos 18 años
        const hoy = new Date();
        const edad = hoy.getFullYear() - date.getFullYear();
        const mesActual = hoy.getMonth();
        const mesNacimiento = date.getMonth();
        const diaActual = hoy.getDate();
        const diaNacimiento = date.getDate();

        if (edad < 18 || (edad === 18 && (mesActual < mesNacimiento || (mesActual === mesNacimiento && diaActual < diaNacimiento)))) {
          return 'Debes tener al menos 18 años';
        }
        return null;
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
      Swal.fire({
        title: 'Error',
        text: 'Por favor, corrige los errores en el formulario',
        icon: 'error',
        confirmButtonColor: '#05889C',
        customClass: {
          popup: 'swal2-popup',
          title: 'swal2-title',
          htmlContainer: 'swal2-html-container',
          confirmButton: 'swal2-confirm'
        },
        buttonsStyling: true
      });
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
        Swal.fire({
          title: '¡Éxito!',
          text: 'Perfil actualizado correctamente',
          icon: 'success',
          confirmButtonColor: '#05889C',
          customClass: {
            popup: 'swal2-popup',
            title: 'swal2-title',
            htmlContainer: 'swal2-html-container',
            confirmButton: 'swal2-confirm'
          },
          buttonsStyling: true
        });
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
              Swal.fire({
                title: 'Error',
                text: 'Por favor, corrige los errores en el formulario',
                icon: 'error',
                confirmButtonColor: '#05889C',
                customClass: {
                  popup: 'swal2-popup',
                  title: 'swal2-title',
                  htmlContainer: 'swal2-html-container',
                  confirmButton: 'swal2-confirm'
                },
                buttonsStyling: true
              });
            } else {
              this.error = errorMessage;
              Swal.fire({
                title: 'Error',
                text: errorMessage,
                icon: 'error',
                confirmButtonColor: '#05889C',
                customClass: {
                  popup: 'swal2-popup',
                  title: 'swal2-title',
                  htmlContainer: 'swal2-html-container',
                  confirmButton: 'swal2-confirm'
                },
                buttonsStyling: true
              });
            }
          } else {
            this.error = 'Error de validación en el servidor';
            Swal.fire({
              title: 'Error',
              text: 'Error de validación en el servidor',
              icon: 'error',
              confirmButtonColor: '#05889C',
              customClass: {
                popup: 'swal2-popup',
                title: 'swal2-title',
                htmlContainer: 'swal2-html-container',
                confirmButton: 'swal2-confirm'
              },
              buttonsStyling: true
            });
          }
        } else if (error.status === 500) {
          this.error = 'Error interno del servidor. Por favor, intenta de nuevo más tarde.';
          Swal.fire({
            title: 'Error',
            text: this.error,
            icon: 'error',
            confirmButtonColor: '#05889C',
            customClass: {
              popup: 'swal2-popup',
              title: 'swal2-title',
              htmlContainer: 'swal2-html-container',
              confirmButton: 'swal2-confirm'
            },
            buttonsStyling: true
          });
        } else {
          this.error = error.message || 'Error al actualizar el perfil';
          Swal.fire({
            title: 'Error',
            text: this.error || 'Error al actualizar el perfil',
            icon: 'error',
            confirmButtonColor: '#05889C',
            customClass: {
              popup: 'swal2-popup',
              title: 'swal2-title',
              htmlContainer: 'swal2-html-container',
              confirmButton: 'swal2-confirm'
            },
            buttonsStyling: true
          });
        }
      }
    });
  }

  loadReservations(): void {
    this.profileService.getReservations().subscribe({
      next: (data: Reserva[]) => {
        console.log('Datos de reservas recibidos:', JSON.stringify(data, null, 2));
        this.reservas = data;
        this.reservas.forEach(r => {
          console.log('Reserva completa:', JSON.stringify(r, null, 2));
          console.log('Vehículo:', JSON.stringify(r.vehiculo, null, 2));
          if (typeof r.vehiculo === 'object') {
            console.log('Marca:', r.vehiculo.marca);
            console.log('Modelo:', r.vehiculo.modelo);
          }
        });
        // Agrupar reservas por estado y ordenar por fecha de inicio
        this.reservasEnCurso = this.reservas
          .filter(r => r.estado === 'CURSO')
          .sort((a, b) => new Date(a.inicio).getTime() - new Date(b.inicio).getTime());

        this.reservasReservadas = this.reservas
          .filter(r => r.estado === 'RESERVADA')
          .sort((a, b) => new Date(a.inicio).getTime() - new Date(b.inicio).getTime());

        this.reservasFinalizadas = this.reservas
          .filter(r => r.estado === 'FINALIZADA')
          .sort((a, b) => new Date(a.inicio).getTime() - new Date(b.inicio).getTime());

        console.log('Reservas en curso:', JSON.stringify(this.reservasEnCurso, null, 2));
        console.log('Reservas reservadas:', JSON.stringify(this.reservasReservadas, null, 2));
        console.log('Reservas finalizadas:', JSON.stringify(this.reservasFinalizadas, null, 2));
      },
      error: (error: any) => {
        console.error('Error al cargar las reservas:', error);
      }
    });
  }

  deleteAccount(): void {
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'Se eliminará tu cuenta y todas tus reservas',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.profileService.deleteAccount().subscribe({
          next: () => {
            this.authService.logout();
            Swal.fire({
              title: '¡Cuenta eliminada!',
              text: 'Tu cuenta ha sido eliminada correctamente',
              icon: 'success',
              confirmButtonColor: '#3085d6'
            }).then(() => {
              this.router.navigate(['/']);
            });
          },
          error: (error) => {
            console.error('Error al eliminar la cuenta:', error);
            Swal.fire({
              title: 'Error',
              text: 'Ha ocurrido un error, por favor, inténtalo de nuevo más tarde',
              icon: 'error',
              confirmButtonColor: '#3085d6'
            });
          }
        });
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

  // Método para formatear texto con primera letra en mayúscula
  private capitalizeFirstLetter(text: string): string {
    if (!text) return text;
    return text.toLowerCase().split(' ').map(word =>
      word.charAt(0).toUpperCase() + word.slice(1)
    ).join(' ');
  }

  // Método para manejar el cambio en los campos de texto
  onTextChange(field: 'nombre' | 'apellido', event: Event): void {
    const input = event.target as HTMLInputElement;
    const value = input.value;
    this.personalInfo[field] = this.capitalizeFirstLetter(value);
  }

  getImageUrl(imagen: string): SafeUrl {
    if (!imagen) return this.sanitizer.bypassSecurityTrustUrl('assets/img/Coche.png');

    if (this.imageCache[imagen]) {
      return this.imageCache[imagen];
    }

    this.imageService.getImage(imagen).subscribe({
      next: (blob: Blob) => {
        const url = URL.createObjectURL(blob);
        this.imageCache[imagen] = this.sanitizer.bypassSecurityTrustUrl(url);
      },
      error: error => {
        console.error('Error loading image:', error);
        this.imageCache[imagen] = this.sanitizer.bypassSecurityTrustUrl('assets/img/Coche.png');
      }
    });

    return this.sanitizer.bypassSecurityTrustUrl('assets/img/Coche.png');
  }

  navigateToVehicleDetails(vehicleId: number): void {
    this.router.navigate(['/detalles', vehicleId]);
  }

  deleteReservation(idRes: number) {
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'Esta acción no se puede deshacer',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, cancelar',
      cancelButtonText: 'No, mantener',
      customClass: {
        popup: 'swal2-popup',
        confirmButton: 'swal2-confirm',
        cancelButton: 'swal2-cancel'
      }
    }).then((result) => {
      if (result.isConfirmed) {
        this.profileService.deleteReservation(idRes).subscribe({
          next: () => {
            Swal.fire({
              title: '¡Reserva cancelada!',
              text: 'La reserva ha sido cancelada correctamente',
              icon: 'success',
              customClass: {
                popup: 'swal2-popup',
                confirmButton: 'swal2-confirm'
              }
            });
            // Actualizar la lista de reservas
            this.loadReservations();
          },
          error: (error: HttpErrorResponse) => {
            console.error('Error al cancelar la reserva:', error);
            const errorMessages: { [key: number]: string } = {
              403: 'No tienes permisos para cancelar esta reserva',
              401: 'Tu sesión ha expirado. Por favor, vuelve a iniciar sesión'
            };

            const errorMessage = errorMessages[error.status] || 'Ha ocurrido un error al cancelar la reserva';

            if (error.status === 401) {
              this.router.navigate(['/login']);
            }

            Swal.fire({
              title: 'Error',
              text: errorMessage,
              icon: 'error',
              customClass: {
                popup: 'swal2-popup',
                confirmButton: 'swal2-confirm'
              }
            });
          }
        });
      }
    });
  }

  navigateToComments(idReser: number): void {
    this.router.navigate(['/comments', idReser]);
  }

  navigateToAdminPanel(): void {
    if (isPlatformBrowser(this.platformId)) {
      const token = localStorage.getItem('token');
      if (token) {
        window.location.href = `${environment.apiUrl.replace('/api', '')}/admin?token=${token}`;
      }
    }
  }
}
