import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { SettingsService } from './service/settings.service';
import { Cookie } from 'ng2-cookies';
import { I18nSettings } from './service/i18n-settings';
import { Title } from '@angular/platform-browser';
import { AppSettings } from './app-settings';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  readonly c = 'AppComponent';

  title = 'app';

  constructor(
    private translate: TranslateService
    , private settingsService: SettingsService
    , private titleService: Title
  ) {
    const f = 'constructor';
    console.log(this.c, f);

    console.log(this.c, f, 'translate.getBrowserCultureLang()[' + translate.getBrowserCultureLang() + ']');
    console.log(this.c, f, 'translate.getBrowserLang()[' + translate.getBrowserLang() + ']');
    console.log(this.c, f, 'translate.getDefaultLang()[' + translate.getDefaultLang() + ']');
    console.log(this.c, f, 'translate.getLangs()[' + translate.getLangs() + ']');

    const setting = this.settingsService.getSettings();
    const i18n = setting[I18nSettings.STR_I18N];
    const defaultLanguage = i18n[I18nSettings.STR_DEFAULT_LANG];
    const preferedLanguage = this.getPreferedLanguage();
    console.log(this.c, f,
                          '[Language]',
                          'Default:', defaultLanguage,
                          'Prefered:', preferedLanguage
    );
    // this language will be used as a fallback when a translation isn't found in the current language
    translate.setDefaultLang(defaultLanguage);
    if (preferedLanguage) {
        // the lang to use, if the lang isn't available, it will use the current loader to get them
        translate.use(preferedLanguage);
        console.log(this.c, f, 'use preferred language ', preferedLanguage);
    }

    this.loadSettings();

    this.titleService.setTitle(this.title);
  }

  private getPreferedLanguage(): string {
    const f = 'getPreferedLanguage';
    const setting = this.settingsService.getSettings();
    const i18n = setting[I18nSettings.STR_I18N];
    if (i18n[I18nSettings.STR_RESOLVE_BY_BROWSER_LANG]) {
        console.log(this.c, f, 'Resolve prefered language by browser\'s language');
        let browserLanguage = this.translate.getBrowserCultureLang();
        if (!i18n[I18nSettings.STR_USE_CULTURE_LANG]) {
            browserLanguage = this.translate.getBrowserLang();
        }
        return browserLanguage;
    } else if (i18n[I18nSettings.STR_RESOLVE_BY_BROWSER_COOKIE]) {
        const cookieName = i18n[I18nSettings.STR_USE_COOKIE_NAME];
        console.log(this.c, f, 'Resolve prefered language by browser\'s cookie:', cookieName);
        return Cookie.get(cookieName);
    } else {
        console.warn(this.c, f, 'No defined way to obtain prefered language');
    }
    return undefined;
  }

  private loadSettings() {
    const f = 'loadSettings';
    console.log(this.c, f);

    this.title = this.settingsService.getSetting(this.c, f, this.c, AppSettings.STR_TITLE);
  }
}
