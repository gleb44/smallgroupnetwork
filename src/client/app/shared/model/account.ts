import {BaseModel} from "./base-model";

export class Account extends BaseModel<number> {
    public login:string;
    public password:string;

    constructor(id?:number,
                login?:string,
                password?:string) {
        super(id);
        this.login = login;
        this.password = password;
    }
}
