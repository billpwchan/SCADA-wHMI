import { Injectable } from '@angular/core';
import {Http, Response, RequestOptions, Headers} from '@angular/http';
import 'rxjs/add/operator/map';

import {Observable} from "rxjs";
import {OpmFunction, OpmLocation, OpmAction, OpmProfile, OpmMask} from "./model";

@Injectable()
export class OpmService {

  private opmMgrUrl: string;
  private functions: Observable<OpmFunction[]>;

  constructor(private http: Http) {
    this.opmMgrUrl = "opm/";
    this.functions = null;
  }

  public getFunctions(): Observable<OpmFunction[]> {
    this.functions = this.http.get(this.opmMgrUrl + "functions")
                .map(this.extractFunctionData).catch(this.handleError);

    return this.functions;
  }

  public getLocations(): Observable<OpmLocation[]> {
    return this.http.get(this.opmMgrUrl + "locations")
      .map(this.extractLocationData)
      .catch(this.handleError);
  }

  public getActions(): Observable<OpmAction[]> {
    return this.http.get(this.opmMgrUrl + "actions")
      .map(this.extractActionData)
      .catch(this.handleError);
  }

  public getProfiles(): Observable<OpmProfile[]> {
    return this.http.get(this.opmMgrUrl + "profiles")
      .map(this.extractProfileData)
      .catch(this.handleError);
  }

  public getMasks(): Observable<OpmMask[]> {
    return this.http.get(this.opmMgrUrl + "masks")
      .map(this.extractMaskData)
      .catch(this.handleError);
  }

  private extractFunctionData(res: Response) {
    let body = res.json();

    let data = body.map(d => {return new OpmFunction(d);});
    data.push(new OpmFunction(null));
    return data;
  }

  private extractLocationData(res: Response) {
    let body = res.json();

    let data = body.map(d => {return new OpmLocation(d);})
    data.push(new OpmLocation(null));
    return data;
  }

  private extractActionData(res: Response) {
    let body = res.json();

    let data =  body.map(d => {return new OpmAction(d);})
    data.push(new OpmAction(null));
    return data;
  }

  private extractProfileData(res: Response) {
    let body = res.json();

    let data =  body.map(d => {return new OpmProfile(d);})
    data.push(new OpmProfile(null));
    return data;
  }

  private extractMaskData(res: Response) {
    let body = res.json();

    let data =  body.map(d => {return new OpmMask(d);})
    data.push(new OpmMask(null));
    return data;
  }

  private handleError (error: Response | any) {
    // In a real world app, we might use a remote logging infrastructure
    let errMsg: string;
    console.log(error.toString());
    if (error instanceof Response) {
      const body = error.json() || '';
      const err = body.error || JSON.stringify(body);
      const msg = body.message || '';
      if (error.status == 0) {
          errMsg = `${error.status} - Connection Refused`;
      } else {
          errMsg = `${error.status} - ${error.statusText || ''} ${err}:${msg}`;
      }
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    console.error(errMsg);
    return Observable.throw(errMsg);
  }

  public deleteFunction(id: number) {
    console.info("Delete function " + id + this.opmMgrUrl + "functions/" + id);
    return this.http.delete(this.opmMgrUrl + "functions/" + id).catch(this.handleError);
  }

  public deleteLocation(id: number) {
    console.info("Delete location " + id + this.opmMgrUrl + "locations/" + id);
    return this.http.delete(this.opmMgrUrl + "locations/" + id).catch(this.handleError);
  }

  public deleteProfile(id: number) {
    console.info("Delete profile " + id + this.opmMgrUrl + "profiles/" + id);
    return this.http.delete(this.opmMgrUrl + "profiles/" + id).catch(this.handleError);
  }

  public deleteAction(id: number) {
    console.info("Delete action " + id + this.opmMgrUrl + "actions/" + id);
    return this.http.delete(this.opmMgrUrl + "actions/" + id).catch(this.handleError);
  }

  public deleteMask(id: number) {
    console.info("Delete mask " + id + this.opmMgrUrl + "masks/" + id);
    return this.http.delete(this.opmMgrUrl + "masks/" + id).catch(this.handleError);
  }

  public updateFunction(data: OpmFunction) {
    console.info("Update function: " + data.toString());
    return this.http.put(this.opmMgrUrl + "functions/" + data.id, data).catch(this.handleError);
  }

  public updateLocation(data: OpmLocation) {
    console.info("Update location: " + data.toString());
    return this.http.put(this.opmMgrUrl + "locations/" + data.id, data).catch(this.handleError);
  }

  public updateAction(data: OpmAction) {
    console.info("Update action: " + data.toString());
    return this.http.put(this.opmMgrUrl + "actions/" + data.id, data).catch(this.handleError);
  }

  public updateProfile(data: OpmProfile) {
    console.info("Update profile: " + data.toString());
    return this.http.put(this.opmMgrUrl + "profiles/" + data.id, data).catch(this.handleError);
  }

  public updateMask(data: OpmMask) {
    console.info("Update mask: " + data.toString());
    return this.http.put(this.opmMgrUrl + "masks/" + data.id, data).catch(this.handleError);
  }

  public createFunction(data: OpmFunction) {
    console.info("Create function: " + data.toString());
    let headers      = new Headers({ 'Content-Type': 'application/json' });
    let options       = new RequestOptions({ headers: headers });

    return this.http.post(this.opmMgrUrl + "functions", data, options).catch(this.handleError);
  }

  public createLocation(data: OpmLocation) {
    console.info("Create location: " + data.toString());
    let headers      = new Headers({ 'Content-Type': 'application/json' });
    let options       = new RequestOptions({ headers: headers });

    return this.http.post(this.opmMgrUrl + "locations", data, options).catch(this.handleError);
  }

  public createAction(data: OpmAction) {
    console.info("Create action: " + data.toString());
    let headers      = new Headers({ 'Content-Type': 'application/json' });
    let options       = new RequestOptions({ headers: headers });

    return this.http.post(this.opmMgrUrl + "actions", data, options).catch(this.handleError);
  }

  public createProfile(data: OpmProfile) {
    console.info("Create profile: " + data.toString());
    let headers      = new Headers({ 'Content-Type': 'application/json' });
    let options       = new RequestOptions({ headers: headers });

    return this.http.post(this.opmMgrUrl + "profiles", data, options).catch(this.handleError);
  }

  public createMask(data: OpmMask) {
    console.info("Create mask: " + data.toString());
    let headers      = new Headers({ 'Content-Type': 'application/json' });
    let options       = new RequestOptions({ headers: headers });

    return this.http.post(this.opmMgrUrl + "masks", data, options).catch(this.handleError);
  }

  public getProfile(id: number) {
    return this.http.get(this.opmMgrUrl + "profiles/" + id)
      .map(this.extractProfileDetailData)
      .catch(this.handleError);
  }

  private extractProfileDetailData(res: Response) {
    let body = res.json();

    return new OpmProfile(body);

  }
}
