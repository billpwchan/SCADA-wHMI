import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { UtilsHttpModule } from '../../../utils-http/utils-http.module';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/forkJoin';
import 'rxjs/add/operator/map';
import { DbmService } from '../../dbm.service';
import { Card, PhasesType } from '../../../../model/Scenario';
import { DbmSettings } from '../../dbm-settings';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

export class ReadWriteCEResult {
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
    const obs = Array<Observable<any>>();

    for (let cond = condStartId; cond <= condEndId; cond++) {
      const condpath = univname + '.condition' + cond;
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
        console.log(this.c, f, 'ce[' + ce + ']');
        console.log(this.c, f, 'subAlias[' + subAlias + ']');
        console.log(this.c, f, 'subValue[' + subValue + ']');

        const readWriteCEREsult: ReadWriteCEResult = new ReadWriteCEResult();
        readWriteCEREsult.base = univname;
        if ( null != subAlias && null != subValue ) {
          const alias = subAlias[0].slice(1, subAlias[0].length - 1);
          const value = subValue[0].slice(1);

          console.log(this.c, f, 'alias[' + alias + ']');
          console.log(this.c, f, 'value[' + value + ']');

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

  saveAllRulesToRTDB(connAddr: string, univname: string, condStartId: number, condEndId: number, card: Card, formulaDefaultVal: number) {
    const f = 'saveAllRulesToRTDB';
    const obs = new Array<Observable<any>>();

    let condpath = '';

      let stepId = 0;
    for (let cond = condStartId; cond <= condEndId; cond++) {
      condpath = univname + '.condition' + cond;
      let formula: string;

      if (stepId < card.steps.length) {
        formula =
        '['
        + DbmSettings.STR_URL_ALIAS + card.steps[stepId].equipment.univname
        + ']='
        + card.steps[stepId].equipment.phases[PhasesType.SINGLE_EV][0].value;
        obs.push(this.dbmService.writeFormulaStr(connAddr, condpath, formula));
      } else {
        obs.push(this.dbmService.writeFormulaNum(connAddr, condpath, formulaDefaultVal));
      }
      stepId++;
    }

    Observable.forkJoin(obs).subscribe(
      () => {
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
