import { Injectable } from '@angular/core';
import { CanActivate, CanDeactivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot} from '@angular/router';
import { Observable } from 'rxjs/Observable';
 
import { AccountService } from '../rest-services/index';

export interface CanComponentDeactivate {
    canDeactivate:() => boolean | Observable<boolean>;
}

@Injectable()
export class AuthGuardService implements CanActivate, CanDeactivate<CanComponentDeactivate> {

    constructor(private accountService:AccountService, private router:Router) {
    }

    canActivate(route:ActivatedRouteSnapshot, state:RouterStateSnapshot):Observable<boolean> | boolean {
        return Observable.create(observer => {
            this.accountService.getInfo().subscribe(info => {
                observer.next(true);
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
