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
        return Observable.forkJoin(this.getDescFilterEnableObservables(taskNames)).map((data: any[]) => data );
    }
    private getDescFilterEnableObservables(taskNames: string []): Array<Observable<string>> {
        console.log('{ScsTscService}', '[getDescFilterEnableObservables]', taskNames);
        const obs: Array<Observable<string>> = new Array<Observable<string>>() ;
        for (const taskname of taskNames) {
            obs.push(this.http.get(this.urlScsTsc + ScsTscDef.GET_DESCRIPTION + encodeURIComponent(taskname)).map(this.extractDescription));
            obs.push(this.http.get(this.urlScsTsc + ScsTscDef.GET_FILTER + encodeURIComponent(taskname)).map(this.extractFilter));
            obs.push(this.http.get(this.urlScsTsc + ScsTscDef.IS_ENABLED + encodeURIComponent(taskname)).map(this.extractEnableFlag));
        }
        return obs;
    }
    private extractEnableFlag(res: Response) {
    //    console.log('{ScsTscService}', '[extractEnableFlag]', res);
        const jsonObj = res.json();
    //    console.log('{ScsTscService}', '[extractEnableFlag]', jsonObj.response.status);
        return jsonObj.response.status;
    }
    //
    //  getTaskNames
    //
    public getTaskNames(): Observable<string[]> {
        console.log('{ScsTscService}', '[getTaskNames] begin');
        const url = this.urlScsTsc + ScsTscDef.GET_TASK_NAMES;
        return this.http.get(url)
            .map(this.extractTaskNames).catch(this.handleError);
    }
    private extractTaskNames(res: Response) {
        console.log('{ScsTscService}', '[extractTaskNames]', res);
        const jsonObj = res.json();
        console.log('{ScsTscService}', '[extractTaskNames]', jsonObj.response.taskNames);
        return jsonObj.response.taskNames || {};
    }
    //
    //  getDescription
    //
    public getDescription(taskName: string): Observable<string> {
        const url = this.urlScsTsc + ScsTscDef.GET_DESCRIPTION + encodeURIComponent(taskName);
        return this.http.get(url).map(this.extractDescription).catch(this.handleError);
    }
    private extractDescription(res: Response) {
    //    console.log('{ScsTscService}', '[extractTaskDescription]', res);
        const jsonObj = res.json();
        console.log('{ScsTscService}', '[extractTaskDescription]', jsonObj.response.description);
        return jsonObj.response.description || {};
    }
    //
    //  getStartTime
    //
    public getStartTime(taskName: string): Observable<string> {
        const url = this.urlScsTsc + ScsTscDef.GET_START_TIME + encodeURIComponent(taskName);
        return this.http.get(url).map(this.extractStartTime).catch(this.handleError);
    }
    private extractStartTime(res: Response) {
        console.log('{ScsTscService}', '[extractStartTime]', res);
        const jsonObj = res.json();
        console.log('{ScsTscService}', '[extractStartTime]', jsonObj.response.startTime);
        return jsonObj.response.startTime || {};
    }
    //
    //  getEndTime
    //
    public getEndTime(taskName: string): Observable<string> {
        const url = this.urlScsTsc + ScsTscDef.GET_END_TIME + encodeURIComponent(taskName);
        return this.http.get(url).map(this.extractEndTime).catch(this.handleError);
    }
    private extractEndTime(res: Response) {
        console.log('{ScsTscService}', '[extractEndTime]', res);
        const jsonObj = res.json();
        console.log('{ScsTscService}', '[extractEndTime]', jsonObj.response.endTime);
        return jsonObj.response.endTime || {};
    }
    //
    //  getFilter
    //
    public getFilter(taskName: string): Observable<string> {
        const url = this.urlScsTsc + ScsTscDef.GET_FILTER + encodeURIComponent(taskName);
        return this.http.get(url).map(this.extractFilter).catch(this.handleError);
    }
    private extractFilter(res: Response) {
    //    console.log('{ScsTscService}', '[extractFilter]', res);
        const jsonObj = res.json();
        console.log('{ScsTscService}', '[extractFilter]', jsonObj.response.filter);
        return jsonObj.response.filter || {};
    }
    private handleError(error: Response | any) {
        console.error('{ScsTscService}', '[handleError]', error);
        return Promise.reject(error.message || error);
    }
    //
    // setFilter
    //
    public setFilter(taskName: string, filter: string, clientName: string): Observable<any> {
        const quotedFilter = '"' + filter + '"';
        const quotedclientName = '"' + clientName + '"';
        const url = this.urlScsTsc + ScsTscDef.SET_FILTER + encodeURIComponent(taskName) + ScsTscDef.SET_FILTER_PARAM + quotedFilter +
                    ScsTscDef.CLIENT_PARAM + quotedclientName;
        console.log('{ScsTscService}', '[setFilter]', 'taskName=', taskName, ' filter=', filter, 'url=', url);
        return this.http.get(url).map(res => res.json()).catch(this.handleError);
    }
    //
    // setEnableFlag
    //
    public enableTask(taskName: string, enableFlag: number, clientName: string): Observable<any> {
        let url: string;
        const quotedclientName = '"' + clientName + '"';
        console.log('{ScsTscService}', '[enableTask]', 'taskName=', taskName, ' enableFlag=', enableFlag);
        if (enableFlag) {
            url = this.urlScsTsc + ScsTscDef.ENABLE_TASK + encodeURIComponent(taskName) + ScsTscDef.CLIENT_PARAM + quotedclientName;
        } else {
            url = this.urlScsTsc + ScsTscDef.DISABLE_TASK + encodeURIComponent(taskName)  + ScsTscDef.CLIENT_PARAM + quotedclientName;
        }
        return this.http.get(url).map(res => res.json()).catch(this.handleError);
    }
    //
    // setDescription
    //
    public setDescription(taskName: string, description: string, clientName: string): Observable<string[]> {
        const quotedclientName = '"' + clientName + '"';
        const url = this.urlScsTsc + ScsTscDef.SET_DESCRIPTION + encodeURIComponent(taskName) + ScsTscDef.SET_DESCRIPTION_PARAM +
                    encodeURIComponent(description) + ScsTscDef.CLIENT_PARAM + quotedclientName;
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
    //
    // getDates
    //
    public getDates(dayGroupId: string): Observable<string[]> {
        const url = this.urlScsTsc + ScsTscDef.GET_DATES + dayGroupId;
        console.log('{ScsTscService}', '[getDates]', 'dayGroupId=', dayGroupId, 'url=', url);
        return this.http.get(url).map(this.extractDatesList).catch(this.handleError);
    }
    private extractDatesList(res: Response) {
        console.log('{ScsTscService}', '[extractDatesList]', res);
        const jsonObj = res.json();
        console.log('{ScsTscService}', '[extractDatesList]', jsonObj.response.datesList);
        return jsonObj.response.datesList || {};
    }
    public setDates(dayGroupId: string, datesList: string[], clientName: string): Observable<string[]> {
        let dates = '';
        if (datesList.length > 0) {
            for (const d of datesList) {
                if (dates.length > 0) {
                    dates = dates + ',' + d;
                } else {
                    dates = '[' + d;
                }
            }
            dates = dates + ']';
        }
        const quotedclientName = '"' + clientName + '"';
        const url = this.urlScsTsc + ScsTscDef.SET_DATES + dayGroupId + ScsTscDef.SET_DATES_PARAM + dates + ScsTscDef.CLIENT_PARAM + quotedclientName;
        console.log('{ScsTscService}', '[setDates]', 'taskName=', dayGroupId, ' datesList=', datesList, 'url=', url);
        return this.http.get(url).map(res => res.json()).catch(this.handleError);
    }
    public getDayGroups(): Observable<any[]> {
        const url = this.urlScsTsc + ScsTscDef.GET_DAYGROUPS;
        return this.http.get(url).map(this.extractDayGroup).catch(this.handleError);
    }
    private extractDayGroup(res: Response) {
        console.log('{ScsTscService}', '[extractDayGroup]', res);
        const jsonObj = res.json();
        console.log('{ScsTscService}', '[extractDayGroup]', jsonObj.response.dayGroup);
        return jsonObj.response.dayGroup || {};
    }
}
