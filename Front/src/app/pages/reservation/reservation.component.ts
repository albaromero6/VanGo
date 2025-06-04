import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { VehicleService } from '../../services/vehicle.service';
import { Vehicle } from '../../services/vehicle.service';
import { SedeService, Sede } from '../../services/sede.service';

@Component({
  selector: 'app-reservation',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reservation.component.html',
  styleUrl: './reservation.component.scss'
})
export class ReservationComponent implements OnInit {
  vehicle: Vehicle | null = null;
  loading: boolean = true;
  error: string | null = null;
  sedes: Sede[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private vehicleService: VehicleService,
    private sedeService: SedeService
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadVehicle(Number(id));
    }
    this.sedeService.getSedes().subscribe({
      next: (data) => this.sedes = data,
      error: (err) => console.error('Error cargando sedes', err)
    });
  }

  loadVehicle(id: number): void {
    this.loading = true;
    this.vehicleService.getVehicleById(id).subscribe({
      next: (data) => {
        this.vehicle = data;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Error al cargar los detalles del vehículo. Por favor, inténtalo de nuevo más tarde.';
        this.loading = false;
        console.error('Error loading vehicle details:', error);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/detalles', this.vehicle?.idVeh]);
  }

  capitalizeFirstLetter(text: string): string {
    if (!text) return '';
    return text
      .toLowerCase()
      .split(' ')
      .map(word => word.charAt(0).toUpperCase() + word.slice(1))
      .join(' ');
  }
}
