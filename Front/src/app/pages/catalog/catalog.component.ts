import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VehicleService, Vehicle } from '../../services/vehicle.service';

@Component({
  selector: 'app-catalog',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './catalog.component.html',
  styleUrl: './catalog.component.scss'
})
export class CatalogComponent implements OnInit {
  vehicles: Vehicle[] = [];
  loading: boolean = true;
  error: string | null = null;

  constructor(private vehicleService: VehicleService) { }

  ngOnInit(): void {
    this.loadVehicles();
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
    return `http://localhost:8080/uploads/vehiculos/${vehicle.imagen}`;
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

  onImageError(event: Event): void {
    const imgElement = event.target as HTMLImageElement;
    imgElement.src = 'assets/img/placeholder-vehicle.jpg';
  }
}
