import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {InputTextModule} from 'primeng/primeng';

import { HomeComponent } from './home.component';

import { SharedModule } from '../shared/shared.module';
import { NameListService, AccountService } from '../shared/index';

@NgModule({
  imports: [CommonModule, SharedModule, InputTextModule],
  declarations: [HomeComponent],
  exports: [HomeComponent],
  providers: [NameListService, AccountService]
})
export class HomeModule {}
