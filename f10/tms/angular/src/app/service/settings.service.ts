import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { AppSettings } from '../app-settings';
import { Observable } from 'rxjs/Observable';
import { Http} from '@angular/http';
import { HttpErrorResponse, HttpClient } from '@angular/common/http';
import { UtilsHttpModule } from './utils-http/utils-http.module';

@Injectable()
export class SettingsService {

  readonly c = SettingsService.name;

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

  getSetting(c: string, func: string, component: string, key: string, url: string = AppSettings.STR_URL_SETTINGS): any {
    const f = 'getSetting';
    let value;
    try {
      value = this.settings.get(url)[component][key];
      console.log(this.c, f, 'Loading setting for', c, func, url, component, key, value);
    } catch (err) {
      console.log(this.c, f, 'Error when loading setting for', c, func, url, component, key, err);
    }
    return value;
  }

  getSettings(url: string = AppSettings.STR_URL_SETTINGS): any {
    const f = 'getSettings';
    console.log(this.c, f, 'url', url);
    return this.settings.get(url);
  }

  // Aync Loading
  retriveSetting(url: string): void {
    const f = 'retriveSetting';
    console.log(this.c, f, 'url', url);
    this.httpClient.get(
      url
    ).subscribe(
        ( res: string ) => {
          this.settings.set(url, res);
          this.settingChanged(url);
        }
        , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(f, err); }
        , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The GET observable is now completed.'); }
    );
  }

  // Sync Loading
  load(): Promise<any> {
    const f = 'load';
    console.log(this.c, f);
    return this.http.get(AppSettings.STR_URL_SETTINGS)
    .toPromise()
    .then(
      data => {
        console.log(this.c, f, 'success');
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
