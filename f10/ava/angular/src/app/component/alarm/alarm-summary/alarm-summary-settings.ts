import { AppSettings } from '../../../app-settings';

export class AlarmSummarySettings {

  public static readonly STR_INIT = AppSettings.STR_INIT;

  public static readonly STR_NORIFY_FROM_PARENT         = 'notifyFromParent';
  public static readonly STR_CONFIG                     = 'config';

  public static readonly STR_ENVS                       = 'envs';

  public static readonly STR_INSTANCE_CLASSNAME         = 'instance_classname';
  public static readonly STR_INSTANCE_ROOT              = 'instance_root';
  public static readonly STR_MAX_RULE_NUM               = 'max_rule_num';

  public static readonly STR_RULE_BASE                  = 'rule_base';
  public static readonly STR_CONDITION_BEGIN_ID         = 'condition_begin_id';
  public static readonly STR_CONDITION_END_ID           = 'condition_end_id';

  public static readonly STR_AVAR_PREFIX                = 'AVAR';
  public static readonly STR_AVAS_PREFIX                = 'AVAS';
  public static readonly STR_RULE_PREFIX                = 'RULE';

  public static readonly STR_RULE_UNIVNAME              = '.UNIVNAME';
  public static readonly STR_RULE_INDEX                 = '.index';
  public static readonly STR_RULE_NAME                  = '.label';
  public static readonly STR_RULE_ENABLE                = '.enable';
  public static readonly STR_RULE_CONDITION_GL          = '.conditionGL';
  public static readonly RULE_ATTR_LIST                 = [
                                                            AlarmSummarySettings.STR_RULE_UNIVNAME
                                                            , AlarmSummarySettings.STR_RULE_INDEX
                                                            , AlarmSummarySettings.STR_RULE_NAME
                                                            , AlarmSummarySettings.STR_RULE_ENABLE
                                                            , AlarmSummarySettings.STR_RULE_CONDITION_GL
                                                          ];

}

export class Env {
  label: string;
  value: string;
}

export class AVASummaryConfig {
  public envs: Env[];
}

export class CardSummaryConfig {
  public instanceClassName: string;
  public instanceRoot: string;
  public ruleBase: number;
  public maxRuleNum: number;
}

export class StepSummaryConfig {
  public ruleBase: number;
  public conditionBeginId: number;
  public conditionEndId: number;
}

export class AlarmSummaryConfig {
}
