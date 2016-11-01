import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { InputTextModule } from 'primeng/primeng';
import { AgmCoreModule } from 'angular2-google-maps/core';

import { GoogleMapsComponent } from './google-maps/index';
import { HomeComponent } from './home.component';
import { SharedModule } from '../shared/shared.module';
import { AccountService } from '../shared/index';

@NgModule({
  imports: [
    CommonModule, 
    SharedModule, 
    InputTextModule,
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
  providers: [AccountService]
})
export class HomeModule {}
