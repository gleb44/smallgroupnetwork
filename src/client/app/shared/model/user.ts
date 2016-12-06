import {BaseModel} from "./base-model";
import {Profile} from "./profile";
import {AdminAccess} from "./admin-access";

export class User extends BaseModel<number> {
    public email:string;
    public firstName:string;
    public lastName:string;
    public avatar:any;
    public profile:Profile;
    public adminAccess:AdminAccess;

    constructor(id?:number,
                email?:string,
                firstName?:string,
                lastName?:string,
                avatar?:any,
                profile?:Profile,
                adminAccess?:AdminAccess) {
        super(id);

        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.profile = profile;
        this.adminAccess = adminAccess;
    }
}
