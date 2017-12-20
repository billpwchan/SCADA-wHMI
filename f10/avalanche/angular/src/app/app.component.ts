import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { ConfigService } from './services/config.service';
import { Cookie } from 'ng2-cookies';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  private static defaultLanguage = 'en';
  public title = 'Avalanche';

  constructor(private configService: ConfigService, private translate: TranslateService) {
    const defaultLanguage = configService.config.getIn(['i18n', 'default_lang']);
    const preferedLanguage = this.getPreferedLanguage();
    console.log(
        '{AppComponent}',
        '[Language]',
        'Default:', defaultLanguage,
        'Prefered:', preferedLanguage
    );
    translate.setDefaultLang(defaultLanguage);
    if (preferedLanguage) {
        translate.use(preferedLanguage);
        console.log('{AppComponent}', '[Language]', 'use preferred language ', preferedLanguage);
    }
  }

  private getPreferedLanguage(): string {
    if (this.configService.config.getIn(['i18n', 'resolve_by_browser_lang'])) {
        console.log('{AppComponent}', 'Resolve prefered language by browser\'s language');
        let browserLanguage = this.translate.getBrowserCultureLang();
        if (!this.configService.config.getIn(['i18n', 'use_culture_lang'])) {
            browserLanguage = this.translate.getBrowserLang();
        }
        return browserLanguage;
    } else if (this.configService.config.getIn(['i18n', 'resolve_by_browser_cookie'])) {
        const cookieName = this.configService.config.getIn(['i18n', 'use_cookie_name']);
        console.log('{AppComponent}', 'Resolve prefered language by browser\'s cookie:', cookieName);
        return Cookie.get(this.configService.config.getIn(['i18n', 'use_cookie_name']));
    } else {
        console.log('{AppComponent}', 'No defined way to obtain prefered language');
    }
    return undefined;
  }
}
