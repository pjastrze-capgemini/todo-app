import { afterRenderEffect, Component, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AvatarModule } from 'primeng/avatar';
import { ButtonModule } from 'primeng/button';
import { Dialog } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { TodoDto } from '../../services/todo-api';
import { TodoService } from '../../services/todo-service';
import { wait } from '../../utils';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';


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
    this.activeRoute?.paramMap.pipe(takeUntilDestroyed()).subscribe(params => {
      const id = params.get('id');
      if (id && !Number.isNaN(Number(id))) {
        this.todoService.getTodo(Number(id)).then(todo => this.todo.set(todo))
      }
    })
  }

  async backToTodoList() {
    this.visible = false;
    await wait(200) // wait for dialog to close
    this.router.navigate(["/todos"])
  }
}
