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

    public user:User;

    constructor(private accountService:AccountService) {
    }

    private init() {
        this.accountService.getInfo().subscribe(info => {
            let user:User = jQuery.extend(true, {}, info);
            user.adminAccess = null;
            user.profile.birthDate = new Date(user.profile.birthDate);
            this.user = user;
        });
    }

    ngOnInit() {
        this.init();
    }

    onSubmit() {
        let user:User = jQuery.extend(true, {}, this.user);
        user.profile.birthDate = user.profile.birthDate ? user.profile.birthDate.getTime() : null;
        this.accountService.update(user).subscribe(response => {
            console.log('User Updated...');
        });
    }

    onCancel() {
        this.init();
    }
}
