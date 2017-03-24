import {Injectable} from '@angular/core'
import {Headers, Http} from '@angular/http'

import 'rxjs/add/operator/toPromise';

import {Mask} from '../type/mask';
import {Profile} from '../type/profile';
import {ConfigService} from './config.service';

@Injectable()
export class MaskService{
	private headers = new Headers({
		'Content-Type': 'application/json;charset=UTF-8'
	})
	private url = this.configService.config.opmmgr_url + '/opm/masks';
	constructor(
		private http: Http,
		private configService: ConfigService
	){}

	getMasks(): Promise<Mask[]>{
		return this.http.get(this.url).toPromise().then(
			response => {
				console.debug('[MaskService]', response);
				return response.json() as Mask[];
			}).catch(this.handleError);
	}

	getMask(id: number): Promise<Mask>{
		const url = `${this.url}/${id}`;
		return this.http.get(url).toPromise().then(
			response => {
				console.debug('[MaskService]', response);
				return response.json() as Mask;
			}).catch(this.handleError);
	}

	createMask(mask: Mask): Promise<Mask>{
		const url = this.url;
		return this.http.post(url, JSON.stringify(mask), {headers: this.headers}).toPromise().then(
			response => response.json() as Mask
		).catch(this.handleError);
	}

	createMaskForProfile(mask: Mask, profile: Profile): Promise<Mask>{
		const url = this.url;
		let tempMask = new Mask(mask);
		tempMask.profile = new Profile(profile);
		tempMask.profile.masks = [];
		const jsonMask = JSON.stringify(tempMask);
		console.debug('[MaskService]', jsonMask);
		return this.http.post(url, jsonMask, {headers: this.headers}).toPromise().then(
			response => response.json() as Mask
		).catch(this.handleError);
	}


	deleteMask(mask: Mask): Promise<boolean>{
		const url = `${this.url}/${mask.id}`;
		return this.http.delete(url, {headers: this.headers}).toPromise().then(
			success => true, failure => false
		).catch(this.handleError);
	}

	updateMask(mask: Mask): Promise<Mask>{
		const url = `${this.url}/${mask.id}`;
		return this.http.put(url, JSON.stringify(mask), {headers: this.headers}).toPromise().then(
			response => response.json() as Mask
		).catch(this.handleError);
	}

	updateMaskForProfile(mask: Mask, profile: Profile): Promise<Mask>{
		const url = `${this.url}/${mask.id}`;
		let tempMask = new Mask(mask);
		tempMask.profile = new Profile(profile);
		tempMask.profile.masks = [];
		const jsonMask = JSON.stringify(tempMask);
		console.debug('[MaskService]', url, jsonMask);
		return this.http.put(url, jsonMask, {headers: this.headers}).toPromise().then(
			response => response.json() as Mask
		).catch(this.handleError);
	}

	private handleError(error: any): Promise<any>{
		console.error('MaskService', 'An error occurred', error);
		return Promise.reject(error.message || error);
	}
}