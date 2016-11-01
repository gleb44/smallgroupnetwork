import { Route } from '@angular/router';

import { AdminDashboardComponent } from './admin-dashboard.component';
import { AuthGuardService, CanDeactivateGuard } from '../shared/index';

export const adminDashboardPath = '';

export const AdminDashboardRoutes: Route[] = [
  <Route>{
    path: adminDashboardPath,
    component: AdminDashboardComponent,
    canActivate: [AuthGuardService],
    canDeactivate: [CanDeactivateGuard]
  }
];
