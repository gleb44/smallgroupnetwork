import {Component, OnInit} from "@angular/core";
import {User, AccountService} from "../shared/index";

/**
 * This class represents the lazy loaded ProfileComponent.
 */
@Component({
    moduleId: module.id,
    selector: 'sd-profile',
    templateUrl: 'profile.component.html',
    styleUrls: ['profile.component.css']
})
export class ProfileComponent implements OnInit {

    user:User;

    constructor(private accountService:AccountService) {
    }

    ngOnInit() {
        this.accountService.getInfo().subscribe(info => {
            this.user = <User>Object.assign(new User(), info);
            this.user.adminAccess = null;
            this.user.profile.birthDate = new Date(this.user.profile.birthDate); // TODO fix it
        });
    }

    onSubmit() {
        this.accountService.update(this.user).subscribe(response => {
            console.log('User Updated...');
        });
    }
}
