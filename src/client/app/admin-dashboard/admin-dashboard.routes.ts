import {Route} from "@angular/router";
import {AdminDashboardComponent} from "./admin-dashboard.component";

export const adminDashboardPath = '';

export const AdminDashboardRoutes:Route[] = [
    <Route>{
        path: adminDashboardPath,
        component: AdminDashboardComponent
    }
];
