import {Routes} from "@angular/router";
import {AboutRoutes} from "./about/index";
import {HomeRoutes} from "./home/index";
import {LoginRoutes} from "./login/index";
import {AdminRoutes} from "./admin/index";
import {ProfileRoutes} from "./profile/index";

export const routes:Routes = [
    ...HomeRoutes,
    ...AboutRoutes,
    ...ProfileRoutes,
    ...LoginRoutes,
    ...AdminRoutes,
    {
        path: '**',
        redirectTo: ''
    }
];
