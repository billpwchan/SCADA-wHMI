import {Injectable} from '@angular/core'
import {Headers, Http} from '@angular/http'

import 'rxjs/add/operator/toPromise';

import {Location} from '../type/location';
import {ConfigService} from './config.service';

@Injectable()
export class LocationService{
	private headers = new Headers({
		'Content-Type': 'application/json;charset=UTF-8'
	})
	private url = this.configService.config.opmmgr_url + '/opm/locations';
	constructor(
		private http: Http,
		private configService: ConfigService
	){}

	getLocations(): Promise<Location[]>{
		return this.http.get(this.url).toPromise().then(
			response => {
				console.debug('LocationService', response);
				return response.json() as Location[];
			}).catch(this.handleError);
	}

	getLocation(id: number): Promise<Location>{
		const url = `${this.url}/${id}`;
		return this.http.get(url).toPromise().then(
			response => {
				console.debug('LocationService', response);
				return response.json() as Location;
			}).catch(this.handleError);
	}

	private handleError(error: any): Promise<any>{
		console.error('LocationService', 'An error occurred', error);
		return Promise.reject(error.message || error);
	}
}