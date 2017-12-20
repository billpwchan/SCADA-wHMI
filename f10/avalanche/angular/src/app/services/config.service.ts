import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import 'rxjs/add/operator/toPromise';
import { Config } from '../types/config';
import { Map, fromJS } from 'immutable';
@Injectable()
export class ConfigService {
    private static url = './assets/config/settings.json';
    private static defaultConfig = Map({
        scs_db_url: 'http://127.0.0.1:8899/scs/service/DbmComponent/',
        default_client_name: 'ROOT',
        i18n: Map({
            default_lang: 'en',
            resolve_by_browser_lang: true,
            use_culture_lang: true,
            resolve_by_browser_cookie: false,
            use_cookie_name: ''
        }),
        ava_cond: Map({
            filter: {},
            sort: {},
            display_app_navigation: false,
            manual_refresh_enabled: true,
            geocat_translation_prefix: 'Location_',
            funcat_translation_prefix: 'System_',
            page_size: 5,
            display_spinner: true,
        })
    });
    public config: any;
    constructor(private http: HttpClient) {
    }
    private mergeDefault(config: any, defaultConfig: any): Config {
        return defaultConfig.withMutations(map => map.mergeDeep(config));
    }
    public load(): Promise<boolean> {
        return this.http.get(ConfigService.url).toPromise().then(
            response => {
                const json = response;
                console.log(
                    '{Configervice}',
                    '[load]',
                    'json:', json
                );
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
