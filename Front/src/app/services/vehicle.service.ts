import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export interface Marca {
    idMar: number;
    nombre: string;
}

export interface Modelo {
    idMod: number;
    nombre: string;
    marca: Marca;
}

export interface Vehicle {
    idVeh: number;
    modelo: Modelo;
    matricula: string;
    descripcion: string;
    imagen: string;
    precio: number;
    anio: number;
    pasajeros: number;
    puertas: number;
    transmision: 'MANUAL' | 'AUTOMATICO';
    combustible: 'GASOLINA' | 'DIESEL';
    detalles1: string;
    detalles2: string;
    detalles3: string;
    detalles4: string;
}

@Injectable({
    providedIn: 'root'
})
export class VehicleService {
    private apiUrl = 'http://localhost:8080/api/vehiculos';

    constructor(private http: HttpClient) { }

    getAllVehicles(): Observable<Vehicle[]> {
        return this.http.get<Vehicle[]>(this.apiUrl);
    }

    getVehicleById(id: number): Observable<Vehicle> {
        return this.http.get<Vehicle>(`${this.apiUrl}/${id}`);
    }

    getAvailableVehicles(): Observable<Vehicle[]> {
        return this.http.get<Vehicle[]>(`${this.apiUrl}/disponibles`);
    }

    deleteVehicle(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
            catchError((error: HttpErrorResponse) => {
                let errorMessage = 'Error al eliminar el vehículo';

                if (error.status === 500) {
                    errorMessage = 'No se puede eliminar este vehículo porque tiene reservas asociadas';
                } else if (error.status === 404) {
                    errorMessage = 'El vehículo no existe';
                } else if (error.status === 403) {
                    errorMessage = 'No tienes permisos para eliminar vehículos';
                }

                return throwError(() => errorMessage);
            })
        );
    }
} 