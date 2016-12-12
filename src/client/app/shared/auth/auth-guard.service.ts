import {Injectable} from "@angular/core";
import {CanActivate, CanDeactivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot} from "@angular/router";
import {Observable} from "rxjs/Observable";
import {AuthService} from "./auth.service";
import {User} from "../model/index";
import {ErrorMessagesEventEmitter} from "../notification/notification";
import {AdminRole} from "../model/admin-access";

export interface CanComponentDeactivate {
    canDeactivate:() => boolean | Observable<boolean>;
}

@Injectable()
export class AuthGuardService implements CanActivate, CanDeactivate<CanComponentDeactivate> {

    constructor(private authService:AuthService, private errorMessagesEventEmitter:ErrorMessagesEventEmitter, private router:Router) {
    }

    private checkRole(info:User, roles:Array<AdminRole>):boolean {
        return roles == null || roles.length === 0 ||
            (info.adminAccess && info.adminAccess.adminRole && roles.includes(info.adminAccess.adminRole));
    }

    canActivate(route:ActivatedRouteSnapshot, state:RouterStateSnapshot):Observable<boolean> | boolean {
        let roles = route.data['roles'] as Array<AdminRole>;

        return Observable.create(observer => {
            this.authService.getInfo().subscribe(info => {
                if (this.checkRole(info, roles)) {
                    observer.next(true);
                } else {
                    this.errorMessagesEventEmitter.emit(['Permissions Denied']);
                    this.router.navigate(['login']);
                    observer.next(false);
                }
            }, error => {
                this.router.navigate(['login']);
                observer.next(false);
            }, ()=> {
                observer.complete();
            });
        });
    }

    canDeactivate(component:CanComponentDeactivate):Observable<boolean> | boolean {
        return component.canDeactivate ? component.canDeactivate() : true;
    }

}
