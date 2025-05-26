import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { SedeService, Sede } from '../../services/sede.service';

@Component({
  selector: 'app-location',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './location.component.html',
  styleUrl: './location.component.scss'
})
export class LocationComponent implements OnInit {
  sedes: Sede[] = [];

  constructor(private sedeService: SedeService) { }

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
}
