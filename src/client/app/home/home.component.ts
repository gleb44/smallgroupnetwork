import { Component } from '@angular/core';

import {AccountService, Admin} from '../shared/index';

/**
 * This class represents the lazy loaded HomeComponent.
 */
@Component({
  moduleId: module.id,
  selector: 'sd-home',
  templateUrl: 'home.component.html',
  styleUrls: ['home.component.css'],
})

export class HomeComponent {
  password: string = '';

  constructor(public accountService:AccountService) {
  }

  signInUser() {
    let admin = new Admin();
    admin.password = this.password;
    this.accountService.login(admin).subscribe(response => {
      console.log((<Admin>response).id);
    });
  }
}
