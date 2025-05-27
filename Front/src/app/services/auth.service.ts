import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map, tap } from 'rxjs';
import { BehaviorSubject } from 'rxjs';

interface LoginResponse {
    token: string;
}

interface User {
    nombre: string;
    email: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
    private apiUrl = 'http://localhost:8080/api/auth';
    private currentUserSubject = new BehaviorSubject<User | null>(null);
    public currentUser$ = this.currentUserSubject.asObservable();

    constructor(private http: HttpClient) {

        const user = localStorage.getItem('user');
        if (user) {
            this.currentUserSubject.next(JSON.parse(user));
        }
    }

    login(email: string, password: string): Observable<string> {
        return this.http.post<LoginResponse>(`${this.apiUrl}/login`, { email, password })
            .pipe(
                map(response => response.token),
                tap(token => {
                    // Descodificar el token para obtener la información del usuario
                    const userInfo = this.decodeToken(token);
                    this.currentUserSubject.next(userInfo);
                    localStorage.setItem('user', JSON.stringify(userInfo));
                })
            );
    }

    logout() {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        this.currentUserSubject.next(null);
    }

    isLoggedIn(): boolean {
        return !!this.currentUserSubject.value;
    }

    private decodeToken(token: string): User {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        const payload = JSON.parse(jsonPayload);
        console.log('Token payload:', payload);
        return {
            nombre: payload.nombre || 'Usuario',
            email: payload.sub
        };
    }
}
