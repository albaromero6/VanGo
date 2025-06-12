import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';

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

@Injectable({ providedIn: 'root' })
export class VehicleService {

  private readonly base = environment.apiUrl;          // '/api'

  constructor(private http: HttpClient) {}

  /* ---------- listados ---------- */
  getAllVehicles(page = 0, size = 9): Observable<PaginatedResponse<Vehicle>> {
    const params = new HttpParams()
        .set('page', page)
        .set('size', size);

    return this.http.get<PaginatedResponse<Vehicle>>(
      `${this.base}/vehiculos`, { params }
    );
  }

  getVehicleById(id: number): Observable<Vehicle> {
    return this.http.get<Vehicle>(`${this.base}/vehiculos/${id}`);
  }

  getAllMarcas(): Observable<Marca[]> {
    return this.http.get<Marca[]>(`${this.base}/marcas`)
      .pipe(catchError(this.handleError('getAllMarcas')));
  }

  getModelosByMarca(marcaId: number): Observable<Modelo[]> {
    return this.http.get<Modelo[]>(`${this.base}/marcas/${marcaId}/modelos`)
      .pipe(catchError(this.handleError('getModelosByMarca')));
  }

  getAvailableVehicles(): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(`${this.base}/vehiculos/disponibles`);
  }

  /* ---------- CRUD ---------- */
  createVehicle(vehicle: Vehicle): Observable<Vehicle> {
    return this.http.post<Vehicle>(`${this.base}/vehiculos`, vehicle);
  }

  updateVehicle(vehicle: Vehicle): Observable<Vehicle> {
    return this.http.put<Vehicle>(`${this.base}/vehiculos/${vehicle.idVeh}`, vehicle);
  }

  deleteVehicle(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/vehiculos/${id}`)
      .pipe(catchError(this.translateDeleteError));
  }

  uploadImage(formData: FormData): Observable<{ nombreArchivo: string }> {
    return this.http.post<{ nombreArchivo: string }>(
      `${this.base}/vehiculos/upload`, formData
    );
  }

  /* ---------- helpers ---------- */
  private handleError(op: string) {
    return (error: HttpErrorResponse) => {
      console.error(`Error en ${op}:`, error);
      return throwError(() => error);
    };
  }

  private translateDeleteError = (error: HttpErrorResponse) => {
    let msg = 'Error al eliminar el vehículo';
    if (error.status === 500)      msg = 'No se puede eliminar: tiene reservas asociadas';
    else if (error.status === 404) msg = 'El vehículo no existe';
    else if (error.status === 403) msg = 'No tienes permisos para eliminar vehículos';
    return throwError(() => msg);
  };
}