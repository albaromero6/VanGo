import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { VehicleService } from '../../services/vehicle.service';
import { Vehicle } from '../../services/vehicle.service';
import { SedeService, Sede } from '../../services/sede.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-reservation',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reservation.component.html',
  styleUrl: './reservation.component.scss'
})
export class ReservationComponent implements OnInit {
  vehicle: Vehicle | null = null;
  loading: boolean = true;
  error: string | null = null;
  sedes: Sede[] = [];
  today: string = (() => {
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    return tomorrow.toISOString().split('T')[0];
  })();

  // Nuevas propiedades para los inputs
  pickupDate: string = '';
  returnDate: string = '';
  pickupLocation: string = '';
  returnLocation: string = '';

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

  // Nuevo método para obtener el nombre de la sede
  getSedeName(id: string | number): string {
    if (!id) return '';
    const sede = this.sedes.find(s => s.idSed.toString() === id.toString());
    return sede ? sede.ciudad : '';
  }

  // Nuevo método para formatear fechas
  formatDate(date: string): string {
    if (!date) return '';
    const [year, month, day] = date.split('-');
    return `${day}/${month}/${year}`;
  }

  // Método para verificar si hay datos de reserva
  hasReservationData(): boolean {
    return !!(this.pickupDate && this.returnDate && this.pickupLocation && this.returnLocation);
  }

  calculateDays(): number {
    if (!this.pickupDate || !this.returnDate) {
      return 0;
    }
    const start = new Date(this.pickupDate);
    const end = new Date(this.returnDate);
    const diffTime = Math.abs(end.getTime() - start.getTime());
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;
    return diffDays;
  }

  getMinReturnDate(): string {
    if (!this.pickupDate) {
      return this.today;
    }
    const minDate = new Date(this.pickupDate);
    minDate.setDate(minDate.getDate() + 1);
    return minDate.toISOString().split('T')[0];
  }
}
