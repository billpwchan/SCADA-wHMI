
export class AppSettings {

    public static readonly STR_URL_SETTINGS = './assets/config/settings.json';

    public static readonly STR_RESPONSE = 'response';

    public static readonly STR_TITLE = 'title';

    // Default case for the each widget
    public static readonly STR_INIT = 'init';

    // Common event message for widgets communication
    // Each which should handle it if they has a dependent to the source of the widget
    //
    // Handle List in the Cards widget already reloaded
    // Source: Cards Widget
    // Use Case: Card(s) already Added/Deleted, Card Conteiner reloaded
    public static readonly STR_CARD_RELOADED = 'cardreloaded';

    // Handle Card(s) contain in the the Cards widget already modified
    // Source: Cards Widget
    // Use Case: Actor(Operation) modified the Card details
    public static readonly STR_CARD_MODIFIED = 'cardmodified';

    // Handle Card(s) contain in the the Cards widget already updated
    // Source: Cards Widget
    // Use Case: Actor(Data Source) updated the Card state/status
    public static readonly STR_CARD_UPDATED = 'cardupdated';

    // Handle Cards Widget has a(n) card(s) selected
    // Source: Cards Widget
    public static readonly STR_CARD_SELECTED = 'cardselected';

    // Handle Steps List in the Steps widget already reloaded
    // Source: Steps Widget
    // Use Case: Step(s) already Added/Deleted
    public static readonly STR_STEP_RELOADED = 'stepreloaded';

    // Handle Step(s) contain in the the Steps widget already modified
    // Source: Steps Widget
    // Use Case: Actor(Operation) modified the Step details
    public static readonly STR_STEP_MODIFIED = 'stepmodified';

    // Handle Step(s) contain in the the Steps widget already updated
    // Source: Steps Widget
    // Use Case: Actor(Data Source) updated the Step state/status
    public static readonly STR_STEP_UPDATED = 'stepupdated';

    // Handle Steps Widget has a(n) step(s) selected
    // Source: Steps Widget
    public static readonly STR_STEP_SELECTED = 'stepselected';
}
