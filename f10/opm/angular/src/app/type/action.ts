export class Action {
    public id: number;
    public category: number;
    public name: string;
    public description: string;
    public abbrev: string;

    constructor(act: Action = undefined) {
        if (act) {
            this.id = act.id;
            this.category = act.category;
            this.name = act.name;
            this.description = act.description;
            this.abbrev = act.abbrev;
        }
    }
}
