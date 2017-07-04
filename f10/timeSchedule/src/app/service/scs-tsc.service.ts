import { Injectable } from '@angular/core';
import { Headers, Http, Response } from '@angular/http';

import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import { Observable } from 'rxjs/Rx';

import { ConfigService } from './config.service';

import { ScsTscDef } from './scs-tsc-def';

@Injectable()
export class ScsTscService {
    private static headers = new Headers({
        'Content-Type': 'application/json;charset=UTF-8'
    });

    private urlScsTsc = this.configService.config.getIn(['scs_tsc_url']);

    constructor(
        private http: Http,
        private configService: ConfigService
    ) { }

    public getDescFilterEnable(taskNames: string[]): Observable<any> {
        return Observable.forkJoin(this.getDescFilterEnableObservables(taskNames)).map((data: any[]) => { return data; });
    }

    private getDescFilterEnableObservables(taskNames: string []) : Observable<any> [] {
        console.log('{ScsTscService}', '[getDescFilterEnableObservables]', taskNames);
        let obs : Observable<any> [] = new Array<Observable<any>>() ;
        for (let taskname of taskNames) {
            obs.push(this.http.get(this.urlScsTsc + ScsTscDef.GET_DESCRIPTION + taskname).map(this.extractDescription));
            obs.push(this.http.get(this.urlScsTsc + ScsTscDef.GET_FILTER + taskname).map(this.extractFilter));
            obs.push(this.http.get(this.urlScsTsc + ScsTscDef.IS_ENABLED + taskname).map(this.extractEnableFlag));
        }
        return obs;
    }

    private extractEnableFlag(res: Response) {
    //    console.log('{ScsTscService}', '[extractEnableFlag]', res);
        let jsonObj = res.json();
    //    console.log('{ScsTscService}', '[extractEnableFlag]', jsonObj.response.status);

        return jsonObj.response.status;
    }


    //
    //  getTaskNames
    //
    public getTaskNames() : Observable<string[]>{
        console.log('{ScsTscService}', '[getTaskNames] begin');
        let url = this.urlScsTsc + ScsTscDef.GET_TASK_NAMES;
        return this.http.get(url)
            .map(this.extractTaskNames).catch(this.handleError);
    }

    private extractTaskNames(res: Response) {
        console.log('{ScsTscService}', '[extractTaskNames]', res);
        let jsonObj = res.json();
        console.log('{ScsTscService}', '[extractTaskNames]', jsonObj.response.taskNames);

        return jsonObj.response.taskNames || {};
    }


    //
    //  getDescription
    //
    public getDescription(taskName: string) : Observable<string>{
        let url = this.urlScsTsc + ScsTscDef.GET_DESCRIPTION + taskName;
        return this.http.get(url).map(this.extractDescription).catch(this.handleError);
    }

    private extractDescription(res: Response) {
    //    console.log('{ScsTscService}', '[extractTaskDescription]', res);
        let jsonObj = res.json();
   //     console.log('{ScsTscService}', '[extractTaskDescription]', jsonObj.response.description);

        return jsonObj.response.description || {};
    }


    //
    //  getStartTime
    //
    public getStartTime(taskName: string) : Observable<string>{
        let url = this.urlScsTsc + ScsTscDef.GET_START_TIME + taskName;
        return this.http.get(url).map(this.extractStartTime).catch(this.handleError);
    }

    private extractStartTime(res: Response) {
        console.log('{ScsTscService}', '[extractStartTime]', res);
        let jsonObj = res.json();
        console.log('{ScsTscService}', '[extractStartTime]', jsonObj.response.startTime);

        return jsonObj.response.startTime || {};
    }


    //
    //  getEndTime
    //
    public getEndTime(taskName: string) : Observable<string>{
        let url = this.urlScsTsc + ScsTscDef.GET_END_TIME + taskName;
        return this.http.get(url).map(this.extractEndTime).catch(this.handleError);
    }

    private extractEndTime(res: Response) {
        console.log('{ScsTscService}', '[extractEndTime]', res);
        let jsonObj = res.json();
        console.log('{ScsTscService}', '[extractEndTime]', jsonObj.response.endTime);

        return jsonObj.response.endTime || {};
    }


    //
    //  getFilter
    //
    public getFilter(taskName: string) : Observable<string> {
        let url = this.urlScsTsc + ScsTscDef.GET_FILTER + taskName;
        return this.http.get(url).map(this.extractFilter).catch(this.handleError);
    }

    private extractFilter(res: Response) {
    //    console.log('{ScsTscService}', '[extractFilter]', res);
        let jsonObj = res.json();
    //    console.log('{ScsTscService}', '[extractFilter]', jsonObj.response.filter);

        return jsonObj.response.filter || {};
    }

    private handleError(error: Response | any) {
        console.error('{ScsTscService}', '[handleError]', error);
        return Promise.reject(error.message || error);
    }


    //
    // setFilter
    //
    public setFilter(taskName:string, filter:string): Observable<string[]> {
        let url = this.urlScsTsc + ScsTscDef.SET_FILTER + taskName + ScsTscDef.SET_FILTER_PARAM + filter + ScsTscDef.CLIENT_PARAM;
        console.debug('{ScsTscService}', '[setFilter]', 'taskName=', taskName, ' filter=', filter, 'url=', url);       
        return this.http.get(url).map(res => res.json()).catch(this.handleError);
    }


    //
    // setEnableFlag
    //
    public enableTask(taskName:string, enableFlag:number): Observable<string[]> {
        let url: string;

        console.log('{ScsTscService}', '[enableTask]', 'taskName=', taskName, ' enableFlag=', enableFlag);

        if (enableFlag) {
            url = this.urlScsTsc + ScsTscDef.ENABLE_TASK + taskName + ScsTscDef.CLIENT_PARAM;
        } else {
            url = this.urlScsTsc + ScsTscDef.DISABLE_TASK + taskName  + ScsTscDef.CLIENT_PARAM;
        }
        return this.http.get(url).map(res => res.json()).catch(this.handleError);
    }


    //
    // setDescription
    //
    public setDescription(taskName:string, description:string): Observable<string[]> {
        
        let url = this.urlScsTsc + ScsTscDef.SET_DESCRIPTION + taskName + ScsTscDef.SET_DESCRIPTION_PARAM + description + ScsTscDef.CLIENT_PARAM;
        console.log('{ScsTscService}', '[setDescription]', 'taskName=', taskName, ' description=', description, 'url=', url);
        return this.http.get(url).map(res => res.json()).catch(this.handleError);
    }

    // public getFilters(taskNames: string[]): Observable<string> [] {
    //     return Observable.forkJoin(this.getFiltersObservables(taskNames)).map((data: string[]) => { return data; });
    // }

    // public getFiltersObservables(taskNames: string []): Observable<string> [] {
    //     console.log('{ScsTscService}', '[getDescFilterEnableObservables]', taskNames);
    //     let obs : Observable<string> [] = new Array<Observable<string>>() ;
    //     for (let taskname of taskNames) {
    //         obs.push(this.http.get(this.urlScsTsc + ScsTscDef.GET_FILTER + taskname).map(this.extractFilter));
    //     }
    //     return obs;
    // }
}
