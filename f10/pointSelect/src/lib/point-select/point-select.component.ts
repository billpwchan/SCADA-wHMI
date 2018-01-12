import { Component, OnInit, OnDestroy, Input, Output, EventEmitter } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { PointSelectSettings } from './point-select-settings';
import { OlsSettings } from './service//scs/ols-settings';
import { DbmSettings } from './service/scs/dbm-settings';
import { OlsService } from './service/scs/ols.service';
import { DbmService } from './service/scs/dbm.service';
import { Subscription } from 'rxjs/Subscription';

export class SelOptStr {
  constructor(
    public label: string
    , public value: string
  ) { }
}

export class SelOptNum {
  constructor(
    public label: string
    , public value: number
  ) { }
}

export interface IPointSelection {
    connAddr: string;
    envlabel: string;
    univname: string;
    classId: number;
    geo: number;
    func: number;
    eqplabel: string;
    pointlabel: string;
    initlabel: string;
    valuelabel: string;
    currentlabel: string;
    evname: string;
    delay: number;
    value: number;
    initvalue: number;
}

@Component({
  selector: 'app-point-select',
  templateUrl: './point-select.component.html',
  styleUrls: ['./point-select.component.css']
})
export class PointSelectComponent implements OnInit, OnDestroy {

  public static readonly STR_INIT = PointSelectSettings.STR_INIT;

  public static readonly STR_ACI_SELECTED = 'aciselected';
  public static readonly STR_DCI_SELECTED = 'dciselected';

  readonly c = 'PointSelectComponent';

  @Input() set enable(enable: boolean) {
    this.editEnableNewStep = enable;
    this.init();
  }
  @Input() geoPrefix: string;
  @Input() funcPrefix: string;
  @Input() eqplabelPrefix: string;
  @Input() pointlabelPrefix: string;
  @Input() valuePrefix: string;
  @Input() delayPrefix: string;
  @Input() delayRangeStep: number;
  @Input() delayRangeStart: number;
  @Input() delayRangeEnd: number;
  @Input() delayDefaultValue: number;
  @Input() delayRangePrefix: string;
  @Input() set envArray(arr: Array<SelOptStr>) {
    this.envs = new Array<SelOptStr>();
    arr.forEach((env) => {
      const e = new SelOptStr(env.label, env.value);
      this.envs.push(e);
    });
  }
  @Input() set showSelectDelay(show: boolean) {
    this.hiddenSelectDelay = !show;
  }
  @Input() set showClassid(show: boolean) {
    this.hiddenClassId = !show;
  }
  @Input() set showUnivname(show: boolean) {
    this.hiddenUnivname = !show;
  }
  @Input() set showEvname(show: boolean) {
    this.hiddenEVName = !show;
  }
  @Input() set showInitValue(show: boolean) {
    this.hiddenAciInitValue = !show;
    this.hiddenDciInitValue = !show;
  }

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();
  @Output() notifyPointSelection: EventEmitter<any> = new EventEmitter();

  olsSubscription: Subscription;
  dbmSubscription: Subscription;

  selectedCardId: string;
  selectedStepId: number;

  // GUI Data Binding
  editEnableNewStep: boolean;

  btnDisabledAddAciStep: boolean;
  btnDisabledAddDciStep: boolean;

  btnDisabledAddAciCancelStep: boolean;
  btnDisabledAddDciCancelStep: boolean;

  hiddenSelectDelay: boolean;
  hiddenClassId: boolean;
  hiddenUnivname: boolean;
  hiddenEVName: boolean;

  hiddenAciInitValue: boolean;
  hiddenDciInitValue: boolean;

  envs: Array<SelOptStr>;
  selEnv: SelOptStr;
  public selOptEnv: Array<SelOptStr> = new Array<SelOptStr>();

  selGeo: number;
  public selOptGeo: Array<SelOptNum> = new Array<SelOptNum>();

  selFunc: number;
  public selOptFunc: Array<SelOptNum> = new Array<SelOptNum>();

  selEqpLabel: string;
  public selOptEqpLabel: Array<SelOptStr> = new Array<SelOptStr>();

  selPointLabel: string;
  public selOptPointLabel: Array<SelOptStr> = new Array<SelOptStr>();

  // data binding

  txtClassId: string;
  txtUnivname: string;
  txtEVName: string;

  // Step - Form Edit
  editEnableAddAciStep: boolean;

  // data binding
  aciInitValue: string;
  aciValue: string;

  editEnableAddDciStep: boolean;

  // data binding
  dciInitValue: string;
  dciValue: string;

  selDciInitValue: number;
  public selOptDciInitValue: Array<SelOptNum> = new Array<SelOptNum>();

  selDciValue: number;
  public selOptDciValue: Array<SelOptNum> = new Array<SelOptNum>();

  editEnableDelay = false;

  selDelay: number;
  public selOptDelay: Array<SelOptNum> = new Array<SelOptNum>();

  constructor(
    public translate: TranslateService
    , private olsService: OlsService
    , private dbmService: DbmService
  ) {
    this.geoPrefix = 'location_';
    this.funcPrefix = 'function_';
    this.eqplabelPrefix = '';
    this.pointlabelPrefix = '';
    this.valuePrefix = '';
    this.delayPrefix = '';
    this.delayRangeStep = 1;
    this.delayRangeStart = 1;
    this.delayRangeEnd = 60;
    this.delayDefaultValue = 5;
    this.delayRangePrefix = '';
    this.envs = [
      {'label': 'M100', 'value': 'http://127.0.0.1:8990'}
    ];

    this.hiddenSelectDelay = true;
    this.hiddenClassId = true;
    this.hiddenUnivname = true;
    this.hiddenEVName = true;
  }

  ngOnInit(): void {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.olsSubscription = this.olsService.olsItem
      .subscribe(item => {
        console.log(this.c, f, 'olsSubscription', item);
        const eqps: Map<string, Map<number, number[]>> = this.olsService.getEqps();
        if (undefined !== eqps && null != eqps) {
          if (eqps.size > 0) {
            this.olsService.getEqps().get(this.selEnv.value)
              .forEach((value: number[], key: number) => {
                this.selOptGeo.push(
                  new SelOptNum(
                    this.translate.instant(this.geoPrefix + key)
                    , key
                  )
                );
              });
          }
        }
      });

    this.dbmSubscription = this.dbmService.dbmItem
      .subscribe(item => {
        console.log(this.c, f, 'dbmSubscription', item);

        if ('retriveClassId' === item) {
          this.txtClassId = this.dbmService.getRetriveClassIdData(this.selEnv.value, this.txtUnivname);
        } else if ('retriveAcquiredDataAttributeFormulas' === item) {
          const formulas: string = this.dbmService.getRetriveAcquiredDataAttributeFormulasData(this.selEnv.value, this.txtUnivname);
          if (null != formulas && formulas.length > 0) {
            // Is acquired data equipment
            if (DbmSettings.STR_FORMULAS_ACQ_SINGLE === formulas[0]) {
              if (DbmSettings.INT_ACI_TYPE === Number.parseInt(this.txtClassId)) {

                this.btnClicked(PointSelectComponent.STR_ACI_SELECTED);
              } else if (DbmSettings.INT_DCI_TYPE === Number.parseInt(this.txtClassId)) {

                this.btnClicked(PointSelectComponent.STR_DCI_SELECTED);
              }
            } else {
              // Prompt the user this is not a single acquired data equipment
              const msg = 'Selected Equipment point is not a single acquired data equipment (Maybe a combine points)';
              console.warn(this.c, f, msg);
            }
          } else {
            // Prompt the user this is not a acquired data equipment
            const msg = 'Selected Equipment point is not an acquired data equipment (Without AAC/DAC)';
            console.warn(this.c, f, msg);
          }
        } else if ('retriveDci' === item) {
          const dbvalue = this.dbmService.getRetriveDciData(this.selEnv.value, this.txtUnivname);
          const evname: string = dbvalue[0][0];
          const initValue: number = dbvalue[1];
          const labels: Array<string> = dbvalue[2];
          const values: Array<number> = dbvalue[3];

          if (null != evname) {
            this.txtEVName = evname;
          }

          if (null != labels && null != values) {

            this.initSelOptDciInitValue();
            this.initSelOptDciValue();

            for (let i = 0; i < labels.length; ++i) {
              if (labels[i].length > 0) {
                this.selOptDciInitValue.push(
                  new SelOptNum(
                    this.translate.instant(labels[i])
                    , values[i]
                  )
                );
                this.selOptDciValue.push(
                  new SelOptNum(
                    this.translate.instant(labels[i])
                    , values[i]
                  )
                );
              }
            }
          }

          if (null != initValue) {
            this.dciInitValue = '' + initValue;
          }
        } else if ('retriveAci' === item) {
          const dbvalue = this.dbmService.getRetriveAciData(this.selEnv.value, this.txtUnivname);
          const evname: string = dbvalue[0][0];
          if (null != evname) {
            this.txtEVName = evname;
          }

          const initValue: number = dbvalue[1];
          if (null != initValue) {
            this.aciInitValue = '' + initValue;
            this.aciValue = '' + PointSelectSettings.INT_ACI_DEFAULT_VALUE;
          }
          this.validate('aci');
        }
      });

    this.btnClicked('init');
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
    this.olsSubscription.unsubscribe();
    this.dbmSubscription.unsubscribe();
  }

  sendNotifyParent(str: string) {
    const f = 'sendNotifyParent';
    console.log(this.c, f, str);
    this.notifyParent.emit(str);
  }

  sendNotifyPointSelection(initValue: number, initLabel: string, value: number, valueLabel: string) {
    const f = 'sendNotifySelection';

    const sel: IPointSelection = <any>{};
    sel.connAddr = this.selEnv.value;
    sel.envlabel = this.selEnv.label;
    sel.univname = this.txtUnivname;
    sel.classId = Number(this.txtClassId).valueOf();
    sel.geo = Number(this.selGeo).valueOf();
    sel.func = Number(this.selFunc).valueOf();
    sel.eqplabel = this.selEqpLabel;
    sel.pointlabel = this.selPointLabel;
    sel.initlabel = initLabel;
    sel.valuelabel = valueLabel;
    sel.currentlabel = undefined;
    sel.evname = this.txtEVName;
    sel.delay = Number(this.selDelay).valueOf();
    sel.value = Number(value).valueOf();
    sel.initvalue = Number(initValue).valueOf();

    console.log(this.c, f, sel);

    this.notifyPointSelection.emit(sel);
  }

  private initSelOptEnv(): void {
    const f = 'initSelOptEnv';
    console.log(this.c, f);

    this.selOptEnv = [];
    this.selOptEnv.push(new SelOptStr(
      this.translate.instant('&step_lst_env_default')
      , ''
    ));

    console.log(this.c, f, 'this.envs', this.envs);
    if (this.envs) {
      this.envs.forEach((item, index) => {
        this.selOptEnv.push(
          new SelOptStr(
            item[PointSelectSettings.STR_LABEL]
            , item[PointSelectSettings.STR_VALUE]
          )
        );
      });
    }

    this.selEnv = this.selOptEnv[0];
  }

  private initSelOptGeo() {
    this.selOptGeo.length = 0;
    this.selOptGeo.push(new SelOptNum(
      this.translate.instant('&step_lst_geo_default')
      , -1
    ));
    this.selGeo = -1;
  }

  private initSelOptFunc() {
    this.selOptFunc.length = 0;
    this.selOptFunc.push(new SelOptNum(
      this.translate.instant('&step_lst_func_default')
      , -1
    ));
    this.selFunc = -1;
  }

  private initSelOptEqpLabel() {
    this.selOptEqpLabel.length = 0;
    this.selOptEqpLabel.push(new SelOptStr(
      this.translate.instant('&step_lst_eqplabel_default')
      , ''
    ));
    this.selEqpLabel = '';
  }

  private initSelOptPointLabel() {
    this.selOptPointLabel.length = 0;
    this.selOptPointLabel.push(new SelOptStr(
      this.translate.instant('&step_lst_pointlabel_default')
      , ''
    ));
    this.selPointLabel = '';
  }

  private initSelOptDelay() {
    this.selOptDelay.length = 0;
    this.selOptDelay.push(
      new SelOptNum(
        this.translate.instant('&step_lst_delay_placeholder')
        , 0
      )
    );

    for (let i = this.delayRangeStart
      ; i <= this.delayRangeEnd
      ; i += this.delayRangeStep) {
      this.selOptDelay.push(
        new SelOptNum(
          this.delayRangePrefix + i
          , i
        )
      );
    }

    this.selDelay = this.delayDefaultValue;
  }

  private initSelOptDciInitValue(): void {
    this.selOptDciInitValue.length = 0;
    this.selOptDciInitValue.push(new SelOptNum(
      this.translate.instant('&step_lst_dciinitvalue_placeholder')
      , -1
    ));
    this.selDciInitValue = -1;
  }

  private initSelOptDciValue(): void {
    this.selOptDciValue.length = 0;
    this.selOptDciValue.push(new SelOptNum(
      this.translate.instant('&step_lst_dcivalue_placeholder')
      , -1
    ));
    this.selDciValue = -1;
  }

  onLstChange(name: string, event) {
    const f = 'onLstChange';
    console.log(this.c, f, 'name[' + name + '] event[' + event + ']');

    switch (name) {
      case 'selEnv': {
        console.log(this.c, f, 'selEnv this.selEnv[' + this.selEnv.value + ']');
        this.initSelOptGeo();
        this.initSelOptFunc();
        this.olsService.retriveEquipments(this.selEnv.value);
      } break;
      case 'selGeo': {

        this.initSelOptFunc();
        console.log(this.c, f, 'this.selEnv[' + this.selEnv.value + '] this.selGeo[' + this.selGeo + ']');
        const eqps: Map<string, Map<number, number[]>> = this.olsService.getEqps();
        eqps.get(this.selEnv.value).get(this.selGeo)
          .forEach((value, index) => {
            this.selOptFunc.push(new SelOptNum(
              this.funcPrefix + value
              , value
            ));
          });
      } break;
      case 'selFunc': {

        this.initSelOptEqpLabel();
        const pointData = this.olsService.getPointData();
        pointData.get(this.selEnv.value).forEach((item1, index) => {
          // console.log(this.c, f, 'item', item);

          if (
            item1[OlsSettings.STR_ATTR_GEO] === this.selGeo
            && item1[OlsSettings.STR_ATTR_FUNC] === this.selFunc) {

            const eqplabel = item1[OlsSettings.STR_ATTR_EQPLABEL];
            // console.log(this.c, f, 'Added', eqplabel);

            let found = false;
            this.selOptEqpLabel.forEach(item2 => {
              if (item2.value === eqplabel) {
                found = true;
                return;
              }
            });

            if (!found) {
              this.selOptEqpLabel.push(
                new SelOptStr(
                  eqplabel
                  , eqplabel
                ));
            }
          }
        });

      } break;
      case 'selEqpLabel': {

        // console.log(this.c, f, 'item',item);
        this.initSelOptPointLabel();
        const pointData = this.olsService.getPointData();
        pointData.get(this.selEnv.value).forEach((item1, index) => {

          if (
            item1[OlsSettings.STR_ATTR_GEO] === this.selGeo
            && item1[OlsSettings.STR_ATTR_FUNC] === this.selFunc
            && item1[OlsSettings.STR_ATTR_EQPLABEL] === this.selEqpLabel) {

            const pointlabel = item1[OlsSettings.STR_ATTR_POINTLABEL];

            let found = false;
            this.selOptPointLabel.forEach(item2 => {
              if (item2.value === pointlabel) {
                found = true;
                return;
              }
            });

            if (!found) {
              this.selOptPointLabel.push(
                new SelOptStr(
                  pointlabel
                  , pointlabel
                ));
            }
          }

        });
      } break;
      case 'selPointLabel': {
        console.log(this.c, f, 'this.selEnv[' + this.selEnv.value + '] this.selGeo[' + this.selGeo + '] this.selFunc[' + this.selFunc + ']');
        console.log('this.selEqpLabel[' + this.selEqpLabel + '] this.selPointLabel[' + this.selPointLabel + ']');
        const pointData = this.olsService.getPointData();
        pointData.get(this.selEnv.value).forEach((item, index) => {

          if (
            item[OlsSettings.STR_ATTR_GEO] === this.selGeo
            && item[OlsSettings.STR_ATTR_FUNC] === this.selFunc
            && item[OlsSettings.STR_ATTR_EQPLABEL] === this.selEqpLabel
            && item[OlsSettings.STR_ATTR_POINTLABEL] === this.selPointLabel
          ) {

            this.txtUnivname = item[OlsSettings.STR_ATTR_UNIVNAME];

            this.dbmService.retriveClassId(this.selEnv.value, this.txtUnivname);
          }
        });
      }
        break;

      case 'selDciValue': {
        this.validate('dci');
      } break;

    }
  }

  private validate(name: string) {
    if ('aci' === name) {
      if (!isNaN(Number.parseFloat(this.aciInitValue))
        && !isNaN(Number.parseFloat(this.aciValue))) {
        this.btnDisabledAddAciStep = false;
      } else {
        this.btnDisabledAddAciStep = true;
      }
    } else if ('dci' === name) {
      if (this.selDciInitValue !== -1
        && this.selDciValue !== -1) {
        this.btnDisabledAddDciStep = false;
      } else {
        this.btnDisabledAddDciStep = true;
      }
    }
  }

  onChange(name: string, event?: Event): void {
    const f = 'onChange';
    console.log(this.c, f);
    console.log(this.c, f, 'name[' + name + ']');

    if (name === 'aciInitValue' || name === 'aciValue') {
      this.validate('aci');
    }
  }

  private addStep(): void {
    const f = 'addStep';
    console.log(this.c, f, 'addStep');

    let initValue: number;
    let initLabel: string;
    let value: number;
    let valueLabel: string;
    if (DbmSettings.INT_ACI_TYPE === Number.parseInt(this.txtClassId)) {
      initValue = Number.parseFloat(this.aciInitValue);
      initLabel = '' + initValue;
      value = Number.parseFloat(this.aciValue);
      valueLabel = '' + value;
      this.sendNotifyPointSelection(initValue, initLabel, value, valueLabel);
    } else if (DbmSettings.INT_DCI_TYPE === Number.parseInt(this.txtClassId)) {
      initValue = this.selDciInitValue;
      initLabel = '';
      value = this.selDciValue;
      valueLabel = '';
      for (let i = 0; i < this.selOptDciValue.length; ++i) {
        if (this.selOptDciValue[i].value === initValue) {
          initLabel = this.selOptDciValue[i].label;
        }
        if (this.selOptDciValue[i].value === value) {
          valueLabel = this.selOptDciValue[i].label;
        }
      }
      this.sendNotifyPointSelection(initValue, initLabel, value, valueLabel);
    }
  }

  private init(): void {
    const f = 'init';
    console.log(this.c, f);

    this.editEnableAddAciStep = false;
    this.editEnableAddDciStep = false;
    this.editEnableDelay = false;

    this.btnDisabledAddAciStep = true;
    this.btnDisabledAddDciStep = true;
    this.btnDisabledAddAciCancelStep = false;
    this.btnDisabledAddDciCancelStep = false;

    this.hiddenAciInitValue = false;
    this.hiddenDciInitValue = false;

    this.initSelOptEnv();
    this.initSelOptGeo();
    this.initSelOptFunc();
    this.initSelOptEqpLabel();
    this.initSelOptPointLabel();
    this.initSelOptDelay();
    this.initSelOptDciInitValue();
    this.initSelOptDciValue();

    this.txtClassId = '';
    this.txtUnivname = '';
    this.txtEVName = '';

    this.aciInitValue = '' + PointSelectSettings.INT_ACI_DEFAULT_VALUE;
    this.aciValue = '' + PointSelectSettings.INT_ACI_DEFAULT_VALUE;
    this.dciInitValue = '' + PointSelectSettings.INT_DCI_DEFAULT_VALUE;
    this.dciValue = '' + PointSelectSettings.INT_DCI_DEFAULT_VALUE;
  }

  btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(this.c, f);
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');
    switch (btnLabel) {
      case PointSelectComponent.STR_INIT: {
        this.init();
      } break;
      case PointSelectComponent.STR_ACI_SELECTED: {
        this.editEnableAddAciStep = true;
        this.editEnableAddDciStep = false;
        if (!this.hiddenSelectDelay) {
          this.editEnableDelay = true;
        }
        this.dbmService.retriveAci(this.selEnv.value, this.txtUnivname);
        this.sendNotifyParent(PointSelectComponent.STR_ACI_SELECTED);
      } break;
      case PointSelectComponent.STR_DCI_SELECTED: {
        this.editEnableAddAciStep = false;
        this.editEnableAddDciStep = true;
        if (!this.hiddenSelectDelay) {
          this.editEnableDelay = true;
        }
        this.dbmService.retriveDci(this.selEnv.value, this.txtUnivname);
        this.sendNotifyParent(PointSelectComponent.STR_DCI_SELECTED);
      } break;
      case 'addcancelstep': {
        this.init();
      } break;
      case 'addacistep': {
        this.addStep();
        this.sendNotifyParent('addacistep');
        this.init();
        this.editEnableNewStep = false;
      } break;
      case 'adddcistep': {
        this.addStep();
        this.sendNotifyParent('adddcistep');
        this.init();
        this.editEnableNewStep = false;
      } break;
      case 'addacicancelstep': {
        this.init();
        this.editEnableNewStep = false;
        this.sendNotifyParent('addcancelstep');
      } break;
      case 'adddcicancelstep': {
        this.init();
        this.editEnableNewStep = false;
        this.sendNotifyParent('addcancelstep');
      } break;
    }
  }
}
