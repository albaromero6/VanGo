import { Component, OnInit, AfterViewInit, ElementRef, ViewChildren, QueryList, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { VehicleService, Vehicle, Marca, Modelo } from '../../services/vehicle.service';
import { AuthService } from '../../services/auth.service';
declare const anime: any;
import { HostListener } from '@angular/core';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-details',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './details.component.html',
  styleUrl: './details.component.scss'
})
export class DetailsComponent implements OnInit, AfterViewInit {
  editMode: boolean = false;
  vehicle: Vehicle | null = null;
  loading: boolean = true;
  error: string | null = null;
  galleryImages: string[] = [];
  isAdmin: boolean = false;
  originalVehicle: Vehicle | null = null;
  isMatriculaValid: boolean = true;
  marcas: Marca[] = [];
  modelos: Modelo[] = [];
  selectedMarca: number | null = null;
  selectedModelo: number | null = null;
  @ViewChildren('galleryItem') galleryItems!: QueryList<ElementRef>;
  @ViewChildren('fileInput') fileInputs!: QueryList<ElementRef>;

  showLightbox: boolean = false;
  currentImageIndex: number = 0;
  isNewVehicle = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private vehicleService: VehicleService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.isAdmin = this.authService.isAdmin();
    const id = this.route.snapshot.paramMap.get('id');
    console.log('ID recibido:', id);

    if (id === 'new') {
      console.log('Inicializando nuevo vehículo');
      this.isNewVehicle = true;
      this.editMode = true;
      this.loading = false;
      this.vehicle = {
        idVeh: 0,
        modelo: { idMod: 0, nombre: '', marca: { idMar: 0, nombre: '' } },
        matricula: '',
        imagen: '',
        precio: 0,
        anio: null,
        pasajeros: 0,
        puertas: null,
        transmision: '',
        combustible: '',
        detalles1: '',
        detalles2: '',
        detalles3: '',
        detalles4: ''
      };
      this.loadMarcas();
    } else if (id) {
      this.loadVehicle(Number(id));
    }

    // Suscribirse a los cambios en el modo de edición
    this.route.queryParams.subscribe(params => {
      if (params['edit'] === 'true' && !this.isNewVehicle) {
        this.editMode = true;
        this.loadMarcas();
      }
    });
  }

  ngAfterViewInit(): void {
    // Espera a que las imágenes se carguen
    setTimeout(() => {
      this.initializeGalleryAnimation();
    }, 500);
  }

  initializeGalleryAnimation(): void {
    if (!this.galleryItems || this.editMode) return;

    const items = this.galleryItems.toArray();
    if (items.length === 0) return;

    console.log('Initializing gallery animation with', items.length, 'items');

    // Verificar si anime está disponible
    if (typeof anime !== 'undefined') {
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
    } else {
      console.log('anime.js no está disponible, omitiendo animaciones');
    }
  }

  loadVehicle(id: number): void {
    this.loading = true;
    if (id === 0) {
      // Crea un nuevo vehículo vacío
      this.vehicle = {
        idVeh: 0,
        modelo: {
          idMod: 0,
          nombre: '',
          marca: {
            idMar: 0,
            nombre: ''
          }
        },
        combustible: '',
        transmision: '',
        puertas: null,
        pasajeros: 0,
        anio: null,
        precio: 0,
        imagen: '',
        detalles1: '',
        detalles2: '',
        detalles3: '',
        detalles4: '',
        matricula: ''
      };
      this.originalVehicle = JSON.parse(JSON.stringify(this.vehicle));
      this.galleryImages = [];
      this.loading = false;
      return;
    }

    this.vehicleService.getVehicleById(id).subscribe({
      next: (data) => {
        this.vehicle = data;
        // Guardar una copia del vehículo original para poder cancelar cambios
        this.originalVehicle = JSON.parse(JSON.stringify(data));
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
      return 'assets/img/Coche.png';
    }
    return `http://localhost:8080/api/vehiculos/imagen/${this.vehicle.imagen}`;
  }

  getGalleryImage(imageName: string): string {
    if (!imageName) {
      return 'assets/img/Coche.png';
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
    if (imgElement.src.includes('placeholder-vehicle.jpg')) {
      // Si ya estamos intentando cargar la imagen placeholder, no hacemos nada
      return;
    }
    imgElement.src = 'assets/img/Coche.png';
    imgElement.onerror = null;
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

  onImageUpload(event: Event, index: number): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      const file = input.files[0];

      // Crear un FormData para enviar el archivo
      const formData = new FormData();
      formData.append('file', file);

      // Llamar al servicio para subir la imagen
      this.vehicleService.uploadImage(formData).subscribe({
        next: (response: { nombreArchivo: string }) => {
          if (response && response.nombreArchivo) {
            // Actualizar la imagen correspondiente en el vehículo
            switch (index) {
              case 0:
                this.vehicle!.detalles1 = response.nombreArchivo;
                break;
              case 1:
                this.vehicle!.detalles2 = response.nombreArchivo;
                break;
              case 2:
                this.vehicle!.detalles3 = response.nombreArchivo;
                break;
              case 3:
                this.vehicle!.detalles4 = response.nombreArchivo;
                break;
            }

            // Actualiza el array de imágenes de la galería
            this.galleryImages[index] = response.nombreArchivo;
          }
        },
        error: (error: Error) => {
          console.error('Error al subir la imagen:', error);
        }
      });
    }
  }

  guardarCambios(): void {
    if (!this.vehicle) return;

    // Convertir matrícula a mayúsculas
    if (this.vehicle.matricula) {
      this.vehicle.matricula = this.vehicle.matricula.toUpperCase();
    }

    // Validar el formato de matrícula
    if (this.vehicle.matricula && !/^\d{4}[A-Z]{3}$/.test(this.vehicle.matricula)) {
      Swal.fire({
        title: 'Error en la matrícula',
        text: 'La matrícula debe tener 4 números seguidos de 3 letras mayúsculas',
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

    // Validar campos requeridos
    const camposFaltantes = [];
    if (!this.vehicle.modelo?.nombre) camposFaltantes.push('Modelo');
    if (!this.vehicle.matricula) camposFaltantes.push('Matrícula');
    if (!this.vehicle.precio) camposFaltantes.push('Precio');
    if (!this.vehicle.combustible) camposFaltantes.push('Combustible');
    if (!this.vehicle.transmision) camposFaltantes.push('Transmisión');
    if (!this.vehicle.puertas) camposFaltantes.push('Puertas');
    if (!this.vehicle.pasajeros) camposFaltantes.push('Pasajeros');

    if (camposFaltantes.length > 0) {
      Swal.fire({
        title: 'Campos obligatorios',
        html: `Por favor, completa los siguientes campos:<br><br>${camposFaltantes.join('<br>')}`,
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

    if (this.vehicle.idVeh === 0) {
      // Crear nuevo vehículo
      this.vehicleService.createVehicle(this.vehicle).subscribe({
        next: (response: Vehicle) => {
          Swal.fire({
            title: '¡Éxito!',
            text: 'El vehículo ha sido creado correctamente',
            icon: 'success',
            confirmButtonColor: '#05889C',
            customClass: {
              popup: 'swal2-popup',
              title: 'swal2-title',
              htmlContainer: 'swal2-html-container',
              confirmButton: 'swal2-confirm'
            },
            buttonsStyling: true
          }).then(() => {
            this.router.navigate(['/catalogo']);
          });
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error al crear el vehículo:', error);
          Swal.fire({
            title: 'Error',
            text: 'Error al crear el vehículo. Por favor, inténtalo de nuevo.',
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
    } else {
      // Actualizar vehículo existente
      this.vehicleService.updateVehicle(this.vehicle).subscribe({
        next: (response: Vehicle) => {
          Swal.fire({
            title: '¡Éxito!',
            text: 'Los cambios han sido guardados correctamente',
            icon: 'success',
            confirmButtonColor: '#05889C',
            customClass: {
              popup: 'swal2-popup',
              title: 'swal2-title',
              htmlContainer: 'swal2-html-container',
              confirmButton: 'swal2-confirm'
            },
            buttonsStyling: true
          }).then(() => {
            this.router.navigate(['/catalogo']);
          });
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error al actualizar el vehículo:', error);
          Swal.fire({
            title: 'Error',
            text: 'Error al guardar los cambios. Por favor, inténtalo de nuevo.',
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
  }

  cancelarEdicion(): void {
    if (this.originalVehicle) {
      this.vehicle = JSON.parse(JSON.stringify(this.originalVehicle));
      if (this.vehicle) {
        this.galleryImages = [
          this.vehicle.detalles1,
          this.vehicle.detalles2,
          this.vehicle.detalles3,
          this.vehicle.detalles4
        ].filter(img => img);
      }
      this.editMode = false;
      this.router.navigate(['/catalogo']);
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

  getYears(): number[] {
    const currentYear = new Date().getFullYear();
    const years: number[] = [];
    for (let year = currentYear; year >= 2000; year--) {
      years.push(year);
    }
    return years;
  }

  loadMarcas(): void {
    console.log('Iniciando carga de marcas...');
    this.vehicleService.getAllMarcas().subscribe({
      next: (marcas) => {
        console.log('Respuesta del servidor (marcas):', marcas);
        if (Array.isArray(marcas)) {
          this.marcas = marcas;
          console.log('Marcas cargadas correctamente:', this.marcas);
        } else {
          console.error('La respuesta no es un array de marcas:', marcas);
        }
      },
      error: (error) => {
        console.error('Error al cargar marcas:', error);
        Swal.fire({
          title: 'Error',
          text: 'No se pudieron cargar las marcas. Por favor, inténtalo de nuevo.',
          icon: 'error',
          confirmButtonColor: '#05889C'
        });
      }
    });
  }

  loadModelos(marcaId: number): void {
    console.log('Cargando modelos para marca ID:', marcaId);
    this.vehicleService.getModelosByMarca(marcaId).subscribe({
      next: (modelos) => {
        console.log('Respuesta del servidor (modelos):', modelos);
        if (Array.isArray(modelos)) {
          this.modelos = modelos;
          console.log('Modelos cargados correctamente:', this.modelos);
        } else {
          console.error('La respuesta no es un array de modelos:', modelos);
        }
      },
      error: (error) => {
        console.error('Error al cargar modelos:', error);
        Swal.fire({
          title: 'Error',
          text: 'No se pudieron cargar los modelos. Por favor, inténtalo de nuevo.',
          icon: 'error',
          confirmButtonColor: '#05889C'
        });
      }
    });
  }

  onMarcaChange(): void {
    console.log('Marca seleccionada:', this.selectedMarca);
    if (this.selectedMarca) {
      this.loadModelos(this.selectedMarca);
      this.selectedModelo = null;
    } else {
      this.modelos = [];
      this.selectedModelo = null;
    }
  }

  onModeloChange(): void {
    console.log('Modelo seleccionado:', this.selectedModelo);
    if (this.selectedModelo && this.vehicle) {
      const modeloSeleccionado = this.modelos.find(m => m.idMod === this.selectedModelo);
      if (modeloSeleccionado) {
        this.vehicle.modelo = modeloSeleccionado;
        console.log('Modelo actualizado en el vehículo:', this.vehicle.modelo);
      }
    }
  }

  toggleEditMode(): void {
    if (!this.isNewVehicle) {
      this.editMode = !this.editMode;
    }
    if (this.editMode) {
      this.loadMarcas();
    } else {
      this.cancelarEdicion();
    }
  }
}
