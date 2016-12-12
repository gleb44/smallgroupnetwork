import {Component} from "@angular/core";
import {Config} from "./shared/index";
import "./operators";
import {UrlTrackingService} from "./shared/url-tracking/url-tracking.service";

/**
 * This class represents the main application component.
 */
@Component({
    moduleId: module.id,
    selector: 'sd-app',
    templateUrl: 'app.component.html',
})

export class AppComponent {

    constructor(urlTrackingService:UrlTrackingService) {
        // Start url tracking

        console.log('Environment config', Config);
    }

}
