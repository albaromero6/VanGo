import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Reserva {
    idReser?: number;
    idUsu: number;
    idVeh: number;
    idSedeSalid: number;
    idSedeLleg: number;
    inicio: string;
    fin: string;
    total: number;
    estado?: string;
}

@Injectable({
    providedIn: 'root'
})
export class ReservationService {
    private apiUrl = `${environment.apiUrl}/reservas`;

    constructor(private http: HttpClient) { }

    createReservation(reserva: Reserva): Observable<Reserva> {
        return this.http.post<Reserva>(this.apiUrl, reserva);
    }
} 