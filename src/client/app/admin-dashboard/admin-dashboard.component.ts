import {Component, OnInit} from "@angular/core";
import {UserService} from "../shared/index";

/**
 * This class represents the lazy loaded AdminDashboardComponent.
 */
@Component({
    moduleId: module.id,
    selector: 'sd-admin-dashboard',
    templateUrl: 'admin-dashboard.component.html',
    styleUrls: ['admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit{

    constructor(private userService:UserService) {
    }

    ngOnInit():void {
    }
    
}
