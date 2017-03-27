import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import { Config } from '../type/config';

@Injectable()
export class ConfigService {
    private static url = './assets/config/settings.json';
    private static opmmgr_url_default = 'http://localhost:12080/';
    private static i18n_default_lang_default = 'en';
    private static i18n_use_culture_lang_default = true;

    public config: Config;

    constructor(private http: Http) {
        this.config = new Config();
    }

    public load(): Promise<boolean> {
        return this.http.get(ConfigService.url).toPromise().then(
            response => {
                this.config = response.json() as Config;
                if (!this.config.opmmgr_url) { this.config.opmmgr_url = ConfigService.opmmgr_url_default; }
                if (!this.config.i18n_default_lang) { this.config.i18n_default_lang = ConfigService.i18n_default_lang_default; }
                if (!this.config.i18n_use_culture_lang) { this.config.i18n_use_culture_lang = ConfigService.i18n_use_culture_lang_default; }
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
        console.error('[ConfigService]', 'An error occurred', error);
        return Promise.reject(error.message || error);
    }
}
