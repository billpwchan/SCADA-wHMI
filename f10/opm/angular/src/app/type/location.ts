export class Location {
    public id: number;
    public category: number;
    public name: string;
    public description: string;

    constructor(loc: Location = undefined) {
        if (loc) {
            this.id = loc.id;
            this.category = loc.category;
            this.name = loc.name;
            this.description = loc.description;
        }
    }
}
