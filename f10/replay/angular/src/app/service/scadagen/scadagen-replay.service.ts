import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class ScadagenReplayService {

  readonly c = 'ScadagenReplayService';

  constructor() { }

  public getInfo(): Observable<any> {
    const f = 'getInfo';
    console.log(this.c, f);

    const testObservable = new Observable((observer) => {
      const obj = {
        'speed': 0.0,
        'startDate': 0,
        'currentState': 1,
        'userInfo': '',
        'fullSnapshots': [
          {'fileName': 'S_DbmServer_1518488037_254000', 'fileDate': 1518488037},
          {'fileName': 'S_AlmServer_1518488042_106000', 'fileDate': 1518488042},
          {'fileName': 'S_AlmServer_1518488100_213000', 'fileDate': 1518488100},
          {'fileName': 'S_DbmServer_1518488100_306000', 'fileDate': 1518488100},
          {'fileName': 'S_AlmServer_1518489000_205000', 'fileDate': 1518489000},
          {'fileName': 'S_DbmServer_1518489000_314000', 'fileDate': 1518489000},
          {'fileName': 'S_DbmServer_1518489199_708000', 'fileDate': 1518489199},
          {'fileName': 'S_AlmServer_1518489204_606000', 'fileDate': 1518489204},
          {'fileName': 'S_AlmServer_1518489900_715000', 'fileDate': 1518489900},
          {'fileName': 'S_DbmServer_1518489900_809000', 'fileDate': 1518489900},
          {'fileName': 'S_AlmServer_1518490979_751000', 'fileDate': 1518490979},
          {'fileName': 'S_DbmServer_1518490979_751000', 'fileDate': 1518490979},
          {'fileName': 'S_AlmServer_1518491294_865000', 'fileDate': 1518491294},
          {'fileName': 'S_DbmServer_1518491294_865000', 'fileDate': 1518491294},
          {'fileName': 'S_AlmServer_1518491402_476000', 'fileDate': 1518491402},
          {'fileName': 'S_DbmServer_1518491402_476000', 'fileDate': 1518491402},
          {'fileName': 'S_AlmServer_1518494392_482000', 'fileDate': 1518494392},
          {'fileName': 'S_DbmServer_1518494392_482000', 'fileDate': 1518494392},
          {'fileName': 'S_AlmServer_1518494400_706000', 'fileDate': 1518494400},
          {'fileName': 'S_DbmServer_1518494400_815000', 'fileDate': 1518494400},
          {'fileName': 'S_AlmServer_1518494445_799000', 'fileDate': 1518494445},
          {'fileName': 'S_DbmServer_1518494445_799000', 'fileDate': 1518494445},
          {'fileName': 'S_AlmServer_1518495300_708000', 'fileDate': 1518495300},
          {'fileName': 'S_DbmServer_1518495300_802000', 'fileDate': 1518495300},
          {'fileName': 'S_AlmServer_1518496200_701000', 'fileDate': 1518496200},
          {'fileName': 'S_DbmServer_1518496200_811000', 'fileDate': 1518496200},
          {'fileName': 'S_AlmServer_1518497100_701000', 'fileDate': 1518497100},
          {'fileName': 'S_DbmServer_1518497100_810000', 'fileDate': 1518497100},
          {'fileName': 'S_AlmServer_1518498000_714000', 'fileDate': 1518498000},
          {'fileName': 'S_DbmServer_1518498000_808000', 'fileDate': 1518498000},
          {'fileName': 'S_AlmServer_1518498900_702000', 'fileDate': 1518498900},
          {'fileName': 'S_DbmServer_1518498900_811000', 'fileDate': 1518498900},
          {'fileName': 'S_AlmServer_1518499800_701000', 'fileDate': 1518499800},
          {'fileName': 'S_DbmServer_1518499800_810000', 'fileDate': 1518499800},
          {'fileName': 'S_AlmServer_1518500700_711000', 'fileDate': 1518500700},
          {'fileName': 'S_DbmServer_1518500700_804000', 'fileDate': 1518500700},
          {'fileName': 'S_AlmServer_1518501600_706000', 'fileDate': 1518501600},
          {'fileName': 'S_DbmServer_1518501600_815000', 'fileDate': 1518501600}
        ],
        'incrementalSnapshots': [
          {'fileName': 'I_DbmServer_1518488100_306000', 'fileDate': 1518488100},
          {'fileName': 'I_DbmServer_1518488160_303000', 'fileDate': 1518488160},
          {'fileName': 'I_DbmServer_1518489060_314000', 'fileDate': 1518489060},
          {'fileName': 'I_DbmServer_1518489240_812000', 'fileDate': 1518489240},
          {'fileName': 'I_DbmServer_1518489660_812000', 'fileDate': 1518489660},
          {'fileName': 'I_DbmServer_1518490200_803000', 'fileDate': 1518490200},
          {'fileName': 'I_DbmServer_1518490320_811000', 'fileDate': 1518490320},
          {'fileName': 'I_DbmServer_1518494760_801000', 'fileDate': 1518494760},
          {'fileName': 'I_DbmServer_1518495180_814000', 'fileDate': 1518495180},
          {'fileName': 'I_DbmServer_1518495600_807000', 'fileDate': 1518495600},
          {'fileName': 'I_DbmServer_1518496200_811000', 'fileDate': 1518496200},
          {'fileName': 'I_DbmServer_1518496800_870000', 'fileDate': 1518496800},
          {'fileName': 'I_DbmServer_1518496980_802000', 'fileDate': 1518496980},
          {'fileName': 'I_DbmServer_1518497160_814000', 'fileDate': 1518497160}
        ]
      };
      observer.next(obj);
      observer.complete();
    });

    return testObservable;
  }
}
