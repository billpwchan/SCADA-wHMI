export class AlarmsSettings {
    public static readonly STR_COL_HEADER_PREFIX        = 'col_header_prefix';
    public static readonly STR_COL_HEADER_IDS           = 'col_header_ids';
    public static readonly STR_COL_WIDTH                = 'col_width';

    public static readonly STR_ROW_HEADER_PREFIX        = 'row_header_prefix';
    public static readonly STR_ROW_HEADER_IDS           = 'row_header_ids';
    public static readonly STR_ROW_HEADER_WIDTH         = 'row_header_width';

    public static readonly STR_DELFAULT_CELL_EMPTY      = '&alarms_default_cell_empty';
    public static readonly STR_DELFAULT_CELL_COMMA      = '&alarms_default_cell_comma';
    public static readonly STR_DELFAULT_CELL_INCORRECT  = '&alarms_default_cell_incorrect';

    public static readonly STR_ALARM_SERVERITYS         = 'alarm_serveritys';
}

export class AlarmServerity {
    index: number;
    label: string;
    shortlabel: string;
    title: string;
}

export class AlarmServeritySelection {
    x: number;
    x2: number;
    y: number;
    y2: number;
}
