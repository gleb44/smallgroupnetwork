import {Component, OnInit} from '@angular/core';
import { Router } from '@angular/router';

import {AccountService} from '../rest-services/index';
import {AuthEventEmitter} from "../notification/notification";

/**
 * This class represents the navigation bar component.
 */
@Component({
  moduleId: module.id,
  selector: 'sd-navbar',
  templateUrl: 'navbar.component.html',
  styleUrls: ['navbar.component.css'],
})

export class NavbarComponent implements OnInit {

  login = false;

  constructor(private router:Router, private accountService:AccountService, private authEventEmitter:AuthEventEmitter) {
  }

  ngOnInit() {
    this.authEventEmitter.subscribe(info => {
      this.login = (info != null);
    });  

    this.accountService.getInfo().subscribe(info => {
      this.login = true;
    });
  }

  onLogout() {
    this.accountService.logout().subscribe(response => {
      this.login = false;

      if(this.router.url.indexOf('admin') >= 0) {
        this.router.navigate(['login']);
      } else {
        this.router.navigate(['']);
      }
    });
  }

}
