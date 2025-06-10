import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

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
    imagen: string;
    precio: number;
    anio: number | null;
    pasajeros: number;
    puertas: number | null;
    transmision: 'MANUAL' | 'AUTOMATICO' | '';
    combustible: 'GASOLINA' | 'DIESEL' | '';
    detalles1: string;
    detalles2: string;
    detalles3: string;
    detalles4: string;
}

export interface PaginatedResponse<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
}

@Injectable({
    providedIn: 'root'
})
export class VehicleService {
    private apiUrl = '/api/vehiculos';

    constructor(private http: HttpClient) { }

    getAllVehicles(page: number = 0, size: number = 9): Observable<PaginatedResponse<Vehicle>> {
        return this.http.get<PaginatedResponse<Vehicle>>(`${this.apiUrl}?page=${page}&size=${size}`);
    }

    getVehicleById(id: number): Observable<Vehicle> {
        return this.http.get<Vehicle>(`${this.apiUrl}/${id}`);
    }

    getAllMarcas(): Observable<Marca[]> {
        return this.http.get<Marca[]>(`/api/marcas`).pipe(
            catchError(error => {
                console.error('Error en getAllMarcas:', error);
                return throwError(() => error);
            })
        );
    }

    getModelosByMarca(marcaId: number): Observable<Modelo[]> {
        const url = `/api/marcas/${marcaId}/modelos`;
        return this.http.get<Modelo[]>(url).pipe(
            catchError(error => {
                console.error('Error en getModelosByMarca:', error);
                return throwError(() => error);
            })
        );
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

    updateVehicle(vehicle: Vehicle): Observable<Vehicle> {
        return this.http.put<Vehicle>(`${this.apiUrl}/${vehicle.idVeh}`, vehicle);
    }

    uploadImage(formData: FormData): Observable<{ nombreArchivo: string }> {
        return this.http.post<{ nombreArchivo: string }>(`${this.apiUrl}/upload`, formData);
    }

    createVehicle(vehicle: Vehicle): Observable<Vehicle> {
        return this.http.post<Vehicle>(this.apiUrl, vehicle);
    }
} 