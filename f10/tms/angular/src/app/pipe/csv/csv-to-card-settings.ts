export class CsvToCardSettings {

    public static readonly STR_FILENAME = 'tms.csv';
    public static readonly STR_EMPTY = '';
    public static readonly STR_COMMA = ',';
    public static readonly STR_QUOTE = '\"';
    public static readonly STR_EOL  = '\r\n';
}

export enum ExecutionIndex {
  PHASE_TYPE = 0
  , EXEC_TYPE
  , EXEC_NAME
  , EXEC_VALUE
}

export enum TokenIndex {
  // Card Session
  CARD_NAME = 0
  , CARD_STATE
  , CARD_STEP

  // Step Session
  , STEP_STEP
  , STEP_STATE
  , STEP_DELAY
  , STEP_EXECUTE

  // Equipment Session
  , EQUIPMENT_CONNADDR
  , EQUIPMENT_ENVLABEL
  , EQUIPMENT_UNIVNAME
  , EQUIPMENT_CLASSID
  , EQUIPMENT_GEO
  , EQUIPMENT_FUNC
  , EQUIPMENT_EQPLABEL
  , EQUIPMENT_POINTLABEL
  , EQUIPMENT_INITLABEL
  , EQUIPMENT_VALUELABEL
  , EQUIPMENT_CURRENTLABEL

  // Execution Session
  , EXECUTION_BASE
}
