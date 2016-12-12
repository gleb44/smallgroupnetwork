import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {AdminDashboardComponent} from "./admin-dashboard.component";
import {UserService} from "../shared/rest-services/user.service";

@NgModule({
    imports: [CommonModule],
    declarations: [AdminDashboardComponent],
    exports: [AdminDashboardComponent],
    providers: [UserService]
})

export class AdminDashboardModule {
}
