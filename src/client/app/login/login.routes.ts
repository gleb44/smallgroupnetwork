import { Route } from '@angular/router';
import { LoginComponent } from './index';

export const signInPath = 'login';

export const LoginRoutes: Route[] = [
  <Route>{
    path: signInPath,
    component: LoginComponent
  }
];
