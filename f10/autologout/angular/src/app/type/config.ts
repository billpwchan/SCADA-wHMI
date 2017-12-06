// definition of the Config structure
export interface Config {
    // Identifier of the browser storage
    storage: {
        // name of the storage
        idName: string,

        // name of the field containing whether auto logout is enabled or not
        enabledName: string,

        // name of the field containing the idle threshold value
        idleThresholdMsName: string,

        // name of the field containing the warning threshold value
        warnThresholdMsName: string
    };

    // default values to be used, in case the values cannot be obtained from the browser storage
    defaultValue: {
        // whether auto logout is enabled or not
        enabled: boolean,

        // idle threshold value, in ms
        idleThresholdMs: number,

        // warning threshold value, in ms
        warnThresholdMs: number
    };

    // i18n settings
    i18n: {
        // detault language to use, if translation of the user's language is not available
        default_lang: string;

        // resolve language code from user's browser default language
        resolve_by_browser_lang: boolean;
        // resolve culture language from user's browser language or not, only used when resolve_by_browser_lang is true
        use_culture_lang: boolean;

        // resolve language code from user's browser cookie
        resolve_by_browser_cookie: boolean;
        // name of cookie containing the language code, only used when resolve_by_browser_cookie is true
        use_cookie_name: string;
    };
}

// default values of the Config structure
export const CONFIG_DEFAULT: Config = {
    'storage': {
        'idName': 'SCADAGEN_AUTOLOGOUT',
        'enabledName': 'enabled',
        'idleThresholdMsName': 'idleThresholdMs',
        'warnThresholdMsName': 'warnThresholdMs'
    },
    'defaultValue': {
        'enabled': false,
        'idleThresholdMs': 15 * 60 * 1000,
        'warnThresholdMs': 13 * 60 * 1000
    },
    'i18n': {
        'default_lang': 'en',
        'resolve_by_browser_lang': true,
        'use_culture_lang': false,
        'resolve_by_browser_cookie': false,
        'use_cookie_name': 'lang'
    }
};
