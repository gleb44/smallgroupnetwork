import {Injectable} from '@angular/core';
import {Account, User} from '../model/index';
import {Http} from '@angular/http';
import {Observable} from "rxjs/Rx";

import {GET, PUT, POST, DELETE, BaseUrl, Headers, Header, Produces, MediaType, DefaultHeaders, Path, Body, Query} from './rest-client';
import {BaseService} from './base.service';
import {HttpLoaderService} from '../http-loader/index';
import {HttpErrorHandlerService} from '../http-error-handler/index';
import {AuthEventEmitter} from "../notification/notification";

@Injectable()
@BaseUrl('/api/account/')
@DefaultHeaders({
    'Accept': 'application/json',
    'Content-Type': 'application/json'
})
export class AccountService extends BaseService {

    constructor(protected http:Http,
                protected httpLoaderService:HttpLoaderService,
                protected httpErrorHandlerService:HttpErrorHandlerService,
                private authEventEmitter:AuthEventEmitter) {
        super(http, httpLoaderService, httpErrorHandlerService);
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

    @PUT('user')
    @Produces(MediaType.JSON)
    public update(@Body user:User):Observable<any> {
        return null;
    }

    private token:Promise<any> = null;

    public getInfo():Observable<any> {
        if (!this.token) {
            let self = this;
            this.token = new Promise<any>(function (resolve, reject) {
                self.info().subscribe(result => {
                    resolve(result);
                }, error => {
                    reject(error);
                });
            });
        }
        return Observable.fromPromise(this.token);
    }

    public login(account:Account):Observable<any> {
        let self = this;
        let promise = new Promise<any>(function (resolve, reject) {
            self.signIn(account.login, account.password).subscribe(result => {
                resolve(result);

                self.token = promise;
                self.authEventEmitter.emit(result); // notify login
            }, error => {
                reject(error);
            });
        });
        return Observable.fromPromise(promise);
    }

    public logout():Observable<any> {
        return Observable.create(observer => {
            this.signOut().subscribe(null, null, () => {
                this.token = null;
                this.authEventEmitter.emit(null); // notify logout

                observer.next(true);
                observer.complete();
            });
        });
    }
}
