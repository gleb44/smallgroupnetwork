import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {AdminDashboardComponent} from "./admin-dashboard.component";
import {SharedModule as AppSharedModule } from "../shared/shared.module";
import {DataTableModule, SharedModule} from 'primeng/primeng';

@NgModule({
    imports: [CommonModule, AppSharedModule, DataTableModule, SharedModule],
    declarations: [AdminDashboardComponent],
    exports: [AdminDashboardComponent]
})

export class AdminDashboardModule {
}
