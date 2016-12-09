import { Route } from '@angular/router';
import { ProfileComponent } from './index';
import {AuthGuardService} from "../shared/index";

export const profilePath = 'profile';

export const ProfileRoutes: Route[] = [
  <Route>{
    path: profilePath,
    component: ProfileComponent,
    canActivate: [AuthGuardService],
    canDeactivate: [AuthGuardService],
  }
];
