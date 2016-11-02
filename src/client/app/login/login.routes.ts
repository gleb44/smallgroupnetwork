import { Route } from '@angular/router';
import { LoginComponent } from './index';

export const loginPath = 'login';

export const LoginRoutes: Route[] = [
  <Route>{
    path: loginPath,
    component: LoginComponent
  }
];
