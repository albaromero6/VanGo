import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { SedeService, Sede } from '../../services/sede.service';
import { MapsService } from '../../services/maps.service';

@Component({
  selector: 'app-location',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './location.component.html',
  styleUrl: './location.component.scss'
})
export class LocationComponent implements OnInit {
  sedes: Sede[] = [];

  constructor(
    private sedeService: SedeService,
    private mapsService: MapsService
  ) { }

  ngOnInit(): void {
    this.cargarSedes();
  }

  cargarSedes(): void {
    this.sedeService.getSedes().subscribe({
      next: (data) => {
        this.sedes = data;
      },
      error: (error) => {
        console.error('Error al cargar las sedes:', error);
      }
    });
  }

  verEnMapa(direccion: string, ciudad: string): void {
    const url = this.mapsService.getGoogleMapsUrl(direccion, ciudad);
    window.open(url, '_blank');
  }

  onImageError(event: Event): void {
    const imgElement = event.target as HTMLImageElement;
    imgElement.src = 'assets/img/placeholder-sede.jpg';
    imgElement.onerror = null;
  }
}
