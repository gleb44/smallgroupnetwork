import {Injectable} from "@angular/core";
import {Router, NavigationStart} from "@angular/router";

@Injectable()
export class UrlTrackingService {

    private _originalUrl:string;
    private _newUrl:string;

    get originalUrl() {
        return this._originalUrl;
    }

    constructor(private router:Router) {
        router.events.filter(event => event instanceof NavigationStart).subscribe((event:NavigationStart) => {
            this._originalUrl = this._newUrl;
            this._newUrl = event.url;
        });
    }

}