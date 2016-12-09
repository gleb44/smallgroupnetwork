import {Injectable, OnDestroy} from "@angular/core";
import {Request} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {Subject} from "rxjs/Subject";
import {HttpLoaderEventEmitter} from "../notification/notification";

interface IHttpLoaderService {
    process(req:Request, observable:Observable<any>):Observable<any>;
}

@Injectable()
export class HttpLoaderService implements IHttpLoaderService, OnDestroy {

    private numLoadings:number;
    private subject:Subject<number>;

    constructor(private httpLoaderEventEmitter:HttpLoaderEventEmitter) {
        this.numLoadings = 0;
        this.subject = new Subject<number>();

        this.subject.asObservable().subscribe((sequence) => {
            httpLoaderEventEmitter.emit(sequence > 0);
        });
    }

    private increment(url:string) {
        this.numLoadings++;
        this.subject.next(this.numLoadings);
    }

    private decrement(url:string) {
        if (this.numLoadings > 0)
            this.numLoadings--;
        this.subject.next(this.numLoadings);
    }

    public process(req:Request, observable:Observable<any>):Observable<any> {
        this.increment(req.url);

        return Observable.create(observer => {
            observable.subscribe(data => {
                this.decrement(req.url);
                observer.next(data);
            }, error => {
                this.decrement(req.url);
                observer.error(error);
            }, () => {
                observer.complete();
            });
        });
    }

    ngOnDestroy() {
        this.subject.unsubscribe();
    }

}