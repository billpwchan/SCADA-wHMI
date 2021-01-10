import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { TranslateService } from '@ngx-translate/core';
import { Cookie } from 'ng2-cookies';
import { ConfigService } from './service/config.service';


@Component({
    selector: 'app-tsc-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
    private static defaultLanguage = 'en-US';
    private token: any;
    constructor(
        private configService: ConfigService,
        private translate: TranslateService,
        private title: Title,
    ) {
        const defaultLanguage = configService.config.getIn(['i18n', 'default_lang']);
        const preferedLanguage = this.getPreferedLanguage();
        translate.setDefaultLang(defaultLanguage);
        if (preferedLanguage) {
            translate.use(preferedLanguage);
        }
    }
    ngOnInit() {
        this.translate.get('RTT').subscribe(name => {
            this.title.setTitle(name);
        });
    }
    private getPreferedLanguage(): string {
        if (this.configService.config.getIn(['i18n', 'resolve_by_browser_lang'])) {
            let browserLanguage = this.translate.getBrowserCultureLang();
            if (!this.configService.config.getIn(['i18n', 'use_culture_lang'])) {
                browserLanguage = this.translate.getBrowserLang();
            }
            return browserLanguage;
        } else if (this.configService.config.getIn(['i18n', 'resolve_by_browser_cookie'])) {
            return Cookie.get(this.configService.config.getIn(['i18n', 'use_cookie_name']));
        } else {
            console.log('{AppComponent}', 'No defined way to obtain prefered language');
        }
        return 'en-US';
    }
}
