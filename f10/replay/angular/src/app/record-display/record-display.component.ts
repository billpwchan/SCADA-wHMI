import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { GridOptions } from 'ag-grid';
import { TranslateService } from '@ngx-translate/core';

import { SettingsService } from '../service/settings.service';
import { ReplayService } from '../service/replay.service';
import { Record } from '../model/record';

@Component({
  selector: 'app-record-display',
  templateUrl: './record-display.component.html',
  styleUrls: ['./record-display.component.css']
})



export class RecordDisplayComponent implements OnInit {
  readonly c = 'RecordDisplayComponent';
  readonly COLUMN_DEFS = 'columnDefs';

  private columnDefs = [];
  private rowData = [];

  private selectedRecord: Record = null;

  constructor(
    private translate: TranslateService
    , private settingsService: SettingsService
    , private replayService: ReplayService
  ) {}

  ngOnInit() {
    const f = 'ngOnInit';
    this.columnDefs = this.settingsService.getSetting(this.c, f, this.c, this.COLUMN_DEFS);
    this.rowData = [];
    this.getRecords();
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

    this.replayService.setSelectedRecord(event.data);
  }

}
