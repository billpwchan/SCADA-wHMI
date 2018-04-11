import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import { Observable } from 'rxjs/Rx';
import { ConfigService } from './config.service';
import { ScsTscDef } from './scs-tsc-def';
@Injectable()
export class ScsTscService {
    private urlScsTsc: string;
    private tscTimeOffset = 0;

    constructor(private http: Http, private configService: ConfigService) {

        // const httpParams = new HttpParams({ fromString: window.location.href });
        // this.urlScsTsc = httpParams.get('scs_tsc_url');
        // console.log('{ScsTscService}', '[constructor]', 'urlScsTsc =', this.urlScsTsc);

        this.tscTimeOffset = this.configService.config.getIn(['tsc_time_offset']);

        console.log('{ScsTscService}', '[constructor]', 'tscTimeOffset =', this.tscTimeOffset);
    }

    public setUrlScsTsc(urlScsTsc: string) {
        this.urlScsTsc = urlScsTsc;
        console.log('{ScsTscService}', '[setUrlScsTsc]', 'urlScsTsc =', this.urlScsTsc);
    }

    public getDescFilterEnable(taskNames: string[]): Observable<any> {
        return Observable.from(taskNames).concatMap( (taskName) => {
            return this.http.get(this.urlScsTsc + ScsTscDef.GET_DESCRIPTION + this.getEncodedURIComponent(taskName))
                .concat(this.http.get(this.urlScsTsc + ScsTscDef.GET_FILTER + this.getEncodedURIComponent(taskName)))
                .concat(this.http.get(this.urlScsTsc + ScsTscDef.IS_ENABLED + this.getEncodedURIComponent(taskName)))
                .map((res: any) => {
                    res.json()
                })
            });
    }
    private extractEnableFlag(res: Response) {
    //    console.log('{ScsTscService}', '[extractEnableFlag]', res);
        const jsonObj = res.json();
    //    console.log('{ScsTscService}', '[extractEnableFlag]', jsonObj.response.status);
        return jsonObj.response.status;
    }
    public getEncodedURIComponent(uriComponent: string): string {
        if (uriComponent) {
            return encodeURIComponent('"' + uriComponent + '"');
        }
        return uriComponent;
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
        const url = this.urlScsTsc + ScsTscDef.GET_DESCRIPTION + this.getEncodedURIComponent(taskName);
        return this.http.get(url).map(this.extractDescription).catch(this.handleError);
    }
    private extractDescription(res: Response) {
    //    console.log('{ScsTscService}', '[extractDescription]', res);
        const jsonObj = res.json();
        console.log('{ScsTscService}', '[extractDescription]', jsonObj.response.description);
        return jsonObj.response.description || {};
    }
    //
    //  getStartTime
    //
    public getStartTime(taskName: string): Observable<string> {
        const url = this.urlScsTsc + ScsTscDef.GET_START_TIME + this.getEncodedURIComponent(taskName);
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
        const url = this.urlScsTsc + ScsTscDef.GET_END_TIME + this.getEncodedURIComponent(taskName);
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
        const url = this.urlScsTsc + ScsTscDef.GET_FILTER + this.getEncodedURIComponent(taskName);
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
        const url = this.urlScsTsc + ScsTscDef.SET_FILTER + this.getEncodedURIComponent(taskName) + ScsTscDef.SET_FILTER_PARAM +
            this.getEncodedURIComponent(filter) + ScsTscDef.CLIENT_PARAM + this.getEncodedURIComponent(clientName);
        console.log('{ScsTscService}', '[setFilter]', 'taskName=', taskName, ' filter=', filter, 'url=', url);
        return this.http.get(url).map(res => res.json()).catch(this.handleError);
    }
    //
    // getEnableFlag
    //
    public getEnableFlag(taskName: string): Observable<string> {
        const url = this.urlScsTsc + ScsTscDef.IS_ENABLED + this.getEncodedURIComponent(taskName);
        return this.http.get(url).map(this.extractEnableFlag).catch(this.handleError);
    }
    //
    // setEnableFlag
    //
    public enableTask(taskName: string, enableFlag: number, clientName: string): Observable<any> {
        let url: string;
        console.log('{ScsTscService}', '[enableTask]', 'taskName=', taskName, ' enableFlag=', enableFlag);
        if (enableFlag) {
            url = this.urlScsTsc + ScsTscDef.ENABLE_TASK + this.getEncodedURIComponent(taskName) + ScsTscDef.CLIENT_PARAM +
                this.getEncodedURIComponent(clientName);
        } else {
            url = this.urlScsTsc + ScsTscDef.DISABLE_TASK + this.getEncodedURIComponent(taskName)  + ScsTscDef.CLIENT_PARAM +
                this.getEncodedURIComponent(clientName);
        }
        return this.http.get(url).map(res => res.json()).catch(this.handleError);
    }
    //
    // setDescription
    //
    public setDescription(taskName: string, description: string, clientName: string): Observable<string[]> {
        const url = this.urlScsTsc + ScsTscDef.SET_DESCRIPTION + this.getEncodedURIComponent(taskName) + ScsTscDef.SET_DESCRIPTION_PARAM +
                    this.getEncodedURIComponent(description) + ScsTscDef.CLIENT_PARAM + this.getEncodedURIComponent(clientName);
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
    private extractDatesList = (res: Response) => {
        console.log('{ScsTscService}', '[extractDatesList]', res);
        const jsonObj = res.json();
        const datesList = jsonObj.response.datesList;
        if (this.tscTimeOffset > 0) {
            this.subtractOffsetFromDateList(datesList);
        }
        console.log('{ScsTscService}', '[extractDatesList]', jsonObj.response.datesList, 'tscTimeOffset =', this.tscTimeOffset);
        return datesList || {};
    }
    public setDates(dayGroupId: string, datesList: string[], clientName: string): Observable<string[]> {
        let dates = '';
        if (datesList.length > 0) {
            if (this.tscTimeOffset > 0) {
                this.addOffsetToDateList(datesList);
            }
            for (const d of datesList) {
                if (dates.length > 0) {
                    dates = dates + ',' + d;
                } else {
                    dates = '[' + d;
                }
            }
            dates = dates + ']';
        } else {
            dates = '[86400]';  // Use 86400 instead of 0 to ensure time zone will not cause negative unix time
        }
        const url = this.urlScsTsc + ScsTscDef.SET_DATES + dayGroupId + ScsTscDef.SET_DATES_PARAM + dates + ScsTscDef.CLIENT_PARAM +
            this.getEncodedURIComponent(clientName);
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
    public addOffsetToDateList(dateList: string[]) {
        for (let i = 0; i < dateList.length; i++) {
            const timevalue = +dateList[i] + this.tscTimeOffset;
            dateList[i] = timevalue + '';
        }
    }
    public subtractOffsetFromDateList(dateList: string[]) {
        for (let i = 0; i < dateList.length; i++) {
            let timevalue = +dateList[i] - this.tscTimeOffset;
            if (timevalue < 0) {
                timevalue = 86400; // Use 86400 instead of 0 to ensure time zone will not cause negative unix time
            }
            dateList[i] = timevalue + '';
        }
        console.log('{ScsTscService}', '[subtractOffsetFromDateList] subtracted dateList', dateList);
    }
}
