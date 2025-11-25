import { CommonModule } from '@angular/common';
import { afterRenderEffect, AfterViewInit, Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { PageNav } from './components/page-nav/page-nav';
import { AuthService } from './services/auth-service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, PageNav, CommonModule],
  providers: [],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {

  constructor(private auth: AuthService) {
    afterRenderEffect(() => {
      this.auth.init()
    })
  }
}
