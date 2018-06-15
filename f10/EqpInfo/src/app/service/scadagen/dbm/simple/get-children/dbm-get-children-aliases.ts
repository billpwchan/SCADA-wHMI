import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { AppSettings } from '../../../../../app-settings';
import { UtilsHttpModule } from '../../../common/utils-http.module';
import { DbmSettings } from '../../../../scadagen/dbm/dbm-settings';
import { DbmGetChildrenAliasesResult } from './dbm-get-children-aliases-settings';
import { EnvironmentMappingService } from '../../../envs/environment-mapping.service';

@Injectable()
export class DbmGetChildrenAliases {

  readonly c = 'DbmGetChildrenAliases';

  // Observable source
  private dbmSource = new BehaviorSubject<DbmGetChildrenAliasesResult>(null);

  // Observable cardItem stream
  dbmItem = this.dbmSource.asObservable();

  data: Map<string, Map<string, string[]>> = new Map<string, Map<string, string[]>>();

  // Service command
  dbmChanged(result: DbmGetChildrenAliasesResult) {
    const f = 'dbmChanged';
    console.log(this.c, f);
    this.dbmSource.next(result);
  }

  constructor(
    private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
    , private environmentMappingService: EnvironmentMappingService
  ) { }

  private getData(env: string, className: string): string[] {
    return this.data.get(env).get(className);
  }

  readData(alias: string, dbAddress: string) {
    const f = 'readData';
    console.log(this.c, f);

    const env = this.environmentMappingService.getEnv(alias);
    console.log(this.c, f, 'alias', alias, 'env', env);
    let url = null;

    url = env
         + DbmSettings.STR_URL_GET_CHILDREN_ALIASES
         + dbAddress;

    console.log(this.c, f, 'url', url);

    // Get Label and Value
    this.httpClient.get(
      url
    )
      .subscribe(
        (res: any[]) => {
          console.log(this.c, f, res);
          const json = res;
          const childrenAliases = json[AppSettings.STR_RESPONSE][DbmSettings.STR_ATTR_CHILDREN_ALIASES];
          console.log(this.c, f, 'env', env, 'url', url);
          if ( null == this.data.get(env) ) {
            this.data.set(env, new Map<string, string[]>());
          }
          this.data.get(env).set(dbAddress, childrenAliases);

          const result: DbmGetChildrenAliasesResult = new DbmGetChildrenAliasesResult();
          result.env = env;
          result.dbAddress = dbAddress;
          result.data = childrenAliases;
          this.dbmChanged(result);
        }
        , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(f, err); }
        , () => { this.utilsHttp.httpClientHandlerComplete(f); }
    );
  }
}
