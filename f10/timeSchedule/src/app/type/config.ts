export interface Config {
    // URL to time schedule
    time_schedule_url: string;
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
    schedule_table: {
        // 
        cutoff_time: string;
        // 
        show_cutoff_offset: string;
        // 
        unavailable_on_off_time: string;
        //
        inhibited_on_off_time: string,
        //
        max_userdefined_schedule_count: number,
        // 
        schedule_daygroup: any;
        //
        filter: any,
        //
        sort: any
    };
    schedule_planning: {
        //
        periodic_planning_duration: any;
    }
}
