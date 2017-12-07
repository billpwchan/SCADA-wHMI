import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import { Config, CONFIG_DEFAULT } from '../type/config';

@Injectable()
export class ConfigService {
    public config: Config;

    constructor(
        private http: Http
    ) {
        console.log('{ConfigService}', '[constructor]');
    }

    private mergeDefault(config: Config, defaultConfig: Config): Config {
        for (const i in defaultConfig) {
            if (undefined === config[i]) {
                config[i] = defaultConfig[i];
            } else if (typeof({}) === typeof(defaultConfig[i])) {
                if (typeof({}) !== typeof(config[i])) { config[i] = {}; }
                this.mergeDefault(config[i], defaultConfig[i]);
            } // else... no need to merge, as the config already have user defined settings
        }
        return config;
    }

    public load(url: string, defaultWhenFail = true): Promise<boolean> {
        return this.http.get(url).toPromise().then(
            response => {
                const json = response.json();
                console.log(
                    '{ConfigService}', '[load]',
                    'json:', json
                )
                this.config = json as Config;
                this.config = this.mergeDefault(this.config, CONFIG_DEFAULT);
                console.log(
                    '{ConfigService}', '[load]',
                    'Resulting Config:', this.config
                );
                return true;
            },
            failure => {
                if (defaultWhenFail) {
                    this.config = CONFIG_DEFAULT;
                    console.log(
                        '{ConfigService}', '[load]',
                        'Using Default Config Config:', this.config
                    );
                    return true;
                } else {
                    console.error(
                        '{ConfigService}', '[load]',
                        'Failed to retrieve config:', url,
                        'Response:', failure
                    );
                    return false;
                }
            }
        ).catch(this.handleError);
    }

    private handleError(error: any): Promise<any> {
        console.error('{ConfigService}', '[handleError]', error);
        return Promise.reject(error.message || error);
    }
}
