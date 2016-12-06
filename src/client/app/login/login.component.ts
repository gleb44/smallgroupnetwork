import { Component } from '@angular/core';
import {Router} from '@angular/router';

import {AccountService, Account} from '../shared/index';
import {adminPath} from "../admin/admin.routes";

/**
 * This class represents the lazy loaded LoginComponent.
 */
@Component({
  moduleId: module.id,
  selector: 'sd-login',
  templateUrl: 'login.component.html',
  styleUrls: ['login.component.css'],
})

export class LoginComponent {

  model = new Account();

  constructor(public router:Router, public accountService:AccountService) {
  }

  signInUser() {
    this.accountService.login(this.model).subscribe(response => {
      this.afterSignIn(<Account>response);
    });
  }

  afterSignIn(user: Account) {
    this.router.navigate([adminPath]);
  }
}
