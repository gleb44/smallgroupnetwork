import {Injectable, OnInit} from "@angular/core";
import {Account, User} from "../model/index";
import {Http} from "@angular/http";
import {Observable} from "rxjs/Rx";
import {GET, PUT, POST, BaseUrl, Produces, MediaType, DefaultHeaders, Body, Query} from "./rest-client";
import {BaseService} from "./base.service";
import {HttpLoaderService} from "../http-loader/index";
import {HttpErrorHandlerService} from "../http-error-handler/index";
import {AuthEventEmitter} from "../notification/notification";

@Injectable()
@BaseUrl('/api/account/')
@DefaultHeaders({
    'Accept': 'application/json',
    'Content-Type': 'application/json'
})
export class AccountService extends BaseService {

    private token:Promise<any> = null;

    constructor(protected http:Http,
                protected httpLoaderService:HttpLoaderService,
                protected httpErrorHandlerService:HttpErrorHandlerService,
                private authEventEmitter:AuthEventEmitter) {
        super(http, httpLoaderService, httpErrorHandlerService);

        this.httpErrorHandlerService.onUnauthorizedError.subscribe(error => {
            this.token = null;
            this.authEventEmitter.emit(null); // notify logout
        });
    }

    @GET('info')
    @Produces(MediaType.JSON)
    private info():Observable<any> {
        return null;
    }

    @POST('login')
    @Produces(MediaType.JSON)
    private signIn(@Query('login') login?:string,
                   @Query('password') password?:string):Observable<any> {
        return null;
    }

    @GET('logout')
    private signOut():Observable<any> {
        return null;
    }

    @POST('user')
    @Produces(MediaType.JSON)
    private create(@Body account:Account):Observable<any> {
        return null;
    }

    @PUT('user')
    @Produces(MediaType.JSON)
    public update(@Body user:User):Observable<any> {
        return null;
    }

    public getInfo():Observable<any> {
        return this.token ? Observable.fromPromise(this.token) : this.updateInfo();
    }

    public updateInfo():Observable<any> {
        this.token = this.info().toPromise();
        return Observable.fromPromise(this.token);
    }

    public register(account:Account):Observable<any> {
        let promise = this.create(account).toPromise();
        promise.then(result => {
            this.token = promise;
            this.authEventEmitter.emit(result); // notify login
        });
        return Observable.fromPromise(promise);
    }

    public login(account:Account):Observable<any> {
        let promise = this.signIn(account.login, account.password).toPromise();
        promise.then(result => {
            this.token = promise;
            this.authEventEmitter.emit(result); // notify login
        });
        return Observable.fromPromise(promise);
    }

    public logout():Observable<any> {
        let obs = this.signOut().share();
        obs.subscribe(null, null, () => {
            this.token = null;
            this.authEventEmitter.emit(null); // notify logout
        });
        return obs;
    }
}
