import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { UtilsHttpModule } from '../../../utils-http/utils-http.module';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/forkJoin';
import 'rxjs/add/operator/map';
import { DbmService } from '../../dbm.service';
import { Card, PhasesType, Step, Execution } from '../../../../model/Scenario';
import { DbmSettings } from '../../dbm-settings';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

export class ReadWriteCEResult {
  public method: string;
  public base: string;
  public targetFullpath: string;
  public targetValue: string;
}

@Injectable()
export class ReadWriteCEService {

  readonly c = 'ReadWriteCEService';

  // Observable source
  private dbmSource = new BehaviorSubject<ReadWriteCEResult[]>(null);

  // Observable cardItem stream
  dbmItem = this.dbmSource.asObservable();

  // Service command
  dbmChanged(result: ReadWriteCEResult[]) {
    const f = 'dbmChanged';
    console.log(this.c, f);
    this.dbmSource.next(result);
  }

  constructor(
    private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
    , private dbmService: DbmService
  ) { }

  readConditions(connAddr: string, alias: string, condStartId: number, condEndId: number) {
    const f = 'readConditions';
    console.log(this.c, f);
    const obs = Array<Observable<any>>();

    for (let cond = condStartId; cond <= condEndId; cond++) {
      const condpath = alias + DbmSettings.STR_ATTR_CONDITION + cond;
      obs.push(this.dbmService.readFormulas(connAddr, condpath));
    }

    // Combining array of observables and emit values based on array order
    Observable.forkJoin(obs).subscribe((res: string[]) => {
      console.log(this.c, f, 'res[' + res + ']');

      const readWriteCEREsults: ReadWriteCEResult[] = new Array<ReadWriteCEResult>();

      for ( let i = 0 ; i < res.length ; ++i ) {
        const ce: string = res[i];
        const subFullpath: string[] = ce.toString().match(/\[(.*?)\]/g);
        const subValue: string[] = ce.toString().match(/=(.*)/g);

        const readWriteCEREsult: ReadWriteCEResult = new ReadWriteCEResult();
        readWriteCEREsult.method = 'readConditions';
        readWriteCEREsult.base = alias;
        if ( null != subFullpath && null != subValue ) {
          const fullpath = subFullpath[0].slice(1, subFullpath[0].length - 1);
          const value = subValue[0].slice(1);

          readWriteCEREsult.targetFullpath = fullpath;
          readWriteCEREsult.targetValue = value;
        }

        readWriteCEREsults.push(readWriteCEREsult);
      }

      this.dbmChanged(readWriteCEREsults);
    }
    , (err: HttpErrorResponse) => {
      this.utilsHttp.httpClientHandlerError(f, err);
    }
    , () => { this.utilsHttp.httpClientHandlerComplete(f, 'The POST observable is now completed.'); });
  }

  writeConditions(connAddr: string
                , univname: string
                , condStartId: number
                , condEndId: number
                , steps: Step[]
                , formulaDefaultVal: number
                , formulaZeroDefaultVal: number) {
    const f = 'writeConditions';
    console.log(this.c, f);
    const obs = new Array<Observable<any>>();

    let condpath = '';

    let stepId = 0;
    for (let cond = condStartId; cond <= condEndId; cond++) {
      condpath = univname + DbmSettings.STR_ATTR_CONDITION + cond;
      let formula: string;

      if (stepId < steps.length) {

        const step: Step = steps[stepId];

        formula =
        DbmSettings.STR_OPEN_BRACKET
        + step.equipment.fullpath + DbmSettings.STR_ATTR_VALUE
        + DbmSettings.STR_CLOSE_BRACKET
        + DbmSettings.STR_EQUAL
        + step.equipment.value;

        console.log(this.c, f, 'stepId[' + stepId + '] condpath[' + condpath + '] formula[' + formula + ']' );

        obs.push(this.dbmService.writeFormulaStr(connAddr, condpath, formula));
      } else {

        const forDefVal: number = ( cond !== 1 ? formulaDefaultVal : formulaZeroDefaultVal );

        console.log(this.c, f, 'stepId[' + stepId + '] condpath[' + condpath + '] formula[' + forDefVal + ']' );

        obs.push(this.dbmService.writeFormulaNum(connAddr, condpath, forDefVal));
      }
      stepId++;
    }

    Observable.forkJoin(obs).subscribe(
      () => {
        const readWriteCEREsult: ReadWriteCEResult = new ReadWriteCEResult();
        readWriteCEREsult.method = 'writeConditions';
        this.dbmChanged([readWriteCEREsult]);
        // this.storageChanged(StorageResponse.SAVE_SUCCESS);
      },
      (err: HttpErrorResponse) => {
        // this.storageChanged(StorageResponse.SAVE_FAILED);
        this.utilsHttp.httpClientHandlerError(f, err);
      },
      () => {
        this.utilsHttp.httpClientHandlerComplete(f, 'The save rules observable is now completed.');
      }
    );
  }

}
