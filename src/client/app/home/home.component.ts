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
  names: any[] = [];
  items: any[] = [
    {text:"1It's easy to feel isolated in small group ministry. It's easy to get bogged down with the issues you're facing, the problems you're tackling, and the hurdles you just can't seem to clear. That's why I love the Small Group Network. It's a group of folks who are working in the trenches of group life, getting together for encouragement, sharing resources, and building relationships. The Small Group Network is an answer to a lot of the problems that small group pastors face.", author: "John C. Maxwell Author, Speaker and Founder - INJOY Stewardship Services and EQUIP"},
    {text:"2It's easy to feel isolated in small group ministry. It's easy to get bogged down with the issues you're facing, the problems you're tackling, and the hurdles you just can't seem to clear. That's why I love the Small Group Network. It's a group of folks who are working in the trenches of group life, getting together for encouragement, sharing resources, and building relationships. The Small Group Network is an answer to a lot of the problems that small group pastors face.", author: "John C. Maxwell Author, Speaker and Founder - INJOY Stewardship Services and EQUIP"},
    {text:"3It's easy to feel isolated in small group ministry. It's easy to get bogged down with the issues you're facing, the problems you're tackling, and the hurdles you just can't seem to clear. That's why I love the Small Group Network. It's a group of folks who are working in the trenches of group life, getting together for encouragement, sharing resources, and building relationships. The Small Group Network is an answer to a lot of the problems that small group pastors face.", author: "John C. Maxwell Author, Speaker and Founder - INJOY Stewardship Services and EQUIP"},
    {text:"4It's easy to feel isolated in small group ministry. It's easy to get bogged down with the issues you're facing, the problems you're tackling, and the hurdles you just can't seem to clear. That's why I love the Small Group Network. It's a group of folks who are working in the trenches of group life, getting together for encouragement, sharing resources, and building relationships. The Small Group Network is an answer to a lot of the problems that small group pastors face.", author: "John C. Maxwell Author, Speaker and Founder - INJOY Stewardship Services and EQUIP"},
  ];

  model = new Admin();

  constructor(public accountService:AccountService) {
  }

  signInUser() {
    this.accountService.login(this.model).subscribe(response => {
      this.afterSignIn(<Admin>response);
    });
  }

  afterSignIn(user: Admin) {
    console.log(user.id);
  }
}
