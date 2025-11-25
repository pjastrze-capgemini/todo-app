import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TableModule } from 'primeng/table';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { TodoDto } from '../../services/todo-api';
import { TodoService } from '../../services/todo-service';
import { Button } from 'primeng/button';

@Component({
  selector: 'app-todos-page',
  imports: [RouterOutlet, TableModule, FormsModule, CommonModule, Button],
  templateUrl: './todos-page.html',
  styleUrl: './todos-page.scss',
})
export class TodosPage {
  products = signal<TodoDto[]>([])
  selectedProducts: TodoDto[] = []

  constructor(private todoService: TodoService) {
    this.products = todoService.userTodos
  }

}
