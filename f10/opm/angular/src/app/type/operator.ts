import { Profile } from './profile';

export class Operator {
    public id: number;
    public name: string;
    public description: string;
    public password: string;
    public profiles: Profile[];

    constructor(o: Operator = undefined) {
        this.profiles = [];
        if (o) {
            this.id = o.id;
            this.name = o.name;
            this.description = o.description;
            this.password = o.password;
            if (o.profiles) {
                o.profiles.forEach(
                    p => {
                        const profile = new Profile(p);
                        // only need the profile id, other fields are not needed
                        profile.description = null;
                        profile.masks = null;
                        profile.name = null;
                        this.profiles.push(profile);
                    }
                );
            }
        }
    }
}
