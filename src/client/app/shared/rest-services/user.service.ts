import {Injectable} from "@angular/core";
import {User} from "../model/index";
import {Http} from "@angular/http";
import {Observable} from "rxjs/Rx";
import {PUT, BaseUrl, Produces, MediaType, DefaultHeaders, Body, GET, Query, Path} from "./rest-client";
import {BaseService} from "./base.service";
import {HttpLoaderService} from "../http-loader/index";
import {HttpErrorHandlerService} from "../http-error-handler/index";

@Injectable()
@BaseUrl('/api/')
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

    @GET('admin/user')
    @Produces(MediaType.JSON)
    public get(@Query('limit') limit?:number,
               @Query('offset') offset?:number,
               @Query('sortDesc') sortDesc?:boolean,
               @Query('sortColumn') sortColumn?:string):Observable<any> {
        return null;
    }

    @GET('admin/user/{id}')
    @Produces(MediaType.JSON)
    public read(@Path("id") id:string):Observable<any> {
        return null;
    }

    @PUT('user')
    @Produces(MediaType.JSON)
    public update(@Body user:User):Observable<any> {
        return null;
    }
}
