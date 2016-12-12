import {Http, Request} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {RESTClient} from "./rest-client";
import {HttpLoaderService} from "../http-loader/index";
import {HttpErrorHandlerService} from "../http-error-handler/index";

export abstract class BaseService extends RESTClient {

    constructor(protected http:Http,
                protected httpLoaderService:HttpLoaderService,
                protected httpErrorHandlerService:HttpErrorHandlerService) {
        super(http);
    }

    protected requestInterceptor(req:Request) {
        // none
    }

    protected responseInterceptor(req:Request, observable:Observable<any>):Observable<any> {
        // Load handling        
        observable = this.httpLoaderService.process(req, observable);
        // Error handling
        observable = this.httpErrorHandlerService.process(req, observable);

        return observable;
    }

}
