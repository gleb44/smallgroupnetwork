import {Injectable} from '@angular/core';
import {Admin} from '../model/admin';
import {Http} from '@angular/http';
import {Observable} from 'rxjs/Observable';

import { GET, PUT, POST, DELETE, BaseUrl, Headers, Header, Produces, MediaType, DefaultHeaders, Path, Body, Query } from './rest-client';
import {BaseService} from './base.service';
import {HttpLoaderService} from '../http-loader/index';
import {HttpErrorHandlerService} from '../http-error-handler/index';

@Injectable()
@BaseUrl('/api/admin/account/')
@DefaultHeaders({
    'Accept': 'application/json',
    'Content-Type': 'application/json'
})
export class AccountService extends BaseService {

    token:Admin = null;

    constructor(protected http:Http,
                protected httpLoaderService:HttpLoaderService,
                protected httpErrorHandlerService:HttpErrorHandlerService) {
        super(http, httpLoaderService, httpErrorHandlerService);
    }

    @GET('info')
    @Produces(MediaType.JSON)
    private info():Observable<any> {
        return null;
    }

    @POST('sign-in')
    @Produces(MediaType.JSON)
    private signIn(@Body admin:Admin):Observable<any> {
        return null;
    }

    @GET('sign-out')
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
            this.signIn(admin).subscribe(
                response => {
                    this.token = response;
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
                this.token = null;
                observer.next(true);
                observer.complete();
            });
        });
    }

}
