import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MessageModule } from 'primeng/message';
import { Button } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputTextModule } from 'primeng/inputtext';
import { AuthService } from '../../services/auth-service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-login-form',
  imports: [CardModule, Button, InputTextModule, FloatLabelModule, FormsModule, ReactiveFormsModule, RouterModule, MessageModule],
  templateUrl: './login-form.html',
  styleUrl: './login-form.scss',
})
export class LoginForm implements OnInit {
  loginForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      login: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  isInvalid(controlName: string) {
    const control = this.loginForm.get(controlName);
    return control?.invalid && control.touched;
  }

  async onLogin(): Promise<void> {
    if (!this.loginForm.valid) return;
    const { login, password } = this.loginForm.value;
    try {
      await this.authService.login({
        login: login,
        password: password,
      })
    } catch (e) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Invalid login or Password', life: 3000 });
    } finally {
      this.loginForm.reset()
    }
  }

}
