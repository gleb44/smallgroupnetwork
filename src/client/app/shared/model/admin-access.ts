import {BaseModel} from "./base-model";

export enum AdminRole {
    Admin = <any>'Admin',
    Staff = <any>'Staff'
}

export class AdminAccess extends BaseModel {
    public adminRole:AdminRole;
}
