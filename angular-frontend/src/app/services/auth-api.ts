import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { firstValueFrom, Observable } from 'rxjs';

export interface UserDto {
  id: number,
  name: string
}

export interface UserCredentialsDto {
  login: string,
  password: string
}

export interface RegisterUserDto {
  login: string,
  password: string
  confirmPassword: string
}

@Injectable({
  providedIn: 'root',
})
export class AuthApi {

  constructor(private http: HttpClient) { }

  login(credentials: UserCredentialsDto): Promise<{token: string}> {
    return firstValueFrom(
      this.http.post<{token: string}>(`${environment.apiUrl}/auth/login`, credentials)
    )
  }

  register(credentials: RegisterUserDto): Promise<void> {
    return firstValueFrom(
      this.http.post<void>(`${environment.apiUrl}/auth/register`, credentials)
    )
  }

  getProfile(): Promise<UserDto> {
    return firstValueFrom
      (this.http.get<UserDto>(`${environment.apiUrl}/auth/me`)
      )
  }

}
