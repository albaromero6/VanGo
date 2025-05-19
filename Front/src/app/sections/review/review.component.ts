import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

interface Review {
  image: string;
  alt: string;
  stars: number;
  text: string;
  name: string;
  location: string;
}

@Component({
  selector: 'app-review',
  standalone: true,
  imports: [ CommonModule ],
  templateUrl: './review.component.html',
  styleUrl: './review.component.scss'
})
export class ReviewComponent {

  reviews: Review[] = [
    {
      image: 'assets/img/Laura.jpg',
      alt: 'Laura Pérez',
      stars: 5,
      text: '"Me siento muy segura usando la app de Van&Go para alquilar caravanas. La atención al cliente es excelente y los vehículos siempre están listos a tiempo. ¡Totalmente recomendable!"',
      name: 'Laura Pérez',
      location: 'Valencia'
    },
    {
      image: 'assets/img/Alberto.jpg',
      alt: 'Cliente Van&Go',
      stars: 5,
      text: '"Alquilamos una caravana para un fin de semana en familia y fue una experiencia increíble. La app funciona genial y el proceso fue rapidísimo."',
      name: 'Alberto Gómez',
      location: 'Asturias'
    },
    {
      image: 'assets/img/Miguel.jpg',
      alt: 'Cliente Van&Go',
      stars: 5,
      text: '"Van&Go me ha permitido descubrir nuevos destinos con total libertad. La calidad de las caravanas y la facilidad de uso de la plataforma son top."',
      name: 'Miguel Durán',
      location: 'Murcia'
    }
  ];

  currentIndex = 0;

  prevReview() {
    this.currentIndex = (this.currentIndex - 1 + this.reviews.length) % this.reviews.length;
  }

  nextReview() {
    this.currentIndex = (this.currentIndex + 1) % this.reviews.length;
  }

  getStars(count: number): number[] {
    return Array.from({ length: count }, (_, i) => i);
  }
  
}
