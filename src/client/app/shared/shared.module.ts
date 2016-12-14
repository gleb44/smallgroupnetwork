import {NgModule, ModuleWithProviders} from "@angular/core";
import {CommonModule} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {RouterModule} from "@angular/router";
import {MessageComponent} from "./message/index";
import {NavbarComponent} from "./navbar/index";
import {FooterComponent} from "./footer/index";
import {NOTIFICATION_PROVIDERS} from "./notification/notification";
import {UserService, AccountService} from "./rest-services/index";
import {HttpErrorHandlerService} from "./http-error-handler/index";
import {HttpLoaderService, HttpLoaderComponent} from "./http-loader/index";
import {AuthGuardService, AuthService} from "./auth/index";
import {InfiniteScroll} from "./infinite-scroll/index";
import {UrlTrackingService} from "./url-tracking/url-tracking.service";

/**
 * Do not specify providers for modules that might be imported by a lazy loaded module.
 */

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        FormsModule
    ],
    declarations: [
        NavbarComponent,
        FooterComponent,
        HttpLoaderComponent,
        MessageComponent,
        InfiniteScroll
    ],
    exports: [
        CommonModule,
        RouterModule,
        FormsModule,

        NavbarComponent,
        FooterComponent,
        HttpLoaderComponent,
        MessageComponent,
        InfiniteScroll
    ]
})
export class SharedModule {
    static forRoot():ModuleWithProviders {
        return {
            ngModule: <any>SharedModule,
            providers: [
                NOTIFICATION_PROVIDERS,
                <any>UrlTrackingService,
                <any>HttpErrorHandlerService,
                <any>HttpLoaderService,

                // Auth
                <any>AuthGuardService,
                <any>AuthService,

                // REST
                <any>UserService,
                <any>AccountService
            ]
        };
    }
}
