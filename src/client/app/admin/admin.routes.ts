import { Route } from '@angular/router';

import { AdminComponent } from './admin.component';
import { AdminDashboardRoutes } from '../admin-dashboard/index';
import { AuthGuardService, CanDeactivateGuard } from '../shared/index';


export const adminPath = 'admin';

const AdminChildrenRoutes:Route = [
    ...AdminDashboardRoutes
];

export const AdminRoutes: Route[] = [
  <Route>{
    path: adminPath,
    component: <any>AdminComponent,
    canActivate: [AuthGuardService],
    canDeactivate: [CanDeactivateGuard],
    children: <any>AdminChildrenRoutes
  }
];
