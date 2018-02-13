import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/forkJoin';
import 'rxjs/add/operator/map';
import { Card, PhasesType, Step, Execution } from '../../../../model/Scenario';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { UtilsHttpModule } from '../../../scadagen/utils/utils-http.module';
import { DbmService } from '../../../scadagen/dbm/dbm.service';
import { DbmSettings } from '../../../scadagen/dbm/dbm-settings';

export class ReadWriteCEResult {
  public key: string;
  public base: string;
  public fullpath: string;
  public value: number;
}

@Injectable()
export class ReadWriteCEService {

  readonly c = 'ReadWriteCEService';

  // Observable source
  private dbmSource = new BehaviorSubject<ReadWriteCEResult[]>(null);

  // Observable cardItem stream
  dbmItem = this.dbmSource.asObservable();

  private conditions: Map<string, Map<string, number>> = new Map<string, Map<string, number>>();

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

  getConditions(connAddr: string): Map<string, number> {
    const f = 'getConditions';
    console.log(this.c, f);
    return this.conditions.get(connAddr);
  }

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

      this.conditions.set(connAddr, new Map<string, number>());
      const conditions: Map<string, number> = this.conditions.get(connAddr);

      const readWriteCEResults: ReadWriteCEResult[] = new Array<ReadWriteCEResult>();

      for ( let i = 0 ; i < res.length ; ++i ) {
        const ce: string = res[i];
        const subFullpath: string[] = ce.toString().match(/\[(.*?)\]/g);
        const subValue: string[] = ce.toString().match(/=(.*)/g);

        const readWriteCEREsult: ReadWriteCEResult = new ReadWriteCEResult();
        readWriteCEREsult.key = 'readConditions';
        readWriteCEREsult.base = alias;
        if ( null != subFullpath && null != subValue ) {
          const fullpath: string = subFullpath[0].slice(1, subFullpath[0].length - 1);
          const value: number = Number(subValue[0].slice(1));

          readWriteCEREsult.fullpath = fullpath;
          readWriteCEREsult.value = value;
          conditions.set(fullpath, value);
        }

        readWriteCEResults.push(readWriteCEREsult);
      }

      this.dbmChanged(readWriteCEResults);
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
        readWriteCEREsult.key = 'writeConditions';
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
