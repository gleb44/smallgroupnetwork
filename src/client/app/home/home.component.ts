import {Component, OnInit} from "@angular/core";
import {Account, AuthEventEmitter} from "../shared/index";
import {AuthService} from "../shared/auth/auth.service";

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

    constructor(private authService:AuthService, private authEventEmitter:AuthEventEmitter) {
    }

    ngOnInit():void {
        this.authEventEmitter.subscribe(info => {
            this.login = (info != null);
        });

        this.authService.getInfo().subscribe(info => {
            this.login = true;
        });
    }

    onRegister() {
        this.authService.register(this.model).subscribe(response => {
            this.afterRegister(<Account>response);
        });
    }

    afterRegister(user:Account) {
        console.log(user.id);
    }
}
