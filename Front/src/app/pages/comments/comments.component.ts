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
  isEditing: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private reviewService: ReviewService
  ) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.reservaId = +params['id'];
      if (this.reservaId) {
        this.loadExistingReview();
      }
    });
  }

  loadExistingReview() {
    if (!this.reservaId) return;

    this.reviewService.getReviewByReserva(this.reservaId).subscribe({
      next: (response: any) => {
        console.log('Reseña cargada:', response);
        if (response) {
          this.rating = response.puntuacion;
          this.comment = response.comentario;
          this.isEditing = true;
        }
      },
      error: (error) => {
        console.error('Error al cargar la reseña:', error);
        if (error.status === 404) {
          this.isEditing = false;
        } else {
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: error.error?.error || 'Error al cargar la reseña'
          });
        }
      }
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
            title: this.isEditing ? '¡Actualizado!' : '¡Gracias!',
            text: this.isEditing ? 'Tu reseña ha sido actualizada correctamente' : 'Tu reseña ha sido enviada correctamente',
            icon: 'success',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#3085d6'
          }).then(() => {
            this.router.navigate(['/perfil']);
          });
        },
        error: (error) => {
          console.error('Error al enviar la reseña:', error);
          let errorMessage = 'Por favor, inténtalo de nuevo';

          if (error.status === 400 && error.error && error.error.error) {
            errorMessage = error.error.error;
          }

          Swal.fire({
            title: 'Error',
            text: errorMessage,
            icon: 'error',
            confirmButtonText: 'Entendido',
            confirmButtonColor: '#3085d6'
          }).then(() => {
            this.router.navigate(['/perfil']);
          });
        }
      });
  }

  goBack() {
    this.router.navigate(['/perfil']);
  }
}
