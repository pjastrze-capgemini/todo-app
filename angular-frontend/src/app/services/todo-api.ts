import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { environment } from '../../environments/environment';
import { UserDto } from './auth-api';

export interface TodoDto {
  id: number,
  title: string,
  status: "OPEN" | "CLOSED",
  owner: UserDto
}

export interface CreateTodoDto {
  title?: string,
  status?: "OPEN" | "CLOSED"
}

@Injectable({
  providedIn: 'root',
})
export class TodoApi {

  constructor(private http: HttpClient) { }

  getAll(): Promise<TodoDto[]> {
    return firstValueFrom(
      this.http.get<TodoDto[]>(`${environment.apiUrl}/todo`)
    )
  }

  getTodo(id: number): Promise<TodoDto> {
    return firstValueFrom(
      this.http.get<TodoDto>(`${environment.apiUrl}/todo/${id}`)
    )
  }

  deleteTodo(id: number): Promise<TodoDto> {
    return firstValueFrom(
      this.http.delete<TodoDto>(`${environment.apiUrl}/todo/${id}`)
    )
  }

  createTodo(dto: CreateTodoDto): Promise<TodoDto> {
    return firstValueFrom(
      this.http.post<TodoDto>(`${environment.apiUrl}/todo`, dto)
    )
  }

  updateTodo(id: number, dto: CreateTodoDto): Promise<TodoDto[]> {
    return firstValueFrom(
      this.http.put<TodoDto[]>(`${environment.apiUrl}/todo/${id}`, dto)
    )
  }

}
