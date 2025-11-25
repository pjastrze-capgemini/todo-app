import { Component, effect, signal } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { TableModule, TableRowSelectEvent, TableRowUnSelectEvent } from 'primeng/table';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { TodoDto } from '../../services/todo-api';
import { TodoService } from '../../services/todo-service';
import { Button } from 'primeng/button';
import { AddTodoForm } from '../../components/add-todo-form/add-todo-form';

@Component({
  selector: 'app-todos-page',
  imports: [RouterOutlet, TableModule, FormsModule, CommonModule, Button, AddTodoForm],
  templateUrl: './todos-page.html',
  styleUrl: './todos-page.scss',
})
export class TodosPage {
  todos = signal<TodoDto[]>([])
  selectedTodos: TodoDto[] = []

  constructor(
    private todoService: TodoService,
    private router: Router,
  ) {
    this.todos = todoService.userTodos
    effect(() => {
      this.selectedTodos = this.todos().filter(t => t.status == "CLOSED")
    })
  }

  async deleteTodo(todo: TodoDto) {
    this.todoService.deleteTodo(todo)
  }

  async previewTodo(todo: TodoDto) {
    this.router.navigate(["/todos/details", todo.id])
  }

  async onRowSelect(event: TableRowSelectEvent) {
    if (event.data) {
      await this.todoService.markTodoClosed(event.data as TodoDto)
    }
  }

  async onRowUnselect(event: TableRowUnSelectEvent<TodoDto>) {
    if (event.data) {
      await this.todoService.markTodoOpen(event.data as TodoDto)
    }
  }

}
