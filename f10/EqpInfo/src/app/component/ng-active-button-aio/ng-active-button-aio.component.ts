import { Component, OnInit, Input, Output, EventEmitter, OnDestroy, OnChanges, SimpleChanges } from '@angular/core';
// tslint:disable-next-line:max-line-length
import { NgActiveButtonCfg, NgActiveButtonSettings, NgActiveButtonClassCfg, NgActiveButtonDbmCfg, NgActiveButtonUpdate } from './ng-active-button-aio-settings';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { SettingsService } from '../../service/settings.service';
import { HttpClient } from '@angular/common/http';
import { UtilsHttpModule } from '../../service/scadagen/common/utils-http.module';
import { EnvironmentMappingService } from '../../service/scadagen/envs/environment-mapping.service';
import { Subscription } from 'rxjs/Subscription';
import { GuiUtil } from '../../common/gui-util';
import { CtlSendIntCommand } from '../../service/scadagen/ctl/simple/ctl-send-int-commend';
import { CtlSendFloatCommand } from '../../service/scadagen/ctl/simple/ctl-send-float-commend';
import { AioSource } from '../../service/scadagen/dbm/helper/aio/aio-source';
import { AioGui } from '../../service/scadagen/dbm/helper/aio/aio-gui';

@Component({
  selector: 'app-ng-active-button-aio',
  templateUrl: './ng-active-button-aio.component.html',
  styleUrls: ['./ng-active-button-aio.component.css']
})
export class NgActiveButtonAioComponent implements OnInit, OnDestroy {

  readonly c = 'NgActiveButtonAioComponent';

  groupClass = 'form-group';

  labelClass = '';
  labelText = '-';

  buttonClass = 'btn';
  buttonText = 'Execute';
  buttonDisabled = true;

  inputValue: '0.0';

  messageClass = 'badge';
  messageText = 'message';

  alertClass = '';
  alertTitle = '';
  alertText = '';

  private guiUtil: GuiUtil;
  private gui: AioGui;

  private cfg: NgActiveButtonCfg;

  private source: AioSource;
  sourceSubscription: Subscription;

  private ctlSendCommand: CtlSendFloatCommand;
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
    this.gui = new AioGui(this.guiUtil);

    this.source = new AioSource(translate, httpClient, utilsHttp, environmentMappingService);
    this.sourceSubscription = this.source.item.subscribe(data => {
      console.log(this.c, f, 'sourceSubscription', data);
      if (null != data) {
        const source = data['source'];
        const result = data['result'];
        console.log(this.c, f, 'source', source);
console.log(this.c, f, 'result', result);
        if ('DbmRead' === source) {
          this.updateLabel(data['attributes']);
          this.updateButton();
          this.onDbmReadUpdate(result);
        } else if ('DbmPolling' === source) {
          this.updateMessage();
          this.updateButton();
          this.onDbmPollingUpdate(result);
        }
      }
    });

    this.ctlSendCommand = new CtlSendFloatCommand(httpClient, utilsHttp, environmentMappingService);
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
    const pointLvAttributeName = this.source.getPointLvAddress();
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

console.log(this.c, f, 'this.inputValue', this.inputValue);
    let value;
    {
      const staticData = this.source.getStaticData();
      {
        const env = staticData['env'];
        const alias = staticData['alias'];
        const point = staticData['point'];
        console.log(this.c, f, 'env', env, 'alias', alias, 'point', point);
        value = this.inputValue;

        const dioPoint = alias + point['point'];
        console.log(this.c, f, 'dioPoint', dioPoint);
        {
          const key = env + dioPoint + value;
          this.ctlSendCommand.sendCommand(key, env, [dioPoint], value);
        }
      }
    }
  }

  updateButton() {
    const f = 'updateButton';
    console.log(this.c, f);
      const dynamicData = this.source.getDynamicData();
console.log(this.c, f, 'dynamicData', dynamicData);

    let value = 0;
    if (null != dynamicData) {
      const pointLvAttributeName = this.source.getPointLvAddress();
      console.log(this.c, f, 'pointLvAttributeName', pointLvAttributeName);
      const alias = pointLvAttributeName['.execStatus'];
      console.log(this.c, f, 'alias', alias);
      value = dynamicData[alias];
    }
    this.buttonDisabled = (value < 3 ? true : false);
  }

}
