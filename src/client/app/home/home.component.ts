import {Component, OnInit} from '@angular/core';

import {AccountService, Account, AuthEventEmitter} from '../shared/index';

/**
 * This class represents the lazy loaded HomeComponent.
 */
@Component({
  moduleId: module.id,
  selector: 'sd-home',
  templateUrl: 'home.component.html',
  styleUrls: ['home.component.css'],
})

export class HomeComponent implements OnInit {

  model = new Account();
  login = false;

  constructor(private accountService:AccountService, private authEventEmitter:AuthEventEmitter) {
  }

  ngOnInit():void {
    this.authEventEmitter.subscribe(info => {
      this.login = (info != null);
    });

    this.accountService.getInfo().subscribe(info => {
      this.login = true;
    });
  }

  signInUser() {
    this.accountService.login(this.model).subscribe(response => {
      this.afterSignIn(<Account>response);
    });
  }

  afterSignIn(user: Account) {
    console.log(user.id);
  }
}
