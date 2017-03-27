import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import { Action } from '../type/action';
import { ConfigService } from './config.service';

@Injectable()
export class ActionService {
    private url = this.configService.config.opmmgr_url + '/opm/actions';
    constructor(
        private http: Http,
        private configService: ConfigService
    ) { }

    public getActions(): Promise<Action[]> {
        return this.http.get(this.url).toPromise().then(
            response => response.json() as Action[]
        ).catch(this.handleError);
    }

    public getAction(id: number): Promise<Action> {
        const url = `${this.url}/${id}`;
        return this.http.get(url).toPromise().then(
            response => response.json() as Action
        ).catch(this.handleError);
    }

    private handleError(error: any): Promise<any> {
        console.error('[ActionService]', 'An error occurred', error);
        return Promise.reject(error.message || error);
    }
}
