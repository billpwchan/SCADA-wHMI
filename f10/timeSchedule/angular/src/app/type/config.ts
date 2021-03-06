export interface Config {
    // URL to time schedule
    time_schedule_url: string;
    // default client name to identify sender
    default_client_name: string,
    // temp fix for time difference between windows and linux in SetDate and GetDate API.
    // Windows client should set this value to 0
    // Linux client should set this value to 2,177,452,800
    tsc_time_offset: number,
    // default delay to start update schedules running status
    running_status_update_start_delay: number
    // default period to update schedules running status
    running_status_update_period: number
    // default period to update daygroup datelist
    daygroup_update_period: number
    // disable periodic schedule planning
    disable_periodic_schedule_planning: boolean;
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
        // default cutoff time
        cutoff_time: string;
        // option to show equipment on/off time as original hour + 24hr (e.g. 01:00 -> 25:00)
        show_cutoff_offset: string;
        // string to show if on/off time is not configured
        unavailable_on_off_time: string;
        // string to show if on/off time is inhibited
        inhibited_on_off_time: string;
        // maximum userdefined schedule count (Oneshot + predefined + userdefined total cannot be over 8)
        max_userdefined_schedule_count: number;
        // define each schedule's planning and running daygroup
        schedule_daygroup: any;
        // default filter for equipment task
        filter: any;
        // default sort for equipment task
        sort: any;
        // default schedule title length limit
        max_title_length: number;
        // option to show app navigation bar
        display_app_navigation: boolean;
        // option to show manual refresh button
        manual_refresh_enabled: boolean;
        // option to show cut-off time
        display_cutoff_time_periodic: boolean;
        display_cutoff_time_non_periodic: boolean;
        // option to show running schedules
        display_running_schedules: boolean;
        // string separator for concatenation of equipment task column values
        equipment_task_string_separator: string;
        // geocat translation prefix
        geocat_translation_prefix: string;
        // funcat translation prefix
        funcat_translation_prefix: string;
        // table page size
        page_size: number;
        // enable spinner
        display_spinner: boolean;
        // option to display other schedule types in running schedule
        display_other_types_in_running_schedules: boolean;
        // option to add delay to OnOff time checking after scheduled time
        update_onofftime_delay: number;
    };
    schedule_planning: {
        // default schedule assignment for each weekday
        weekly_planning: any;
        // default number of calendar days to add to planning daygroups
        periodic_planning_duration: any;
        // option to update running daygroups when weekly plan is updated
        apply_plan_to_running_daygroup: boolean;
        // option to display app navigation
        display_app_navigation: boolean;
        // option to display manual refresh button
        manual_refresh_enabled: boolean;
        // option to show cut-off time
        display_cutoff_time: boolean;
        // option to show running schedules
        display_running_schedules: boolean;
        // enable spinner
        display_spinner: boolean;
        // option to display other schedule types in running schedules
        display_other_types_in_running_schedules: boolean;
    }
}
