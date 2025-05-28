import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { VehicleService, Vehicle } from '../../services/vehicle.service';
import { AuthService } from '../../services/auth.service';
import { Subscription } from 'rxjs';

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
      this.isAdmin = !!user; // Actualizo isAdmin cuando cambia el estado de autenticación
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
    console.log('Navegando a detalles del vehículo:', vehicleId);
    this.router.navigate(['/detalles', vehicleId]);
  }

  editarVehiculo(vehicleId: number): void {
    this.router.navigate(['/admin/vehiculos/editar', vehicleId]);
  }

  eliminarVehiculo(vehicleId: number): void {
    if (confirm('¿Estás seguro de que deseas eliminar este vehículo?')) {
      console.log('Eliminar vehículo:', vehicleId);
    }
  }

  anadirVehiculo(): void {
    this.router.navigate(['/admin/vehiculos/nuevo']);
  }
}
