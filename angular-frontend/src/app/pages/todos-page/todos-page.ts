import { Component, effect, signal } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { TableModule, TableRowSelectEvent, TableRowUnSelectEvent } from 'primeng/table';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { TodoDto } from '../../services/todo-api';
import { TodoService } from '../../services/todo-service';
import { Button } from 'primeng/button';
import { AddTodoForm } from '../../components/add-todo-form/add-todo-form';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputTextModule } from 'primeng/inputtext';
import { BehaviorSubject, debounceTime } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MessageService } from 'primeng/api';
import { TooltipModule } from 'primeng/tooltip';

@Component({
  selector: 'app-todos-page',
  imports: [RouterOutlet, TableModule, FormsModule, CommonModule, Button, AddTodoForm, FloatLabelModule, InputTextModule, TooltipModule],
  templateUrl: './todos-page.html',
  styleUrl: './todos-page.scss',
})
export class TodosPage {
  searchById: BehaviorSubject<string> = new BehaviorSubject("")
  todos = signal<TodoDto[]>([])
  selectedTodos: TodoDto[] = []

  constructor(
    private todoService: TodoService,
    private router: Router,
    private messageService: MessageService
  ) {
    this.todos = todoService.userTodos
    effect(() => {
      this.selectedTodos = this.todos().filter(t => t.status == "CLOSED")
    })
    this.searchById.pipe(takeUntilDestroyed(), debounceTime(2_000)).subscribe(this.filterTodById.bind(this))
  }

  async filterTodById(id: string) {
    try {
      await this.todoService.loadTodos(id)
    } catch (e) {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: `Todo with id:${id} was not found.`, life: 5_000 });
      this.todoService.loadTodos()
    }
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
