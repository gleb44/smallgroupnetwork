import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../shared/shared.module';
import { HomeComponent } from './home.component';
import { NameListService } from '../shared/name-list/index';

import {AccountService} from '../shared/service/account.service';

import {InputTextModule} from 'primeng/primeng';

@NgModule({
  imports: [CommonModule, SharedModule, InputTextModule],
  declarations: [HomeComponent],
  exports: [HomeComponent],
  providers: [NameListService, AccountService]
})
export class HomeModule {}
