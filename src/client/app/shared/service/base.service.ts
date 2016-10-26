import {Http, Request} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import 'rxjs/add/observable/empty';

import {RESTClient} from './rest-client';

export abstract class BaseService extends RESTClient {

    constructor(protected http:Http) {
        super(http);
    }

    protected requestInterceptor(req:Request) {
        // none
    }

    protected responseInterceptor(req:Request, observable:Observable<any>):Observable<any> {
        return observable;
    }

}
