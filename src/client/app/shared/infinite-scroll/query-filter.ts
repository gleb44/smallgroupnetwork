import {Router, UrlTree} from "@angular/router";

export interface IQueryFilter {
    isFilterChanged(syncOldValues):boolean;
    setFilterDataFromUrl():void;
    updateUrlByFilterData():void;
    makeFilterRequest():any;
}

export class QueryFilter implements IQueryFilter {

    public filter:any;

    constructor(private filterConfig:any, private router:Router) {
        this.filter = {};
        for (var filterConfigItemKey in filterConfig) {
            let filterConfigItem = filterConfig[filterConfigItemKey];
            this.filter[filterConfigItemKey] = {value: filterConfigItem.value, oldValue: filterConfigItem.value}
        }

        this.setFilterDataFromUrl();
    }

    isFilterChanged(syncOldValues):boolean {
        let result:boolean = false;
        for (var filterItemKey in this.filter) {
            let filterItem = this.filter[filterItemKey];
            if (filterItem.value != filterItem.oldValue) {
                result = true;
                if (syncOldValues) {
                    filterItem.oldValue = filterItem.value;
                } else {
                    break;
                }
            }
        }
        return result;
    }

    setFilterDataFromUrl():void {
        let url:UrlTree = (<any>this.router).currentUrlTree;
        for (var filterItemKey in this.filter) {
            let filterItem = this.filter[filterItemKey];
            let routeItemValue = url.queryParams[filterItemKey];
            if (routeItemValue != null && routeItemValue.length > 0) {
                filterItem.value = filterItem.oldValue = decodeURIComponent(routeItemValue);
            }
        }
    }

    updateUrlByFilterData():void {
        let url:UrlTree = (<any>this.router).currentUrlTree;
        url.queryParams = {};
        for (var filterItemKey in this.filter) {
            let filterItem = this.filter[filterItemKey];
            if (filterItem.value != null && filterItem.value.length > 0) {
                url.queryParams[filterItemKey] = encodeURIComponent(filterItem.value);
            }
        }
        this.router.navigateByUrl(url);
    }

    makeFilterRequest():any {
        let data = {};
        for (var filterItemKey in this.filter) {
            let filterItem = this.filter[filterItemKey];
            if (filterItem.value != null && filterItem.value.length > 0) {
                data[filterItemKey] = filterItem.value;
            }
        }
        return data;
    }

}