export class CtlSettings {

  public static readonly STR_RESPONSE = 'response';

  public static readonly STR_FIRST_MESSAGE = 'firstMessage';

  public static readonly STR_URL_CTL_COMPONENT = '/scs/service/CtlComponent';

  public static readonly STR_ATTR_NAMES = '?names=';

  public static readonly STR_URL_SEND_INT_CMD = CtlSettings.STR_URL_CTL_COMPONENT + '/SendIntCommand' + CtlSettings.STR_ATTR_NAMES;
  public static readonly STR_URL_SEND_FLOAT_CMD = CtlSettings.STR_URL_CTL_COMPONENT + '/SendFloatCommand' + CtlSettings.STR_ATTR_NAMES;
  public static readonly STR_URL_SEND_STRING_CMD = CtlSettings.STR_URL_CTL_COMPONENT + '/SendStringCommand' + CtlSettings.STR_ATTR_NAMES;

  public static readonly STR_ATTR_VALUE = '&value=';

  public static readonly STR_ATTR_SEND_ANYWAY = '&sendAnyway=';
  public static readonly STR_ATTR_BYPASS_RET_COND = '&bypassRetCond=';
  public static readonly STR_ATTR_BYPASS_INIT_COND = '&bypassInitCond=';
}
