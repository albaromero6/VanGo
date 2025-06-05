import { Component, OnInit, AfterViewInit, ElementRef, ViewChildren, QueryList, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { VehicleService, Vehicle, Marca, Modelo } from '../../services/vehicle.service';
import { AuthService } from '../../services/auth.service';
declare const anime: any;
import { HostListener } from '@angular/core';
import { FormsModule } from '@angular/forms';
import Swal, { SweetAlertResult } from 'sweetalert2';
import { HttpErrorResponse } from '@angular/common/http';
import { ReviewService } from '../../services/review.service';
import { ImageService } from '../../services/image.service';

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
  @ViewChild('mainFileInput') mainFileInput!: ElementRef;

  showLightbox: boolean = false;
  currentImageIndex: number = 0;
  isNewVehicle = false;
  showReviews = false;
  reviews: any[] = [];
  averageRating: number = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private vehicleService: VehicleService,
    private authService: AuthService,
    private reviewService: ReviewService,
    private imageService: ImageService
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
        modelo: {
          idMod: 0,
          nombre: '',
          marca: {
            idMar: 0,
            nombre: ''
          }
        },
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
      this.originalVehicle = JSON.parse(JSON.stringify(this.vehicle));
      this.galleryImages = ['', '', '', '']; // Inicializar con strings vacíos
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

        // Cargar las reseñas automáticamente
        this.loadReviews();
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
      return;
    }
    imgElement.src = 'assets/img/Coche.png';
    imgElement.onerror = null;
  }

  reservar(): void {
    if (this.vehicle) {
      if (this.authService.isLoggedIn()) {
        // Redirije al componente de reserva con el ID del vehículo
        this.router.navigate(['/reserva', this.vehicle.idVeh]);
      } else {
        // Guardar la URL y redirije al login
        this.authService.saveReturnUrl(`/reserva/${this.vehicle.idVeh}`);
        this.router.navigate(['/login']);
      }
    }
  }

  onImageUpload(event: Event, index: number): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      const file = input.files[0];

      // Valida el tipo de archivo
      if (!file.type.startsWith('image/')) {
        Swal.fire({
          title: 'Error',
          text: 'Por favor, selecciona un archivo de imagen válido.',
          icon: 'error',
          confirmButtonColor: '#05889C'
        });
        return;
      }

      const formData = new FormData();
      formData.append('file', file);

      this.vehicleService.uploadImage(formData).subscribe({
        next: (response: { nombreArchivo: string }) => {
          if (response && response.nombreArchivo) {
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

            this.galleryImages[index] = response.nombreArchivo;
            Swal.fire({
              title: '¡Genial!',
              text: 'La imagen se ha subido correctamente',
              icon: 'success',
              confirmButtonColor: '#05889C'
            });
          }
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error al subir la imagen:', error);
          Swal.fire({
            title: 'Error',
            text: 'Por favor, inténtalo de nuevo',
            icon: 'error',
            confirmButtonColor: '#05889C'
          });
        }
      });
    }
  }

  onMainImageUpload(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      const file = input.files[0];

      // Valida el tipo de archivo
      if (!file.type.startsWith('image/')) {
        Swal.fire({
          title: 'Error',
          text: 'Por favor, selecciona un archivo de imagen válido.',
          icon: 'error',
          confirmButtonColor: '#05889C'
        });
        return;
      }

      // Crea un FormData para enviar el archivo
      const formData = new FormData();
      formData.append('file', file);

      // Llama al servicio para subir la imagen
      this.vehicleService.uploadImage(formData).subscribe({
        next: (response: { nombreArchivo: string }) => {
          if (response && response.nombreArchivo) {
            // Actualiza la imagen principal del vehículo
            this.vehicle!.imagen = response.nombreArchivo;
            Swal.fire({
              title: '¡Éxito!',
              text: 'La imagen se ha subido correctamente.',
              icon: 'success',
              confirmButtonColor: '#05889C'
            });
          }
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error al subir la imagen principal:', error);
          let errorMessage = 'Error al subir la imagen principal. Por favor, inténtalo de nuevo.';

          if (error.status === 400) {
            errorMessage = 'El archivo seleccionado no es válido. Por favor, selecciona una imagen.';
          } else if (error.status === 413) {
            errorMessage = 'El archivo es demasiado grande. Por favor, selecciona una imagen más pequeña.';
          }

          Swal.fire({
            title: 'Error',
            text: errorMessage,
            icon: 'error',
            confirmButtonColor: '#05889C'
          });
        }
      });
    }
  }

  guardarCambios(): void {
    if (!this.vehicle) return;

    // Valida campos requeridos
    const camposFaltantes = [];
    if (this.isNewVehicle) {
      if (!this.selectedMarca) camposFaltantes.push('Marca');
      if (!this.selectedModelo) camposFaltantes.push('Modelo');
    }
    if (!this.vehicle.precio) camposFaltantes.push('Precio');
    if (!this.vehicle.combustible) camposFaltantes.push('Combustible');
    if (!this.vehicle.transmision) camposFaltantes.push('Transmisión');
    if (!this.vehicle.puertas) camposFaltantes.push('Puertas');
    if (!this.vehicle.pasajeros) camposFaltantes.push('Pasajeros');
    if (!this.vehicle.anio) camposFaltantes.push('Año');

    // Valida imágenes
    const imagenesFaltantes = [];
    if (!this.vehicle.imagen) imagenesFaltantes.push('principal');
    if (!this.vehicle.detalles1) imagenesFaltantes.push('detalle 1');
    if (!this.vehicle.detalles2) imagenesFaltantes.push('detalle 2');
    if (!this.vehicle.detalles3) imagenesFaltantes.push('detalle 3');
    if (!this.vehicle.detalles4) imagenesFaltantes.push('detalle 4');

    if (imagenesFaltantes.length > 0) {
      camposFaltantes.push(`Imágenes`);
    }

    if (camposFaltantes.length > 0) {
      Swal.fire({
        title: 'Campos obligatorios',
        html: `Por favor, completa los siguientes campos:<br><br>${camposFaltantes.join('<br>')}`,
        icon: 'error',
        confirmButtonColor: '#05889C'
      });
      return;
    }

    if (this.vehicle.idVeh === 0) {
      // Crea un nuevo vehículo
      this.vehicleService.createVehicle(this.vehicle).subscribe({
        next: (response: Vehicle) => {
          Swal.fire({
            title: '¡Éxito!',
            text: 'El vehículo ha sido creado correctamente',
            icon: 'success',
            confirmButtonColor: '#05889C'
          }).then((result: SweetAlertResult) => {
            if (result.isConfirmed) {
              this.router.navigate(['/catalogo']);
            }
          });
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error al crear vehículo:', error);
          let errorMessage = 'Error al crear el vehículo. Por favor, inténtalo de nuevo.';

          if (error.status === 400) {
            errorMessage = 'Los datos del vehículo no son válidos. Por favor, revisa los campos.';
          }

          Swal.fire({
            title: 'Error',
            text: errorMessage,
            icon: 'error',
            confirmButtonColor: '#05889C'
          });
        }
      });
    } else {
      // Actualiza el vehículo existente
      this.vehicleService.updateVehicle(this.vehicle).subscribe({
        next: (response: Vehicle) => {
          Swal.fire({
            title: '¡Éxito!',
            text: 'Los cambios han sido guardados correctamente',
            icon: 'success',
            confirmButtonColor: '#05889C'
          }).then((result: SweetAlertResult) => {
            if (result.isConfirmed) {
              this.router.navigate(['/catalogo']);
            }
          });
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error al actualizar el vehículo:', error);
          Swal.fire({
            title: 'Error',
            text: 'Error al guardar los cambios. Por favor, inténtalo de nuevo.',
            icon: 'error',
            confirmButtonColor: '#05889C'
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
    this.vehicleService.getAllMarcas().subscribe({
      next: (marcas) => {
        if (Array.isArray(marcas)) {
          this.marcas = marcas.map(marca => ({
            idMar: marca.idMar,
            nombre: marca.nombre
          }));
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
    this.vehicleService.getModelosByMarca(marcaId).subscribe({
      next: (modelos) => {
        if (Array.isArray(modelos)) {
          this.modelos = modelos.map(modelo => ({
            idMod: modelo.idMod,
            nombre: modelo.nombre,
            marca: {
              idMar: modelo.marca.idMar,
              nombre: modelo.marca.nombre
            }
          }));

          if (this.modelos.length === 0) {
            Swal.fire({
              title: 'Información',
              text: 'No hay modelos disponibles para esta marca',
              icon: 'info',
              confirmButtonColor: '#05889C'
            });
          }
        } else {
          this.modelos = [];
          Swal.fire({
            title: 'Error',
            text: 'Error al cargar los modelos',
            icon: 'error',
            confirmButtonColor: '#05889C'
          });
        }
      },
      error: (error) => {
        console.error('Error al cargar modelos:', error);
        this.modelos = [];
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
    if (this.selectedMarca) {
      const marcaId = Number(this.selectedMarca);
      const marcaSeleccionada = this.marcas.find(m => m.idMar === marcaId);

      if (marcaSeleccionada) {
        this.loadModelos(marcaSeleccionada.idMar);
        this.selectedModelo = null;
        if (this.vehicle) {
          this.vehicle.modelo = {
            idMod: 0,
            nombre: '',
            marca: {
              idMar: marcaSeleccionada.idMar,
              nombre: marcaSeleccionada.nombre
            }
          };
        }
      } else {
        Swal.fire({
          title: 'Error',
          text: 'No se encontró la marca seleccionada',
          icon: 'error',
          confirmButtonColor: '#05889C'
        });
      }
    } else {
      this.modelos = [];
      this.selectedModelo = null;
    }
  }

  onModeloChange(): void {
    if (this.selectedModelo && this.vehicle) {
      const modeloSeleccionado = this.modelos.find(m => m.idMod === this.selectedModelo);
      if (modeloSeleccionado) {
        const marcaSeleccionada = this.marcas.find(m => m.idMar === this.selectedMarca);
        this.vehicle.modelo = {
          idMod: modeloSeleccionado.idMod,
          nombre: modeloSeleccionado.nombre,
          marca: {
            idMar: marcaSeleccionada ? marcaSeleccionada.idMar : modeloSeleccionado.marca.idMar,
            nombre: marcaSeleccionada ? marcaSeleccionada.nombre : modeloSeleccionado.marca.nombre
          }
        };
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

  toggleReviews(): void {
    if (!this.vehicle) return;

    if (!this.showReviews) {
      this.reviewService.getReviewsByVehiculo(this.vehicle.idVeh).subscribe({
        next: (reviews) => {
          this.reviews = reviews;
          this.showReviews = true;
        },
        error: (error) => {
          console.error('Error al cargar las reseñas:', error);
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'No se pudieron cargar las reseñas'
          });
        }
      });
    } else {
      this.showReviews = false;
    }
  }

  loadReviews(): void {
    if (!this.vehicle) return;

    console.log('Cargando reseñas para el vehículo:', this.vehicle.idVeh);

    this.reviewService.getReviewsByVehiculo(this.vehicle.idVeh).subscribe({
      next: (reviews) => {
        console.log('Reseñas recibidas del backend:', reviews);

        // Ordenar las reseñas por fecha de más reciente a más antigua
        this.reviews = reviews.sort((a: { fecha: string }, b: { fecha: string }) => {
          console.log('Comparando fechas:', {
            fechaA: a.fecha,
            fechaB: b.fecha
          });

          // Convertir las fechas a objetos Date y asegurarnos de que son válidas
          const dateA = new Date(a.fecha);
          const dateB = new Date(b.fecha);

          // Verificar si las fechas son válidas
          if (isNaN(dateA.getTime()) || isNaN(dateB.getTime())) {
            console.error('Fecha inválida encontrada:', {
              fechaA: a.fecha,
              fechaB: b.fecha,
              dateA: dateA,
              dateB: dateB
            });
            return 0;
          }

          // Ordenar de más reciente a más antigua
          const result = dateB.getTime() - dateA.getTime();
          console.log('Resultado de la comparación:', result);
          return result;
        });

        // Calcular la media de las puntuaciones
        if (this.reviews.length > 0) {
          const sum = this.reviews.reduce((acc, review) => acc + review.puntuacion, 0);
          this.averageRating = sum / this.reviews.length;
        } else {
          this.averageRating = 0;
        }

        // Verificar el ordenamiento final
        console.log('Reseñas ordenadas:', this.reviews.map(r => ({
          fecha: r.fecha,
          fechaFormateada: new Date(r.fecha).toLocaleDateString(),
          puntuacion: r.puntuacion,
          comentario: r.comentario
        })));
      },
      error: (error) => {
        console.error('Error al cargar las reseñas:', error);
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudieron cargar las reseñas'
        });
      }
    });
  }
}
