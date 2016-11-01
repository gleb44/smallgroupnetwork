import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
 
import { AdminComponent } from './admin.component';
import { SharedModule } from '../shared/shared.module'; 

@NgModule({
    imports: [CommonModule, SharedModule],
    declarations: [AdminComponent],
    exports: [AdminComponent],
    providers: []
})
export class AdminModule {
}
