import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { UtilsHttpModule } from '../scadagen/utils/utils-http.module';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { OlsSettings } from './ols-settings';
import { AppSettings } from '../../app-settings';

@Injectable()
export class OlsService {

  readonly c = 'OlsService';

  // Observable source
  private olsSource = new BehaviorSubject<string>('');

  // Observable cardItem stream
  olsItem = this.olsSource.asObservable();

  private eqps: Map<string, Map<number, number[]>> = new Map<string, Map<number, number[]>>();
  private pointData: Map<string, any> = new Map<string, any>();

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

  getEqps(): Map<string, Map<number, number[]>> { return this.eqps; }
  getPointData(): Map<string, any> { return this.pointData; }

  retriveEquipments(connAddr: string): void {
    const f = 'retriveEquipments';
    console.log(this.c, f);

    let url: string = connAddr;
    url += OlsSettings.STR_URL_READOLSLIST + OlsSettings.STR_LST_SERVER;
    url += '&' + OlsSettings.STR_URL_LISTNAME + '=' + OlsSettings.STR_SCADAGEN_DATA_POINT_MGN;

    this.retriveEquipment(connAddr, url);
  }

  retriveEquipment(env: string, url: string): void {
    const f = 'retriveEquipment';
    console.log(this.c, f, 'env', env, 'url', url);

    this.eqps.set(env, new Map<number, number[]>());

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

          this.pointData.set(env, data);

          this.pointData.get(env).forEach((item, index) => {
            // console.log(this.c, f, 'data', index, item);

            const geo: number = item[OlsSettings.STR_ATTR_GEO];
            const func: number = item[OlsSettings.STR_ATTR_FUNC];
            const eqplabel: string = item[OlsSettings.STR_ATTR_EQPLABEL];
            const pointlabel: string = item[OlsSettings.STR_ATTR_POINTLABEL];
            const univname: string = item[OlsSettings.STR_ATTR_UNIVNAME];

            // console.log(this.c, f, 'geo', geo, 'func', func, 'eqplabel', eqplabel, 'pointlabel', pointlabel, 'univname', univname);

            if ( undefined === this.eqps.get(env).get(geo)
            || null === this.eqps.get(env).get(geo) ) {
              this.eqps.get(env).set(geo, new Array());
              this.eqps.get(env).get(geo).push(func);
              // console.log(this.c, f, 'new env', env, 'geo', geo, 'func', func);
            } else {
              if ( -1 === this.eqps.get(env).get(geo).indexOf(func) ) {
                this.eqps.get(env).get(geo).push(func);
                // console.log(this.c, f, 'add env', env, 'geo', geo, 'func', func);
              }
            }
          });

          this.olsChanged(env);
        }
        , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(f, err); }
        , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The GET observable is now completed.'); }
    );
  }
}
