import { NgActiveBackdropCfg } from '../ng-active-backdrop/ng-active-backdrop-settings';

export class NgActiveTextSettings {
  public static readonly STR_NOTIFY_PARENT = 'notifyParent';
  public static readonly STR_NOTIFY_FROM_PARENT = 'notifyFromParent';
}

export class NgActiveTextDbmCfg {
  public env: string;
  public alias: string;
  public attributes: string[];
}
export class NgActiveTextClassCfg {
  public span: string;
}
export class NgActiveTextCfg {
  public class: NgActiveTextClassCfg;
  public dbm: NgActiveTextDbmCfg;
}
export class NgActiveEqpInfoPointCfg {
  public labelCfg: NgActiveTextCfg;
  public valueCfg: NgActiveTextCfg;
  public statusCfg: NgActiveBackdropCfg;
}
export class NgActiveEqpInfoPointGui {
  public labelText: string;
  public valueText: string;
  public statusText: string;
  public statusBackdrop: string;
}
export class NgActiveEqpInfoPoint {
  public cfg: NgActiveEqpInfoPointCfg;
  public gui: NgActiveEqpInfoPointGui;
}
export class NgActiveTextUpdate {
  public cfg: NgActiveEqpInfoPointCfg;
  public gui: NgActiveEqpInfoPointGui;
}

