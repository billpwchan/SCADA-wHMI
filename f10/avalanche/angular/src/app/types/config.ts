export interface Config {
    // URL to SCADA DB
    scs_db_url: string;
    // default client name to identify sender
    default_client_name: string;
    // i18n related configuration
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
    ava_cond: {
        // default filter for equipment task
        filter: any;
        // default sort for equipment task
        sort: any;
        // geocat translation prefix
        geocat_translation_prefix: string;
        // funcat translation prefix
        funcat_translation_prefix: string;
        // table page size
        page_size: number;
        // enable spinner
        display_spinner: boolean;
    };
}
