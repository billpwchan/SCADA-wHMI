export class StepEditSettings {

    public static readonly STR_STEP_EDIT_EMPTY = '';

    public static readonly INT_ACI_DEFAULT_VALUE = 0;
    public static readonly INT_DCI_DEFAULT_VALUE = 0;

    public static readonly STR_STEP_EDIT_ENABLE = 'stepeditenable';

    public static readonly STR_ENVS     = 'envs';
    public static readonly STR_LABEL    = 'label';
    public static readonly STR_VALUE    = 'value';

    public static readonly STR_OLS_LST_SERVER = 'ols_lst_server';
    public static readonly STR_OLS_LST_NAME = 'ols_lst_name';

    public static readonly STR_GEO_PREFIX = 'geo_prefix';
    public static readonly STR_FUNC_PREFIX = 'func_prefix';

    public static readonly STR_OLS_LST_FILTER = 'ols_lst_filter';
}

export class StepEditConfig {
    public geoPrefix: string;
    public funcPrefix: string;
    public envs: any;
    public olsLstServer: string;
    public olsLstName: string;
    public olsLstFilter: string;
}
