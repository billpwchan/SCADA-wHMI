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

  constructor(
    private scgRlyService: ScadagenReplayService
  ) { }

  public getRecords(): Observable<Array<Record>> {
    const f = 'getRecords';
    console.log(this.c, f);

    this.scgRlyService.getInfo().subscribe( info => {
      console.log(this.c, f, ' info = ' + info);
      this.speed = info.speed;
      this.startDate = info.startDate;
      this.currentState = info.currentState;
      info.fullSnapshots.forEach( file => {
        const rec = new Record();
        rec.fileName = file.fileName;
        rec.fileDate = file.fileDate;
        rec.fileType = RecordType.SNAPSHOT;
        this.records.push(rec);
      });
      this.subjRecords.next(this.records);
    });

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
    this.subjRecords.next(this.records);
  }


}
