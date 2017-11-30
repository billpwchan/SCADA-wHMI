import { Injectable } from '@angular/core';
import { Headers, Http, Response } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import { Observable } from 'rxjs/Rx';
import { ConfigService } from './config.service';
import { ScsOlsDef } from './scs-ols-def';
@Injectable()
export class ScsOlsService {
    private static headers = new Headers({
        'Content-Type': 'application/json;charset=UTF-8'
    });
    private urlScsOls = this.configService.config.getIn(['scs_ols_url']);
    constructor(
        private http: Http,
        private configService: ConfigService
    ) { }
    //
    // readOlslist
    //
    public readOlslist(listserver: string, listName: string ): Promise<any[]> {
        console.log('{ScsOlsService}', '[getTaskNames] begin');
        const url = this.urlScsOls + ScsOlsDef.READ_OLSLIST + listserver + ScsOlsDef.LST_NAME_PARAM + listName;
        return this.http.get(url).toPromise().then(
            this.extractDataList
        ).catch(this.handleError);
    }
    private extractDataList(res: Response) {
        console.log('{ScsOlsService}', '[response]', res);
        const jsonObj = res.json();
        console.log('{ScsOlsService}', '[extractTaskNames]', jsonObj.response.data);
        return jsonObj.response.data || {};
    }
    private handleError(error: Response | any) {
        console.error('{ScsOlsService}', '[handleError]', error);
        return Promise.reject(error.message || error);
    }
}
