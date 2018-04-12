import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { GridOptions } from 'ag-grid';
import { TranslateService } from '@ngx-translate/core';

import { SettingsService } from '../service/settings.service';
import { ReplayService, WorkingState } from '../service/replay.service';
import { Record } from '../model/record';

@Component({
  selector: 'app-record-display',
  templateUrl: './record-display.component.html',
  styleUrls: ['./record-display.component.css']
})

export class RecordDisplayComponent implements OnInit {
  readonly c = 'RecordDisplayComponent';
  readonly COLUMN_DEFS = 'columnDefs';

  public columnDefs = [];
  public rowData = [];

  public selectedRecord: Record = null;
  public replayState: WorkingState;
  public replayStateStr = '';

  public replayReady = false;
  public replayRunning = false;

  constructor(
    private translate: TranslateService
    , private settingsService: SettingsService
    , private replayService: ReplayService
  ) {}

  ngOnInit() {
    const f = 'ngOnInit';
    this.columnDefs = this.settingsService.getSetting(this.c, f, this.c, this.COLUMN_DEFS);
    this.rowData = [];
    this.getReplayState();
    this.getRecords();
  }

  getReplayState(): void {
    const f = 'getReplayState';
    console.log(this.c, f);

    this.replayService.getReplayState().subscribe( state => {
      console.log(this.c, f, 'update state', state);
      this.replayState = state;
      this.replayStateStr = 'ReplayState_' + state;

      if (this.replayState === WorkingState.WAITFORINIT) {
        this.replayReady = false;
        this.replayRunning = false;
      } else if (this.replayState === WorkingState.READY) {
        this.replayReady = true;
        this.replayRunning = false;
      } else if (this.replayState === WorkingState.RUNNING || this.replayState === WorkingState.FREEZE) {
        this.replayReady = true;
        this.replayRunning = true;
      }

    });
  }

  getRecords(): void {
    const f = 'getRecords';
    console.log(this.c, f);

    this.replayService.getRecords().subscribe( records => {
      console.log(this.c, f, 'update display records', records.length);
      this.rowData = records;
    });
  }

  onRowClicked(event: any) {
    const f = 'onRowClicked';
    console.log(this.c, f, event);

    if (event.data != null && !this.replayRunning) {
      this.replayService.setSelectedRecord(event.data);
    }
  }

}
