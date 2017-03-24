import {Injectable} from '@angular/core'
import {Headers, Http} from '@angular/http'

import 'rxjs/add/operator/toPromise';

import {Action} from '../type/action';
import {ConfigService} from './config.service';

@Injectable()
export class ActionService{
	private headers = new Headers({
		'Content-Type': 'application/json;charset=UTF-8'
	})
	private url = this.configService.config.opmmgr_url + '/opm/actions';
	constructor(
		private http: Http,
		private configService: ConfigService
	){}

	getActions(): Promise<Action[]>{
		return this.http.get(this.url).toPromise().then(
			response => {
				console.debug('ActionService', response);
				return response.json() as Action[];
			}).catch(this.handleError);
	}

	getAction(id: number): Promise<Action>{
		const url = `${this.url}/${id}`;
		return this.http.get(url).toPromise().then(
			response => {
				console.debug('ActionService', response);
				return response.json() as Action;
			}).catch(this.handleError);
	}

	private handleError(error: any): Promise<any>{
		console.debug('ActionService', 'An error occurred', error);
		return Promise.reject(error.message || error);
	}
}