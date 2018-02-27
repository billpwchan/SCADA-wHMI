import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { UtilsHttpModule } from './../utils-http/utils-http.module';
import { SettingsService } from '../settings.service';
import { CommonSettings } from './common-settings';


@Injectable()
export class ScadagenReplayService {

  readonly c = 'ScadagenReplayService';

  readonly STR_URL_RPL_GETINFO = '/scs/service/ReplayComponent/GetInfo';
  readonly STR_URL_RPL_INIT = '/scs/service/ReplayComponent/Init?startDate=';
  readonly STR_URL_RPL_START = '/scs/service/ReplayComponent/Start?speed=';
  readonly STR_URL_RPL_STOP = '/scs/service/ReplayComponent/Stop';
  readonly STR_QUESTION_MARK = '?';
  readonly STR_AMPERSAND = '&';
  readonly STR_EQUAL = '=';
  readonly STR_QUOTE = '\"';

  constructor(
    private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
    , private settingsService: SettingsService
  ) { }

  public getInfo(): Observable<any> {
    const f = 'getInfo';
    console.log(this.c, f);

    const connAddr = this.settingsService.getSetting(this.c, f, this.c, CommonSettings.STR_CONN_ADDR);

    const url = connAddr + this.STR_URL_RPL_GETINFO;

    return this.httpClient.get(url);
  }

  public initReplay(startDate: number): Observable<any> {
    const f = 'initReplay';
    console.log(this.c, f);

    const connAddr = this.settingsService.getSetting(this.c, f, this.c, CommonSettings.STR_CONN_ADDR);

    const url = connAddr + this.STR_URL_RPL_INIT + startDate;

    return this.httpClient.get(url);
  }

  public start(speed: number): Observable<any> {
    const f = 'start';
    console.log(this.c, f);

    const connAddr = this.settingsService.getSetting(this.c, f, this.c, CommonSettings.STR_CONN_ADDR);

    const url = connAddr + this.STR_URL_RPL_START + speed;

    return this.httpClient.get(url);
  }

  public stop(): Observable<any> {
    const f = 'stop';
    console.log(this.c, f);

    const connAddr = this.settingsService.getSetting(this.c, f, this.c, CommonSettings.STR_CONN_ADDR);

    const url = connAddr + this.STR_URL_RPL_STOP;

    return this.httpClient.get(url);
  }

  public sendRestRequest(request: string, params: Map<string, string|number>) {
    const f = 'sendRestRequest';
    console.log(this.c, f);
    let url = request;

    if (params.size > 0) {
      let cnt = 0;
      params.forEach( (value, key) => {

        if (cnt > 0) {
          if (typeof value === 'string') {
            console.log(this.c, f, 'typeof ', value, 'is string');
            url = url + this.STR_AMPERSAND + key + this.STR_EQUAL + this.STR_QUOTE + value + this.STR_QUOTE;
          } else {
            url = url + this.STR_AMPERSAND + key + this.STR_EQUAL + value;
          }
          console.log(this.c, f, 'url =', url);
        } else {
          if (typeof value === 'string') {
            console.log(this.c, f, 'typeof ', value, 'is string');
            url = url + this.STR_QUESTION_MARK + key + this.STR_EQUAL + this.STR_QUOTE + value + this.STR_QUOTE;
          } else {
            url = url + this.STR_QUESTION_MARK + key + this.STR_EQUAL + value;
          }
          console.log(this.c, f, 'url =', url);
        }
        cnt++;
      });
    }

    this.httpClient.get(url).subscribe(res => {
      console.log(this.c, f, res);
    });
  }

}
