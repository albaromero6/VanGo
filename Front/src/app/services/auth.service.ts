// src/app/services/auth.service.ts
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';

interface LoginResponse {
    token: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
    private apiUrl = 'http://localhost:8080/api/auth';

    constructor(private http: HttpClient) { }

    login(email: string, password: string): Observable<string> {
        return this.http.post<LoginResponse>(`${this.apiUrl}/login`, { email, password })
            .pipe(
                map(response => response.token)
            );
    }
}
