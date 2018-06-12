import { NgActiveBackdropCfg } from '../ng-active-backdrop/ng-active-backdrop-settings';

export class NgActiveNumberSettings {
  public static readonly STR_NOTIFY_PARENT = 'notifyParent';
  public static readonly STR_NOTIFY_FROM_PARENT = 'notifyFromParent';
}

export class NgActiveNumberDbmCfg {
  public env: string;
  public alias: string;
  public point: string[];
}
export class NgActiveNumberClassCfg {
  public span: string;
}
export class NgActiveNumberCfg {
  public class: NgActiveNumberClassCfg;
  public dbm: NgActiveNumberDbmCfg;
  public format: string;
}
export class NgActiveEqpPointCfg {
  public labelCfg: NgActiveNumberCfg;
  public valueCfg: NgActiveNumberCfg;
  public statusCfg: NgActiveBackdropCfg;
}
export class NgActiveEqpPointGui {
  public labelNumber: string;
  public valueNumber: string;
  public statusBackdrop: string;
}
export class NgActiveEqpPoint {
  public cfg: NgActiveEqpPointCfg;
  public gui: NgActiveEqpPointGui;
}
export class NgActiveNumberUpdate {
  public cfg: NgActiveEqpPointCfg;
  public gui: NgActiveEqpPointGui;
}
