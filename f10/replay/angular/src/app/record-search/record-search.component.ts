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
  readonly STR_DATE_FORMAT = 'dateFormat';
  readonly STR_TIME_FORMAT = 'timeFormat';
  readonly STR_FIRST_DAY_OF_WEEK = 'firstDayOfWeek';

  public startTime: Date = new Date();

  public endTime: Date = new Date();

  public calendarLabels: any = {};

  public dateFormat = 'yy-mm-dd';

  public hourFormat = '24';

  public filterSet = false;

  constructor(
    private translate: TranslateService
    , private settingsService: SettingsService
    , private replayService: ReplayService
  ) { }

  ngOnInit() {
    const f = 'ngOnInit';
    console.log(this.c, f);

    // this.calendarLabels = {
    //   firstDayOfWeek: 0,
    //   dayNames: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
    //   dayNamesShort: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
    //   dayNamesMin: ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa'],
    //   monthNames: ['January', 'February', 'March', 'April', 'May', 'June',
    //     'July', 'August', 'September', 'October', 'November', 'December'],
    //   monthNamesShort: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
    //   today: 'Today',
    //   clear: 'Clear'
    // };
    this.dateFormat = this.settingsService.getSetting(this.c, f, this.c, this.STR_DATE_FORMAT);
    this.hourFormat = this.settingsService.getSetting(this.c, f, this.c, this.STR_TIME_FORMAT);
    this.getCalendarLabels();
  }

  public getCalendarLabels() {
    const f = 'getCalendarLabels';
    console.log(this.c, f);

    this.calendarLabels['firstDayOfWeek'] = this.settingsService.getSetting(this.c, f, this.c, this.STR_FIRST_DAY_OF_WEEK);

    const dayNames: string[] = new Array(7);
    dayNames[0] = this.translate.instant('Sunday');
    dayNames[1] = this.translate.instant('Monday');
    dayNames[2] = this.translate.instant('Tuesday');
    dayNames[3] = this.translate.instant('Wednesday');
    dayNames[4] = this.translate.instant('Thursday');
    dayNames[5] = this.translate.instant('Friday');
    dayNames[6] = this.translate.instant('Saturday');
    this.calendarLabels['dayNames'] = dayNames;

    const dayNamesShort: string[] = new Array(7);
    dayNamesShort[0] = this.translate.instant('SundayShort');
    dayNamesShort[1] = this.translate.instant('MondayShort');
    dayNamesShort[2] = this.translate.instant('TuesdayShort');
    dayNamesShort[3] = this.translate.instant('WednesdayShort');
    dayNamesShort[4] = this.translate.instant('ThursdayShort');
    dayNamesShort[5] = this.translate.instant('FridayShort');
    dayNamesShort[6] = this.translate.instant('SaturdayShort');
    this.calendarLabels['dayNamesShort'] = dayNamesShort;

    const dayNamesMin: string[] = new Array(7);
    dayNamesMin[0] = this.translate.instant('SundayMin');
    dayNamesMin[1] = this.translate.instant('MondayMin');
    dayNamesMin[2] = this.translate.instant('TuesdayMin');
    dayNamesMin[3] = this.translate.instant('WednesdayMin');
    dayNamesMin[4] = this.translate.instant('ThursdayMin');
    dayNamesMin[5] = this.translate.instant('FridayMin');
    dayNamesMin[6] = this.translate.instant('SaturdayMin');
    this.calendarLabels['dayNamesMin'] = dayNamesMin;

    const monthNames: string[] = new Array(12);
    monthNames[0] = this.translate.instant('January');
    monthNames[1] = this.translate.instant('February');
    monthNames[2] = this.translate.instant('March');
    monthNames[3] = this.translate.instant('April');
    monthNames[4] = this.translate.instant('May');
    monthNames[5] = this.translate.instant('June');
    monthNames[6] = this.translate.instant('July');
    monthNames[7] = this.translate.instant('August');
    monthNames[8] = this.translate.instant('September');
    monthNames[9] = this.translate.instant('October');
    monthNames[10] = this.translate.instant('November');
    monthNames[11] = this.translate.instant('December');
    this.calendarLabels['monthNames'] = monthNames;

    const monthNamesShort: string[] = new Array(12);
    monthNamesShort[0] = this.translate.instant('JanuaryShort');
    monthNamesShort[1] = this.translate.instant('FebruaryShort');
    monthNamesShort[2] = this.translate.instant('MarchShort');
    monthNamesShort[3] = this.translate.instant('AprilShort');
    monthNamesShort[4] = this.translate.instant('MayShort');
    monthNamesShort[5] = this.translate.instant('JuneShort');
    monthNamesShort[6] = this.translate.instant('JulyShort');
    monthNamesShort[7] = this.translate.instant('AugustShort');
    monthNamesShort[8] = this.translate.instant('SeptemberShort');
    monthNamesShort[9] = this.translate.instant('OctoberShort');
    monthNamesShort[10] = this.translate.instant('NovemberShort');
    monthNamesShort[11] = this.translate.instant('DecemberShort');
    this.calendarLabels['monthNamesShort'] = monthNamesShort;

    const today = this.translate.instant('today');
    this.calendarLabels['Today'] = today;

    const clear = this.translate.instant('clear');
    this.calendarLabels['Clear'] = clear;

    console.log(this.c, f, 'calendarLabels', this.calendarLabels);
  }

  public onSearch(event) {
    const f = 'onSearch';
    console.log(this.c, f);

    if (this.startTime != null && this.endTime != null) {
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
