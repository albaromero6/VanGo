import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class ReviewService {
    private apiUrl = `${environment.apiUrl}/resenias`;

    constructor(private http: HttpClient) { }

    createReview(reservaId: number, rating: number, comment: string): Observable<any> {
        return this.http.post(this.apiUrl, {
            reserva: { idReser: reservaId },
            puntuacion: rating,
            comentario: comment
        });
    }

    checkReviewExists(reservaId: number): Observable<boolean> {
        return this.http.get<boolean>(`${this.apiUrl}/exists/${reservaId}`);
    }

    getReviewByReserva(reservaId: number): Observable<any> {
        return this.http.get(`${this.apiUrl}/reserva/${reservaId}`);
    }
} 