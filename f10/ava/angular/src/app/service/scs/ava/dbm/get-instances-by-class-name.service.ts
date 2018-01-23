import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { UtilsHttpModule } from '../../../utils-http/utils-http.module';
import { AppSettings } from '../../../../app-settings';
import { DbmSettings } from '../../dbm-settings';

@Injectable()
export class GetInstancesByClassNameService {

  readonly c = 'GetInstancesByClassNameService';

  // Observable source
  private dbmSource = new BehaviorSubject<string>('');

  // Observable cardItem stream
  dbmItem = this.dbmSource.asObservable();

  data: Map<string, Map<string, string[]>> = new Map<string, Map<string, string[]>>();

  // Service command
  dbmChanged(str: string) {
    const f = 'dbmChanged';
    console.log(this.c, f);
    this.dbmSource.next(str);
  }

  constructor(
    private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
  ) { }

  getData(env: string, className: string): string[] {
    return this.data.get(env).get(className);
  }

  readData(env: string, className: string) {
    const f = 'readData';
    console.log(this.c, f);

    let url = null;

    url = env
         + DbmSettings.STR_URL_GET_INSTANCES_BY_CLASSNAME
         + className;

    console.log(this.c, f, 'url', url);

    this.httpClient.get(
      url
    )
      .subscribe(
        (res: any[]) => {
          console.log(this.c, f, res);
          const json = res;
          const instances = json[AppSettings.STR_RESPONSE][DbmSettings.STR_ATTR_INSTANCES] as string[];
          console.log(this.c, f, 'env', env, 'url', url);
          if ( null == this.data.get(env) ) {
            this.data.set(env, new Map<string, string[]>());
          }
          this.data.get(env).set(className, instances);

          this.dbmChanged(env);
        }
        , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(f, err); }
        , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The GET observable is now completed.'); }
    );
  }

}
