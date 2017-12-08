import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { StorageService } from '../../service/card/storage.service';
import { AppSettings } from '../../app-settings';

@Component({
  selector: 'app-csv-interpret'
  , templateUrl: './csv-interpret.component.html'
  , styleUrls: ['./csv-interpret.component.css']
})
export class CsvInterpretComponent implements OnInit {

  public static readonly STR_INIT = AppSettings.STR_INIT;
  public static readonly STR_CARD_RELOADED = AppSettings.STR_CARD_RELOADED;
  public static readonly STR_CARD_SELECTED = AppSettings.STR_CARD_SELECTED;
  public static readonly STR_STEP_RELOADED = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_SELECTED = AppSettings.STR_STEP_SELECTED;

  readonly c = CsvInterpretComponent.name;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  constructor(
    private translate: TranslateService
    , private _cardStorageService: StorageService
  ) {
  }

  ngOnInit() {
  }

  sendNotifyParent(str: string) {
    const f = 'sendNotifyParent';
    console.log(this.c, f, str);
    this.notifyParent.emit(str);
  }

  // Button Handler
  private btnClicked(btnLabel: string, event?: Event) {
    const func = 'btnClicked';
    console.log(func, 'btnLabel[', btnLabel, ']');

    switch (btnLabel) {
      case 'exportstart': {} break;
      case 'exportcstop': {} break;
      case 'exportcsv': {
        this._cardStorageService.downloadData();
      } break;
      case 'importcsv': {} break;
      case 'loadfile': {
        this._cardStorageService.loadFile(event);
      } break;
    }

    this.sendNotifyParent(btnLabel);
  }
}

