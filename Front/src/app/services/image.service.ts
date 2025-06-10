import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { AuthService } from './auth.service';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
    providedIn: 'root'
})
export class ImageService {
    constructor(
        private http: HttpClient,
        private authService: AuthService,
        @Inject(PLATFORM_ID) private platformId: Object
    ) { }

    getImageUrl(imagePath: string): string {
        if (!imagePath) return 'assets/img/Coche.png';
        return `${environment.apiUrl}/vehiculos/imagen/${imagePath}`;
    }

    getImage(imagePath: string): Observable<Blob> {
        const token = isPlatformBrowser(this.platformId) ? this.authService.getToken() : null;
        const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
        return this.http.get(this.getImageUrl(imagePath), {
            headers,
            responseType: 'blob'
        });
    }
} 