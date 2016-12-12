import {OnInit, Input, Output, EventEmitter, Directive} from "@angular/core";
import {NgModel} from "@angular/forms";
import {Subject} from "rxjs/Subject";
import {BehaviorSubject} from "rxjs/BehaviorSubject";

@Directive({
    selector: '[InfiniteScroll]',
    providers: [NgModel],
    host: {
        '(window:scroll)': 'onScroll($event)'
    },
})
export class InfiniteScroll implements OnInit {

    scrollDisabled:Subject<boolean> = new BehaviorSubject(false);
    private scrollEnabled:boolean = true;
    private checkWhenEnabled:boolean = true;

    @Input('ScrollDistance') scrollDistance:number;

    @Input() set ScrollDisabled(disabled:boolean) {
        this.scrollDisabled.next(disabled);
    };

    @Output() OnScrollMethod = new EventEmitter<any>();

    ngOnInit():any {
        this.scrollDisabled.asObservable().subscribe(disabled => {
            this.scrollEnabled = !disabled;
            if (this.scrollEnabled && this.checkWhenEnabled) {
                this.checkWhenEnabled = false;
                setTimeout(() => this.onScroll(), 300);
            }
        });
    }

    onScroll():void {
        if (jQuery(document).height() > jQuery(window).height()) {
            // scroll visible
            if (jQuery(window).scrollTop() + jQuery(window).height() > jQuery(document).height() - this.scrollDistance) {
                if (this.scrollEnabled) {
                    this.OnScrollMethod.emit(null);
                } else {
                    this.checkWhenEnabled = true;
                }
            }
        } else {
            // scroll not visible
            if (this.scrollEnabled) {
                this.checkWhenEnabled = true;
                this.OnScrollMethod.emit(null);
            }
        }
    };
}