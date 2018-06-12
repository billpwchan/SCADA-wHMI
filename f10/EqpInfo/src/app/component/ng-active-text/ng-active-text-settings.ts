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
export class NgActiveEqpPointCfg {
  public labelCfg: NgActiveTextCfg;
  public valueCfg: NgActiveTextCfg;
  public statusCfg: NgActiveBackdropCfg;
}
export class NgActiveEqpPointGui {
  public labelText: string;
  public valueText: string;
  public statusText: string;
  public statusBackdrop: string;
}
export class NgActiveEqpPoint {
  public cfg: NgActiveEqpPointCfg;
  public gui: NgActiveEqpPointGui;
}
export class NgActiveTextUpdate {
  public cfg: NgActiveEqpPointCfg;
  public gui: NgActiveEqpPointGui;
}

