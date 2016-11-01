import { Component, OnInit, ElementRef } from '@angular/core';

import {HttpLoaderEventEmitter} from '../notification/notification';

@Component({
   moduleId: module.id,
   selector: 'sd-http-loader',
   templateUrl: 'http-loader.component.html',
   styleUrls: ['http-loader.component.css'],
})

export class HttpLoaderComponent implements OnInit {
    private el:JQueryStatic;

    public constructor(el:ElementRef, private loaderEventEmitter:HttpLoaderEventEmitter) {
        this.el = el.nativeElement;
    }

    ngOnInit() {
        jQuery(this.el).hide();

        this.loaderEventEmitter.subscribe(loading => {
            let elem = jQuery(this.el);
            if (loading) {
                if (elem.is(':hidden'))
                    elem.show();
            } else {
                if (elem.is(':visible'))
                    elem.hide();
            }
        });
    }
}
