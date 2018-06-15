import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { UtilsHttpModule } from '../../../common/utils-http.module';
import { AppSettings } from '../../../../../app-settings';
import { DbmSettings } from '../../../../scadagen/dbm/dbm-settings';
import { EnvironmentMappingService } from '../../../envs/environment-mapping.service';

@Injectable()
export class GetInstancesByClassName {

  readonly c = 'GetInstancesByClassName';

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
    , private environmentMappingService: EnvironmentMappingService
  ) { }

  private getData(env: string, className: string): string[] {
    return this.data.get(env).get(className);
  }

  readData(alias: string, className: string) {
    const f = 'readData';
    console.log(this.c, f);

    let url = null;

    const env = this.environmentMappingService.getEnv(alias);
    console.log(this.c, f, 'alias', alias, 'env', env);

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
        , () => { this.utilsHttp.httpClientHandlerComplete(f); }
    );
  }

}
