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
  attachedAxis: string;

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
  column7?: string;
  column8?: string;
  column9?: string;
  column10?: string;
  column11?: string;
  column12?: string;
  column13?: string;
  column14?: string;
  column15?: string;
  column16?: string;
  column17?: string;
  column18?: string;

  ngOnInit(): void {
    this.color = '#ff0000';
  }
}

