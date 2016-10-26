export abstract class BaseModel<T> {
    public id:T;

    constructor(id?:T) {
        this.id = id;
    }
}