import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Button } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputTextModule } from 'primeng/inputtext';
import { MessageModule } from 'primeng/message';
import { TodoService } from '../../services/todo-service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-add-todo-form',
  imports: [CardModule, Button, InputTextModule, FloatLabelModule, FormsModule, ReactiveFormsModule, RouterModule, MessageModule],
  templateUrl: './add-todo-form.html',
  styleUrl: './add-todo-form.scss',
})
export class AddTodoForm {
  addTodoForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private todoService: TodoService,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {
    this.addTodoForm = this.fb.group({
      title: ['', Validators.required],
    });
  }

  isInvalid(controlName: string) {
    const control = this.addTodoForm.get(controlName);
    return control?.invalid && control.touched;
  }

  async onAdd(): Promise<void> {
    if (!this.addTodoForm.valid) return;
    const { title } = this.addTodoForm.value;
    try {
      await this.todoService.addTodo({ title })
    } catch (e) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Something went wrong!', life: 3000 });
    } finally {
      this.addTodoForm.reset()
    }
  }

}
