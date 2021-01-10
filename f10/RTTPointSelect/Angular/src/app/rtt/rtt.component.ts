import { Component, Inject, OnChanges, OnDestroy, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material';
import { DOCUMENT } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { LangChangeEvent, TranslateService } from '@ngx-translate/core';
import { GridOptions } from 'ag-grid';
import { ColorPickerService } from 'ngx-color-picker';
import { AggridTranslateService } from '../service/aggrid-translate.service';
import { ConfigService } from '../service/config.service';
import { Olsdata } from '../service/olsdata';
import { OlsdataService } from '../service/olsdata.service';
import { RttTrendDef } from '../service/rtt-trend-def';
import { RttTrendService } from '../service/rtt-trend.service';
import { ScsOlsService } from '../service/scs-ols.service';
import { SelectedOlsdatasService } from '../service/selected-olsdatas.service';


@Component({
  selector: 'app-rtt',
  templateUrl: './rtt.component.html'
})

export class RttComponent implements OnInit, OnDestroy, OnChanges {

  [key: string]: any;
  // subscriptions
  private subRoute: any;
  private subOlslist: any;
  private token: any
  private legacyMode: boolean

  // Maximum possible column numbers in ols Data.
  private max_allowed_iteration: Number = 19;
  private max_duration: number;


  // stores data list returned from ScsOlslist
  public dataList: String[] = [];
  public olsList: Olsdata[] = [];

  public columnList: String[] = [];
  public colorList: string[] = [];

  // selected
  public data: any;
  public ols: any;

  public columnPointUnit = 8;   // Assume point unit will always be at 8 pos
  public pointUnitExist: Boolean = false;

  public gridOptions: GridOptions;
  public gridOptionsSelected: GridOptions;
  public listServer = 'HisServer';
  public listName = 'TECSTemperature';
  public chType = 'Step';
  public archiveNameList = {};
  public chartTypeList = {};
  public tickInterval = {
    hours: 0,
    minutes: 5,
    seconds: 0
  };
  public warningMessageFlag = false;
  public chartPropHidden = false;
  public chartGenHidden = false;
  public chartModifyHidden = false;
  public pointUnitList: String[] = [];
  public powerUnitDisplayList: String[] = [];

  public selectedOlsdataColumn = [];
  public selectedOlsdataColumnName: string[] = [];
  public selectedOlsdataColumnVar: string[] = [];
  public selectedOlsdataColumnValue: string[] = [];

  public selectedOlsdatas: Olsdata[] = [];
  public selectedOlsdata: Olsdata;

  public filter: string;

  public allowShowYAxis2: boolean;
  public allowAutoMinMax = true;

  public yaxis1: {
    unit: '',
    max: 0,
    min: 0
  };
  public yaxis2: {
    unit: '',
    max: 0,
    min: 0
  };

  public localeText = {};

  public axes: String[] = [];

  public allowShowColorPicker: boolean

  public InvalidDisplayTrendErrMsg: string[] = [];

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
    private configService: ConfigService,
    private route: ActivatedRoute,
    private router: Router,
    private scsOlsService: ScsOlsService,
    private olsdataService: OlsdataService,
    private translateService: TranslateService,
    private rttTrendService: RttTrendService,
    private colorPickerService: ColorPickerService,
    private aggridTranslateService: AggridTranslateService,
    public snackBar: MatSnackBar,
    private selectedOlsdatasService: SelectedOlsdatasService,
    @Inject(DOCUMENT) private document: any) {
    translateService.onLangChange.subscribe((event: LangChangeEvent) => {
    });

  }

  ngOnInit() {
    this.route.queryParams.subscribe(paramsId => {
      this.token = paramsId['token'];
    });
    this.loadConfig();
    this.loadData();
    this.initVariables();
    this.initGridOptions();
    this.initFormControls();
    this.initVariables();
  }


  private initVariables() {
    this.axes = ['1', '2'];
    this.configService.config.getIn(['color_list']).forEach(item => {
      this.colorList.push(item);
    })
    const tempList = [];
    this.configService.config.getIn(['max_duration']).forEach(item => {
      tempList.push(item);
    });
    this.max_duration = Number(tempList[0]) * 3600 + Number(tempList[1]) * 60 + Number(tempList[2]);

    // TO prevent null value from rtt-app reselect
    if (this.selectedOlsdatasService.getyaxis1()) {
      this.yaxis1 = this.selectedOlsdatasService.getyaxis1();
    }
    if (this.selectedOlsdatasService.getyaxis2()) {
      this.yaxis1 = this.selectedOlsdatasService.getyaxis2();
    }
    if (this.selectedOlsdatasService.getTickInterval()) {
      this.tickInterval = this.selectedOlsdatasService.getTickInterval();
    }
    if (this.selectedOlsdatasService.getAllowAutoMinMax()) {
      this.allowAutoMinMax = this.selectedOlsdatasService.getAllowAutoMinMax();
    }
    this.aggridTranslateService.load().then(translateText => {
      this.localeText = translateText;
    });
  }

  private initFormControls(): void {
    this.formControlsArr = [];
    this.chartTypeFormControl = new FormControl('', [
      Validators.required,
      Validators.nullValidator
    ]);
    this.formControlsArr.push(this.chartTypeFormControl);

    this.hoursFormControl = this.legacyMode ? new FormControl('') : new FormControl('', [
      Validators.required,
      Validators.nullValidator,
      Validators.pattern('^([1-9][0-9]*|0)$'),
      Validators.min(0),
      Validators.max(24)
    ]);
    this.formControlsArr.push(this.hoursFormControl);


    this.minutesFormControl = this.legacyMode ? new FormControl('') : new FormControl('', [
      Validators.required,
      Validators.nullValidator,
      Validators.pattern('^([1-9][0-9]*|0)$'),
      Validators.min(0),
      Validators.max(60)
    ]);
    this.formControlsArr.push(this.minutesFormControl);

    this.secondsFormControl = this.legacyMode ? new FormControl('') : new FormControl('', [
      Validators.required,
      Validators.nullValidator,
      Validators.pattern('^([1-9][0-9]*|0)$'),
      Validators.min(0),
      Validators.max(60)
    ]);
    this.formControlsArr.push(this.secondsFormControl);

    this.yAxis1UnitFormControl = new FormControl('', this.commonNullValidator);
    this.formControlsArr.push(this.yAxis1UnitFormControl);

    this.yAxis1MaxFormControl = this.legacyMode ? new FormControl('') : new FormControl('', this.commonAxisValidator);
    this.formControlsArr.push(this.yAxis1MaxFormControl);

    this.yAxis1MinFormControl = this.legacyMode ? new FormControl('') : new FormControl('', this.commonAxisValidator);
    this.formControlsArr.push(this.yAxis1MinFormControl);

    this.yAxis2UnitFormControl = new FormControl('', this.commonNullValidator);
    this.formControlsArr.push(this.yAxis2UnitFormControl);

    this.yAxis2MaxFormControl = this.legacyMode ? new FormControl('') : new FormControl('', this.commonAxisValidator);
    this.formControlsArr.push(this.yAxis2MaxFormControl);

    this.yAxis2MinFormControl = this.legacyMode ? new FormControl('') : new FormControl('', this.commonAxisValidator);
    this.formControlsArr.push(this.yAxis2MinFormControl);

    this.attachedAxisFormControl = this.legacyMode ? new FormControl('') : new FormControl('', this.commonNullValidator);
    this.formControlsArr.push(this.attachedAxisFormControl);
  }

  // TO activate mat-error if user did not touch the field.
  private activateFormControls(): void {
    this.formControlsArr.forEach(formControl => {
      formControl.markAsTouched();
    });
  }


  private initGridOptions(): void {
    this.gridOptions = <GridOptions>{
      defaultColDef: {
        filter: 'agTextColumnFilter'
      },
      floatingFilter: true,
      animateRows: true,
      rowStyle: { 'white-space': 'normal', 'overflow': 'visible', 'overflow-wrap': 'break-word' },
      onGridReady: () => {
        this.gridOptions.api.sizeColumnsToFit();
      }
    };
    this.gridOptions.columnDefs = this.createColumnDefs();
    this.gridOptionsSelected = <GridOptions>{
      defaultColDef: {
        filter: 'agTextColumnFilter'
      },
      animateRows: true,
      stopEditingWhenGridLosesFocus: true,
      rowStyle: { 'white-space': 'normal', 'overflow': 'visible', 'overflow-wrap': 'break-word' },
      onGridReady: () => {
        this.gridOptionsSelected.api.sizeColumnsToFit();
      }
    };
    this.gridOptionsSelected.columnDefs = this.createColumnDefsWithColor();
  }


  private loadConfig() {
    if (this.translateService.get('ARCHIVE_NAMES') && this.translateService.get('CHART_TYPES')) {
      this.translateService.get('ARCHIVE_NAMES').subscribe(
        names => {
          Object.keys(names).forEach(key => {
            this.archiveNameList[key] = names[key];
            this.listName = this.archiveNameList[key];
          })
        }
      );
      this.translateService.get('CHART_TYPES').subscribe(
        types => {
          Object.keys(types).forEach(key => {
            this.chartTypeList[key] = types[key];
            this.chType = types[key];
            console.log(this.chType)
          })
        }
      );
    }
  }

  keys(): Array<string> {
    return Object.keys(this.chartTypeList);
  }

  archiveKeys(): Array<string> {
    return Object.keys(this.archiveNameList);
  }

  private loadData() {
    this.subRoute = this.route.queryParams.subscribe(params => { });
  }

  ngOnDestroy() {
  }

  ngOnChanges(): void {
    this.gridOptions.rowData = this.olsList;
  }

  public getOlsDataArr(): Promise<Olsdata[]> {
    let archiveName = '';
    for (const key in this.archiveNameList) {
      if (this.archiveNameList[key] === this.listName) {
        archiveName = key;
      }
    }
    return this.scsOlsService.readOlslist(this.listServer, archiveName, this.token)
      .then(
        dataList => {
          console.log(dataList);

          if (!dataList) { return null; }
          // Filter out duplicate retrieved data
          dataList = dataList.filter((olsData, index, selfArr) =>
            index === selfArr.findIndex((d) => (
              d.Name === olsData.Name
            ))
          );
          console.log(dataList);
          if (!this.configService.config.getIn(['legacy_mode']) && dataList[0].Name.split('|').length < 5) { return null; }
          this.olsList = dataList;
          this.gridOptions.rowData = this.olsList;
          if (this.gridOptions.api) {
            this.gridOptions.api.setRowData(this.olsList);
            this.gridOptions.api.refreshCells();
          }
          return dataList;
        }
      );
  }

  public prepareOlsdataObj(): void {
    this.onReset();
    this.olsList = [];
    this.refreshPowerUnitList();
    this.translateService.onLangChange.subscribe((event: LangChangeEvent) => {
    })
    this.getOlsDataArr()
      .then(
        myOlsData => {
          if (!myOlsData) { return; }
          this.olsdataService.populateComplexPointName(myOlsData);
        },
        failure => {
          this.InvalidDisplayTrendErrMsg = [];
          this.InvalidDisplayTrendErrMsg.push(this.translateService.instant('GENERAL_CONNECTION_ERR_MSG') + '\n');
          this.openSnackBar();
        }
      );
    // this.gridOptions.api.setRowData(this.olsList);
  }

  public selectOls(event): void {
    const ols: Olsdata = event.data;
    const index: number = this.selectedOlsdatas.lastIndexOf(ols);
    if (index === -1 && this.selectedOlsdatas.length < this.configService.config.getIn(['max_point_allowed'])) {
      this.selectedOlsdatas.push(ols);
      // Assign default colors
      ols.color = this.colorList[this.selectedOlsdatas.length % this.colorList.length];
      this.selectedOlsdatasService.setSelectedOlsdatas(this.selectedOlsdatas);
      this.refreshPowerUnitList();
      // Maximum allowed yAxis name is 2
      if (this.pointUnitList.length > 2) {
        this.selectedOlsdatas.pop();
      }
      this.gridOptionsSelected.rowData = this.selectedOlsdatas;
      if (this.gridOptionsSelected.api) {
        this.gridOptionsSelected.api.setRowData(this.selectedOlsdatas);
        this.gridOptionsSelected.api.setGridAutoHeight(true);
      }
      this.refreshPowerUnitList();
    }
  }

  public deselectOls(event): void {
    const ols: Olsdata = event.data;
    const index: number = this.selectedOlsdatas.lastIndexOf(ols);
    if (index !== -1) {
      this.selectedOlsdatas.splice(index, 1);
      this.gridOptionsSelected.rowData = this.selectedOlsdatas;
      if (this.gridOptionsSelected.api) {
        this.gridOptionsSelected.api.setRowData(this.selectedOlsdatas);
        this.gridOptionsSelected.api.setGridAutoHeight(true);
      }
      this.selectedOlsdata = null;
      this.refreshPowerUnitList();
    }
  }

  private refreshPowerUnitList(): void {
    let pointUnitTableColumn: number;
    this.pointUnitList = [];
    this.powerUnitDisplayList = [];
    this.selectedOlsdatas.map(row => {
      const nameArr: string[] = row.Name.split('|');
      // Assume this.columnPointUnit will always be at pos 8. Can be reassigned to config file
      // Find the point unit column, push to the array and remove duplications
      if (!pointUnitTableColumn) {
        for (let iter_index = 1; iter_index < this.max_allowed_iteration; iter_index++) {
          const varName = 'column' + iter_index.toString();
          if (nameArr[this.columnPointUnit] === row[varName]) {
            pointUnitTableColumn = iter_index;
            this.pointUnitList.push(row[varName]);
          }
        }
      } else {
        const varName = 'column' + pointUnitTableColumn.toString();
        this.pointUnitList.push(row[varName]);
      }
    });
    this.pointUnitList = this.pointUnitList.filter((item, pos) => {
      return this.pointUnitList.indexOf(item) === pos;
    });
    if (this.yaxis1.unit === '' && this.pointUnitList.length !== 0) {
      (this.yaxis1.unit as string) = this.translateService.instant(this.pointUnitList[0].toString());
    }
    this.pointUnitList.forEach(item => {
      this.powerUnitDisplayList.push(this.translateService.instant(item.toString()));
    });
  }

  public isEmptyList(someList: any[]): boolean {
    return someList && someList.length === 0;
  }

  public isEmptyStr(str: String): boolean {
    return (!str || 0 === str.length);
  }

  private createColumnDefsWithColor() {
    const JsonObj = this.createColumnDefs();
    // Add customized editor.
    JsonObj.push(
      {
        headerName: 'Color',
        field: 'color',
        editable: true,
        pinned: 'left',
        autoHeight: true,
        valueFormatter: (params) => this.commonFormatter(params),
        getQuickFilterText: (params) => this.commonFormatter(params),
        filterValueGetter: (params) => this.commonValueGetter(params)
      });
    return JsonObj;
  }

  private createColumnDefs() {
    const JsonObj = [];
    // Use string as var name. Dynamically assign column number to Ag-grid
    for (let i = 1; i < this.max_allowed_iteration; i++) {
      if (this.translateService.get('COL_LBL' + i.toString())) {
        this.translateService.get('COL_LBL' + i.toString()).subscribe(
          value => {
            if (value !== 'COL_LBL' + i.toString()) {
              JsonObj.push(
                {
                  headerName: value,
                  field: 'column' + i.toString(),
                  autoHeight: true,
                  valueFormatter: (params) => this.commonFormatter(params),
                  getQuickFilterText: (params) => this.commonFormatter(params),
                  filterValueGetter: (params) => this.commonValueGetter(params)
                });
              this.selectedOlsdataColumnName.push(value);
              this.selectedOlsdataColumnVar.push('column' + i.toString());
              this.selectedOlsdataColumnName = this.selectedOlsdataColumnName.filter(function (item, pos) {
                return this.selectedOlsdataColumnName.indexOf(item) === pos;
              }.bind(this));
              this.selectedOlsdataColumnVar = this.selectedOlsdataColumnVar.filter(function (item, pos) {
                return this.selectedOlsdataColumnVar.indexOf(item) === pos;
              }.bind(this));
            }
          }
        );
      }
    }
    return JsonObj;
  }

  private commonValueGetter(params: any) {
    // The params passed in here includes the name of the variable.
    const varName = params.colDef.field;
    // For value getter, it use .data instead of value (Different from Formatter)
    if (!varName || !params.data[varName]) {
      return ' ';
    }
    return this.translateService.instant(params.data[varName]);
  }

  private commonFormatter(params: any) {
    if (!params.value || params.value === '') {
      return ' ';
    }
    return this.translateService.instant(params.value);
  }

  public displayTrend(): void {
    if (this.allowDisplayTrend()) {
      this.goToUrl();
    }
  }

  private allowDisplayTrend(): boolean {
    this.activateFormControls();
    this.InvalidDisplayTrendErrMsg = [];
    console.log(this.yaxis1.unit);
    if (this.chType !== '' && this.selectedOlsdatas !== [] &&
      this.hoursFormControl.valid &&
      this.minutesFormControl.valid &&
      this.secondsFormControl.valid &&
      this.yAxis1UnitFormControl.valid &&
      this.yaxis1.unit !== '' &&
      (this.allowAutoMinMax || this.yAxis1MaxFormControl.valid) &&
      (this.allowAutoMinMax || this.yAxis1MinFormControl.valid) &&
      (
        !this.allowShowYAxis2 ||
        (this.allowShowYAxis2 && this.yAxis2UnitFormControl.valid &&
          this.yaxis2.unit !== '' &&
          (this.allowAutoMinMax || this.yAxis2MaxFormControl.valid) &&
          (this.allowAutoMinMax || this.yAxis2MinFormControl.valid)))
      && this.chartTypeFormControl.valid) {
      // Basic form control validation is completed
      let allowDisplayTrendFlag = true;
      if (!this.legacyMode) {
        // Incorrect attached axis (Null, 2 but yAxis2 is not enabled)
        this.selectedOlsdatas.forEach((JsonObj, index) => {
          if (!JsonObj.attachedAxis || (!this.allowShowYAxis2 && JsonObj.attachedAxis !== '1')) {
            allowDisplayTrendFlag = false;
            this.InvalidDisplayTrendErrMsg.push(
              this.translateService.instant('GENERAL_INVALID_ATTACHED_AXIS_ERR_MSG') + 'Row' + (index + 1).toString() + '\n '
            );
          }
        });
      }
      // Invalid yAxis 2 information (Null, disable autominmax without filling min & max info)
      if (this.allowShowYAxis2 && (!this.yaxis2.unit || (!this.allowAutoMinMax && (this.yaxis2.min === null || this.yaxis2.max === null)))) {
        this.yAxis2UnitFormControl.markAsTouched();
        this.yAxis2MinFormControl.markAsTouched();
        this.yAxis1MaxFormControl.markAsTouched();
        allowDisplayTrendFlag = this.legacyMode ? this.legacyMode : false;
      }
      // Invalid minmax information (Max value is smaller than min value)
      if (!this.allowAutoMinMax &&
        (Number(this.yaxis1.max) < Number(this.yaxis1.min) ||
          (this.allowShowYAxis2 && (Number(this.yaxis2.max) < Number(this.yaxis2.min))))) {
        this.InvalidDisplayTrendErrMsg.push(this.translateService.instant('GENERAL_INVALID_MINMAX_ERR_MSG') + '\n');
        allowDisplayTrendFlag = this.legacyMode ? this.legacyMode : false;
      }
      // Invalid duration configuration (Exceed maximum allowed duration in config file)
      if ((Number(this.tickInterval['hours']) * 3600 + Number(this.tickInterval['minutes']) * 60 + Number(this.tickInterval['seconds'])) >=
        this.max_duration) {
        this.InvalidDisplayTrendErrMsg.push(this.translateService.instant('GENERAL_EXCEED_MAX_DURATION_ERR_MSG') + '\n');
        allowDisplayTrendFlag = this.legacyMode ? this.legacyMode : false;
      }
      this.openSnackBar();
      return allowDisplayTrendFlag;
    }
    return false;
  }

  private openSnackBar(): void {
    if (this.isEmptyList(this.InvalidDisplayTrendErrMsg)) { return; }
    let errMsg = '';
    this.InvalidDisplayTrendErrMsg.forEach(Msg => { errMsg += Msg; });

    this.snackBar.open(errMsg, 'Dismiss', {
      duration: 50000,
    });
  }

  onRowClicked(event): void {
    this.selectedOlsdata = event.data;
    this.allowShowColorPicker = true;
    this.selectedOlsdataColumn = [];

    // Update column information specified alongside with the attached axis
    for (let index = 0; index < this.selectedOlsdataColumnName.length; index++) {
      this.selectedOlsdataColumn.push(
        {
          'columnName': this.selectedOlsdataColumnName[index],
          'columnValue': this.selectedOlsdata[this.selectedOlsdataColumnVar[index]],
          'translatedColumnName': this.translateService.instant(this.selectedOlsdataColumnName[index]),
          'translatedColumnValue': this.translateService.instant(this.selectedOlsdata[this.selectedOlsdataColumnVar[index]])
        });
    }
  }

  onColorPickerChange(): void {
    this.gridOptionsSelected.api.setRowData(this.selectedOlsdatas)
    this.gridOptionsSelected.api.refreshCells();
  }

  onChangeTickInterval(): void {
    if (Number(this.tickInterval.hours) === 0 && Number(this.tickInterval.minutes) < 5) {
      this.warningMessageFlag = true;
    } else {
      this.warningMessageFlag = false;
    }
  }

  // Invoke when user click 'back'
  public clearSelection(): void {
    this.allowShowColorPicker = false;
    this.selectedOlsdataColumn = [];
    this.selectedOlsdata = null;
    this.selectedOlsdatas = [];
    this.pointUnitList = [];
    this.powerUnitDisplayList = [];
    this.gridOptionsSelected.rowData = [];
    if (this.gridOptionsSelected.api) {
      this.gridOptionsSelected.api.setRowData(this.selectedOlsdatas);
    }
  }

  // Invoke when user click 'reset'
  public onReset(): void {
    this.warningMessageFlag = false;
    this.allowShowYAxis2 = false;
    this.yaxis1 = {
      unit: '',
      max: 0,
      min: 0
    };
    this.yaxis2 = {
      unit: '',
      max: 0,
      min: 0
    };
    this.tickInterval = {
      hours: 0,
      minutes: 5,
      seconds: 0
    };
    this.clearSelection();
    this.filter = '';
    this.olsList = [];
    this.selectedOlsdataColumnName = [];
    this.selectedOlsdataColumnVar = [];
    this.selectedOlsdataColumnValue = [];
    this.initVariables();
    this.initGridOptions();
    this.initFormControls();
    this.gridOptions.rowData = [];
    if (this.gridOptions.api) {
      this.gridOptions.api.setRowData(this.olsList);
    }

    this.selectedOlsdatasService.setarchiveName(null);
    this.selectedOlsdatasService.setselectedChart(null);
    this.selectedOlsdatasService.setyaxis1(null);
    this.selectedOlsdatasService.setyaxis2(null);
    this.selectedOlsdatasService.setTickInterval(null);
  }

  public onFilterTextBoxChanged(): void {
    this.gridOptions.api.setQuickFilter(this.filter);
  }

  public goToUrl(): void {
    // loop through the olsdata[]
    // extract the needed arguments from the olsdata[] lists:
    // - subscriptionInfo1, xaxisLabel, yaxisLabel, callerId
    // -- subscriptionInfo: env_name+eqpclass+line+"equipment"+java_class+pointname("trackno")
    const env = this.configService.config.getIn(['env']);
    let archiveName = '';
    for (const key in this.archiveNameList) {
      if (this.archiveNameList[key] === this.listName) {
        archiveName = key;
      }
    }
    let selectedChart = '';
    for (const key in this.chartTypeList) {
      if (this.chartTypeList[key] === this.chType) {
        selectedChart = key;
      }
    }
    const yaxisLabel1 = this.pointUnitList[this.powerUnitDisplayList.indexOf(this.yaxis1.unit)];
    const yaxisMin1 = this.yaxis1.min;
    const yaxisMax1 = this.yaxis1.max;
    let yaxisLabel2;
    let yaxisMin2;
    let yaxisMax2;
    const interval = Number(this.tickInterval['hours']) * 3600 + Number(this.tickInterval['minutes']) * 60 + Number(this.tickInterval['seconds']);
    if (this.allowShowYAxis2) {
      yaxisLabel2 = this.pointUnitList[this.powerUnitDisplayList.indexOf(this.yaxis2.unit)];
      yaxisMin2 = this.yaxis2.min;
      yaxisMax2 = this.yaxis2.max;
    }
    const xaxisLabel = this.configService.config.getIn(['xaxisLabel']);

    // Pass the data to service for retrieving in rtt-app
    this.selectedOlsdatasService.setarchiveName(this.listName);
    this.selectedOlsdatasService.setselectedChart(this.chType);
    this.selectedOlsdatasService.setyaxis1(this.yaxis1);
    this.selectedOlsdatasService.setyaxis2(this.allowShowYAxis2 ? this.yaxis2 : null);
    this.selectedOlsdatasService.setTickInterval(this.tickInterval);
    this.selectedOlsdatasService.setAllowAutoMinMax(this.allowAutoMinMax);
    this.selectedOlsdatasService.setPointUnitList(this.pointUnitList);
    this.selectedOlsdatasService.setAllowShowYAxis2(this.allowShowYAxis2);

    let rttparams = '';
    let amper = '';
    this.selectedOlsdatas.forEach((element, index) => {
      const hvid = element.DB_point_Alias;
      const color = element.color.substring(1);
      const subinfoparam = env + '+' + hvid + '+' + archiveName + '+' + color;
      if (index === 0) {
        rttparams += RttTrendDef.HV_SUBSCRIBE_Q;
        rttparams += amper + RttTrendDef.YAXIS + (index + 1) + '=' + yaxisLabel1;
        rttparams += this.legacyMode ? '' : '+' + yaxisMin1 + '+' + yaxisMax1;
      }
      if (index === 1 && this.allowShowYAxis2) {
        rttparams += amper + RttTrendDef.YAXIS + (index + 1) + '=' + yaxisLabel2;
        rttparams += this.legacyMode ? '' : '+' + yaxisMin2 + '+' + yaxisMax2;
      }
      amper = '&';
      rttparams += amper + RttTrendDef.SUBINFO + (index + 1) + '=' + subinfoparam;
    });

    // Add params that affect the overall chart
    rttparams += amper + RttTrendDef.XAXIS_E
      + xaxisLabel + amper
      + RttTrendDef.CALLERID_E + (Math.floor(Math.random() * 1000)) + amper + RttTrendDef.CHARTTYPE_E + selectedChart + 'chart';
    rttparams += this.legacyMode ? '' : amper
      + RttTrendDef.INTERVAL_E + interval;
    console.log('Rtt Params: ' + rttparams);

    // Legacy model => Call rttApp java.
    // Non-legacy mode => redirect to rtt-app. Need to prevet user directly enter rtt-app without using rtt first
    // User should not pass any validation
    if (this.legacyMode) {
      this.rttTrendService.readTrendUrl(rttparams)
        .then(charturl => this.document.location.href = charturl);
    } else {
      this.router.navigateByUrl('rtt-app');
    }
  }
}

