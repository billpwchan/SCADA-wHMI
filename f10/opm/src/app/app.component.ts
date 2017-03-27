import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { ConfigService } from './service/config.service';

@Component({
  selector: 'app-opm',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  private static defaultLanguage = 'en';
  constructor(
    private configService: ConfigService,
    private translate: TranslateService
  ) {
    let browserLanguage = translate.getBrowserCultureLang();
    if (!configService.config.i18n_use_culture_lang) {
      browserLanguage = translate.getBrowserLang();
    }
    console.log(
      '[Language]',
      'Default:', configService.config.i18n_default_lang,
      'CultureLang?', configService.config.i18n_use_culture_lang,
      'Browser:', browserLanguage
    );

    translate.setDefaultLang(configService.config.i18n_default_lang);
    translate.use(browserLanguage);
  }
}
