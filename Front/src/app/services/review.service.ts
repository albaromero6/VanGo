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
            puntuacion: rating,
            comentario: comment,
            fecha: new Date().toISOString().split('T')[0],
            reserva: {
                idReser: reservaId
            }
        });
    }
} 