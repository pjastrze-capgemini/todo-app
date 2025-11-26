import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Button } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputTextModule } from 'primeng/inputtext';
import { MessageModule } from 'primeng/message';
import { AuthService } from '../../services/auth-service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-register-form',
  imports: [CardModule, Button, InputTextModule, FloatLabelModule, FormsModule, ReactiveFormsModule, RouterModule, MessageModule],
  templateUrl: './register-form.html',
  styleUrl: './register-form.scss',
})
export class RegisterForm {
  registerForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      login: ['', Validators.required],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required]
    });
  }

  isInvalid(controlName: string) {
    const control = this.registerForm.get(controlName);
    return control?.invalid && control.touched;
  }

  async onRegister(): Promise<void> {
    if (!this.registerForm.valid) return;
    const { login, password, confirmPassword } = this.registerForm.value;

    try {
      await this.authService.register({
        login: login,
        password: password,
        confirmPassword: confirmPassword
      })
    } catch (e) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Something went wrong!', life: 3000 });
    } finally {
      this.registerForm.reset()
    }
  }
}
