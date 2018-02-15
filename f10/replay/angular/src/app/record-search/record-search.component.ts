import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

import { SettingsService } from '../service/settings.service';
import { ReplayService } from '../service/replay.service';

@Component({
  selector: 'app-record-search',
  templateUrl: './record-search.component.html',
  styleUrls: ['./record-search.component.css']
})
export class RecordSearchComponent implements OnInit {
  readonly c = 'RecordSearchComponent';

  public startTime: Date = new Date();

  public endTime: Date = new Date();

  public calendarLabels: any;

  public dataFormat = 'yy-mm-dd';

  public filterSet = false;

  constructor(
    private translate: TranslateService
    , private settingsService: SettingsService
    , private replayService: ReplayService
  ) { }

  ngOnInit() {

    this.calendarLabels = {
      firstDayOfWeek: 0,
      dayNames: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
      dayNamesShort: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
      dayNamesMin: ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa'],
      monthNames: [ 'January', 'February', 'March', 'April', 'May', 'June',
        'July', 'August', 'September', 'October', 'November', 'December' ],
      monthNamesShort: [ 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec' ],
      today: 'Today',
      clear: 'Clear'
    };

  }

  public onSearch(event) {
    const f = 'onSearch';
    console.log(this.c, f);

    if (this.startTime != null && this.endTime != null ) {
      const startUnixTime = this.startTime.getTime() / 1000;
      const endUnixTime = this.endTime.getTime() / 1000;
      console.log(this.c, f, 'startTime (', startUnixTime, ')  endTime (', endUnixTime, ')');

      this.replayService.filterRecords(startUnixTime, endUnixTime);

      this.filterSet = true;
    }
  }

  public onCancelSearch(event) {
    const f = 'onCancelSearch';
    console.log(this.c, f);

    if (this.filterSet) {
      this.replayService.cancelFilter();
    }

    this.filterSet = false;
  }
}
