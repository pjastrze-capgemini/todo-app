import { Routes } from '@angular/router';
import { LoginForm } from './components/login-form/login-form';
import { RegisterForm } from './components/register-form/register-form';
import { LoginPage } from './pages/login-page/login-page';
import { TodosPage } from './pages/todos-page/todos-page';
import { authGuard } from './services/auth.guard';

export const routes: Routes = [
    {
        path: '',
        redirectTo: "/login",
        pathMatch: "full"
    },
    {
        path: 'login',
        component: LoginPage,
        children: [
            {
                path: "",
                component: LoginForm
            },
            {
                path: "create-account",
                component: RegisterForm
            }
        ]
    },
    {
        path: 'todos',
        component: TodosPage,
        canActivate: [authGuard],
        children: [
            {
                path: "details/:id",
                component: RegisterForm
            },
        ]
    }
];
