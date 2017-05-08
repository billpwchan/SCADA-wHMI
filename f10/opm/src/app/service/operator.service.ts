import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import { Operator } from '../type/operator';
import { ConfigService } from './config.service';

@Injectable()
export class OperatorService {
    private static headers = new Headers({
        'Content-Type': 'application/json;charset=UTF-8'
    });
    private url = this.configService.config.getIn(['opmmgr_url']) + '/opm/operators';
    constructor(
        private http: Http,
        private configService: ConfigService
    ) { }

    public getAll(): Promise<Operator[]> {
        return this.http.get(this.url).toPromise().then(
            success => success.json() as Operator[],
            failure => null
        ).catch(this.handleError);
    }

    public get(id: number): Promise<Operator> {
        const url = `${this.url}/${id}`;
        return this.http.get(url).toPromise().then(
            success => success.json() as Operator,
            failure => null
        ).catch(this.handleError);
    }

    public getByName(name: string): Promise<Operator[]> {
        const url = `${this.url}/names/${name}`;
        return this.http.get(url).toPromise().then(
            success => success.json() as Operator[],
            failure => null
        ).catch(this.handleError);
    }

    public update(o: Operator): Promise<Operator> {
        const url = `${this.url}/${o.id}`;
        return this.http.put(url, JSON.stringify(o), {headers: OperatorService.headers}).toPromise().then(
            response => response.json() as Operator
        ).catch(this.handleError);
    }

    public create(o: Operator): Promise<number> {
        return this.http.post(this.url, JSON.stringify(o), {headers: OperatorService.headers}).toPromise().then(
            success => success.json() as number,
            failure => null
        ).catch(this.handleError);
    }

    public delete(id: number): Promise<boolean> {
        const url = `${this.url}/${id}`;
        return this.http.delete(url, {headers: OperatorService.headers}).toPromise().then(
            success => success.json() as boolean, failure => false
        ).catch(this.handleError);
    }

    public changePassword(id: number, oldPassword: string, newPassword: string): Promise<boolean> {
        const url = `${this.url}/${id}/password/${oldPassword},${newPassword}`;
        return this.http.put(url, {headers: OperatorService.headers}).toPromise().then(
            success => success.json() as boolean, failure => false
        ).catch(this.handleError);
    }

    private handleError(error: any): Promise<any> {
        console.error('{OperatorService}', '[handleError]', error);
        return Promise.reject(error.message || error);
    }

    // common operator function
    public compareOperator(o1: Operator, o2: Operator): number {
        if (o1.name < o2.name) { return -1; }
        if (o1.name > o2.name) { return 1; }
        if (o1.id < o2.id) {return -1; }
        if (o1.id > o2.id) {return 1; }
        return 0;
    }
}
