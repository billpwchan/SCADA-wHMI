import {Function} from './function';
import {Location} from './location';
import {Profile} from './profile';

export class Mask{
	constructor(mask: Mask = undefined){
		if(mask){
			this.id = mask.id;
			this.mask1 = mask.mask1;
			this.mask2 = mask.mask2;
			this.mask3 = mask.mask3;
			this.mask4 = mask.mask4;
			this.function = new Function(mask.function);
			this.location = new Location(mask.location);
			this.profile = new Profile(mask.profile);
		}else{
			this.function = new Function();
			this.location = new Location();
			this.profile = new Profile();
		}
	}
	id: number;
	mask1: string;
	mask2: string;
	mask3: string;
	mask4: string;
	function: Function;
	location: Location;
	profile: Profile;
}