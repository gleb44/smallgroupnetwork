import {BaseModel} from "./baseModel";

export class Admin extends BaseModel<string>
{
    login:number;
    password:string;
}
