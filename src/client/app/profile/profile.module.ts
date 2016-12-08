import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {InputTextModule, ButtonModule, CalendarModule, FileUploadModule} from 'primeng/primeng';

import {ProfileComponent} from './profile.component';
import {SharedModule} from '../shared/shared.module';
import {AccountService} from "../shared/index";

@NgModule({
    imports: [
        CommonModule,
        SharedModule,
        InputTextModule,
        CalendarModule,
        FileUploadModule,
        ButtonModule
    ],
    declarations: [ProfileComponent],
    exports: [ProfileComponent],
    providers: [AccountService]
})

export class ProfileModule {
}
