import { Component } from '@angular/core';
import { Router } from '@angular/router';

import {AccountService} from '../rest-services/index';

/**
 * This class represents the navigation bar component.
 */
@Component({
  moduleId: module.id,
  selector: 'sd-navbar',
  templateUrl: 'navbar.component.html',
  styleUrls: ['navbar.component.css'],
})

export class NavbarComponent {

  constructor(private router:Router, private accountService:AccountService) {
  }

  onLogout() {
    this.accountService.logout().subscribe(response => {
      if(this.router.url.indexOf('admin') >= 0) {
        this.router.navigate(['login']);
      } else {
        this.router.navigate(['']);
      }
    });
  }

}
