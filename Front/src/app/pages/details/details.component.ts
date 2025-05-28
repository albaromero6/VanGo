import { Component, OnInit, AfterViewInit, ElementRef, ViewChildren, QueryList } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { VehicleService, Vehicle } from '../../services/vehicle.service';
import { AuthService } from '../../services/auth.service';
declare const anime: any;
import { HostListener } from '@angular/core';

@Component({
  selector: 'app-details',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './details.component.html',
  styleUrl: './details.component.scss'
})
export class DetailsComponent implements OnInit, AfterViewInit {
  vehicle: Vehicle | null = null;
  loading: boolean = true;
  error: string | null = null;
  galleryImages: string[] = [];
  @ViewChildren('galleryItem') galleryItems!: QueryList<ElementRef>;

  showLightbox: boolean = false;
  currentImageIndex: number = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private vehicleService: VehicleService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const vehicleId = +params['id'];
      this.loadVehicleDetails(vehicleId);
    });
  }

  ngAfterViewInit(): void {
    // Espera a que las imágenes se carguen
    setTimeout(() => {
      this.initializeGalleryAnimation();
    }, 500);
  }

  initializeGalleryAnimation(): void {
    if (!this.galleryItems) return;

    const items = this.galleryItems.toArray();
    if (items.length === 0) return;

    console.log('Initializing gallery animation with', items.length, 'items');
    // Animación inicial 
    anime({
      targets: items.map(item => item.nativeElement),
      translateY: [50, 0],
      opacity: [0, 1],
      duration: 800,
      delay: anime.stagger(100),
      easing: 'easeOutExpo'
    });

    // Añade hover a cada imagen
    items.forEach(item => {
      const element = item.nativeElement;

      element.addEventListener('mouseenter', () => {
        anime({
          targets: element,
          scale: 1.05,
          rotate: 2,
          duration: 400,
          easing: 'easeOutQuad'
        });
      });

      element.addEventListener('mouseleave', () => {
        anime({
          targets: element,
          scale: 1,
          rotate: 0,
          duration: 400,
          easing: 'easeOutQuad'
        });
      });
    });
  }

  loadVehicleDetails(id: number): void {
    this.loading = true;
    this.vehicleService.getVehicleById(id).subscribe({
      next: (data) => {
        this.vehicle = data;
        this.galleryImages = [
          this.vehicle.detalles1,
          this.vehicle.detalles2,
          this.vehicle.detalles3,
          this.vehicle.detalles4
        ].filter(img => img);
        this.loading = false;

        // Reinicializa las animaciones después de cargar los datos
        setTimeout(() => {
          this.initializeGalleryAnimation();
        }, 100);
      },
      error: (error) => {
        this.error = 'Error al cargar los detalles del vehículo. Por favor, inténtalo de nuevo más tarde.';
        this.loading = false;
        console.error('Error loading vehicle details:', error);
      }
    });
  }

  getVehicleImage(): string {
    if (!this.vehicle?.imagen) {
      return 'assets/img/placeholder-vehicle.jpg';
    }
    return `http://localhost:8080/api/vehiculos/imagen/${this.vehicle.imagen}`;
  }

  getGalleryImage(imageName: string): string {
    if (!imageName) {
      return 'assets/img/placeholder-vehicle.jpg';
    }
    return `http://localhost:8080/api/vehiculos/imagen/${imageName}`;
  }

  getVehicleName(): string {
    if (!this.vehicle) {
      return 'Vehículo sin nombre';
    }

    if (!this.vehicle.modelo) {
      return 'Modelo no especificado';
    }

    const marcaNombre = this.vehicle.modelo.marca?.nombre || 'Sin marca';
    const modeloNombre = this.vehicle.modelo.nombre || 'Sin modelo';

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
    imgElement.onerror = null; // Prevent infinite loop
  }

  reservar(): void {
    if (this.vehicle) {
      if (this.authService.isLoggedIn()) {
        // Aquí irá la lógica de reserva
        console.log('Reservando vehículo:', this.vehicle.idVeh);
        this.router.navigate(['/catalogo']);
      } else {
        // Redirigir al login si no está logueado
        this.router.navigate(['/login']);
      }
    }
  }

  openLightbox(index: number): void {
    this.currentImageIndex = index;
    this.showLightbox = true;
    document.body.style.overflow = 'hidden';
  }

  closeLightbox(): void {
    this.showLightbox = false;
    document.body.style.overflow = '';
  }

  nextImage(): void {
    if (this.currentImageIndex < this.galleryImages.length - 1) {
      this.currentImageIndex++;
    }
  }

  prevImage(): void {
    if (this.currentImageIndex > 0) {
      this.currentImageIndex--;
    }
  }

  @HostListener('window:keydown', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent): void {
    if (!this.showLightbox) return;

    switch (event.key) {
      case 'Escape':
        this.closeLightbox();
        break;
      case 'ArrowRight':
        this.nextImage();
        break;
      case 'ArrowLeft':
        this.prevImage();
        break;
    }
  }
}
