import {Injectable} from '@angular/core'
import {Headers, Http} from '@angular/http'

import 'rxjs/add/operator/toPromise';

import {Profile} from '../type/profile';
import {ConfigService} from './config.service';

@Injectable()
export class ProfileService{
	private headers = new Headers({
		'Content-Type': 'application/json;charset=UTF-8'
	})
	private url = this.configService.config.opmmgr_url + '/opm/profiles';
	constructor(
		private http: Http,
		private configService: ConfigService
	){}

	getProfiles(): Promise<Profile[]>{
		return this.http.get(this.url).toPromise().then(
			response => {
				console.debug('ProfileService', response);
				return response.json() as Profile[];
			}).catch(this.handleError);
	}

	getProfile(id: number): Promise<Profile>{
		const url = `${this.url}/${id}`;
		return this.http.get(url).toPromise().then(
			response => {
				console.debug('ProfileService', response);
				return response.json() as Profile;
			}).catch(this.handleError);
	}

	updateProfile(profile: Profile): Promise<Profile>{
		const url = `${this.url}/${profile.id}`;
		console.log(url);
		console.log(JSON.stringify(profile));
		return this.http.put(url, JSON.stringify(profile), {headers: this.headers}).toPromise().then(
			response => response.json() as Profile
		).catch(this.handleError);
	}

	createProfile(profile: Profile): Promise<Profile>{
		const url = this.url;
		let tempProfile = new Profile(profile);
		tempProfile.masks = [];
		return this.http.post(url, JSON.stringify(tempProfile), {headers: this.headers}).toPromise().then(
			response => response.json() as Profile
		).catch(this.handleError);
	}

	deleteProfile(profile: Profile): Promise<boolean>{
		const url = `${this.url}/${profile.id}`;
		return this.http.delete(url, {headers: this.headers}).toPromise().then(
			success => true, failure => false
		).catch(this.handleError);
	}

	private handleError(error: any): Promise<any>{
		console.debug('ProfileService', 'An error occurred', error);
		return Promise.reject(error.message || error);
	}
}