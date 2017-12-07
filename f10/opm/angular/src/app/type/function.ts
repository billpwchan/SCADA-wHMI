export class Function {
    public id: number;
    public category: number;
    public name: string;
    public description: string;
    public family: string;

    constructor(func: Function = undefined) {
        if (func) {
            this.id = func.id;
            this.category = func.category;
            this.name = func.name;
            this.description = func.description;
            this.family = func.family;
        }
    }
}
