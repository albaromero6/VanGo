import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { AuthService } from './auth.service';

@Injectable({
    providedIn: 'root'
})
export class ImageService {
    constructor(
        private http: HttpClient,
        private authService: AuthService
    ) { }

    getImageUrl(imagePath: string): string {
        if (!imagePath) return 'assets/img/Coche.png';
        return `${environment.apiUrl}/vehiculos/imagen/${imagePath}`;
    }

    getImage(imagePath: string): Observable<Blob> {
        const headers = new HttpHeaders().set('Authorization', `Bearer ${this.authService.getToken()}`);
        return this.http.get(this.getImageUrl(imagePath), {
            headers,
            responseType: 'blob'
        });
    }
} 