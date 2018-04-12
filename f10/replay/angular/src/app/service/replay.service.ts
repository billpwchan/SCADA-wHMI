import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';
import { timer } from 'rxjs/observable/timer';
import { Record, RecordType } from '../model/record';
import { ScadagenReplayService } from './scadagen/scadagen-replay.service';

export enum WorkingState {
  WAITFORINIT = 1,
  READY,
  RUNNING,
  FREEZE
}

@Injectable()
export class ReplayService {

  readonly c = 'ReplayService';

  private subjRecords = new BehaviorSubject(new Array<Record>());
  private subjStartDate = new BehaviorSubject(0);
  private subjState = new BehaviorSubject(WorkingState.WAITFORINIT);
  private subjSpeed = new BehaviorSubject(1);
  private records = new Array<Record>();
  private filteredRecords = new Array<Record>();

  private currentSpeed: number;
  private currentStartDate: number;
  private currentState: number;

  private userInfo: string;
  private startDate: number;

  private selectedRecord: Record = null;
  private replaySpeed = 1;

  constructor(
    private scgRlyService: ScadagenReplayService
  ) {}

  public load(startDelay: number, period: number) {
    this.startGetReplayInfoTimer(startDelay, period);
  }

  public getRecords(): Observable<Array<Record>> {
    const f = 'getRecords';
    console.log(this.c, f);

    return this.subjRecords;
  }

  public getReplayState(): Observable<number> {
    const f = 'getReplayState';
    console.log(this.c, f);

    return this.subjState;
  }

  public startGetReplayInfoTimer(startDelay: number, updatePeriod: number) {
    const f = 'startGetReplayInfoTimer';
    console.log(this.c, f);

    const getInfoTimer = timer(startDelay, updatePeriod);
    const subTimer = getInfoTimer.subscribe(val => {
        this.getReplayInfo();
    });
  }

  public getReplayInfo() {
    const f = 'getReplayInfo';
    console.log(this.c, f);

    this.scgRlyService.getInfo().subscribe(
      res => {
        console.log(this.c, f, ' res =', JSON.stringify(res.response) );
        this.currentSpeed = res.response.speed;
        this.subjSpeed.next(this.currentSpeed);

        this.currentStartDate = res.response.startDate;
        this.subjStartDate.next(this.currentStartDate);

        this.currentState = res.response.currentState;
        this.subjState.next(this.currentState);

        this.records = [];
        res.response.fullSnapshots.forEach( file => {
          const rec = new Record();
          rec.fileName = file.fileName;
          rec.fileDate = file.fileDate;
          const d = new Date();
          d.setTime(file.fileDate * 1000);
          const dateOptions = { year: 'numeric', month: 'numeric', day: 'numeric', hour: 'numeric', minute: 'numeric', second: 'numeric' };
          rec.fileDateStr = d.toLocaleString('en-US', dateOptions);
          rec.fileType = RecordType.SNAPSHOT;
          this.records.push(rec);
        });
        this.subjRecords.next(this.records);
      }
    );
  }

  public filterRecords(startDate: number, endDate: number) {
    const f = 'filterRecords';
    console.log(this.c, f);

    console.log(this.c, f, 'startDate', startDate, 'endDate', endDate );
    this.filteredRecords = new Array<Record>();
    this.records.forEach( record => {
      const recDate = new Date(record.fileDate * 1000);
      console.log(this.c, f, 'record fileDate', record.fileDate, recDate.toLocaleDateString(), recDate.toLocaleTimeString());
      if (record.fileDate >= startDate && record.fileDate <= endDate) {
        console.log(this.c, f, 'insert record');
        this.filteredRecords.push(record);
      } else {
        console.log(this.c, f, 'NOT insert record');
      }
    });
    console.log(this.c, f, 'update records');
    this.subjRecords.next(this.filteredRecords);
  }

  public cancelFilter() {
    const f = 'cancelFilter';
    console.log(this.c, f);

    this.subjRecords.next(this.records);
  }

  public setSelectedRecord(record: Record) {
    const f = 'setSelectedRecord';
    console.log(this.c, f);

    this.selectedRecord = record;
    this.startDate = record.fileDate;

    // init replay with record's fileDate as startDate
    this.initReplay(this.startDate);
  }

  public initReplay(d: number) {
    const f = 'initReplay';
    console.log(this.c, f);

    this.scgRlyService.initReplay(d).subscribe(res => {
      console.log(this.c, f, res);
    });
  }

  public setReplaySpeed(speed: number) {
    const f = 'setReplaySpeed';
    console.log(this.c, f, 'speed =', speed, typeof(speed));

    this.replaySpeed = speed;
  }

  public execAction(action: any) {
    const f = 'execAction';
    console.log(this.c, f, 'action =', action);

    if (action['type'] === 'REST') {
      console.log(this.c, f, 'action is REST');
      const url: string = action['url'];
      const params: Map<string, string|number> = new Map();
      const obj = action['params'];
      if (obj) {
        Object.keys(obj).forEach(key => {
          if (obj[key] === '&speed') {
            params.set(key, this.replaySpeed);
            // console.log(this.c, f, 'typeof ', params.get(key), typeof(params.get(key)));
          } else if (obj[key] === '&startDate') {
            params.set(key, this.startDate);
          } else {
            params.set(key, obj[key]);
            // console.log(this.c, f, 'typeof ', params.get(key), typeof(params.get(key)));
          }
        });
      }
      this.scgRlyService.sendRestRequest(url, params);
    } else {
      console.warn(this.c, f, 'action type not supported');
    }
  }
}
