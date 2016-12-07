import {BaseModel} from "./base-model";

export class Profile extends BaseModel {
    public birthDate:any;
    public country:string;
    public state:string;
    public postalCode:string;
    public city:string;
    public address:string;
}
