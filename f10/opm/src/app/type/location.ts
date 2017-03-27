export class Location{
	constructor(loc: Location = undefined){
		if(loc){
			this.id = loc.id;
			this.category = loc.category;
			this.name = loc.name;
			this.description = loc.description;
		}
	}
	id: number;
	category: number;
	name: string;
	description: string;
}