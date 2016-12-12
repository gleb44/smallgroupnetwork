import {EventEmitter} from "@angular/core";

export class AuthEventEmitter extends EventEmitter<any> {
    constructor() {
        super(true);
    }
}
export class HttpLoaderEventEmitter extends EventEmitter<boolean> {
    constructor() {
        super(true);
    }
}
export class ErrorMessagesEventEmitter extends EventEmitter<any[]> {
    constructor() {
        super(true);
    }
}

export const NOTIFICATION_PROVIDERS = [HttpLoaderEventEmitter, ErrorMessagesEventEmitter, AuthEventEmitter];