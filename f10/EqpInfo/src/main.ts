import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';
import { environment } from './environments/environment';

if (environment.production) {
  enableProdMode();

  // // Disable log in production
  // console.log = function(a, b, c, d, e, f, g, h, i, j, k, l) { return false; };
}

platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.log(err));
