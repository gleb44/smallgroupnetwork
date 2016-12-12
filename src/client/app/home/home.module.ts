import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {InputTextModule, PasswordModule, ButtonModule} from "primeng/primeng";
import {AgmCoreModule} from "angular2-google-maps/core";
import {GoogleMapsComponent} from "./google-maps/index";
import {HomeComponent} from "./home.component";
import {SharedModule} from "../shared/shared.module";
import {AuthService} from "../shared/index";

@NgModule({
    imports: [
        CommonModule,
        SharedModule,
        InputTextModule,
        PasswordModule,
        ButtonModule,
        AgmCoreModule.forRoot({
            apiKey: 'AIzaSyA93LGzhjbOIDkL-A7cLEPXnBBQD-TiPOk'
        })
    ],
    declarations: [
        HomeComponent,
        GoogleMapsComponent
    ],
    exports: [
        HomeComponent,
        GoogleMapsComponent
    ],
    providers: [AuthService]
})
export class HomeModule {
}
