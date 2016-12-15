import {EventEmitter} from "@angular/core";

class BaseEventEmitter<T> extends EventEmitter<T> {
    constructor() {
        super(true);
    }
}

export class AuthEventEmitter extends BaseEventEmitter<any> {
}
export class HttpLoaderEventEmitter extends BaseEventEmitter<boolean> {
}
export class ErrorMessagesEventEmitter extends BaseEventEmitter<any[]> {
}

export const NOTIFICATION_PROVIDERS = [HttpLoaderEventEmitter, ErrorMessagesEventEmitter, AuthEventEmitter];