export class DbmSettings {

    public static readonly INT_ACI_TYPE = 106;
    public static readonly INT_DCI_TYPE = 104;
    public static readonly STR_OPEN_PARENTHESIS = '(';
    public static readonly STR_CLOSE_PARENTHESIS = ')';
    public static readonly STR_OPEN_BRACKET = '[';
    public static readonly STR_CLOSE_BRACKET = ']';
    public static readonly STR_EQUAL = '=';
    public static readonly STR_COLON = ':';
    public static readonly STR_URL_ALIAS = '%3Calias%3E';
    public static readonly STR_ALIAS = '<alias>';
    public static readonly STR_AAC = 'aac';
    public static readonly STR_DAC = 'dac';
    public static readonly STR_DAL = 'dal';
    public static readonly STR_QUOTE = '%22';

    public static readonly STR_INITVALUE = '.initValue';
    public static readonly STR_VETABLE_VENAME = '.veTable(0:$,vename)';
    public static readonly STR_VALUETABLE_LABEL = DbmSettings.STR_DAL + '.valueTable(0:$,label)';
    public static readonly STR_VALUETABLE_VALUE = DbmSettings.STR_DAL + '.valueTable(0:$,value)';
    public static readonly STR_URL_MULTIREAD = '/scs/service/DbmComponent/multiReadValue?dbaddress=';
    public static readonly STR_URL_MULTIWRITE = '/scs/service/DbmComponent/multiWriteValue?values=';
    public static readonly STR_URL_GET_INSTANCES_BY_CLASSNAME = '/scs/service/DbmComponent/GetInstancesByClassName?className=';
    public static readonly STR_URL_GET_CHILDREN_ALIASES = '/scs/service/DbmComponent/GetChildrenAliases?dbaddress=';

    public static readonly STR_ATTR_DBVALUE = 'dbvalue';
    public static readonly STR_ATTR_CLASSID = 'classId';
    public static readonly STR_ATTR_CHILDREN_ALIASES = 'childrenAliases';
    public static readonly STR_ATTR_INSTANCES = 'instances';

    public static readonly STR_URL_GETCLASSID = '/scs/service/DbmComponent/GetClassId?dbaddress=';

    public static readonly STR_ACQVALUE = '.acqValue';
    public static readonly STR_AAC_ACQVALUE = DbmSettings.STR_AAC + DbmSettings.STR_ACQVALUE;
    public static readonly STR_DAC_ACQVALUE = DbmSettings.STR_DAC + DbmSettings.STR_ACQVALUE;
    public static readonly STR_FORMULAS = 'formulas';
    public static readonly STR_FORMULAS_ACQ_SINGLE = '[.veTable(0,value)]';
    public static readonly STR_URL_GETATTRIBUTEFORMULAS  = '/scs/service/DbmComponent/GetAttributeFormulas?dbaddress=';
    public static readonly STR_URL_SETATTRIBUTEFORMULA  = '/scs/service/DbmComponent/SetAttributeFormula?dbaddress=';
    public static readonly STR_FORMULA_OPTION = '&formula=';

    public static readonly STR_ATTR_VALUE = '.value';
    public static readonly STR_ATTR_UNIT = '.unit';
    public static readonly STR_ATTR_COMPUTED_MESSAGE = '.computedMessage';
    public static readonly STR_ATTR_LEVEL = '.level';
    public static readonly STR_ATTR_GEO = ':^.geographicalCat';
    public static readonly STR_ATTR_FUNC = ':^.functionalCat';
    public static readonly STR_ATTR_EQUIPMENT_LABEL = ':^.label';
    public static readonly STR_ATTR_POINT_FUNC = '.label';

    public static readonly STR_RULE = 'RULE';
    public static readonly STR_THREE_ZERO = '000';
    public static readonly STR_ATTR_NAME = '.name';
    public static readonly STR_ATTR_UNIVNAME = '.UNIVNAME';
    public static readonly STR_ATTR_CONDITION = '.condition';
}
