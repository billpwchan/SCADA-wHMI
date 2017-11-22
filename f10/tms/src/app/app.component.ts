import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-root'
  , templateUrl: './app.component.html'
  , styleUrls: ['./app.component.css']
  // , template: `<div>{{ 'HELLO' | translate:param }}</div>`
})
export class AppComponent {

  param = {value: 'world'};
  
  constructor(translate: TranslateService) {
      // this language will be used as a fallback when a translation isn't found in the current language
      translate.setDefaultLang('en');

        // the lang to use, if the lang isn't available, it will use the current loader to get them
      translate.use('en');
  }

  title = 'app';
  
  rows_scenariolist = [
    { name: 'Scenario # 1', state: 'Stop' }
    ,{ name: 'Scenario # 2', state: 'Stop' }
    ,{ name: 'Scenario # 3', state: 'Stop' }
    ,{ name: 'Scenario # 4', state: 'Stop' }
    ,{ name: 'Scenario # 5', state: 'Stop' }
  ];
  columns_scenariolist = [
    { prop: 'name' }
    ,{ name: 'State' }
  ];

  rows_steplist = [
    { step: '1', location: 'Location # 1', system: 'POW', equipment: 'Breaker (301)', point: 'Point # 1', value: 'Open', delay: '5', status: 'Stop' }
    ,{ step: '2', location: 'Location # 2', system: 'POW', equipment: 'Breaker (501)', point: 'Point # 2', value: 'Open', delay: '5', status: 'Stop' }
  ];
  columns_steplist = [
    { prop: 'step' }
    ,{ name: 'Location' }
    ,{ name: 'System' }
    ,{ name: 'Equipment' }
    ,{ name: 'Point' }
    ,{ name: 'Value' }
    ,{ name: 'Delay' }
    ,{ name: 'Status' }
  ];
}
