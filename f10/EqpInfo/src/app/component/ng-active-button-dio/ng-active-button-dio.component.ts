import { Component, OnInit, Input, Output, EventEmitter, OnDestroy, OnChanges, SimpleChanges } from '@angular/core';
// tslint:disable-next-line:max-line-length
import { NgActiveButtonCfg, NgActiveButtonSettings, NgActiveButtonClassCfg, NgActiveButtonDbmCfg, NgActiveButtonUpdate } from './ng-active-button-dio-settings';
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
  selector: 'app-ng-active-button-dio',
  templateUrl: './ng-active-button-dio.component.html',
  styleUrls: ['./ng-active-button-dio.component.css']
})
export class NgActiveButtonDioComponent implements OnInit, OnDestroy {

  readonly c = 'NgActiveButtonDioComponent';

  groupClass = 'form-group';

  labelClass = '';
  labelText = '-';

  buttonClass = 'btn';
  buttonText = 'Execute';
  buttonDisabled = true;

  selectClass = 'form-control';
  selectValues = [
    {id: 0, name: '-'}
  ];
  selectedValue;

  messageClass = 'badge';
  messageText = 'message';

  alertClass = '';
  alertTitle = '';
  alertText = '';

  private guiUtil: GuiUtil;
  private gui: DioGui;

  private cfg: NgActiveButtonCfg;

  private source: DioSource;
  sourceSubscription: Subscription;

  private ctlSendCommand: CtlSendIntCommand;
  ctlSendCommandSubscription: Subscription;

  @Input()
  set setCfg(cfg: NgActiveButtonCfg) {
    const f = 'setCfg';
    console.log(this.c, f);
    console.log(this.c, f, 'cfg', cfg);
    this.cfg = cfg;
    if (null != this.cfg) {
      if (null != this.cfg.class) { this.buttonClass = this.cfg.class.button; }
      if (null != this.cfg.dbm) {
        this.gui.setCfg(this.cfg.dbm);
        this.source.connect(this.cfg.dbm);
      }
    }
  }
  @Input()
  set setText(str: string) {
    const f = 'setText';
    console.log(this.c, f);
    console.log(this.c, f, 'str', str);
    if (null != str) { this.buttonText = str; }
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

    this.guiUtil = new GuiUtil(translate);
    this.gui = new DioGui(this.guiUtil);

    this.source = new DioSource(translate, httpClient, utilsHttp, environmentMappingService);
    this.sourceSubscription = this.source.item.subscribe(data => {
      console.log(this.c, f, 'sourceSubscription', data);
      if (null != data) {
        const source = data['source'];
        const result = data['result'];
        console.log(this.c, f, 'source', source);
console.log(this.c, f, 'result', result);
        if ('DbmRead' === source) {
          this.updateLabel(data['attributes']);
          this.selectValues = this.gui.getValueTableWithTranslate(data['valueTable']);
console.log(this.c, f, 'this.selectValues', this.selectValues);
          this.updateButton(this.selectedValue);
          this.onDbmReadUpdate(result);
        } else if ('DbmPolling' === source) {
          this.updateMessage();
          this.updateButton(this.selectedValue);
          this.onDbmPollingUpdate(result);
        }
      }
    });

    this.ctlSendCommand = new CtlSendIntCommand(httpClient, utilsHttp, environmentMappingService);
    this.ctlSendCommandSubscription = this.ctlSendCommand.item.subscribe(data => {
      console.log(this.c, f, 'ctlSendCommandSubscription', data);
      if (null != data) {
      }
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
    this.sourceSubscription.unsubscribe();
    this.source.disconnect();

    this.ctlSendCommandSubscription.unsubscribe();
  }

  loadTranslations(): void {
    const f = 'loadTranslations';
    console.log(this.c, f);
  }

  onDbmReadUpdate(result: any): void {
    const f = 'onDbmReadUpdate';
    console.log(this.c, f);
    if ( null != result ) {
console.log(this.c, f, 'result', result);

      result['env'] = this.cfg.dbm.env;
      result['alias'] = this.cfg.dbm.alias;
      result['point'] = this.cfg.dbm.attributes;
      this.notifyParent.emit([result]);
    }
  }

  onDbmPollingUpdate(result: any): void {
    const f = 'onDbmPollingUpdate';
    console.log(this.c, f);
    if ( null != result ) {
console.log(this.c, f, 'result', result);

      result['env'] = this.cfg.dbm.env;
      result['alias'] = this.cfg.dbm.alias;
      result['point'] = this.cfg.dbm.attributes;
      this.notifyParent.emit([result]);
    }
  }

  updateLabel(attributes) {
    const f = 'updateLabel';
    console.log(this.c, f);
    console.log(this.c, f, 'event', event);
    this.labelText = this.guiUtil.getDisplayMessage(attributes['label']);
console.log(this.c, f, 'this.labelText', this.labelText);
  }

  updateMessage() {
    const f = 'updateMessage';
    console.log(this.c, f);
    const pointLvAttributeName = this.source.getPointLvValue();
console.log(this.c, f, 'pointLvAttributeName', pointLvAttributeName);
    const alias = pointLvAttributeName['.execStatus'];
    console.log(this.c, f, 'alias', alias);

const dynamicData = this.source.getDynamicData();
    const value = dynamicData[alias];
    console.log(this.c, f, 'value', value);

    switch (value) {
      case 0:
      case 1:
      {
        this.messageClass = 'alert alert-info';
        this.messageText  = this.guiUtil.getDisplayMessage('&control_command_init');
      }
      {
        this.alertClass = 'alert alert-info';
        this.alertTitle = this.guiUtil.getDisplayMessage('&info');
        this.alertText  = this.guiUtil.getDisplayMessage('&control_command_init');
      }
      break;
      case 2:
      {
        this.messageClass = 'alert alert-info';
        this.messageText  = this.guiUtil.getDisplayMessage('&control_command_sending');
      }
      {
        this.alertClass = 'alert alert-info';
        this.alertTitle = this.guiUtil.getDisplayMessage('&info');
        this.alertText  = this.guiUtil.getDisplayMessage('&control_command_sending');
      }
      break;
      case 3:
      {
        this.messageClass = 'alert alert-success';
        this.messageText  = this.guiUtil.getDisplayMessage('&control_command_succeed');
      }
      {
        this.alertClass = 'alert alert-success';
        this.alertTitle = this.guiUtil.getDisplayMessage('&success');
        this.alertText  = this.guiUtil.getDisplayMessage('&control_command_succeed');
      }
      break;
      case 4:
      case 5:
      {
        this.messageClass = 'alert alert-warning';
        this.messageText = this.guiUtil.getDisplayMessage('&control_command_failed');
      }
      {
        this.alertClass = 'alert alert-warning';
        this.alertTitle = this.guiUtil.getDisplayMessage('&warning');
        this.alertText = this.guiUtil.getDisplayMessage('&control_command_failed');
      }
      break;
    }
  }

  onButton(event) {
    const f = 'onButton';
    console.log(this.c, f);
    console.log(this.c, f, 'event', event);

    console.log(this.c, f, 'this.selectedValue', this.selectedValue);
    if (null != this.selectedValue) {
      const id = this.selectedValue['id'];
      console.log(this.c, f, 'id', id);

      let value;
      {
        const staticData = this.source.getStaticData();
console.log(this.c, f, 'staticData', staticData);
        const env = staticData['env'];
        const alias = staticData['alias'];
        const point = staticData['point'];
        console.log(this.c, f, 'env', env, 'alias', alias, 'point', point);

        const dioPoint = alias + point['point'];
        console.log(this.c, f, 'dioPoint', dioPoint);

        const valueTableValueColumn = point['.valueTable(0:$,value)'];
        console.log(this.c, f, 'valueTableValueColumn', valueTableValueColumn);
        const dovValuesAlias = dioPoint + valueTableValueColumn;
        console.log(this.c, f, 'dovValuesAlias', dovValuesAlias);

        const values = staticData[dovValuesAlias];
console.log(this.c, f, 'values', values);

        value = values[id];
        console.log(this.c, f, 'value', value);

        {
          const key = env + dioPoint + value;
          this.ctlSendCommand.sendCommand(key, env, [dioPoint], value);
        }
      }
    }
  }

  updateButton(event) {
    const f = 'updateButton';
    console.log(this.c, f);
    console.log(this.c, f, 'event', event);
    if (null != event) {
      const id = event['id'];
      console.log(this.c, f, 'id', id);
      const dynamicData = this.source.getDynamicData();
      const dovAddress = this.source.getInternalLvValue();
console.log(this.c, f, 'dovAddress', dovAddress);
      this.buttonDisabled = this.gui.getDovAddressValue(dynamicData, dovAddress, event);
    }
  }

  onSelectValue(event) {
    const f = 'onSelectChange';
    console.log(this.c, f);
    console.log(this.c, f, 'event', event);
    this.selectedValue = event;
    this.updateButton(this.selectedValue);
  }

}
