import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { UtilsHttpModule } from './../utils-http/utils-http.module';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { DacSimSettings } from './dac-sim-settings';

export enum ExecType {
  start = 0
  , stop = 1  
}

export class EIV {
  constructor(
      public name: string
      , public value: number
  ) {}
}

export enum ExecResult {
  init = 0
  , sent = 1
  , finish = 2
  , faild = 3
}

export class DacSimExecution {
  constructor(
    public execType: ExecType
    , public cardId: string
    , public stepId: number
    , public connAddr: string
    , public eivs: EIV[]
    , public ret: ExecResult
  ) {}
}

@Injectable()
export class DacSimService {

  readonly c = DacSimService.name;

  // Observable source
  private dacSimSource = new BehaviorSubject<DacSimExecution>(
    new DacSimExecution(0, '', 0, '', [], 0)
  );

  // Observable dacSimItem stream
  dacSimItem = this.dacSimSource.asObservable();

  // Service command
  dacSimChanged(obj: DacSimExecution) {
    const f = 'dacSimChanged';
    console.log(this.c, f);
    this.dacSimSource.next(obj);
  }

  constructor(
    private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
  ) {}

  writeEv(dacSimExec: DacSimExecution) {
    const f = 'writeEv';

    let url = dacSimExec.connAddr;
    url += DacSimSettings.STR_URL_DACSIM_WRITEEXTVAR + JSON.stringify(dacSimExec.eivs);

    // Init Message
    dacSimExec.ret = ExecResult.init;
    this.dacSimChanged(dacSimExec);

    console.log(f, 'sturl[', url, ']');
    // send to the dac sim
    this.httpClient.get(
      url
    )
      .subscribe(
        (res: any[]) => {
          console.log(this.c, f, 'res', JSON.stringify(res));

          // Asnyc Return
          dacSimExec.ret = ExecResult.finish;
          this.dacSimChanged(dacSimExec);
        }
        , (err: HttpErrorResponse) => {
          // Error Message
          dacSimExec.ret = ExecResult.faild;
          this.dacSimChanged(dacSimExec);
          this.utilsHttp.httpClientHandlerError(f, err);
        }
        , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The GET observable is now completed.'); }
    );

    // Sent Message
    dacSimExec.ret = ExecResult.sent;
    this.dacSimChanged(dacSimExec);
  }
}
