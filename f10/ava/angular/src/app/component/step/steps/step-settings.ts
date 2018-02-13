import { DbmSettings } from '../../../service/scadagen/dbm/dbm-settings';

export class StepsSettings {

    public static readonly STR_STEP_PREFIX = 'step_prefix';
    public static readonly STR_STEP_BASE = 'step_base';

    public static readonly STR_GEO_PREFIX = 'geo_prefix';
    public static readonly STR_FUNC_PREFIX = 'func_prefix';

    public static readonly STR_EQPLABEL_PREFIX = 'eqplabel_prefix';
    public static readonly STR_POINTLABEL_PREFIX = 'pointlabel_prefix';
    public static readonly STR_VALUE_PREFIX = 'value_prefix';

    public static readonly STR_READ_STEP_ATTR_LIST = [
        DbmSettings.STR_ATTR_UNIVNAME
        , DbmSettings.STR_ATTR_NAME
        , DbmSettings.STR_ATTR_GEO
        , DbmSettings.STR_ATTR_FUNC
        , DbmSettings.STR_ATTR_EQUIPMENT_LABEL
        , DbmSettings.STR_ATTR_POINT_FUNC
        , DbmSettings.STR_ATTR_VALUE
        , DbmSettings.STR_COLON + DbmSettings.STR_VALUETABLE_VALUE
        , DbmSettings.STR_COLON + DbmSettings.STR_VALUETABLE_LABEL
    ];

    public static readonly STR_READ_STEP = 'readStep';
}

export class StepsConfig {

}
