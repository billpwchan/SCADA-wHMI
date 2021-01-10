import { Injectable } from '@angular/core';
import * as moment from 'moment';

@Injectable()
export class HighstockDataService {

  dataList = [];
  tickInterval = 0;
  constructor() {
    this.initDataList();
  }

  initDataList() {
    this.dataList = [];
    for (let i = 0; i < 24; i++) {
      this.dataList.push([]);
    }
  }

  cleanData() {
    for (let i = 0; i < this.dataList.length; i++) {
      if (this.dataList[i].length > 0) {  // Not empty this.dataList[i] (Contain subscribed data)
        this.dataList[i] =
          this.dataList[i].filter(record => record && record.x && moment(record.x).isAfter(moment(new Date()).subtract(this.tickInterval / 1000, 'seconds')));
      }
    }
  }
}
