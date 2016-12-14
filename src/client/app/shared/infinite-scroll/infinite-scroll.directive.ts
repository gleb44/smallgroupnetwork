import {OnInit, Input, Output, EventEmitter, Directive} from "@angular/core";
import {NgModel} from "@angular/forms";
import {Subject} from "rxjs/Subject";
import {BehaviorSubject} from "rxjs/BehaviorSubject";

@Directive({
    selector: '[infiniteScroll]',
    providers: [NgModel],
    host: {
        '(window:scroll)': 'onScroll($event)'
    },
})
export class InfiniteScroll implements OnInit {

    private _scrollDisabled:Subject<boolean> = new BehaviorSubject(false);
    private _scrollEnabled:boolean = true;
    private _checkWhenEnabled:boolean = true;

    @Input('scrollDistance') scrollDistance:number;
    @Input() set scrollDisabled(disabled:boolean) {
        this._scrollDisabled.next(disabled);
    };
    @Output() onScrollMethod = new EventEmitter<any>();

    ngOnInit():any {
        this._scrollDisabled.asObservable().subscribe(disabled => {
            this._scrollEnabled = !disabled;
            if (this._scrollEnabled && this._checkWhenEnabled) {
                this._checkWhenEnabled = false;
                setTimeout(() => this.onScroll(), 300);
            }
        });
    }

    onScroll():void {
        if (jQuery(document).height() > jQuery(window).height()) {
            // scroll visible
            if (jQuery(window).scrollTop() + jQuery(window).height() > jQuery(document).height() - this.scrollDistance) {
                if (this._scrollEnabled) {
                    this.onScrollMethod.emit(null);
                } else {
                    this._checkWhenEnabled = true;
                }
            }
        } else {
            // scroll not visible
            if (this._scrollEnabled) {
                this._checkWhenEnabled = true;
                this.onScrollMethod.emit(null);
            }
        }
    };
}