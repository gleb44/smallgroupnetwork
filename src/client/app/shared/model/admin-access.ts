import {BaseModel} from "./base-model";

export enum AdminRole {
    Admin, Staff
}

export class AdminAccess extends BaseModel {
    public adminRole:AdminRole;
}
