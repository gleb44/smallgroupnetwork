import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {InputTextModule, PasswordModule, ButtonModule} from 'primeng/primeng';

import { LoginComponent } from './login.component';
import { SharedModule } from '../shared/shared.module';
import { UrlTrackingService, AccountService } from '../shared/index';

@NgModule({
  imports: [
    CommonModule,
    InputTextModule,
    PasswordModule,
    ButtonModule,
    SharedModule
  ],
  declarations: [LoginComponent],
  exports: [LoginComponent],
  providers: [UrlTrackingService, AccountService]
})
export class LoginModule {}
