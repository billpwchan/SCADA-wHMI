import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { UtilsHttpModule } from '../../../utils-http/utils-http.module';
import { DbmSettings } from '../../dbm-settings';
import { AppSettings } from '../../../../app-settings';

export class MultiWriteResult {
  public env: string;
  public dbAddresses: string[];
  public dbValue;
}

@Injectable()
export class MultiWriteService {

  readonly c = 'MultiWriteService';

  // Observable source
  private dbmSource = new BehaviorSubject<MultiWriteResult>(null);

  // Observable cardItem stream
  dbmItem = this.dbmSource.asObservable();

  // Service command
  dbmChanged(result: MultiWriteResult) {
    const f = 'dbmChanged';
    console.log(this.c, f);
    this.dbmSource.next(result);
  }

  constructor(
    private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
    // , private dbmCacheAvaSupService: DbmCacheAvaSupService
  ) { }

  writeData(env: string, values: any) {
    const f = 'writeData';
    console.log(this.c, f);

    let url = null;
    url = env + DbmSettings.STR_URL_MULTIWRITE + JSON.stringify(values);
    console.log(this.c, f, 'url', url);

    // Get Label and Value
    this.httpClient.get(
      url
    )
      .subscribe(
        (res: any[]) => {
          console.log(this.c, f, res);
          const json = res;

          const result: MultiWriteResult = new MultiWriteResult();
          this.dbmChanged(result);
        }
        , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(f, err); }
        , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The GET observable is now completed.'); }
    );
  }

}
