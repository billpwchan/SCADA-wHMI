import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import { Config } from '../type/config';
import { Map, fromJS } from 'immutable';

@Injectable()
export class ConfigService {
    private static url = '../../assets/config/settings.json';
    private static defaultConfig = Map({
        'scs_tsc_url': 'http://127.0.0.1:8899/scs/service/TscComponent/',
        'i18n': Map({
            'default_lang': 'en',
            'resolve_by_browser_lang': true,
            'use_culture_lang': true,
            'resolve_by_browser_cookie': false,
            'use_cookie_name': ''
        }),       
        'schedule_table': Map({
            'cutoff_time': "02:00",
            'show_cutoff_offset': true,
            'unavailable_on_off_time': "N/A"
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
}
