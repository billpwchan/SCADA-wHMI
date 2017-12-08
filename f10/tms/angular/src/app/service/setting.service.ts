import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { UtilsHttpModule } from './utils-http/utils-http.module';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

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
    private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
  ) {}

  retriveSettings(): void {
    const f = 'retriveSetting';
    console.log(this.c, f);

  }

  retriveSetting(url: string): void {
    const f = 'retriveSetting';
    console.log(this.c, f, 'url', url);

    this.httpClient.get(
      url
    )
      .subscribe(
        ( res: string )=> {
          console.log(this.c, f, 'subscribe', 'res', res);

          this.settings.set(url, res);

          this.settingChanged(url);
        }
        , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(f, err); }
        , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The GET observable is now completed.'); }
    );
  }
}
