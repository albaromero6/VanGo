import { HttpClient } from '@angular/common/http';
import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { Observable, map, tap } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';
import { environment } from '../../environments/environment';
import { Router } from '@angular/router';

export interface User {
    nombre: string;
    apellido: string;
    email: string;
    rol?: string;
}

interface LoginResponse {
    token: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
    private apiUrl = `${environment.apiUrl}/auth`;
    private currentUserSubject = new BehaviorSubject<User | null>(null);
    public currentUser$ = this.currentUserSubject.asObservable();
    private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);

    constructor(
        private http: HttpClient,
        @Inject(PLATFORM_ID) private platformId: Object,
        private router: Router
    ) {
        this.initializeAuth();
    }

    private initializeAuth(): void {
        if (isPlatformBrowser(this.platformId)) {
            const storedUser = localStorage.getItem('user');
            const token = localStorage.getItem('token');

            if (storedUser) {
                this.currentUserSubject.next(JSON.parse(storedUser));
            }
            this.isAuthenticatedSubject.next(!!token);
        }
    }

    login(email: string, password: string): Observable<any> {
        return this.http.post<LoginResponse>(`${this.apiUrl}/login`, { email, password }).pipe(
            tap((response: LoginResponse) => {
                if (response && response.token) {
                    if (isPlatformBrowser(this.platformId)) {
                        localStorage.setItem('token', response.token);
                    }
                    this.isAuthenticatedSubject.next(true);
                    const userInfo = this.decodeToken(response.token);
                    console.log('Usuario decodificado del token:', userInfo);
                    const isAdmin = userInfo.rol === 'ADMINISTRADOR';
                    console.log('¿Es administrador?:', isAdmin);

                    this.currentUserSubject.next(userInfo);
                    if (isPlatformBrowser(this.platformId)) {
                        localStorage.setItem('user', JSON.stringify(userInfo));
                    }
                }
            })
        );
    }

    logout(): void {
        this.currentUserSubject.next(null);
        if (isPlatformBrowser(this.platformId)) {
            localStorage.removeItem('user');
            localStorage.removeItem('token');
        }
        this.isAuthenticatedSubject.next(false);
        this.router.navigate(['/']);
    }

    isAuthenticated(): boolean {
        if (isPlatformBrowser(this.platformId)) {
            return !!this.getToken();
        }
        return false;
    }

    isLoggedIn(): boolean {
        return this.isAuthenticated();
    }

    getToken(): string | null {
        if (isPlatformBrowser(this.platformId)) {
            return localStorage.getItem('token');
        }
        return null;
    }

    getAuthStatus(): Observable<boolean> {
        return this.isAuthenticatedSubject.asObservable();
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
                apellido: payload.apellido || '',
                email: payload.sub,
                rol: payload.rol
            };
            console.log('Usuario decodificado:', user);
            return user;
        } catch (error) {
            console.error('Error decoding token:', error);
            return {
                nombre: 'Usuario',
                apellido: '',
                email: '',
                rol: 'USER'
            };
        }
    }
}
