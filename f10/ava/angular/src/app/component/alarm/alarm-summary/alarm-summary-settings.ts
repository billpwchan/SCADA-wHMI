import { AppSettings } from '../../../app-settings';
import { DbmSettings } from '../../../service/scs/dbm-settings';

export class AlarmSummarySettings {

  public static readonly STR_CONFIG                     = 'config';

  public static readonly STR_ENVS                       = 'envs';

  public static readonly STR_INSTANCE_CLASSNAME         = 'instance_classname';
  public static readonly STR_INSTANCE_ROOT              = 'instance_root';
  public static readonly STR_MAX_RULE_NUM               = 'max_rule_num';


  public static readonly STR_AVAR_BASE                  = 'avar_base';
  public static readonly STR_MAX_AVAR_NUM               = 'max_avar_num';

  public static readonly STR_RULE_BASE                  = 'rule_base';
  public static readonly STR_CONDITION_BEGIN_ID         = 'condition_begin_id';
  public static readonly STR_CONDITION_END_ID           = 'condition_end_id';
  public static readonly STR_FORMULA_DEF_VAL            = 'formula_default_value';
  public static readonly STR_FORMULA_ZERO_DEF_VAL       = 'formula_zero_default_value';

  public static readonly STR_AVAR_PREFIX                = 'AVAR';
  public static readonly STR_AVAS_PREFIX                = 'AVAS';
  public static readonly STR_RULE_PREFIX                = 'RULE';

  public static readonly RULE_ATTR_LIST                 = [
                                                            DbmSettings.STR_ATTR_UNIVNAME
                                                            , DbmSettings.STR_ATTR_NAME
                                                            , DbmSettings.STR_ATTR_INDEX
                                                            , DbmSettings.STR_ATTR_LABEL
                                                            , DbmSettings.STR_ATTR_ENABLE
                                                            , DbmSettings.STR_ATTR_CONDITION_GL
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
  public formulaDefaultVal: number;
  public formulaZeroDefaultVal: number;
}

export class AlarmSummaryConfig {
  public avarBase: number;
  public maxAvarNum: number;
}