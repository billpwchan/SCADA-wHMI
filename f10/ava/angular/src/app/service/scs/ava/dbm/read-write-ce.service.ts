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
  public targetAlias: string;
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

  readConditions(connAddr: string, univname: string, condStartId: number, condEndId: number) {
    const f = 'readConditions';
    console.log(this.c, f);
    const obs = Array<Observable<any>>();

    for (let cond = condStartId; cond <= condEndId; cond++) {
      const condpath = univname + DbmSettings.STR_ATTR_CONDITION + cond;
      obs.push(this.dbmService.readFormulas(connAddr, condpath));
    }

    // Combining array of observables and emit values based on array order
    Observable.forkJoin(obs).subscribe((res: string[]) => {
      console.log(this.c, f, 'res[' + res + ']');

      const readWriteCEREsults: ReadWriteCEResult[] = new Array<ReadWriteCEResult>();

      for ( let i = 0 ; i < res.length ; ++i ) {
        const ce: string = res[i];
        const subAlias: string[] = ce.toString().match(/\[(.*?)\]/g);
        const subValue: string[] = ce.toString().match(/=(.*)/g);
        // console.log(this.c, f, 'ce[' + ce + ']');
        // console.log(this.c, f, 'subAlias[' + subAlias + ']');
        // console.log(this.c, f, 'subValue[' + subValue + ']');

        const readWriteCEREsult: ReadWriteCEResult = new ReadWriteCEResult();
        readWriteCEREsult.method = 'readConditions';
        readWriteCEREsult.base = univname;
        if ( null != subAlias && null != subValue ) {
          const alias = subAlias[0].slice(1, subAlias[0].length - 1);
          const value = subValue[0].slice(1);

          // console.log(this.c, f, 'alias[' + alias + ']');
          // console.log(this.c, f, 'value[' + value + ']');

          readWriteCEREsult.targetAlias = alias;
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

  writeConditions(connAddr: string, univname: string, condStartId: number, condEndId: number, steps: Step[], formulaDefaultVal: number) {
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

        if ( null != step.equipment.phases ) {
          if ( step.equipment.phases.length > 0 ) {
            const executions: Execution[] = step.equipment.phases[PhasesType.SINGLE_EV];
            if ( null != executions ) {
              if ( executions.length > 0 ) {
                const execution: Execution = executions[0];

                formula =
                DbmSettings.STR_OPEN_BRACKET
                + step.equipment.univname + DbmSettings.STR_ATTR_VALUE
                + DbmSettings.STR_CLOSE_BRACKET
                + DbmSettings.STR_EQUAL
                + execution.value;

                console.log(this.c, f, 'stepId[' + stepId + '] condpath[' + condpath + '] formula[' + formula + ']' );

                obs.push(this.dbmService.writeFormulaStr(connAddr, condpath, formula));
              }
            }
          }
        }
      } else {

        console.log(this.c, f, 'stepId[' + stepId + '] condpath[' + condpath + '] formula[' + formulaDefaultVal + ']' );

        obs.push(this.dbmService.writeFormulaNum(connAddr, condpath, formulaDefaultVal));
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
