import {BaseModel} from "./base-model";

export class Admin extends BaseModel<string>
{
    login:number;
    password:string;
}
