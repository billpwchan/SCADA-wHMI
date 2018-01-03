import { Component, OnInit } from '@angular/core';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';

@Component({
  selector: 'app-ava-cond-action',
  templateUrl: './ava-cond-action.component.html',
  styleUrls: ['./ava-cond-action.component.css']
})
export class AvaCondActionComponent implements OnInit {

  constructor(public translate: TranslateService) {
    translate.onLangChange.subscribe((event: LangChangeEvent) => {
      this.loadTranslations();
    });
  }

  ngOnInit() {
  }

  public loadTranslations() {
  }

}
