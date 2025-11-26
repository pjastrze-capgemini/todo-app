import { Injectable, signal } from '@angular/core';
import { AuthService } from "./auth-service";
import { CreateTodoDto, TodoApi, TodoDto } from './todo-api';

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
      this.loadTodos()
    })
  }

  async loadTodos(toodId?: string) {
    const todos = await this.todoApi.getAll(toodId)
    this.userTodos.set(todos)
  }

  async addTodo(dto: CreateTodoDto) {
    await this.todoApi.createTodo(dto)
    await this.loadTodos()
  }

  async getTodo(todoId: number): Promise<TodoDto> {
    return await this.todoApi.getTodo(todoId)
  }

  async deleteTodo(dto: TodoDto) {
    await this.todoApi.deleteTodo(dto.id)
    await this.loadTodos()
  }

  async markTodoClosed(todo: TodoDto) {
    await this.todoApi.updateTodo(todo.id, { status: "CLOSED" })
    this.updateTodoInMemory(todo.id, t => t.status = "CLOSED")
  }

  async markTodoOpen(todo: TodoDto) {
    await this.todoApi.updateTodo(todo.id, { status: "OPEN" })
    this.updateTodoInMemory(todo.id, t => t.status = "OPEN")
  }

  updateTodoInMemory(todoId: number, fn: (todo: TodoDto) => void) {
    const allTodos = this.userTodos()
    const memoryTodo = allTodos.find(t => t.id == todoId);
    if (memoryTodo) {
      fn(memoryTodo)
      this.userTodos.set([...allTodos])
    }
  }

}
