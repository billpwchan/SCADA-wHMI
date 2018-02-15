import { Component, OnInit } from '@angular/core';
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

  private columnDefs = [];
  private rowData = [];

  constructor(
    private translate: TranslateService
    , private settingsService: SettingsService
    , private replayService: ReplayService
  ) {}

  ngOnInit() {
    this.columnDefs = [
      {
        headerName: 'Name',
        field: 'fileName',
        width: 500
      }
    ];
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

}
