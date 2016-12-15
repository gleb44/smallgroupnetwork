import {Component, OnInit} from "@angular/core";
import {User, Profile, UserService, AuthService} from "../shared/index";
import {URLSearchParams} from "@angular/http";

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

    constructor(private userService:UserService, private authService:AuthService) {
    }

    private init() {
        this.authService.getInfo().subscribe(info => {
            let user:User = Object.assign({}, info);
            user.adminAccess = null;
            if (!user.profile) {
                user.profile = <Profile> {
                    birthDate: null
                };
            } else {
                user.profile.birthDate = new Date(user.profile.birthDate);
            }
            this.user = user;
        });
    }

    ngOnInit() {
        this.init();
    }

    onCancel() {
        this.init();
    }

    onSubmit() {
        let user:User = Object.assign({}, this.user);
        user.profile.birthDate = user.profile.birthDate ? user.profile.birthDate.getTime() : null;
        this.userService.update(user).subscribe(response => {
            console.log('User Updated...');
            this.authService.updateInfo();
        });
    }

    /**
     * FileUpload
     */

    onUpload(event) {
        this.user.avatar = JSON.parse(event.xhr.response);
        console.log('User Avatar Updated...');
        this.authService.updateInfo();
    }

    avatarURL():string {
        return this.user.avatar ? ProfileComponent.attachURL(this.user.avatar) : '';
    };

    private static attachURL(attach):string {
        if (attach.path) {
            var search = new URLSearchParams();
            search.set('path', attach.path);
            search.set('fileName', attach.fileName);
            search.set('contentType', attach.contentType);
            return '/api/admin/attach?' + search.toString();
        } else {
            return '/api/attach/' + attach.id;
        }
    };
}
