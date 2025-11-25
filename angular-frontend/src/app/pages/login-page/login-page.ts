import { afterRenderEffect, Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-login-page',
  imports: [RouterOutlet],
  templateUrl: './login-page.html',
  styleUrl: './login-page.scss',
})
export class LoginPage {

  // constructor(
  //   private auth: AuthService,
  //   private router: Router
  // ) {
  //   afterRenderEffect(() => {
  //     if (await this.auth.isAuthenticated()) {

  //     }
  //   })
  // }

}
