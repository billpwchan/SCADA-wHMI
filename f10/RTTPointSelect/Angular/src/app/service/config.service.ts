import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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
        rtt_url: 'http://localhost:8090/rttapp/'

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
                this.config = fromJS(json as Config);
                this.config = this.mergeDefault(this.config, ConfigService.defaultConfig);
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

    // notify(status: any, text: any) {
    //     this.snackBar.open(status, text, {
    //         duration: 3000
    //     });
    // }

    // private handleError(error: any) {
    //     this.notify(error.status, error.statusText);
    //     return Promise.reject(error.message || error);
    // }
}
