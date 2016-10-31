import {EventEmitter} from '@angular/core';

export class HttpLoaderEventEmitter extends EventEmitter<boolean> {}
export class ErrorMessagesEventEmitter extends EventEmitter<any[]> {}

export const NOTIFICATION_PROVIDERS = [HttpLoaderEventEmitter, ErrorMessagesEventEmitter];