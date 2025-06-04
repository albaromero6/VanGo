import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { VehicleService, Vehicle } from '../../services/vehicle.service';
import { AuthService } from '../../services/auth.service';
import { Subscription } from 'rxjs';
import Swal, { SweetAlertResult } from 'sweetalert2';

@Component({
  selector: 'app-catalog',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './catalog.component.html',
  styleUrl: './catalog.component.scss'
})
export class CatalogComponent implements OnInit, OnDestroy {
  vehicles: Vehicle[] = [];
  loading: boolean = true;
  error: string | null = null;
  isAdmin: boolean = false;
  private authSubscription: Subscription;

  constructor(
    private vehicleService: VehicleService,
    private router: Router,
    private authService: AuthService
  ) {
    this.authSubscription = this.authService.currentUser$.subscribe(user => {
      this.isAdmin = this.authService.isAdmin();
    });
  }

  ngOnInit(): void {
    this.loadVehicles();
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  loadVehicles(): void {
    this.loading = true;
    this.vehicleService.getAllVehicles().subscribe({
      next: (data) => {
        console.log('Vehicles data:', data);
        this.vehicles = data;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Error al cargar los vehículos. Por favor, inténtalo de nuevo más tarde.';
        this.loading = false;
        console.error('Error loading vehicles:', error);
      }
    });
  }

  getVehicleImage(vehicle: Vehicle): string {
    if (!vehicle.imagen) {
      return 'assets/img/placeholder-vehicle.jpg';
    }
    return `http://localhost:8080/api/vehiculos/imagen/${vehicle.imagen}`;
  }

  getVehicleName(vehicle: Vehicle): string {
    if (!vehicle) {
      return 'Vehículo sin nombre';
    }

    if (!vehicle.modelo) {
      return 'Modelo no especificado';
    }

    const marcaNombre = vehicle.modelo.marca?.nombre || 'Sin marca';
    const modeloNombre = vehicle.modelo.nombre || 'Sin modelo';

    return `${marcaNombre} ${modeloNombre}`;
  }

  formatCombustible(combustible: string): string {
    switch (combustible.toUpperCase()) {
      case 'GASOLINA':
        return 'Gasolina';
      case 'DIESEL':
        return 'Diésel';
      default:
        return combustible;
    }
  }

  formatTransmision(transmision: string): string {
    switch (transmision.toUpperCase()) {
      case 'MANUAL':
        return 'Manual';
      case 'AUTOMATICO':
        return 'Automático';
      default:
        return transmision;
    }
  }

  onImageError(event: Event): void {
    const imgElement = event.target as HTMLImageElement;
    imgElement.src = 'assets/img/placeholder-vehicle.jpg';
    imgElement.onerror = null;
  }

  verDetalles(vehicleId: number): void {
    this.router.navigate(['/detalles', vehicleId]);
  }


  editarVehiculo(vehicleId: number): void {
    this.router.navigate(['/detalles', vehicleId], { queryParams: { edit: 'true' } });
  }


  eliminarVehiculo(vehicleId: number): void {
    Swal.fire({
      title: '¿Estás seguro?',
      text: "No podrás revertir esta acción",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#05889C',
      cancelButtonColor: '#EFBF14',
      confirmButtonText: 'Eliminar',
      cancelButtonText: 'Volver',
      customClass: {
        popup: 'swal2-popup',
        title: 'swal2-title',
        htmlContainer: 'swal2-html-container',
        confirmButton: 'swal2-confirm',
        cancelButton: 'swal2-cancel'
      },
      buttonsStyling: true,
      reverseButtons: true
    }).then((result: SweetAlertResult) => {
      if (result.isConfirmed) {
        this.vehicleService.deleteVehicle(vehicleId).subscribe({
          next: () => {
            // Elimina el vehículo del array
            this.vehicles = this.vehicles.filter(v => v.idVeh !== vehicleId);
            // Muestra el mensaje de éxito
            Swal.fire({
              title: '¡Eliminado!',
              text: 'El vehículo ha sido eliminado correctamente',
              icon: 'success',
              confirmButtonColor: '#05889C',
              confirmButtonText: 'Aceptar',
              customClass: {
                popup: 'swal2-popup',
                title: 'swal2-title',
                htmlContainer: 'swal2-html-container',
                confirmButton: 'swal2-confirm'
              },
              buttonsStyling: true
            });
          },
          error: (errorMessage: string) => {
            console.error('Error al eliminar el vehículo:', errorMessage);
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
        });
      }
    });
  }

  anadirVehiculo(): void {
    this.router.navigate(['/detalles/new']);
  }
}
