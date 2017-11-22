import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { Config } from '../type/config';
import { Map, fromJS } from 'immutable';
@Injectable()
export class ConfigService {
    private static url = './assets/config/settings.json';
    private static defaultConfig = Map({
        scs_ols_url: 'http://localhost:8899/scs/service/OlsComponent/',
        default_client_name: 'ROOT',
        i18n: Map({
            default_lang: 'en',
            resolve_by_browser_lang: true,
            use_culture_lang: true,
            resolve_by_browser_cookie: false,
            use_cookie_name: ''
        }),
        rtt_url: 'http://localhost:8090/rttapp/',
        archive_names: [
            'TECSOperating',
            'TECSPT',
            'TECSTemperature',
            'TM_ARCHIVE',
            'TS_ARCHIVE'
        ],
        col_idx_1: '3',
        col_idx_2: '4',
        col_idx_3: '5',
        col_idx_4: '6',
        col_idx_5: '7',
        col_idx_6: '8'
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
