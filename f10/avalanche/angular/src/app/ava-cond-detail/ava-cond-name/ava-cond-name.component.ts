import { Component, OnInit, Input } from '@angular/core';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { AvaCond } from '../../types/ava-cond';

@Component({
  selector: 'app-ava-cond-name',
  templateUrl: './ava-cond-name.component.html',
  styleUrls: ['./ava-cond-name.component.css']
})
export class AvaCondNameComponent implements OnInit {
  public avaCondName = '';
  private avaCond_: AvaCond;

  @Input() set avaCond(value: AvaCond) {
    this.avaCond_ = value;
    if (value) {
      if (this.avaCond_.name) {
        this.avaCondName = this.avaCond_.name;
      } else {
        this.avaCondName = '';
      }
    } else {
      this.avaCondName = '';
    }
  }

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
