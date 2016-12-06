import {BaseModel} from "./base-model";

export enum AdminRole {
    Admin, Staff
}

export class AdminAccess extends BaseModel<number> {
    public adminRole:AdminRole;

    constructor(id?:number, adminRole?:AdminRole) {
        super(id);
        this.adminRole = adminRole;
    }
}
