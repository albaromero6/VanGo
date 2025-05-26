import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class MapsService {
    constructor() { }

    getGoogleMapsUrl(direccion: string, ciudad: string): string {
        // Codificamos la dirección para la URL
        const direccionCodificada = encodeURIComponent(`${direccion}, ${ciudad}`);
        // Creamos la URL de Google Maps
        return `https://www.google.com/maps/search/?api=1&query=${direccionCodificada}`;
    }
} 