import {Mask} from './mask';

export class Profile {
    public id: number;
    public name: string;
    public description: string;
    public masks: Mask[];

    constructor(profile: Profile = undefined) {
        if (profile) {
            this.id = profile.id;
            this.name = profile.name;
            this.description = profile.description;
            this.masks = [];
            if (profile.masks) {
                for (let i = 0; i < profile.masks.length; ++i) {
                    this.masks.push(new Mask(profile.masks[i]));
                }
            }
        } else {
            this.masks = [];
        }
    }
}
