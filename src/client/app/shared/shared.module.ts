import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { MessageComponent } from './message/index';
import { NavbarComponent } from './navbar/index';
import { NameListService } from './name-list/index';
import { FooterComponent } from './footer/index';

import {NOTIFICATION_PROVIDERS} from "./notification/notification";
import { AccountService } from './rest-services/index';
import { HttpErrorHandlerService } from "./http-error-handler/index";
import { HttpLoaderService, HttpLoaderComponent } from "./http-loader/index";

/**
 * Do not specify providers for modules that might be imported by a lazy loaded module.
 */

@NgModule({
  imports: [CommonModule, RouterModule],
  declarations: [NavbarComponent, FooterComponent, HttpLoaderComponent, MessageComponent],
  exports: [NavbarComponent, FooterComponent, HttpLoaderComponent, MessageComponent, CommonModule, FormsModule, RouterModule]
})
export class SharedModule {
  static forRoot(): ModuleWithProviders {
    return {
      ngModule: <any>SharedModule,
      providers: [
        NOTIFICATION_PROVIDERS,
        <any>NameListService,
        <any>HttpErrorHandlerService,
        <any>HttpLoaderService,
        <any>AccountService
      ]
    };
  }
}
