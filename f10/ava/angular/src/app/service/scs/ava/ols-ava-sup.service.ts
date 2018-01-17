import { Injectable } from '@angular/core';
import { OlsSettings } from '../ols-settings';
import { AppSettings } from '../../../app-settings';
import { HttpErrorResponse, HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { UtilsHttpModule } from '../../utils-http/utils-http.module';

export class AvaSupPoint {
  geo: number;
  func: number;
}

@Injectable()
export class OlsAvaSupService {

  readonly c = 'OlsAvaSupService';

  // Observable source
  private olsSource = new BehaviorSubject<string>('');

  // Observable cardItem stream
  avaSupItem = this.olsSource.asObservable();

  private points: Map<string, Map<string, AvaSupPoint>> = new Map<string, Map<string, AvaSupPoint>>();
  private pointsData: Map<string, any> = new Map<string, any>();

  // Service command
  olsChanged(str: string) {
    const f = 'olsChanged';
    console.log(this.c, f);
    this.olsSource.next(str);
  }

  constructor(
    private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
  ) {}

  public getUnivname(env: string): string[] {
    const f = 'getUnivname';
    console.log(this.c, f);
    const ret = new Array<string>();
    const univnames: Map<string, AvaSupPoint> = this.points.get(env);
    if ( null != univnames ) {
      univnames.forEach ( (value: AvaSupPoint, key: string) => {
        ret.push(key);
      });
    } else {
      console.warn(this.c, f, 'univnames IS NULL');
    }
    return ret;
  }

  public getAvaSupPoint(env: string, univname: string): AvaSupPoint {
    const f = 'getAvaSupPoint';
    console.log(this.c, f);
    let avaSupPoint: AvaSupPoint = null;
    const items: Map<string, AvaSupPoint> = this.points.get(env);
    if ( null != items ) {
      avaSupPoint = items.get(univname);
    } else {
      console.warn(this.c, f, 'items IS NULL');
    }
    return avaSupPoint;
  }

  prepareUrl(connAddr: string): string {
    const f = 'prepareUrl';
    console.log(this.c, f);

    let url: string = connAddr;
    url += OlsSettings.STR_URL_READOLSLIST + OlsSettings.STR_LST_SERVER;
    url += '&' + OlsSettings.STR_URL_LISTNAME + '=' + OlsSettings.STR_SCADAGEN_AVA_SUP;

    return url;
  }

  retriveAvaSup(env: string, url: string): void {
    const f = 'retriveAvaSup';
    console.log(this.c, f, 'env', env, 'url', url);

    this.points.set(env, new Map<string, AvaSupPoint>());

    this.httpClient.get(
      url
    )
      .subscribe(
        (res: any[]) => {
          console.log(this.c, f, 'subscribe');

          const json = res;

          const parameter = json[OlsSettings.STR_ATTR_PARAMETERS];
          const listServer = parameter[OlsSettings.STR_ATTR_LISTSERVER];
          const listName = parameter[OlsSettings.STR_ATTR_LISTNAME];
          const data = json[AppSettings.STR_RESPONSE][OlsSettings.STR_ATTR_DATA];
          const length = data.length;

          this.pointsData.set(env, data);

          this.pointsData.get(env).forEach((item, index) => {
            console.log(this.c, f, 'data', index, item);

            const geo: number = item[OlsSettings.STR_ATTR_GEO];
            const func: number = item[OlsSettings.STR_ATTR_FUNC];
            const univname: string = item[OlsSettings.STR_ATTR_UNIVNAME];

            console.log(this.c, f, 'geo', geo, 'func', func, 'univname', univname);

            const point: AvaSupPoint = new AvaSupPoint();
            point.func = func;
            point.geo = geo;

            this.points.get(env).set(univname, point);
          });

          this.olsChanged(env);
        }
        , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(f, err); }
        , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The GET observable is now completed.'); }
    );
  }

}
