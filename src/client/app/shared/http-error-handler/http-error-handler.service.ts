import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {Request, RequestMethod} from '@angular/http';
import {Observable} from 'rxjs/Observable';

import {ErrorMessagesEventEmitter} from '../notification/notification';

enum ErrorStatus {
    Unauthorized = 401,
    Forbidden = 403
}

interface IHttpErrorHandlerService {
    process(req:Request, observable:Observable<any>):Observable<any>;
}

@Injectable()
export class HttpErrorHandlerService implements IHttpErrorHandlerService {

    constructor(private router:Router, private errorMessagesEventEmitter:ErrorMessagesEventEmitter) {
    }

    public process(req:Request, observable:Observable<any>):Observable<any> {
        return observable.catch((err, source) => {
            switch (err.status) {
                case ErrorStatus.Unauthorized:
                    return this.process401(err);
                case ErrorStatus.Forbidden:
                    return this.process403(err);
                default:
                    return this.processXXX(err, req);
            }
        });
    }

    private process401(err:any):Observable<any> {
        this.errorMessagesEventEmitter.emit(['Unauthorized!']);

        return Observable.throw(err);
    }

    private process403(err:any):Observable<any> {
        let errorData = err.json();
        if (errorData.statusText) {
            this.errorMessagesEventEmitter.emit(['You don’t have permissions to perform this operation']);
        }

        return Observable.throw(err);
    }

    private processXXX(err:any, req:Request):Observable<any> {
        let messageList:any[] = [];
        let errorData = err.json();
        let errList:any[] = errorData.items ? errorData.items : [errorData];

        for (var item of errList) {
            if (item.messageCode) {
                messageList.push({
                                     name: item.name,
                                     msg: item.message,
                                     trace: item.trace
                                 });
            } else if (item.message) {
                messageList.push({
                                     name: item.name,
                                     msg: item.message,
                                     trace: item.trace
                                 });
            } else if (item.reason) {
                messageList.push({
                                     name: item.name,
                                     msg: 'Operation ' + RequestMethod[req.method] + ' Error. Reason: ' + item.reason,
                                     trace: item.trace
                                 });
            } else {
                console.warn('Unhandled error!');
            }
        }

        if (messageList.length) {
            this.errorMessagesEventEmitter.emit(messageList);
        }

        return Observable.throw(err);
    }

}