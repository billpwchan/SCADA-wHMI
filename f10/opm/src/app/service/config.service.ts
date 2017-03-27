import {Injectable} from '@angular/core'
import {Headers, Http} from '@angular/http'

import 'rxjs/add/operator/toPromise';

import {Config} from '../type/config';

@Injectable()
export class ConfigService{
	private static headers = new Headers({
		'Content-Type': 'application/json;charset=UTF-8'
	})
	private static url = './config/settings.json';
	constructor(private http: Http){
        this.config = new Config();
    }

    public config: Config;

	public load(): Promise<boolean>{
        return this.http.get(ConfigService.url).toPromise().then(
            response => {
                console.debug(
                    '[ConfigServer]', response
                );
                this.config = response.json() as Config;
                return true;
            },
            failure => {
                console.error(
                    '[ConfigServer]',
                    'Failed to retrieve config:', ConfigService.url,
                    'Response:', failure
                );
                return false;
            }
        ).catch(this.handleError);
    }

	private handleError(error: any): Promise<any>{
		console.debug('ProfileService', 'An error occurred', error);
		return Promise.reject(error.message || error);
	}
}