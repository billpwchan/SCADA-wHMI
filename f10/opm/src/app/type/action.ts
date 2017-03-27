export class Action{
	constructor(act: Action = undefined){
		if(act){
			this.id = act.id;
			this.category = act.category;
			this.name = act.name;
			this.description = act.description;
			this.abbrev = act.abbrev;
		}
	}
	id: number;
	category: number;
	name: string;
	description: string;
	abbrev: string;
}