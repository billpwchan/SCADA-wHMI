import { Injectable } from '@angular/core';
import { AvaCond } from '../types/ava-cond';
import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import { AVACONDS } from '../ava-cond/mock-ava-cond';

@Injectable()
export class AvaCondService {

  constructor() { }

  getAvaConds(): Observable<AvaCond[]> {
    return of(AVACONDS);
  }
}
