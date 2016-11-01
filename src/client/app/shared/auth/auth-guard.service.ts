import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot} from '@angular/router';
import { Observable } from 'rxjs/Observable';
 
import { AccountService } from '../rest-services/index';

@Injectable()
export class AuthGuardService implements CanActivate {

    constructor(private accountService:AccountService, private router:Router) {
    }

    canActivate(route:ActivatedRouteSnapshot, state:RouterStateSnapshot):Observable<boolean> | boolean {
        return Observable.create(observer => {
            this.accountService.getInfo().subscribe(
                info => {
                    observer.next(true);
                },
                err => {
                    this.router.navigate(['login']);
                    observer.next(false);
                },
                ()=> observer.complete());
        });
    }

}
