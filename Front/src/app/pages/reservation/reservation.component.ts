import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { VehicleService } from '../../services/vehicle.service';
import { Vehicle } from '../../services/vehicle.service';
import { SedeService, Sede } from '../../services/sede.service';
import { FormsModule } from '@angular/forms';
import { ReservationService } from '../../services/reservation.service';
import { AuthService } from '../../services/auth.service';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2';
import { take } from 'rxjs/operators';

@Component({
  selector: 'app-reservation',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reservation.component.html',
  styleUrl: './reservation.component.scss'
})
export class ReservationComponent implements OnInit, OnDestroy {
  vehicle: Vehicle | null = null;
  loading: boolean = true;
  error: string | null = null;
  sedes: Sede[] = [];
  today: string = (() => {
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    return tomorrow.toISOString().split('T')[0];
  })();

  // Propiedades para los inputs
  pickupDate: string = '';
  returnDate: string = '';
  pickupLocation: string = '';
  returnLocation: string = '';
  termsAccepted: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private vehicleService: VehicleService,
    private sedeService: SedeService,
    private reservationService: ReservationService,
    private authService: AuthService
  ) {
    // Eliminamos la suscripción automática aquí
  }

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login'], { state: { returnUrl: this.router.url } });
      return;
    }

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadVehicle(Number(id));
    }
    this.sedeService.getSedes().subscribe({
      next: (data) => this.sedes = data,
      error: (err) => console.error('Error cargando sedes', err)
    });
  }

  ngOnDestroy(): void {
    // Ya no necesitamos desuscribirnos porque eliminamos la suscripción
  }

  loadVehicle(id: number): void {
    this.vehicleService.getVehicleById(id).subscribe({
      next: (data) => {
        this.vehicle = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar el vehículo';
        this.loading = false;
        console.error('Error cargando vehículo', err);
      }
    });
  }

  getMinReturnDate(): string {
    if (!this.pickupDate) return this.today;
    const pickup = new Date(this.pickupDate);
    const nextDay = new Date(pickup);
    nextDay.setDate(pickup.getDate() + 1);
    return nextDay.toISOString().split('T')[0];
  }

  calculateDays(): number {
    if (!this.pickupDate || !this.returnDate) return 0;
    const start = new Date(this.pickupDate);
    const end = new Date(this.returnDate);
    const diffTime = Math.abs(end.getTime() - start.getTime());
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;
  }

  getSedeName(id: string | number): string {
    if (!id) return '';
    const sede = this.sedes.find(s => s.idSed.toString() === id.toString());
    return sede ? sede.ciudad : '';
  }

  formatDate(date: string): string {
    if (!date) return '';
    const [year, month, day] = date.split('-');
    return `${day}/${month}/${year}`;
  }

  capitalizeFirstLetter(text: string | undefined): string {
    if (!text) return '';
    return text.charAt(0).toUpperCase() + text.slice(1).toLowerCase();
  }

  confirmReservation(): void {
    // Verificar que todos los campos necesarios estén completos
    if (!this.vehicle || !this.pickupDate || !this.returnDate || !this.pickupLocation || !this.returnLocation) {
      Swal.fire({
        icon: 'warning',
        title: 'Atención',
        text: 'Por favor, completa todos los campos',
        confirmButtonText: 'Entendido',
        confirmButtonColor: '#3085d6'
      });
      return;
    }

    // Verificar que se hayan aceptado los términos
    if (!this.termsAccepted) {
      Swal.fire({
        icon: 'warning',
        title: 'Atención',
        text: 'Por favor, acepta los términos y condiciones',
        confirmButtonText: 'Entendido',
        confirmButtonColor: '#3085d6'
      });
      return;
    }

    // Obtener el usuario actual y crear la reserva
    this.authService.currentUser$.pipe(
      take(1) // Tomar solo el primer valor y completar la suscripción
    ).subscribe(user => {
      if (!user) {
        this.router.navigate(['/login'], { state: { returnUrl: this.router.url } });
        return;
      }

      // Verificar que el vehículo existe
      if (!this.vehicle) {
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se encontró el vehículo',
          confirmButtonText: 'Entendido',
          confirmButtonColor: '#3085d6'
        });
        return;
      }

      // Crear el objeto de reserva
      const reserva = {
        inicio: this.pickupDate,
        fin: this.returnDate,
        total: this.vehicle.precio * this.calculateDays(),
        vehiculo: { idVeh: this.vehicle.idVeh },
        usuario: { idUsu: user.idUsu },
        idSed_Salid: { idSed: Number(this.pickupLocation) },
        idSed_Lleg: { idSed: Number(this.returnLocation) }
      };

      // Crear la reserva
      this.reservationService.createReservation(reserva).subscribe({
        next: (response) => {
          Swal.fire({
            icon: 'success',
            title: '¡Reserva confirmada!',
            text: 'Tu reserva se ha realizado con éxito',
            confirmButtonText: 'Aceptar',
            confirmButtonColor: '#3085d6'
          }).then((result) => {
            if (result.isConfirmed) {
              this.router.navigate(['/']);
            }
          });
        },
        error: (error) => {
          console.error('Error al crear la reserva:', error);
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Error al crear la reserva. Por favor, inténtalo de nuevo.',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#3085d6'
          });
        }
      });
    });
  }

  goBack(): void {
    this.router.navigate(['/detalles', this.vehicle?.idVeh]);
  }
}
