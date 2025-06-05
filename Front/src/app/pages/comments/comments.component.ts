import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-comments',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './comments.component.html',
  styleUrl: './comments.component.scss'
})
export class CommentsComponent implements OnInit {
  reservaId: number | null = null;
  rating: number = 0;
  comment: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router
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
    console.log('Enviando reseña:', {
      reservaId: this.reservaId,
      rating: this.rating,
      comment: this.comment
    });
  }

  goBack() {
    this.router.navigate(['/perfil']);
  }
}
