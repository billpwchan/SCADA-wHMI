import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Cookie } from 'ng2-cookies';

import { Config } from '../type/config';
import { ConfigService } from '../service/config.service'

@Injectable()
export class I18nService {
    private config: Config = undefined;
    constructor(
        private configService: ConfigService,
        private translateService: TranslateService
    ) {
        console.log('{I18nSerivce}', '[constructor]');
        this.config = configService.config;
        this.load();
    }

    private load(): void {
        const defaultLang = this.config.i18n.default_lang;
        const preferedLang = this.getPreferedLanguage();
        this.translateService.setDefaultLang(defaultLang);
        this.translateService.use(preferedLang);
        console.log(
            '{I18nSerivce}', '[load]',
            'defaultLang:', defaultLang,
            'preferedLang', preferedLang
        );
    }

    private getPreferedLanguage(): string {
        if (this.config.i18n.resolve_by_browser_lang) {
            if (this.config.i18n.use_culture_lang) {
                return this.translateService.getBrowserCultureLang();
            } else {
                return this.translateService.getBrowserLang();
            }
        } else if (this.config.i18n.resolve_by_browser_cookie) {
            const cookieName = this.config.i18n.use_cookie_name;
            return Cookie.get(cookieName);
        }
        return this.config.i18n.default_lang;
    }
}
