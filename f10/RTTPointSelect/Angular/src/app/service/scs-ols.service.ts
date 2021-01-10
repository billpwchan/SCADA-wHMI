import { Injectable } from '@angular/core';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import { ConfigService } from './config.service';
import { ScsOlsDef } from './scs-ols-def';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable()
export class ScsOlsService {
    private httpOptions = {
        headers: new HttpHeaders({
            'Content-Type': 'application/json;charset=UTF-8'
        })
    }
    private urlScsOls = this.configService.config.getIn(['scs_ols_url']);
    constructor(
        private http: HttpClient,
        private configService: ConfigService
    ) { }

    // readOlslist
    public readOlslist(listserver: string, listName: string, token: string): Promise<any[]> {
        if (token && token !== '') { this.httpOptions.headers = this.httpOptions.headers.set('tokenKey', token); console.log('Token: ', token); }
        const url = this.urlScsOls + ScsOlsDef.READ_OLSLIST + listserver + ScsOlsDef.LST_NAME_PARAM + listName;
        return this.http.get(url, this.httpOptions).toPromise().then(
            function (res: any) {
                return res.response.data;
            }
        ).catch(this.handleError);
    }

    private handleError(error: any): Promise<any> {
        console.error('{ScsOlsService}', '[handleError]', error);
        return Promise.reject(error.message || error);
    }
}
