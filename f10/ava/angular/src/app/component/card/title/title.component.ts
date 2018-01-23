import { Component, OnInit, OnDestroy, OnChanges, Input, Output, EventEmitter, SimpleChanges } from '@angular/core';
import { AppSettings } from '../../../app-settings';
import { Subscription } from 'rxjs/Subscription';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-title',
  templateUrl: './title.component.html',
  styleUrls: ['./title.component.css']
})
export class TitleComponent implements OnInit, OnDestroy, OnChanges {

  public static readonly STR_INIT = AppSettings.STR_INIT;

  public static readonly STR_NORIFY_FROM_PARENT = AppSettings.STR_NOTIFY_FROM_PARENT;

  readonly c = 'StepsComponent';

  @Input() notifyFromParent: string;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  @Input()
  set updateTitle(data: string) {
    const f = 'updateTitle';
    console.log(this.c, f);
    if ( null != data ) {
      this.txtName = data;
    }
  }

  txtName: string;

  constructor(
    private translate: TranslateService
  ) { }

  ngOnInit() {
    const f = 'ngOnInit';
    console.log(this.c, f);
  }

  ngOnDestroy() {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
  }

  ngOnChanges(changes: SimpleChanges): void {
    const f = 'ngOnChanges';
    if (changes[TitleComponent.STR_NORIFY_FROM_PARENT]) {
      if (changes[TitleComponent.STR_NORIFY_FROM_PARENT].currentValue) {
      }
    }
  }

  sendNotifyParent(str: string) {
    const f = 'sendNotifyParent';
    console.log(this.c, f, str);
    this.notifyParent.emit(str);
  }

  private loadTranslations(): void {
    const f = 'loadTranslations';
    console.log(this.c, f);
  }

  private init(): void {
    const f = 'init';
    console.log(this.c, f);
    this.txtName = '';
  }

  btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(this.c, f);
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');
    switch (btnLabel) {
      case TitleComponent.STR_INIT: {
        this.init();
      } break;
    }
  }
}
