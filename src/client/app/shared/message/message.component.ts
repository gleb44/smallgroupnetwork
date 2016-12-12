import {Component, OnInit} from "@angular/core";
import {ErrorMessagesEventEmitter} from "../notification/notification";

@Component({
    moduleId: module.id,
    selector: 'sd-message',
    templateUrl: 'message.component.html',
    styleUrls: ['message.component.css'],
})

export class MessageComponent implements OnInit {

    messages = [];
    msgDuration = 3000; // ms

    constructor(private errorMessagesEventEmitter:ErrorMessagesEventEmitter) {
    }

    ngOnInit() {
        this.errorMessagesEventEmitter.subscribe(messages => {
            this.showMsg({items: messages, type: 'danger'});
        });
    }

    hideHandler(index:number) {
        this.messages[index].timer = null;
        this.hideMsg(index);
    }

    hideHandlerExecutor(message:any) {
        for (var index = 0; index < this.messages.length; index++) {
            if (this.messages[index].timer == message.timer) {
                this.hideHandler(index);
                break;
            }
        }
    }

    showMsg(message:any) {
        if (this.msgDuration) {
            message.timer = setTimeout(() => {
                this.hideHandlerExecutor(message);
            }, this.msgDuration);
        }
        this.messages.push(message);
    }

    removeTimer(index:number) {
        var msg = this.messages[index];
        if (msg && msg.timer) {
            clearTimeout(msg.timer);
        }
    }

    hideMsg(index:number) {
        this.removeTimer(index);
        this.messages.splice(index, 1);
    }

    public closeAlertMessage(messageIndex:number) {
        this.hideMsg(messageIndex);
    }

    public showTrace(messageIndex:number, messageItemIndex:number) {
        this.removeTimer(messageIndex);
        var msg = this.messages[messageIndex];
        if (msg) {
            msg.items[messageItemIndex].traceOpen = !msg.items[messageItemIndex].traceOpen;
        }
    }

}
