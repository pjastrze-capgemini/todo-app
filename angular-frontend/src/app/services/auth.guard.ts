
import { inject, PLATFORM_ID } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth-service';
import { isPlatformBrowser } from '@angular/common';

export const authGuard: CanActivateFn = async () => {
    const platformId = inject(PLATFORM_ID);
    if (!isPlatformBrowser(platformId)) {
        return true; // SSR path
    }


    const auth = inject(AuthService);
    const router = inject(Router);
    return await auth.isAuthenticated()
        ? true
        : router.parseUrl('/login');
};

export const loginRouteGuard: CanActivateFn = async () => {
        const platformId = inject(PLATFORM_ID);
    if (!isPlatformBrowser(platformId)) {
        return true; // SSR path
    }
    
    const auth = inject(AuthService);
    const router = inject(Router);
    return !(await auth.isAuthenticated())
        ? true
        : router.parseUrl('/todos');
};

