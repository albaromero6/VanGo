import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Sede {
    idSed: number;
    direccion: string;
    ciudad: string;
    telefono: string;
    imagen: string;
}

@Injectable({
    providedIn: 'root'
})
export class SedeService {
    private apiUrl = '/api/sedes';

    constructor(private http: HttpClient) { }

    getSedes(): Observable<Sede[]> {
        return this.http.get<Sede[]>(this.apiUrl);
    }

    getSedeById(id: number): Observable<Sede> {
        return this.http.get<Sede>(`${this.apiUrl}/${id}`);
    }

    getImagenUrl(filename: string): string {
        return `${this.apiUrl}/imagen/${filename}`;
    }
} 