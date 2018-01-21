import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { UtilsHttpModule } from '../../../utils-http/utils-http.module';
import { DbmSettings } from '../../dbm-settings';
import { AppSettings } from '../../../../app-settings';

export class GetChildrenAliasesResult {
  public env: string;
  public dbAddress: string;
  public data: string[];
}

@Injectable()
export class GetChildrenAliasesService {

  readonly c = 'GetChildrenAliasesService';

  // Observable source
  private dbmSource = new BehaviorSubject<GetChildrenAliasesResult>(null);

  // Observable cardItem stream
  dbmItem = this.dbmSource.asObservable();

  data: Map<string, Map<string, string[]>> = new Map<string, Map<string, string[]>>();

  // Service command
  dbmChanged(result: GetChildrenAliasesResult) {
    const f = 'dbmChanged';
    console.log(this.c, f);
    this.dbmSource.next(result);
  }

  constructor(
    private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
    // , private dbmCacheAvaSupService: DbmCacheAvaSupService
  ) { }

  getData(env: string, className: string): string[] {
    return this.data.get(env).get(className);
  }

  readData(env: string, dbAddress: string) {
    const f = 'readData';
    console.log(this.c, f);

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

          const result: GetChildrenAliasesResult = new GetChildrenAliasesResult();
          result.env = env;
          result.dbAddress = dbAddress;
          result.data = childrenAliases;
          this.dbmChanged(result);
        }
        , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(f, err); }
        , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The GET observable is now completed.'); }
    );
  }
}
