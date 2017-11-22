import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import { Action } from '../type/action';
import { ConfigService } from './config.service';

@Injectable()
export class ActionService {
    private url = this.configService.config.getIn(['opmmgr_url']) + '/opm/actions';
    constructor(
        private http: Http,
        private configService: ConfigService
    ) { }

    public getAll(): Promise<Action[]> {
        return this.http.get(this.url).toPromise().then(
            response => response.json() as Action[]
        ).catch(this.handleError);
    }

    public get(id: number): Promise<Action> {
        const url = `${this.url}/${id}`;
        return this.http.get(url).toPromise().then(
            response => response.json() as Action
        ).catch(this.handleError);
    }

    private handleError(error: any): Promise<any> {
        console.error('{ActionService}', '[handleError]', error);
        return Promise.reject(error.message || error);
    }
}
