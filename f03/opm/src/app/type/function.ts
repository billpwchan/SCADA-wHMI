export class Function{
	constructor(func: Function = undefined){
		if(func){
			this.id = func.id;
			this.category = func.category;
			this.name = func.name;
			this.description = func.description;
			this.family = func.family;
		}
	}
	id: number;
	category: number;
	name: string;
	description: string;
	family: string;
}