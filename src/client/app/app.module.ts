import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {FormsModule} from "@angular/forms";
import {APP_BASE_HREF} from "@angular/common";
import {RouterModule} from "@angular/router";
import {HttpModule} from "@angular/http";
import {AppComponent} from "./app.component";
import {routes} from "./app.routes";
import {HomeModule} from "./home/home.module";
import {AboutModule} from "./about/about.module";
import {ProfileModule} from "./profile/profile.module";
import {LoginModule} from "./login/login.module";
import {AdminModule} from "./admin/admin.module";
import {SharedModule} from "./shared/shared.module";
import {AdminDashboardModule} from "./admin-dashboard/admin-dashboard.module";

@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        HttpModule,
        RouterModule.forRoot(routes),
        SharedModule.forRoot(),

        HomeModule,
        AboutModule,
        ProfileModule,
        LoginModule,
        AdminModule,
        AdminDashboardModule
    ],
    declarations: [AppComponent],
    providers: [{
        provide: APP_BASE_HREF,
        useValue: '<%= APP_BASE %>'
    }],
    bootstrap: [AppComponent]
})

export class AppModule {
}
