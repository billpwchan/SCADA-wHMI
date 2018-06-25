import { Component, OnInit, Input, Output, EventEmitter, OnDestroy, OnChanges, SimpleChanges } from '@angular/core';
// tslint:disable-next-line:max-line-length
import { NgActiveButtonCfg, NgActiveButtonSettings, NgActiveButtonClassCfg, NgActiveButtonDbmCfg, NgActiveButtonUpdate } from './ng-active-button-settings';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { SettingsService } from '../../service/settings.service';
import { HttpClient } from '@angular/common/http';
import { UtilsHttpModule } from '../../service/scadagen/common/utils-http.module';
import { EnvironmentMappingService } from '../../service/scadagen/envs/environment-mapping.service';
import { Subscription } from 'rxjs/Subscription';
import { GuiUtil } from '../../common/gui-util';
import { DioGui } from '../../service/scadagen/dbm/helper/dio/dio-gui';
import { DioSource } from '../../service/scadagen/dbm/helper/dio/dio-source';
import { CtlSendIntCommand } from '../../service/scadagen/ctl/simple/ctl-send-int-commend';

@Component({
  selector: 'app-ng-active-button',
  templateUrl: './ng-active-button.component.html',
  styleUrls: ['./ng-active-button.component.css']
})
export class NgActiveButtonComponent implements OnInit, OnDestroy {

  readonly c = 'NgActiveButtonComponent';

  @Input()
  set setCfg(cfg: NgActiveButtonCfg) {
    const f = 'setCfg';
    console.log(this.c, f);
    console.log(this.c, f, 'cfg', cfg);

  }
  @Input()
  set setText(str: string) {
    const f = 'setText';
    console.log(this.c, f);
    console.log(this.c, f, 'str', str);
  }

  btnCfg;
  @Input()
  set setButton(cfg: any) {
    this.btnCfg = cfg;
  }
  @Output()
  get getButton(): any {
    const env = this.btnCfg.env;
    const alias = this.btnCfg.alias;
    const point = this.btnCfg.point;
    return this.createEqpCtrlBtn(env, alias, point);
  }

  @Output() notifyParent: EventEmitter<any> = new EventEmitter();

  constructor(
    private translate: TranslateService
    , private settingsService: SettingsService
    , private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
    , private environmentMappingService: EnvironmentMappingService
  ) {
    const f = 'constructor';
    console.log(this.c, f);

    translate.onLangChange.subscribe((event: LangChangeEvent) => {
      this.loadTranslations();
    });

  }

  ngOnInit(): void {
    const f = 'ngOnInit';
    console.log(this.c, f);
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
  }

  loadTranslations(): void {
    const f = 'loadTranslations';
    console.log(this.c, f);
  }

  private createEqpCtrlBtn(env: string, alias: string, point: string): NgActiveButtonCfg {
    const f = 'createEqpCtrlBtn';
    console.log(this.c, f);
    const eqpBtnDbmCfg: NgActiveButtonDbmCfg = new NgActiveButtonDbmCfg();
    eqpBtnDbmCfg.env = env;
    eqpBtnDbmCfg.alias = alias;
    eqpBtnDbmCfg.attributes = {
      'point': point

      , '.label': '.label'
      , '.hmiOrder': '.hmiOrder'
      , '.computedMessage': '.computedMessage'

      , '.valueTable(0:$,dovname)': '.valueTable(0:$,dovname)'
      , '.valueTable(0:$,label)': '.valueTable(0:$,label)'
      , '.valueTable(0:$,value)': '.valueTable(0:$,value)'

      , '.execStatus': '.execStatus'

      , '.initCondGL': '.initCondGL'
      , '.returnCondTO': '.returnCondTO'
    };

    const eqpBtnCfg: NgActiveButtonCfg = new NgActiveButtonCfg();
    eqpBtnCfg.dbm = eqpBtnDbmCfg;

    return eqpBtnCfg;
  }

  onDbmReadUpdate(result: any): void {
    const f = 'onDbmReadUpdate';
    console.log(this.c, f);
    if ( null != result ) {
console.log(this.c, f, 'result', result);
      this.notifyParent.emit([result]);
    }
  }

  onDbmPollingUpdate(result: any): void {
    const f = 'onDbmPollingUpdate';
    console.log(this.c, f);
    if ( null != result ) {
console.log(this.c, f, 'result', result);
      this.notifyParent.emit([result]);
    }
  }

}
