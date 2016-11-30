import {Injectable} from '@angular/core';
import {Admin} from '../model/admin';
import {Http} from '@angular/http';
import {Observable} from 'rxjs/Observable';

import { GET, PUT, POST, DELETE, BaseUrl, Headers, Header, Produces, MediaType, DefaultHeaders, Path, Body, Query } from './rest-client';
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

    token:Admin = null;

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

    public getInfo():Observable<any> {
        return Observable.create(observer => {
            if (this.token) {
                observer.next(this.token);
                observer.complete();
            } else {
                this.info().subscribe(
                    result => {
                        this.token = result;
                        observer.next(this.token);
                        observer.complete();
                    },
                    error => {
                        observer.error(error);
                        observer.complete();
                    });
            }
        });
    }

    public login(admin:Admin):Observable<any> {
        return Observable.create(observer => {
            this.signIn(admin.login, admin.password).subscribe(
                result => {
                    this.putToken(result);
                    observer.next(this.token);
                    observer.complete();
                },
                error => {
                    observer.error(error);
                    observer.complete();
                }
            );
        });
    }

    public logout():Observable<any> {
        return Observable.create(observer => {
            this.signOut().subscribe(null, null, () => {
                this.removeToken();
                observer.next(true);
                observer.complete();
            });
        });
    }

    private putToken(token:any):void {
        this.token = token;

        // notify login
        this.authEventEmitter.emit(this.token);
    }

    public removeToken():void {
        this.token = null;

        // notify logout
        this.authEventEmitter.emit(null);
    }

}
