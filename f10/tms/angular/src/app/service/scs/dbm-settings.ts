export class DbmSettings {

    public static readonly INT_ACI_TYPE = 106;
    public static readonly INT_DCI_TYPE = 104;
    public static readonly STR_ALIAS = '%3Calias%3E';
    public static readonly STR_AAC = 'aac';
    public static readonly STR_DAC = 'dac';
    public static readonly STR_DAL = 'dal';

    public static readonly STR_INITVALUE = '.initValue';
    public static readonly STR_VETABLE_VENAME = '.veTable(0:$,vename)';
    public static readonly STR_VALUETABLE_LABEL = DbmSettings.STR_DAL + '.valueTable(0:$,label)';
    public static readonly STR_VALUETABLE_VALUE = DbmSettings.STR_DAL + '.valueTable(0:$,value)';
    public static readonly STR_URL_MULTIREAD = '/scs/service/DbmComponent/multiReadValue?dbaddress=';

    public static readonly STR_ATTR_DBVALUE = 'dbvalue';
    public static readonly STR_ATTR_CLASSID = 'classId';

    public static readonly STR_URL_GETCLASSID = '/scs/service/DbmComponent/GetClassId?dbaddress=';

    public static readonly STR_ACQVALUE = '.acqValue';
    public static readonly STR_AAC_ACQVALUE = DbmSettings.STR_AAC + DbmSettings.STR_ACQVALUE;
    public static readonly STR_DAC_ACQVALUE = DbmSettings.STR_DAC + DbmSettings.STR_ACQVALUE;
    public static readonly STR_FORMULAS = 'formulas';
    public static readonly STR_FORMULAS_ACQ_SINGLE = '[.veTable(0,value)]';
    public static readonly STR_URL_GETATTRIBUTEFORMULAS  = '/scs/service/DbmComponent/GetAttributeFormulas?dbaddress=';


    public static readonly STR_COMPUTED_MESSAGE = '.computedMessage';
}
