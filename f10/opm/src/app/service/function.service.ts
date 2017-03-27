import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import { Function } from '../type/function';
import { ConfigService } from './config.service';

@Injectable()
export class FunctionService {
    private url = this.configService.config.opmmgr_url + '/opm/functions';
    constructor(
        private http: Http,
        private configService: ConfigService
    ) { }

    public getFunctions(): Promise<Function[]> {
        return this.http.get(this.url).toPromise().then(
            response => response.json() as Function[]
        ).catch(this.handleError);
    }

    public getFunction(id: number): Promise<Function> {
        const url = `${this.url}/${id}`;
        return this.http.get(url).toPromise().then(
            response => response.json() as Function
        ).catch(this.handleError);
    }

    private handleError(error: any): Promise<any> {
        console.error('[FunctionService]', 'An error occurred', error);
        return Promise.reject(error.message || error);
    }
}
