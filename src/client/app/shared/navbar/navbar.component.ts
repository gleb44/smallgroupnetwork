import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {AuthEventEmitter} from "../notification/notification";
import {AuthService} from "../auth/auth.service";

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

    constructor(private router:Router, private authService:AuthService, private authEventEmitter:AuthEventEmitter) {
    }

    ngOnInit() {
        this.authEventEmitter.subscribe(info => {
            this.login = (info != null);
        });

        this.authService.getInfo().subscribe(info => {
            this.login = true;
        });
    }

    onLogout() {
        this.authService.logout().subscribe(response => {
            this.login = false;

            if (this.router.url.indexOf('admin') >= 0) {
                this.router.navigate(['login']);
            } else {
                this.router.navigate(['']);
            }
        });
    }

}
