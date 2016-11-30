import {BaseModel} from "./base-model";

export class Admin extends BaseModel<string>
{
    login:string;
    password:string;
}
