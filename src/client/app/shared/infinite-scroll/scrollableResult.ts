import {Observable} from "rxjs/Observable";
import {BaseModel} from "../model/base-model";

export interface IScrollableResult<T extends BaseModel> {
    getItems():T[];
    stop();
    hasNext():boolean;
    next(call?:Function, postCall?:Function);
    readAll(call:Function);
    addExternalFirs(item:T, call?:Function);
    addFirs(item:T, call?:Function);
    updateItemField(item:T, fieldName:string, call?:Function);
    update(item:T, call?:Function);
    remove(item:T, call?:Function);
    clearInternal(call?:Function, nextCall?:Function, nextPostCall?:Function);
    updateFilter(filter:any, call?:Function, nextCall?:Function, nextPostCall?:Function);
    getTotal():number;
}

export class ScrollableResult<T extends BaseModel> implements IScrollableResult<T> {

    private items:T[];
    private itemsReversed:T[];
    private busy:boolean;
    private hasMore:boolean;
    private totalCount:number;
    private itemsCount:number;
    private externalItemsCount:number;

    constructor(private search:(args:any) => Observable<any>,
                private limit?:number,
                private filter?:any,
                private checkDuplicate?:boolean,
                private reversEnable?:boolean) {

        this.items = [];
        this.busy = false;
        this.hasMore = true;
        this.totalCount = 0;
        this.itemsCount = 0;
        this.externalItemsCount = 0;

        if (reversEnable) {
            this.itemsReversed = [];
        }
    }

    public getItems():T[] {
        if (this.reversEnable) {
            return this.itemsReversed;
        } else {
            return this.items;
        }
    }

    private _refreshReversed() {
        if (this.reversEnable) {
            this.itemsReversed = this.items.slice().reverse();
        }
    };

    public stop():void {
        this.hasMore = false;
    };

    public hasNext():boolean {
        return !this.busy && this.hasMore;
    };

    public next(call?:(items:T[]) => void, postCall?:(items:T[]) => void) {
        if (this.busy || !this.hasMore) {
            return;
        }
        this.busy = true;

        let requestData = {
            limit: this.limit,
            offset: (this.itemsCount - this.externalItemsCount)
        };

        if (this.filter) {
            for (var key in this.filter) {
                if (this.filter.hasOwnProperty(key)) {
                    requestData[key] = this.filter[key];
                }
            }
        }

        this.search(requestData).subscribe((res) => {

            this.totalCount = res.count;

            let items:T[] = res.result;
            var i, j;

            if (this.checkDuplicate) {

                var exists;
                for (i = 0; i < items.length; i++) {
                    exists = false;
                    for (j = 0; j < this.items.length; j++) {
                        if (items[i].id === this.items[j].id) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        this.items.push(items[i]);
                    }
                }

            } else {

                for (i = 0; i < items.length; i++) {
                    this.items.push(items[i]);
                }

            }

            this.itemsCount += items.length;

            this.hasMore = res.result.length > 0;

            this._refreshReversed();

            if (call) {
                call(items);
            }

            this.busy = false;

            if (postCall) {
                postCall(items);
            }
        });
    }

    private _readAll(call?:() => void) {
        if (this.hasMore) {
            this.next(null, function () {
                this._readAll(call)
            });
        } else {
            if (call) {
                call();
            }
        }
    };

    public readAll(call?:() => void) {
        this._readAll(call)
    };

    public addExternalFirs(item:T, call?:() => void) {
        this.items.unshift(item);
        this.externalItemsCount++;

        this._refreshReversed();

        if (call) {
            call();
        }
    };

    public addFirs(item:T, call?:() => void) {
        this.items.unshift(item);
        this.totalCount++;

        this._refreshReversed();

        if (call) {
            call();
        }
    };

    public updateItemField(item:T, fieldName:string, call?:() => void) {
        for (var i = 0; i < this.items.length; i++) {
            if (this.items[i].id === item.id) {
                this.items[i][fieldName] = item[fieldName];
                break;
            }
        }

        this._refreshReversed();

        if (call) {
            call();
        }
    };

    public update(item:T, call?:() => void) {
        for (var i = 0; i < this.items.length; i++) {
            if (this.items[i].id === item.id) {
                this.items[i] = item;
                break;
            }
        }

        this._refreshReversed();

        if (call) {
            call();
        }
    };

    public remove(item:T, call?:() => void) {
        for (var i = 0; i < this.items.length; i++) {
            if (this.items[i].id === item.id) {
                this.items.splice(i, 1);
                this.itemsCount--;
                this.totalCount--;
                break;
            }
        }

        this._refreshReversed();

        if (call) {
            call();
        }
    };

    public clearAll(call?:() => void) {
        if (this.busy) {
            let self = this;
            setTimeout(() => self.clearAll(call), 500);
        } else {
            this.items = [];
            this.itemsCount = 0;
            this.totalCount = 0;
            this.hasMore = true;
            this.externalItemsCount = 0;

            this._refreshReversed();

            if (call) {
                call();
            }
        }
    };

    public clearInternal(call?:() => void, nextCall?:(items:T[]) => void, nextPostCall?:(items:T[]) => void) {
        if (this.busy) {
            let self = this;
            setTimeout(()=>self.clearAll(call), 500);
        } else {
            this.items = this.items.slice(0, this.externalItemsCount);
            this.itemsCount = 0;
            this.hasMore = true;
            this.next(nextCall, nextPostCall);

            this._refreshReversed();

            if (call) {
                call();
            }
        }
    };

    public updateFilter(filter:any, call?:() => void, nextCall?:(items:T[]) => void, nextPostCall?:(items:T[]) => void) {
        this.filter = filter;
        this.clearInternal(call, nextCall, nextPostCall);
    };

    public getTotal():number {
        return this.totalCount + this.externalItemsCount;
    };

}