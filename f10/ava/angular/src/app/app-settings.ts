
export class AppSettings {

    public static readonly STR_URL_SETTINGS = './assets/config/settings.json';

    public static readonly STR_RESPONSE = 'response';

    public static readonly STR_TITLE = 'title';

    // Default case for the each widget
    public static readonly STR_INIT = 'init';

    // Common event message for widgets communication
    // Each which should handle it if they has a dependent to the source of the widget
    public static readonly STR_NOTIFY_FROM_PARENT = 'notifyFromParent';
}
