import { Mask } from './mask';

export class Profile {
    public id: number;
    public name: string;
    public description: string;
    public masks: Mask[];

    constructor(p: Profile = undefined) {
        this.masks = [];
        if (p) {
            this.id = p.id;
            this.name = p.name;
            this.description = p.description;
            this.masks = [];
            if (p.masks) {
                p.masks.forEach(m => this.masks.push(new Mask(m)));
            }
        }
    }
}
