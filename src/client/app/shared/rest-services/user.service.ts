import {Injectable} from "@angular/core";
import {User} from "../model/index";
import {Http} from "@angular/http";
import {Observable} from "rxjs/Rx";
import {PUT, BaseUrl, Produces, MediaType, DefaultHeaders, Body} from "./rest-client";
import {BaseService} from "./base.service";
import {HttpLoaderService} from "../http-loader/index";
import {HttpErrorHandlerService} from "../http-error-handler/index";

@Injectable()
@BaseUrl('/api/user/')
@DefaultHeaders({
    'Accept': 'application/json',
    'Content-Type': 'application/json'
})
export class UserService extends BaseService {

    constructor(protected http:Http,
                protected httpLoaderService:HttpLoaderService,
                protected httpErrorHandlerService:HttpErrorHandlerService) {
        super(http, httpLoaderService, httpErrorHandlerService);
    }

    @PUT('')
    @Produces(MediaType.JSON)
    public update(@Body user:User):Observable<any> {
        return null;
    }
}
