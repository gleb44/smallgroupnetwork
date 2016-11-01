import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LoginComponent } from './login.component';
import { SharedModule } from '../shared/shared.module';
import { AccountService } from '../shared/index';

@NgModule({
  imports: [
    CommonModule, 
    SharedModule
  ],
  declarations: [LoginComponent],
  exports: [LoginComponent],
  providers: [AccountService]
})
export class LoginModule {}
