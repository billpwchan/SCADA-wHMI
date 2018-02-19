import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';
import { Record, RecordType } from '../model/record';
import { ScadagenReplayService } from './scadagen/scadagen-replay.service';

@Injectable()
export class ReplayService {

  readonly c = 'ReplayService';

  private subjRecords = new BehaviorSubject(new Array<Record>());
  private records = new Array<Record>();
  private filteredRecords = new Array<Record>();

  private speed: number;
  private startDate: number;
  private currentState: number;
  private userInfo: string;

  private selectedRecord: Record = null;
  private replaySpeed = 1;

  constructor(
    private scgRlyService: ScadagenReplayService
  ) { }

  public getRecords(): Observable<Array<Record>> {
    const f = 'getRecords';
    console.log(this.c, f);

    this.scgRlyService.getInfo().subscribe(
      res => {
        console.log(this.c, f, ' res =', JSON.stringify(res.response) );
        this.speed = res.response.speed;
        this.startDate = res.response.startDate;
        this.currentState = res.response.currentState;

        res.response.fullSnapshots.forEach( file => {
          const rec = new Record();
          rec.fileName = file.fileName;
          rec.fileDate = file.fileDate;
          rec.fileType = RecordType.SNAPSHOT;
          this.records.push(rec);
        });
        this.subjRecords.next(this.records);
      }
    );

    return this.subjRecords;
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
	
	// init replay with record's fileDate as startDate
	this.scgRlyService.initReplay(record.fileDate);
  }

  public setReplaySpeed(speed: number) {
    const f = 'setReplaySpeed';
    console.log(this.c, f);
	
    this.replaySpeed = speed;
  }

  public execAction(action: any) {
    const f = 'execAction';
    console.log(this.c, f);
	
	if (action['type'] === 'REST') {
	  console.log(this.c, f, 'action is REST');
      const url: string = action['url'];
      const params: Map<string, string> = new Map<string, string>();
	  if (action['params']) {
	    const ps: Map<string, string> = acton['params'];
		ps.forEach( (value: string, key: string) => {
		  if (value === '&speed') {
		    params.set(key, speed);
		  } else {
		    params.set(key, value);
		  }
		});
	  }
	  this.scgRlyService.sendRestRequest(url, params);
	} else {
	  console.warn(this.c, f, 'action type not supported');
	}
  }
}
