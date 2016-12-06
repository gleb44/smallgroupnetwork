import { Route } from '@angular/router';
import { ProfileComponent } from './index';

export const profilePath = 'profile';

export const ProfileRoutes: Route[] = [
  <Route>{
    path: profilePath,
    component: ProfileComponent
  }
];
