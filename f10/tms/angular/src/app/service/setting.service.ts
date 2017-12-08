import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { AppSettings } from '../app-settings';
import { Observable } from 'rxjs/Observable';
import { Http} from '@angular/http';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable()
export class SettingService {

  readonly c = SettingService.name;

  // Observable source
  private settingSource = new BehaviorSubject<string>('');

  // Observable cardItem stream
  settingItem = this.settingSource.asObservable();

  private settings: Map<string, any> = new Map<string, any>();
  getSetting(url: string): any {
    const f = 'settingChanged';
    console.log(this.c, f, 'url', url);
    return this.settings.get(url);
  }

  // Service command
  settingChanged(str: string) {
    const f = 'settingChanged';
    console.log(this.c, f);
    this.settingSource.next(str);
  }

  constructor(
    private http: Http
  ) {}

  // retriveSetting(url: string): void {
  //   const f = 'retriveSetting';
  //   console.log(this.c, f, 'url', url);
  //   this.httpClient.get(
  //     url
  //   ).subscribe(
  //       ( res: string )=> {
  //         this.settings.set(url, res);
  //         this.settingChanged(url);
  //       }
  //       , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(f, err); }
  //       , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The GET observable is now completed.'); }
  //   );
  // }

  load(): Promise<any> {
    return this.http.get(AppSettings.STR_SETTINGS_URL)
    .toPromise()
    .then(
      data => {
        this.settings.set(AppSettings.STR_SETTINGS_URL, data.json());
      }
    ).catch((err: HttpErrorResponse) => {
      if (err.error instanceof Error) {
        // A client-side or network error occurred. Handle it accordingly.
        console.error('An error occurred:', err.error.message);
      } else {
        // The backend returned an unsuccessful response code.
        // The response body may contain clues as to what went wrong,
        console.error(`Backend returned code ${err.status}, body was: ${err.error}`);
      }

      // ...optionally return a default fallback value so app can continue (pick one)
      // which could be a default value
      // return Observable.of({my: "default value..."});
      // or simply an empty observable
      return Observable.empty();
    });
  }

}
