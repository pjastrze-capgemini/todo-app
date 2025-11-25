
import { inject } from '@angular/core';
import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { AuthService } from './auth-service';


export const authGuard: CanActivateFn = async () => {
  const auth = inject(AuthService);
  const router = inject(Router);

  return await auth.isAuthenticated()
    ? true
    : router.parseUrl('/login'); // prefer returning UrlTree over navigate()
};
