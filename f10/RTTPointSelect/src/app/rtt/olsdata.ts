import { OnInit } from '@angular/core';

export class Olsdata implements OnInit {

  Name: string;
  Value: number;
  ValueType: number;
  Quality: number;
  ID: string;
  SCSTime: string;
  Timestamp: string;
  color: string;

  // Values that exist after processing "Name" field
  // CBType?: string;
  // ReportType?: string;
  // Station?: string;
  // System?: string;
  // EqpType?: string;
  // EquipmentLabel?: string;
  // EquipmentShortLabel?: string;
  // PointLabel?: string;
  // PointUnit?: string;
  DB_point_Alias?: string

  column1?: string;
  column2?: string;
  column3?: string;
  column4?: string;
  column5?: string;
  column6?: string;

  ngOnInit(): void {
    this.color = '#ff0000';
  }

  // constructor(
  //   ols: Olsdata
  // ) {
  //   this.Name = ols.Name;
  //   this.Value = ols.Value;
  //   this.ValueType = ols.ValueType;
  //   this.Quality = ols.Quality;
  //   this.ID = ols.ID;
  //   this.SCSTime = ols.SCSTime;
  //   this.Timestamp = ols.Timestamp;
  // }

  // public setComplexPointName(
  //   CBType,
  //   ReportType,
  //   Station,
  //   System,
  //   EqpType,
  //   EquipmentLabel,
  //   EquipmentShortLabel,
  //   PointLabel,
  //   PointUnit,
  //   DB_point_Alias
  // ) {
  //     this.complexPointName.CBType = CBType;
  //     this.complexPointName.ReportType = ReportType;
  //     this.complexPointName.Station = Station;
  //     this.complexPointName.System = System;
  //     this.complexPointName.EqpType = EqpType;
  //     this.complexPointName.EquipmentLabel = EquipmentLabel;
  //     this.complexPointName.EquipmentShortLabel = EquipmentShortLabel;
  //     this.complexPointName.PointLabel = PointLabel;
  //     this.complexPointName.PointUnit = PointUnit;
  //     this.complexPointName.DB_point_Alias = DB_point_Alias;

  //   }

}

