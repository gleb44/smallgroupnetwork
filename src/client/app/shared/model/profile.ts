import {BaseModel} from "./base-model";

export class Profile extends BaseModel<number> {
    public birthDate:Date;
    public country:string;
    public state:string;
    public postalCode:string;
    public city:string;
    public address:string;

    constructor(id?:number,
                birthDate?:Date,
                country?:string,
                state?:string,
                postalCode?:string,
                city?:string,
                address?:string) {
        super(id);
        this.birthDate = birthDate;
        this.country = country;
        this.state = state;
        this.postalCode = postalCode;
        this.city = city;
        this.address = address;
    }
}
