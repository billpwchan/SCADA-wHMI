
export class AppSettings {

    public static readonly STR_NAME_CARD = 'card';
    public static readonly STR_NAME_STEP = 'step';

    public static readonly INT_CARD_TYPE_START = 0;
    public static readonly INT_CARD_TYPE_STOP = 1;
    public static readonly INT_CARD_TYPE_RESTART = 2;

    public static readonly INT_CARD_STOP = 0;
    public static readonly INT_CARD_STOP_RUNNING = 1;
    public static readonly INT_CARD_PAUSE = 2;
    public static readonly INT_CARD_START = 3;
    public static readonly INT_CARD_START_RUNNING = 4;

    public static readonly STR_CARD_UNKNOW = 'Unknow';
    public static readonly STR_CARD_STOP = 'Stop';
    public static readonly STR_CARD_STOP_RUNNING = 'Running';
    public static readonly STR_CARD_PAUSE = 'Pause';
    public static readonly STR_CARD_START = 'Start';
    public static readonly STR_CARD_START_RUNNING = 'Running';

    public static readonly STR_STEP_UNKNOW = 'Unknow';
    public static readonly STR_STEP_STOP = 'Stop';
    public static readonly STR_STEP_STOP_RUNNING = 'Running stop';
    public static readonly STR_STEP_STOP_FAILD = 'Stop failed';
    public static readonly STR_STEP_START = 'Start';
    public static readonly STR_STEP_START_RUNNING = 'Running start';
    public static readonly STR_STEP_START_FAILD = 'start faild';

    public static readonly STR_RESPONSE = 'response';

    public static readonly STR_GEO_PREFIX = '&sc_whmi_geo_';
    public static readonly STR_FUNC_PREFIX = '&sc_whmi_func_';

    public static readonly STR_M100 = 'M100';
    public static readonly STR_M001 = 'M001';
    public static readonly STR_M002 = 'M002';

    public static readonly STR_CONN_M100 = 'http://127.0.0.1:8990';
    public static readonly STR_CONN_M001 = 'http://127.0.0.1:8991';
    public static readonly STR_CONN_M002 = 'http://127.0.0.1:8992';


    // Common event should be handle hy each widget
    public static readonly STR_INIT = 'init'; // Default case for the each widget
    public static readonly STR_CARD_RELOADED = 'cardreloaded'; // Handle the Card reloaded
    public static readonly STR_CARD_UPDATED = 'cardupdated'; // Handle the Card updated
    public static readonly STR_CARD_SELECTED = 'cardselected'; // Handle the Card selected
    public static readonly STR_STEP_RELOADED = 'stepreloaded'; // Handle the Step reloaded
    public static readonly STR_STEP_UPDATED = 'stepupdated'; // Handle the Step updated
    public static readonly STR_STEP_SELECTED = 'stepselected'; // Handle the Step selected

}
