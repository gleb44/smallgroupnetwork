import {BaseModel} from "./base-model";
import {Profile} from "./profile";
import {AdminAccess} from "./admin-access";

export class User extends BaseModel {
    public email:string;
    public firstName:string;
    public lastName:string;
    public avatar:any;
    public profile:Profile;
    public adminAccess:AdminAccess;
}
