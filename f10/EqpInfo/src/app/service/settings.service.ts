import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { AppSettings } from '../app-settings';
import { Observable } from 'rxjs/Observable';
import { Http } from '@angular/http';
import { HttpErrorResponse, HttpClient } from '@angular/common/http';
import { UtilsHttpModule } from './scadagen/common/utils-http.module';

@Injectable()
export class SettingsService {

  readonly c = 'SettingsService';

  // Observable source
  private settingSource = new BehaviorSubject<string>('');

  // Observable cardItem stream
  settingItem = this.settingSource.asObservable();

  private settings: Map<string, any> = new Map<string, any>();

  // Service command
  settingChanged(str: string) {
    const f = 'settingChanged';
    console.log(this.c, f);
    this.settingSource.next(str);
  }

  constructor(
    private http: Http
    , private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
  ) {
  }

  public getSettingIn(c: string, func: string, component: string, key: string, url: string = AppSettings.STR_URL_SETTINGS): any {
    const f = 'getSettingIn';
    let value = this.getSetting(c, func, component, key, url);
    const override = this.getSettingInUrl(key);
    if (override && override.length > 1) {
        value = override[1];
        console.log(this.c, f, 'Override by value=', value);
    }
    return value;
  }
  public getSettingInUrl(sParam) {
    const f = 'getSettingInUrl';
    console.log(this.c, f, 'sParam=', sParam);
    let param = null;
    const params: string[] = decodeURIComponent(window.location.href).split('?');
    if (params && params.length > 1) {
        param = params[1].split('&')
        .map((v) => v.split('='))
        .filter((v) => (v[0] === sParam) ? true : false)
        .reduce((prev, curv, index, array) => curv, undefined);
    }
    console.log(this.c, f, 'sParam[' + sParam + '] param[' + param + ']');
    return param;
  }

  getSetting(c: string, func: string, component: string, key: string, url: string = AppSettings.STR_URL_SETTINGS): any {
    const f = 'getSetting';
    let ret;
    try {
      ret = this.settings.get(url)[component][key];
      console.log(this.c, f, 'Loading setting for', c, func, url, component, key, ret);

      if ( undefined === ret ) {
        console.warn(this.c, f, 'Loading setting for', c, func, url, component, key, ret, 'failed!');
      }
    } catch (err) {
      console.warn(this.c, f, 'Error when loading setting for', c, func, url, component, key, err);
    }
    console.log(this.c, f, 'Loading component[' + component + '] key[' + key + '] => ret[' + ret + ']');
    return ret;
  }

  getSettings(url: string = AppSettings.STR_URL_SETTINGS): any {
    const f = 'getSettings';
    console.log(this.c, f, 'url', url);
    return this.settings.get(url);
  }

  // Sync Loading
  load(): Promise<any> {
    const f = 'load';
    console.log(this.c, f);
    console.log(this.c, f, 'loading config...');
    return this.http.get(AppSettings.STR_URL_SETTINGS)
    .toPromise()
    .then(
      data => {
        console.log(this.c, f, 'loading config success');
        this.settings.set(AppSettings.STR_URL_SETTINGS, data.json());
      }
    ).catch((err: HttpErrorResponse) => {
      if (err.error instanceof Error) {
        // A client-side or network error occurred. Handle it accordingly.
        console.error(this.c, f, 'An error occurred:', err.error.message);
      } else {
        // The backend returned an unsuccessful response code.
        // The response body may contain clues as to what went wrong,
        console.error(this.c, f, `Backend returned code ${err.status}, body was: ${err.error}`);
      }
    });
  }

}
