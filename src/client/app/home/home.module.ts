import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../shared/shared.module';
import { HomeComponent } from './home.component';
import { NameListService } from '../shared/name-list/index';

import {AccountService} from '../shared/service/account.service';

import { MaterialModule } from '@angular/material';

@NgModule({
  imports: [CommonModule, SharedModule, MaterialModule.forRoot()],
  declarations: [HomeComponent],
  exports: [HomeComponent],
  providers: [NameListService, AccountService]
})
export class HomeModule {}
