import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {User, UserService, InfiniteScroll, IQueryFilter, QueryFilter, IScrollableResult, ScrollableResult} from "../shared/index";

/**
 * This class represents the lazy loaded AdminDashboardComponent.
 */
@Component({
    moduleId: module.id,
    selector: 'sd-admin-dashboard',
    templateUrl: 'admin-dashboard.component.html',
    styleUrls: ['admin-dashboard.component.css'],
})
export class AdminDashboardComponent implements OnInit {

    queryFilter:IQueryFilter;
    scrollableResult:IScrollableResult<User>;

    sortOptions = [{value: 'id', name: 'Id'}];
    selectedSorting = this.sortOptions[0];

    constructor(private userService:UserService, private router:Router) {
    }

    ngOnInit():void {
        this.initList();
    }

    initList() {
        let filterConfig = {
            pattern: {value: null},
            view: {value: null},
            sortDesc:true,
            sortColumn: {value: this.selectedSorting.value},
        };
        this.queryFilter = new QueryFilter(filterConfig, this.router);
        let filterReq = this.queryFilter.makeFilterRequest();
        this.scrollableResult = new ScrollableResult<User>((data)=> {
            return this.userService.get(data.limit, data.offset, data.sortDesc, data.sortColumn);
        }, 10, filterReq, true, false);
    }

}
