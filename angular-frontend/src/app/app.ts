import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { PageNav } from './components/page-nav/page-nav';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, PageNav],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly title = signal('angular-frontend');
}
