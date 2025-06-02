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

  constructor(
    private profileService: ProfileService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadProfile();
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

  savePersonalInfo(): void {
    this.profileService.updatePersonalInfo(this.personalInfo).subscribe({
      next: (response: ProfileData) => {
        this.user = response;
        this.isEditingPersonal = false;
        this.showAlert('Perfil actualizado correctamente', 'success');
      },
      error: (error: any) => {
        console.error('Error al actualizar el perfil:', error);
        this.showAlert('Error al actualizar el perfil', 'error');
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
