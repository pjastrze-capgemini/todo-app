import { Injectable, signal } from '@angular/core';
import { AuthService } from "./auth-service";
import { TodoApi, TodoDto } from './todo-api';

@Injectable({
  providedIn: 'root',
})
export class TodoService {
  userTodos = signal<TodoDto[]>([]);

  constructor(
    private authService: AuthService,
    private todoApi: TodoApi
  ) {
    this.authService.user$.subscribe(async user => {
      if (!user) {
        this.userTodos.set([]);
        return
      }

      const todos = await this.todoApi.getAll()
      this.userTodos.set(todos)
    })
  }

}
