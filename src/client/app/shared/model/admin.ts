import {BaseModel} from "./base-model";

export class Admin extends BaseModel<string>
{
    email:string;
    password:string;
}
