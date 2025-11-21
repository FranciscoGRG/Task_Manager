import { Routes } from '@angular/router';
import { LandingPageComponent } from './components/landing-page-component/landing-page-component';
import { LoginComponent } from './components/login/login';
import { RegisterComponent } from './components/register/register';
import { TaskComponent } from './components/task-component/task-component';
import { authGuardGuard } from './guards/auth-guard-guard';

export const routes: Routes = [
    { path: '', component: LandingPageComponent },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'task', component: TaskComponent, canActivate: [authGuardGuard] },

];

// {
//     "username": "fran2",
//     "password": "12213123"
// }