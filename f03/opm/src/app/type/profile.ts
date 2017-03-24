import {Location} from './location';
import {Function} from './function';
import {Mask} from './mask';

export class Profile{
	constructor(profile: Profile = undefined){
		if(profile){
			this.id = profile.id;
			this.name = profile.name;
			this.description = profile.description;
			this.masks = [];
			if(profile.masks){
				for(let i in profile.masks){
					this.masks.push(new Mask(profile.masks[i]));
				}
			}
		}else{
			this.masks = [];
		}
	}
	id: number;
	name: string;
	description: string;
	masks: Mask[];
}

