import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Menubar } from 'primeng/menubar';

@Component({
  selector: 'app-page-nav',
  imports: [Menubar],
  templateUrl: './page-nav.html',
  styleUrl: './page-nav.scss',
})
export class PageNav implements OnInit {
  items: MenuItem[] | undefined;

  ngOnInit() {
    this.items = [
      {
        label: 'Home',
        icon: 'pi pi-home',
        routerLink: "/"
      },
      {
        label: 'Login',
        icon: 'pi pi-star',
        routerLink: "/login"
      },
      {
        label: 'Todos',
        icon: 'pi pi-search',
        routerLink: "/todos",
        items: [
          {
            label: 'Components',
            icon: 'pi pi-bolt'
          },
          {
            label: 'Blocks',
            icon: 'pi pi-server'
          },
          {
            label: 'UI Kit',
            icon: 'pi pi-pencil'
          },
          {
            label: 'Templates',
            icon: 'pi pi-palette',
            items: [
              {
                label: 'Apollo',
                icon: 'pi pi-palette'
              },
              {
                label: 'Ultima',
                icon: 'pi pi-palette'
              }
            ]
          }
        ],
      },
      {
        label: 'About',
        icon: 'pi pi-star',
        routerLink: "/about"
      },
    ]
  }

}
