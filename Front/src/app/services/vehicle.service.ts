import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

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
} 