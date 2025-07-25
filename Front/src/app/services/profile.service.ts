import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap, map } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { isPlatformBrowser } from '@angular/common';

export interface ProfileData {
    idUsu: number;
    nombre: string;
    apellido: string;
    email: string;
    telefono: string;
    fechaNacimiento: string;
    direccion: string;
    dni: string;
    numeroLicencia?: string;
    fechaExpedicion?: string;
    fechaCaducidad?: string;
    anosExperiencia?: number;
    rol: string;
    reservas: {
        idRes: number;
        estado: string;
        fechaInicio: string;
        fechaFin: string;
        precioTotal: number;
        sede: {
            idSed: number;
            nombre: string;
        };
        vehiculo: {
            idVeh: number;
            modelo: {
                idMod: number;
                nombre: string;
                marca: {
                    idMar: number;
                    nombre: string;
                };
            };
        };
    }[];
}

interface Sede {
    idSed: number;
    direccion: string;
    ciudad: string;
    telefono: string;
    imagen: string;
}

interface Vehiculo {
    idVeh: number;
    modelo: string;
    marca: string;
    imagen: string;
    camper: string;
}

interface Reserva {
    idReser: number;
    camper: string;
    estado: string;
    inicio: string;
    fin: string;
    idSed_Salid: Sede;
    idSed_Lleg: Sede;
    total: number;
    vehiculo: Vehiculo;
}

@Injectable({
    providedIn: 'root'
})
export class ProfileService {
    private apiUrl = `${environment.apiUrl}/usuarios`;

    constructor(
        private http: HttpClient,
        @Inject(PLATFORM_ID) private platformId: Object
    ) {
        this.cleanInvalidToken();
    }

    private cleanInvalidToken() {
        if (isPlatformBrowser(this.platformId)) {
            const token = localStorage.getItem('token');
            if (token && token.includes('[object Object]')) {
                console.error('Token inválido detectado, limpiando...');
                localStorage.removeItem('token');
                localStorage.removeItem('user');
            }
        }
    }

    private getHeaders(): HttpHeaders {
        if (!isPlatformBrowser(this.platformId)) {
            throw new Error('No authentication token found');
        }

        const token = localStorage.getItem('token');
        console.log('Token almacenado en localStorage:', token);

        if (!token) {
            console.error('No se encontró token en localStorage');
            throw new Error('No authentication token found');
        }

        // Si el token es [object Object], limpiar y lanzar error
        if (token.includes('[object Object]')) {
            console.error('Token inválido detectado');
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            throw new Error('Token inválido');
        }

        // Verificar si el token es un objeto
        let tokenValue: string;
        try {
            // Intentar parsear el token si es un objeto JSON
            const tokenObj = JSON.parse(token);
            tokenValue = tokenObj.token || tokenObj.access_token || tokenObj;
            console.log('Token parseado:', tokenValue);
        } catch (e) {
            // Si no es JSON, usar el token tal cual
            tokenValue = token;
        }

        // Verificar el formato del token
        console.log('Token a usar:', tokenValue);
        console.log('Longitud del token:', tokenValue.length);
        console.log('Formato del token:', tokenValue.startsWith('Bearer ') ? 'Con prefijo Bearer' : 'Sin prefijo Bearer');
        console.log('Primeros 20 caracteres del token:', tokenValue.substring(0, 20) + '...');

        // Asegurarse de que el token tenga el formato correcto
        const finalToken = tokenValue.startsWith('Bearer ') ? tokenValue : `Bearer ${tokenValue}`;

        const headers = new HttpHeaders()
            .set('Authorization', finalToken)
            .set('Content-Type', 'application/json')
            .set('Accept', 'application/json');

        // Log detallado de los headers
        console.log('Headers completos:', headers);
        console.log('Authorization header:', headers.get('Authorization'));
        console.log('Content-Type header:', headers.get('Content-Type'));
        console.log('Accept header:', headers.get('Accept'));

        return headers;
    }

    private handleError(error: HttpErrorResponse) {
        console.error('Error completo:', error);
        console.error('URL de la petición:', error.url);
        console.error('Headers de la petición:', error.headers);
        console.error('Cuerpo del error:', error.error);
        console.error('Status:', error.status);
        console.error('Status Text:', error.statusText);

        let errorMessage = 'Ha ocurrido un error al procesar la solicitud';

        if (error.error instanceof ErrorEvent) {
            errorMessage = `Error: ${error.error.message}`;
        } else {
            switch (error.status) {
                case 403:
                    errorMessage = 'No tienes permisos para acceder a este recurso. Por favor, verifica tus credenciales.';
                    if (isPlatformBrowser(this.platformId)) {
                        localStorage.removeItem('token');
                        localStorage.removeItem('user');
                    }
                    break;
                case 401:
                    errorMessage = 'Sesión expirada. Por favor, vuelve a iniciar sesión.';
                    if (isPlatformBrowser(this.platformId)) {
                        localStorage.removeItem('token');
                        localStorage.removeItem('user');
                    }
                    break;
                case 500:
                    // Manejar errores de texto plano
                    if (typeof error.error === 'string') {
                        errorMessage = error.error;
                    } else if (error.error && error.error.message) {
                        errorMessage = error.error.message;
                    } else {
                        errorMessage = 'Error interno del servidor. Por favor, inténtalo de nuevo más tarde.';
                    }
                    break;
                default:
                    if (error.error && error.error.message) {
                        errorMessage = error.error.message;
                    } else if (typeof error.error === 'string') {
                        errorMessage = error.error;
                    }
            }
        }

        return throwError(() => new Error(errorMessage));
    }

    getProfileData(): Observable<ProfileData> {
        return this.http.get<ProfileData>(`${this.apiUrl}/perfil`);
    }

    updatePersonalInfo(profileData: Partial<ProfileData>): Observable<ProfileData> {
        const headers = this.getHeaders();

        // Asegurarnos de que los datos están en el formato correcto
        const dataToSend = {
            ...profileData,
            // Convertir campos vacíos a null
            telefono: profileData.telefono || null,
            fechaNacimiento: profileData.fechaNacimiento || null,
            direccion: profileData.direccion || null,
            dni: profileData.dni || null
        };

        console.log('Datos a enviar:', dataToSend);
        console.log('Headers:', headers);

        return this.http.put<ProfileData>(`${this.apiUrl}/perfil`, dataToSend, {
            headers,
            observe: 'response',
            responseType: 'text' as 'json'
        }).pipe(
            tap(response => {
                console.log('Respuesta completa:', response);
                console.log('Status:', response.status);
                console.log('Headers:', response.headers);
                console.log('Body:', response.body);
            }),
            map(response => {
                try {
                    // Parsear la respuesta como JSON
                    return typeof response.body === 'string' ? JSON.parse(response.body) : response.body;
                } catch (e) {
                    console.error('Error al parsear la respuesta:', e);
                    throw new Error('Error al procesar la respuesta del servidor');
                }
            }),
            catchError(error => {
                console.error('Error detallado:', {
                    status: error.status,
                    statusText: error.statusText,
                    error: error.error,
                    headers: error.headers,
                    url: error.url
                });

                if (error.error instanceof ErrorEvent) {
                    // Error del cliente
                    console.error('Error del cliente:', error.error.message);
                    return throwError(() => new Error('Error de conexión: ' + error.error.message));
                } else {
                    // Error del servidor
                    let errorMessage = 'Error interno del servidor';

                    if (typeof error.error === 'string') {
                        errorMessage = error.error;
                    } else if (error.error && error.error.message) {
                        errorMessage = error.error.message;
                    }

                    console.error('Mensaje de error del servidor:', errorMessage);
                    return throwError(() => new Error(errorMessage));
                }
            })
        );
    }

    getReservations(): Observable<Reserva[]> {
        const headers = this.getHeaders();
        return this.http.get<any[]>(`${environment.apiUrl}/reservas/cliente/mis-reservas`, { headers }).pipe(
            map(reservas => {
                console.log('Datos crudos recibidos:', JSON.stringify(reservas, null, 2));
                return reservas.map(reserva => {
                    const vehiculo = reserva.vehiculo || {};
                    console.log('Vehículo antes de procesar:', JSON.stringify(vehiculo, null, 2));

                    const reservaProcesada = {
                        ...reserva,
                        vehiculo: {
                            idVeh: vehiculo.idVeh || 0,
                            marca: vehiculo.modelo?.marca?.nombre || '',
                            modelo: vehiculo.modelo?.nombre || '',
                            imagen: vehiculo.imagen || '',
                            camper: vehiculo.camper || ''
                        }
                    };

                    console.log('Reserva procesada:', JSON.stringify(reservaProcesada, null, 2));
                    return reservaProcesada;
                });
            }),
            catchError(error => {
                console.error('Error al obtener las reservas:', error);
                return this.handleError(error);
            })
        );
    }

    deleteAccount(): Observable<void> {
        const headers = this.getHeaders();
        return this.http.delete<void>(`${this.apiUrl}/perfil`, { headers }).pipe(
            catchError(error => {
                console.error('Error al eliminar la cuenta:', error);
                return this.handleError(error);
            })
        );
    }

    deleteReservation(idRes: number): Observable<any> {
        if (!isPlatformBrowser(this.platformId)) {
            return throwError(() => new Error('No hay token de autenticación'));
        }

        const token = localStorage.getItem('token');
        if (!token) {
            return throwError(() => new Error('No hay token de autenticación'));
        }

        return this.http.delete(`${environment.apiUrl}/reservas/${idRes}`, {
            headers: new HttpHeaders({
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            })
        }).pipe(
            catchError(error => {
                console.error('Error al eliminar la reserva:', error);
                return throwError(() => error);
            })
        );
    }
} 