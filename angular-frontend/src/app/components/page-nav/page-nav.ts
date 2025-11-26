import { Component, effect, OnInit, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { AvatarModule } from 'primeng/avatar';
import { Button } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import { Menubar } from 'primeng/menubar';
import { UserDto } from '../../services/auth-api';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-page-nav',
  imports: [Menubar, Button, AvatarModule, MenuModule],
  templateUrl: './page-nav.html',
  styleUrl: './page-nav.scss',
})
export class PageNav {
  userMenuItems: MenuItem[] = [
    {
      label: "Todo List",
      icon: 'pi pi-list-check',
      command: () => this.router.navigate(['/todos']),
    },
    {
      label: "Logout",
      icon: 'pi pi-sign-out',
      command: () => {
        this.authService.logOut()
        this.router.navigate(['/login']);
      }
    },
  ]
  items = signal<MenuItem[]>([]);
  user = signal<UserDto | null>(null);

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
    this.authService.user$
      .pipe(takeUntilDestroyed())
      .subscribe((user) => this.user.set(user ?? null))

    effect(() => {
      let menu: MenuItem[] = [
        {
          label: 'Login',
          icon: 'pi pi-sign-in',
          routerLink: "/login"
        },
      ]

      if (!!this.user()) {
        menu = [
          {
            label: "Todo List",
            icon: 'pi pi-list-check',
            routerLink: "/todos"
          }
        ]
      }
      this.items.set(menu)
    })
  }

}

