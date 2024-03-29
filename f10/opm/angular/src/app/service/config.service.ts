import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import { Config } from '../type/config';
import { Map, fromJS } from 'immutable';

@Injectable()
export class ConfigService {
    private static url = './assets/config/settings.json';
    private static defaultConfig = Map({
        'opmmgr_url': 'http://localhost:12080/',
        'i18n': Map({
            'default_lang': 'en',
            'resolve_by_browser_lang': true,
            'use_culture_lang': true,
            'resolve_by_browser_cookie': false,
            'use_cookie_name': ''
        }),
        'profile_management': Map({
            'enabled': true,
            'profile_detail': Map({
                'location_name_field': 'name',
                'location_tooltip_field': 'description',
                'function_name_field': 'name',
                'function_tooltip_field': 'description'
            })
        }),
        'operator_management': Map({
            'enabled': true
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
