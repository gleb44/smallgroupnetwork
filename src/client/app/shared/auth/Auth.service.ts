import {Injectable} from "@angular/core";
import {Account} from "../model/index";
import {HttpErrorHandlerService} from "../http-error-handler/index";
import {AuthEventEmitter} from "../notification/notification";
import {AccountService} from "../rest-services/index";
import {Observable} from "rxjs/Rx";

@Injectable()
export class AuthService {

    private token:Promise<any> = null;

    constructor(private httpErrorHandlerService:HttpErrorHandlerService,
                private authEventEmitter:AuthEventEmitter,
                private accountService:AccountService) {

        this.httpErrorHandlerService.onUnauthorizedError.subscribe(error => {
            this.token = null;
            this.authEventEmitter.emit(null); // notify logout
        });
    }

    public getInfo():Observable<any> {
        return this.token ? Observable.fromPromise(this.token) : this.updateInfo();
    }

    public updateInfo():Observable<any> {
        this.token = this.accountService.info().toPromise();
        return Observable.fromPromise(this.token);
    }

    public register(account:Account):Observable<any> {
        let promise = this.accountService.create(account).toPromise();
        promise.then(result => {
            this.token = promise;
            this.authEventEmitter.emit(result); // notify login
        });
        return Observable.fromPromise(promise);
    }

    public login(account:Account):Observable<any> {
        let promise = this.accountService.signIn(account.login, account.password).toPromise();
        promise.then(result => {
            this.token = promise;
            this.authEventEmitter.emit(result); // notify login
        });
        return Observable.fromPromise(promise);
    }

    public logout():Observable<any> {
        let obs = this.accountService.signOut().share();
        obs.subscribe(null, null, () => {
            this.token = null;
            this.authEventEmitter.emit(null); // notify logout
        });
        return obs;
    }
}
