import { afterRenderEffect, Component, OnInit, signal } from '@angular/core';
import { Dialog } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { AvatarModule } from 'primeng/avatar';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { TodoService } from '../../services/todo-service';
import { TodoDto } from '../../services/todo-api';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';


function wait(ms: number): Promise<void> {
  return new Promise(resolve => setTimeout(resolve, ms));
}


@Component({
  selector: 'app-todo-details',
  imports: [Dialog, ButtonModule, InputTextModule, AvatarModule],
  templateUrl: './todo-details.html',
  styleUrl: './todo-details.scss',
})
export class TodoDetails {
  visible = true
  todo = signal<TodoDto | undefined>(undefined)

  constructor(
    private router: Router,
    private activeRoute: ActivatedRoute,
    private todoService: TodoService
  ) {
    afterRenderEffect(() => {
      this.activeRoute?.paramMap.subscribe(params => {
        const id = params.get('id');
        if (id && !Number.isNaN(Number(id))) {
          this.todoService.getTodo(Number(id)).then(todo => this.todo.set(todo))
        }
      })
    })
  }

  async backToTodoList() {
    this.visible = false;
    await wait(200) // wait for dialog to close
    this.router.navigate(["/todos"])
  }
}
