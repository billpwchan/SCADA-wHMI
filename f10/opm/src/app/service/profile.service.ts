import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import { Profile } from '../type/profile';
import { ConfigService } from './config.service';

@Injectable()
export class ProfileService {
    private static headers = new Headers({
        'Content-Type': 'application/json;charset=UTF-8'
    });
    private url = this.configService.config.getIn(['opmmgr_url']) + '/opm/profiles';
    constructor(
        private http: Http,
        private configService: ConfigService
    ) { }

    public getAll(): Promise<Profile[]> {
        return this.http.get(this.url).toPromise().then(
            response => response.json() as Profile[]
        ).catch(this.handleError);
    }

    public get(id: number): Promise<Profile> {
        const url = `${this.url}/${id}`;
        return this.http.get(url).toPromise().then(
            response => response.json() as Profile
        ).catch(this.handleError);
    }

    public update(profile: Profile): Promise<Profile> {
        const url = `${this.url}/${profile.id}`;
        return this.http.put(url, JSON.stringify(profile), {headers: ProfileService.headers}).toPromise().then(
            response => response.json() as Profile
        ).catch(this.handleError);
    }

    public create(profile: Profile): Promise<Profile> {
        const url = this.url;
        const tempProfile = new Profile(profile);
        tempProfile.masks = [];
        return this.http.post(url, JSON.stringify(tempProfile), {headers: ProfileService.headers}).toPromise().then(
            response => response.json() as Profile
        ).catch(this.handleError);
    }

    public delete(profile: Profile): Promise<boolean> {
        const url = `${this.url}/${profile.id}`;
        return this.http.delete(url, {headers: ProfileService.headers}).toPromise().then(
            success => true, failure => false
        ).catch(this.handleError);
    }

    private handleError(error: any): Promise<any> {
        console.error('{ProfileService}', '[handleError]', error);
        return Promise.reject(error.message || error);
    }

    // common profile functions
    public compareProfile(p1: Profile, p2: Profile): number {
        if (p1.name < p2.name) { return -1; }
        if (p1.name > p2.name) { return 1; }
        if (p1.id < p2.id) {return -1; }
        if (p1.id > p2.id) {return 1; }
        return 0;
    }
}
