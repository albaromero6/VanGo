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
    rol?: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
    private apiUrl = 'http://localhost:8080/api/auth';
    private currentUserSubject = new BehaviorSubject<User | null>(null);
    public currentUser$ = this.currentUserSubject.asObservable();

    constructor(private http: HttpClient) {
        const token = localStorage.getItem('token');
        const user = localStorage.getItem('user');
        console.log('Token from localStorage:', token);
        console.log('User from localStorage:', user);

        if (token && user) {
            const decodedUser = this.decodeToken(token);
            console.log('Decoded user on init:', decodedUser);
            this.currentUserSubject.next(decodedUser);
        }
    }

    login(email: string, password: string): Observable<string> {
        return this.http.post<LoginResponse>(`${this.apiUrl}/login`, { email, password })
            .pipe(
                map(response => response.token),
                tap(token => {
                    console.log('Token recibido:', token);
                    // Descodificar el token para obtener la información del usuario
                    const userInfo = this.decodeToken(token);
                    console.log('Usuario decodificado del token:', userInfo);

                    // Verificar si el usuario es administrador
                    const isAdmin = userInfo.rol === 'ADMINISTRADOR';
                    console.log('¿Es administrador?:', isAdmin);

                    this.currentUserSubject.next(userInfo);
                    localStorage.setItem('user', JSON.stringify(userInfo));
                    localStorage.setItem('token', token);
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

    isAdmin(): boolean {
        const user = this.currentUserSubject.value;
        console.log('Checking admin status for user:', user);
        console.log('User role:', user?.rol);
        console.log('Is ADMINISTRADOR?', user?.rol === 'ADMINISTRADOR');
        return user?.rol === 'ADMINISTRADOR';
    }

    private decodeToken(token: string): User {
        try {
            const base64Url = token.split('.')[1];
            const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
                return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
            }).join(''));

            const payload = JSON.parse(jsonPayload);
            console.log('Token payload completo:', payload);
            console.log('Rol extraído del token:', payload.rol);

            const user = {
                nombre: payload.nombre || 'Usuario',
                email: payload.sub,
                rol: payload.rol
            };
            console.log('Usuario decodificado:', user);
            return user;
        } catch (error) {
            console.error('Error decoding token:', error);
            return {
                nombre: 'Usuario',
                email: '',
                rol: 'USER'
            };
        }
    }
}
