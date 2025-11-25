import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { AuthApi, RegisterUserDto, UserCredentialsDto, UserDto } from './auth-api';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  public readonly user$: BehaviorSubject<UserDto | undefined> = new BehaviorSubject<UserDto | undefined>(undefined);

  constructor(
    private authApi: AuthApi,
    private router: Router
  ) { }

  async init() {
    if (typeof localStorage == "undefined") return;

    try {
      const token = localStorage.getItem('authToken');
      if (token) {
        const user = await this.authApi.getProfile();
        this.user$.next(user)
      }
    } catch {
      this.logOut()
    }
  }

  async login(credentials: UserCredentialsDto) {
    const { token } = await this.authApi.login(credentials);
    localStorage.setItem('authToken', token);
    const user = await this.authApi.getProfile();
    this.user$.next(user)
    this.router.navigate(['/todos']);
  }

  async logOut() {
    localStorage.removeItem('authToken');
    this.user$.next(undefined)
  }

  async register(credentials: RegisterUserDto) {
    await this.authApi.register(credentials);
    await this.login(credentials)
  }

  async isAuthenticated() {
    await this.init()
    return !!this.user$.getValue()
  }

}
