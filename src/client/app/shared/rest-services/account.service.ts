import {Injectable} from "@angular/core";
import {Account} from "../model/index";
import {Http} from "@angular/http";
import {Observable} from "rxjs/Rx";
import {GET, POST, BaseUrl, Produces, MediaType, DefaultHeaders, Body, Query} from "./rest-client";
import {BaseService} from "./base.service";
import {HttpLoaderService} from "../http-loader/index";
import {HttpErrorHandlerService} from "../http-error-handler/index";

@Injectable()
@BaseUrl('/api/account/')
@DefaultHeaders({
    'Accept': 'application/json',
    'Content-Type': 'application/json'
})
export class AccountService extends BaseService {

    constructor(protected http:Http,
                protected httpLoaderService:HttpLoaderService,
                protected httpErrorHandlerService:HttpErrorHandlerService) {
        super(http, httpLoaderService, httpErrorHandlerService);
    }

    @GET('info')
    @Produces(MediaType.JSON)
    public info():Observable<any> {
        return null;
    }

    @POST('login')
    @Produces(MediaType.JSON)
    public signIn(@Query('login') login?:string,
                  @Query('password') password?:string):Observable<any> {
        return null;
    }

    @GET('logout')
    public signOut():Observable<any> {
        return null;
    }

    @POST('register')
    @Produces(MediaType.JSON)
    public create(@Body account:Account):Observable<any> {
        return null;
    }

}
