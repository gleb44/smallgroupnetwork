import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {Request, RequestMethod} from '@angular/http';

import {Observable} from 'rxjs/Observable';
import 'rxjs/add/observable/throw';
import 'rxjs/add/observable/empty';
import 'rxjs/add/observable/throw';

enum ErrorStatus {
    Unauthorized = 401,
    Forbidden = 403
}

interface IHttpErrorHandlerService {
    process(req:Request, observable:Observable<any>):Observable<any>;
}

@Injectable()
export class HttpErrorHandlerService implements IHttpErrorHandlerService {

    public process(req:Request, observable:Observable<any>):Observable<any> {
        return observable;
    }

}