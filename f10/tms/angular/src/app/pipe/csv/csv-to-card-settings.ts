export class CsvToCardSettings {

    public static readonly STR_FILENAME = 'tms.csv';
    public static readonly STR_EMPTY = '';
    public static readonly STR_COMMA = ',';
    public static readonly STR_EOL  = '\r\n';
}

export enum TokenIndex {
  CARD_NAME = 0
  , CARD_STATE
  , CARD_STEP

  , STEP_STEP
  , STEP_STATE
  , STEP_DELAY

  , EQUIPMENT_CONNADDR
  , EQUIPMENT_UNIVNAME
  , EQUIPMENT_CLASSID
  , EQUIPMENT_GEO
  , EQUIPMENT_FUNC
  , EQUIPMENT_EQPLABEL
  , EQUIPMENT_POINTLABEL
  , EQUIPMENT_VALUELABEL

  , PHASE_TYPE
  
  , EXEC_TYPE
  , EXEC_NAME
  , EXEC_VALUE
}
