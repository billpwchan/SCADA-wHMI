import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { Config } from '../type/config';
import { Map, fromJS } from 'immutable';
@Injectable()
export class ConfigService {
    private static url = './assets/config/settings.json';
    private static defaultConfig = Map({
        scs_tsc_url: 'http://127.0.0.1:8899/scs/service/TscComponent/',
        default_client_name: 'ROOT',
        tsc_time_offset: 0,
        running_status_update_start_delay: 10000,
        running_status_update_period: 5000,
        daygroup_update_period: 60000,
        disable_periodic_schedule_planning: false,
        i18n: Map({
            default_lang: 'en',
            resolve_by_browser_lang: true,
            use_culture_lang: true,
            resolve_by_browser_cookie: false,
            use_cookie_name: ''
        }),
        schedule_table: Map({
            cutoff_time: '02:00',
            show_cutoff_offset: true,
            unavailable_on_off_time: 'N/A',
            inhibited_on_off_time: '',
            max_userdefined_schedule_count: 3,
            schedule_daygroup: {},
            filter: {},
            sort: {},
            max_title_length: 40,
            display_app_navigation: false,
            manual_refresh_enabled: true,
            display_cutoff_time_periodic: true,
            display_cutoff_time_non_periodic: false,
            display_running_schedules: true,
            equipment_task_string_separator: ' - ',
            geocat_translation_prefix: 'Location_',
            funcat_translation_prefix: 'System_',
            page_size: 5,
            display_spinner: true,
            display_other_types_in_running_schedules: true,
            update_onofftime_delay: 5000
        }),
        schedule_planning: Map({
            weekly_planning: {},
            periodic_planning_duration: 180,
            apply_plan_to_running_daygroup: false,
            display_app_navigation: false,
            manual_refresh_enabled: true,
            display_cutoff_time: false,
            display_running_schedules: true,
            display_spinner: true,
            display_other_types_in_running_schedules: true
        })
    });
    public config: any;
    constructor(private http: Http) {
    }
    private mergeDefault(config: any, defaultConfig: any): Config {
        return defaultConfig.withMutations(map => map.mergeDeep(config));
    }
    public load(): Promise<boolean> {
        return this.http.get(ConfigService.url).toPromise().then(
            response => {
                const json = response.json();
                console.log(
                    '{Configervice}',
                    '[load]',
                    'json:', json
                )
                this.config = fromJS(json as Config);
                this.config = this.mergeDefault(this.config, ConfigService.defaultConfig);
                console.log(
                    '{ConfigService}',
                    '[load]',
                    'Resulting Config:', this.config
                );
                return true;
            },
            failure => {
                console.error(
                    '[ConfigService]',
                    'Failed to retrieve config:', ConfigService.url,
                    'Response:', failure
                );
                return false;
            }
        ).catch(this.handleError);
    }
    private handleError(error: any): Promise<any> {
        console.error('{ConfigService}', '[handleError]', error);
        return Promise.reject(error.message || error);
    }
    public getIn(key: string[]): any {
        let value = this.config.getIn(key);
        const override = this.getInUrl(key.join('.'));
        if (override && override.length > 1) {
            value = override[1];
            console.log('{ConfigService}', '[getIn]', 'Override by value=', value);
        }
        return value;
    }
    public getInUrl(sParam) {
        console.log('{ConfigService}', '[getInUrl] sParam=', sParam);
        let param = null;
        const params: string[] = decodeURIComponent(window.location.href).split('?');
        if (params && params.length > 1) {
            param = params[1].split('&')
            .map((v) => v.split('='))
            .filter((v) => (v[0] === sParam) ? true : false)
            .reduce((prev, curv, index, array) => curv, undefined);
        }
        console.log('{ConfigService}', '[getInUrl] sParam[' + sParam + '] param[' + param + ']');
        return param;
      }
}
