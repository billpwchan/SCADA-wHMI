import { Injectable } from '@angular/core';
import { Olsdata } from './olsdata';

@Injectable()
export class SelectedOlsdatasService {

  private allowAutoMinMax: boolean;
  private selectedOlsdatas: Olsdata[];
  private yaxis1: {
    unit: '',
    max: 0,
    min: 0
  };
  private yaxis2: {
    unit: '',
    max: 0,
    min: 0
  };
  private archiveName: string;
  private selectedChart: string;
  private tickInterval: {
    'hours': 0,
    'minutes': 1,
    'seconds': 0
  };
  private pointUnitList: String[];
  private allowShowYAxis2: boolean;

  constructor() { }

  setAllowAutoMinMax(allowAutoMinMax: boolean) {
    this.allowAutoMinMax = allowAutoMinMax;
  }

  getAllowAutoMinMax(): boolean {
    return this.allowAutoMinMax;
  }

  setSelectedOlsdatas(OlsdataList: Olsdata[]) {
    this.selectedOlsdatas = OlsdataList;
  }

  getSelectedOlsdatas(): Olsdata[] {
    return this.selectedOlsdatas;
  }

  setyaxis1(yaxis1: any) {
    this.yaxis1 = yaxis1;
  }

  getyaxis1(): any {
    return this.yaxis1;
  }

  setyaxis2(yaxis2: any) {
    this.yaxis2 = yaxis2;
  }

  getyaxis2(): any {
    return this.yaxis2;
  }

  setarchiveName(archiveName: string) {
    this.archiveName = archiveName;
  }

  getarchiveName(): string {
    return this.archiveName;
  }

  setselectedChart(selectedChart: string) {
    this.selectedChart = selectedChart;
  }

  getselectedChart(): string {
    return this.selectedChart;
  }

  setTickInterval(tickInterval) {
    this.tickInterval = tickInterval;
  }

  getTickInterval() {
    return this.tickInterval;
  }

  setPointUnitList(pointUnitList: String[]) {
    this.pointUnitList = pointUnitList;
  }

  getPointUnitList(): String[] {
    return this.pointUnitList;
  }

  setAllowShowYAxis2(allowShowYAxis2: boolean) {
    this.allowShowYAxis2 = allowShowYAxis2;
  }

  getAllowShowYAxis2(): boolean {
    return this.allowShowYAxis2;
  }

}
