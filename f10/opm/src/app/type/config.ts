export interface Config {
    // URL to opmmgr
    opmmgr_url: string;

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

    profile_management: {
        // whether profile management is enabled
        enabled: boolean,
        // profile detail panel configuration
        profile_detail: {
            // the field to display in profile-detail for location name
            // valid fields refer to location.ts
            location_name_field: string;
            // the field to display in profile-detail for location tooltip
            // valid fields refer to location.ts
            location_tooltip_field: string;

            // the field to display in profile-detail for function name
            // valid fields refer to function.ts
            function_name_field: string;
            // the field to display in profile-detail for function tooltip
            // valid fields refer to function.ts
            function_tooltip_field: string;
        };
    };

    operator_management: {
        // whether operator management is enabled
        enabled: boolean
    }
}
