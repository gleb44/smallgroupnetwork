import {Route} from "@angular/router";
import {HomeComponent} from "./index";

export const homePath = '';

export const HomeRoutes:Route[] = [
    <Route>{
        path: homePath,
        component: HomeComponent
    }
];
