import { NgActiveBackdropCfg } from '../ng-active-backdrop/ng-active-backdrop-settings';

export class NgActiveButtonSettings {
  public static readonly STR_NOTIFY_PARENT = 'notifyParent';
  public static readonly STR_NOTIFY_FROM_PARENT = 'notifyFromParent';
}

export class NgActiveButtonDbmCfg {
  public env: string;
  public alias: string;
  public attributes: string[];
}
export class NgActiveButtonClassCfg {
  public button: string;
}
export class NgActiveButtonCfg {
  public class: NgActiveButtonClassCfg;
  public dbm: NgActiveButtonDbmCfg;
}
export class NgActiveEqpPointCfg {
  public labelCfg: NgActiveButtonCfg;
  public valueCfg: NgActiveButtonCfg;
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
export class NgActiveButtonUpdate {
  public cfg: NgActiveEqpPointCfg;
  public gui: NgActiveEqpPointGui;
}

