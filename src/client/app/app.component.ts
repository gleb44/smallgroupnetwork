import {Component} from "@angular/core";
import "./operators";
import {UrlTrackingService} from "./shared/index";

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
    }

}
