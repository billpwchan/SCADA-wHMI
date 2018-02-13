import { Component, OnInit, OnDestroy, Inject } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';
import { TranslateService } from '@ngx-translate/core';
import { StepsComponent } from './component/step/steps/steps.component';
import { StepEditComponent } from './component/step/step-edit/step-edit.component';
import { SettingsService } from './service/settings.service';
import { StepEditSettings } from './component/step/step-edit/step-edit-settings';
import { AppSettings } from './app-settings';
import { Cookie } from 'ng2-cookies';
import { I18nSettings } from './service/i18n-settings';
import { Title, DOCUMENT } from '@angular/platform-browser';

@Component({
  selector: 'app-root'
  , templateUrl: './app.component.html'
  , styleUrls: [
    './app.component.css'
  ]
})
export class AppComponent implements OnInit, OnDestroy {

  readonly c = 'AppComponent';

  cardSubscription: Subscription;

  cardUpdateValue: string;
  stepEditUpdateValue: string;

  title: string;

  constructor(
    private translate: TranslateService
    , private settingsService: SettingsService
    , private titleService: Title
    , @Inject(DOCUMENT) private document: Document
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

    const STR_PROJECT_CSS = 'project.css';
    this.loadExternalCssFile(STR_PROJECT_CSS);
  }

  loadExternalCssFile(filename) {
    const f = 'loadExternalCss';
    console.log(this.c, f);

    const STR_LINK = 'link';
    const STR_REL = 'rel';
    const STR_STYLESHEET = 'stylesheet';
    const STR_TYPE = 'type';
    const STR_TEXST_CSS = 'text/css';
    const STR_HREF = 'href';
    const STR_HEAD = 'head';

    const fileref = document.createElement(STR_LINK);
    fileref.setAttribute(STR_REL, STR_STYLESHEET);
    fileref.setAttribute(STR_TYPE, STR_TEXST_CSS);
    fileref.setAttribute(STR_HREF, filename);
    this.document.getElementsByTagName(STR_HEAD)[0].appendChild(fileref);
  }

  ngOnInit(): void {
    const f = 'ngOnInit';
    console.log(this.c, f);
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
    this.cardSubscription.unsubscribe();
  }

  getNotification(evt) {
    const f = 'getNotification';
    // Do something with the notification (evt) sent by the child!
    console.log(this.c, f, 'evt', evt);
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
