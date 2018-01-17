import { AppSettings } from '../../../app-settings';

export class AlarmSummarySettings {

  public static readonly STR_INIT = AppSettings.STR_INIT;

  public static readonly STR_NORIFY_FROM_PARENT         = 'notifyFromParent';
  public static readonly STR_CONFIG                     = 'config';

  public static readonly STR_ENVS                       = 'envs';
}

export class Env {
  label: string;
  value: string;
}

export class AlarmSummaryConfig {
  public envs: Env[];
}
