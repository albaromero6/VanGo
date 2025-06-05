import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { ReviewService } from '../../services/review.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-comments',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './comments.component.html',
  styleUrl: './comments.component.scss'
})
export class CommentsComponent implements OnInit {
  reservaId: number | null = null;
  rating: number = 0;
  comment: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private reviewService: ReviewService
  ) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.reservaId = +params['id'];
    });
  }

  setRating(rating: number) {
    this.rating = rating;
  }

  submitReview() {
    if (!this.rating || !this.comment.trim()) {
      Swal.fire({
        title: '¡Atención!',
        text: 'Por favor, completa tanto la valoración como el comentario',
        icon: 'warning',
        confirmButtonText: 'Entendido',
        confirmButtonColor: '#3085d6'
      });
      return;
    }

    if (!this.reservaId) {
      Swal.fire({
        title: 'Error',
        text: 'No se ha podido identificar la reserva.',
        icon: 'error',
        confirmButtonText: 'Entendido',
        confirmButtonColor: '#3085d6'
      });
      return;
    }

    this.reviewService.createReview(this.reservaId, this.rating, this.comment.trim())
      .subscribe({
        next: () => {
          Swal.fire({
            title: '¡Gracias!',
            text: 'Tu reseña ha sido enviada correctamente.',
            icon: 'success',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#3085d6'
          }).then(() => {
            this.router.navigate(['/perfil']);
          });
        },
        error: (error) => {
          console.error('Error al enviar la reseña:', error);
          Swal.fire({
            title: 'Error',
            text: 'Ha ocurrido un error al enviar tu reseña. Por favor, inténtalo de nuevo.',
            icon: 'error',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#3085d6'
          });
        }
      });
  }

  goBack() {
    this.router.navigate(['/perfil']);
  }
}
