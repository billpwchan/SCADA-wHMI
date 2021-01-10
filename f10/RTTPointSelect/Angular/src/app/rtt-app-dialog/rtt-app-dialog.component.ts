import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { LangChangeEvent, TranslateService } from '@ngx-translate/core';
import { ConfigService } from '../service/config.service';


@Component({
  selector: 'app-rtt-app-dialog',
  templateUrl: './rtt-app-dialog.component.html',
  styleUrls: ['./rtt-app-dialog.component.css']
})
export class RttAppDialogComponent implements OnInit {
  public max_duration: number;
  public yAxis1: {
    unit: '',
    max: 0,
    min: 0
  };
  public yAxis2: {
    unit: '',
    max: 0,
    min: 0
  };
  public tickInterval: {
    hours: 0,
    minutes: 0,
    seconds: 0
  };
  public chType: string;
  public chartTypeList: String[] = [];
  public powerUnitList: String[] = [];
  public powerUnitDisplayList: String[] = [];
  public allowShowYAxis2: boolean;
  public allowAutoMinMax: boolean;

  public formControlsArr: FormControl[] = [];

  public chartTypeFormControl: FormControl;
  public hoursFormControl: FormControl;
  public minutesFormControl: FormControl;
  public secondsFormControl: FormControl;

  public yAxis1UnitFormControl: FormControl;
  public yAxis1MaxFormControl: FormControl;
  public yAxis1MinFormControl: FormControl;

  public yAxis2UnitFormControl: FormControl;
  public yAxis2MaxFormControl: FormControl;
  public yAxis2MinFormControl: FormControl;

  public attachedAxisFormControl: FormControl;

  commonAxisValidator = [
    Validators.required,
    Validators.nullValidator,
    Validators.pattern('^([+-]?[1-9][0-9]*|0)$'),
    Validators.maxLength(8)
  ];

  commonNullValidator = [
    Validators.required,
    Validators.nullValidator,
  ];

  constructor(
    private translateService: TranslateService,
    private dialogRef: MatDialogRef<RttAppDialogComponent>,
    private configService: ConfigService,
    @Inject(MAT_DIALOG_DATA) public data: any) {
    translateService.onLangChange.subscribe((event: LangChangeEvent) => {
    })
    this.yAxis1 = data.yAxis1;
    this.yAxis2 = data.yAxis2;
    this.tickInterval = data.tickInterval;
    this.chType = data.chType;
    this.chartTypeList = data.chartTypeList;
    this.powerUnitList = data.powerUnitList;
    this.powerUnitDisplayList = data.powerUnitDisplayList;
    this.allowShowYAxis2 = data.allowShowYAxis2;
    this.allowAutoMinMax = data.allowAutoMinMax;
    const tempList = [];
    this.configService.config.getIn(['max_duration']).forEach(item => {
      tempList.push(item);
    });
    this.max_duration = Number(tempList[0]) * 3600 + Number(tempList[1]) * 60 + Number(tempList[2]);
    console.log(this.powerUnitDisplayList);
  }

  ngOnInit() {
    this.initFormControls();
  }


  private initFormControls(): void {
    this.formControlsArr = [];
    this.chartTypeFormControl = new FormControl('', [
      Validators.required,
      Validators.nullValidator
    ]);
    this.formControlsArr.push(this.chartTypeFormControl);

    this.hoursFormControl = new FormControl('', [
      Validators.required,
      Validators.nullValidator,
      Validators.pattern('^([1-9][0-9]*|0)$'),
      Validators.min(0),
      Validators.max(24)
    ]);
    this.formControlsArr.push(this.hoursFormControl);


    this.minutesFormControl = new FormControl('', [
      Validators.required,
      Validators.nullValidator,
      Validators.pattern('^([1-9][0-9]*|0)$'),
      Validators.min(0),
      Validators.max(60)
    ]);
    this.formControlsArr.push(this.minutesFormControl);

    this.secondsFormControl = new FormControl('', [
      Validators.required,
      Validators.nullValidator,
      Validators.pattern('^([1-9][0-9]*|0)$'),
      Validators.min(0),
      Validators.max(60)
    ]);
    this.formControlsArr.push(this.secondsFormControl);

    this.yAxis1UnitFormControl = new FormControl('', this.commonNullValidator);
    this.formControlsArr.push(this.yAxis1UnitFormControl);

    this.yAxis1MaxFormControl = new FormControl('', this.commonAxisValidator);
    this.formControlsArr.push(this.yAxis1MaxFormControl);

    this.yAxis1MinFormControl = new FormControl('', this.commonAxisValidator);
    this.formControlsArr.push(this.yAxis1MinFormControl);

    this.yAxis2UnitFormControl = new FormControl('', this.commonNullValidator);
    this.formControlsArr.push(this.yAxis2UnitFormControl);

    this.yAxis2MaxFormControl = new FormControl('', this.commonAxisValidator);
    this.formControlsArr.push(this.yAxis2MaxFormControl);

    this.yAxis2MinFormControl = new FormControl('', this.commonAxisValidator);
    this.formControlsArr.push(this.yAxis2MinFormControl);
  }

  public isEmptyList(someList: any[]): boolean {
    return someList && someList.length === 0;
  }

  private allowReplot(): boolean {
    if (this.hoursFormControl.valid &&
      this.minutesFormControl.valid &&
      this.secondsFormControl.valid &&
      this.yAxis1UnitFormControl.valid &&
      (this.allowAutoMinMax || this.yAxis1MaxFormControl.valid) &&
      (this.allowAutoMinMax || this.yAxis1MinFormControl.valid) &&
      (
        !this.allowShowYAxis2 ||
        (this.allowShowYAxis2 && this.yAxis2UnitFormControl.valid &&
          (this.allowAutoMinMax || this.yAxis2MaxFormControl.valid) &&
          (this.allowAutoMinMax || this.yAxis2MinFormControl.valid)))
      && this.chartTypeFormControl.valid) {
      let allowDisplayTrendFlag = true;

      if (this.allowShowYAxis2 && (!this.yAxis2.unit || (!this.allowAutoMinMax && (this.yAxis2.min === null || this.yAxis2.max === null)))) {
        this.yAxis2UnitFormControl.markAsTouched();
        this.yAxis2MinFormControl.markAsTouched();
        this.yAxis1MaxFormControl.markAsTouched();
        allowDisplayTrendFlag = false;
      }
      if (!this.allowAutoMinMax &&
        (Number(this.yAxis1.max) < Number(this.yAxis1.min) ||
          (this.allowShowYAxis2 && (Number(this.yAxis2.max) < Number(this.yAxis2.min))))) {
        alert(this.translateService.instant('GENERAL_INVALID_MINMAX_ERR_MSG') + '\n');
        allowDisplayTrendFlag = false;
      }
      if ((Number(this.tickInterval['hours']) * 3600 + Number(this.tickInterval['minutes']) * 60 + Number(this.tickInterval['seconds'])) >=
        this.max_duration) {
        alert(this.translateService.instant('GENERAL_EXCEED_MAX_DURATION_ERR_MSG') + '\n');
        allowDisplayTrendFlag = false;
      }
      return allowDisplayTrendFlag;
    }
    return false;
  }

  keys(): Array<string> {
    return Object.keys(this.chartTypeList);
  }

  onReplot(): void {
    this.allowReplot();
    this.dialogRef.close({
      data: {
        yAxis1: this.yAxis1,
        yAxis2: this.yAxis2,
        tickInterval: this.tickInterval,
        chType: this.chType,
        chartTypeList: this.chartTypeList,
        powerUnitList: this.powerUnitList,
        allowShowYAxis2: this.allowShowYAxis2,
        allowAutoMinMax: this.allowAutoMinMax
      }
    });
  }

}
