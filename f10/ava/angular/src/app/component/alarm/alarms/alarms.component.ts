import { Component, OnInit, OnDestroy, OnChanges, Input, Output, EventEmitter, SimpleChanges } from '@angular/core';
import { AppSettings } from '../../../app-settings';
import { SettingsService } from '../../../service/settings.service';
import { AlarmsSettings, AlarmServeritySelection } from './alarms-settings';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { CardService } from '../../../service/card/card.service';
import { SelectionService } from '../../../service/card/selection.service';
import { Subscription } from 'rxjs/Subscription';
import { CardServiceType } from '../../../service/card/card-settings';
import { SelectionServiceType } from '../../../service/card/selection-settings';
import { AlarmServerity, Card } from '../../../model/Scenario';

@Component({
  selector: 'app-alarms',
  templateUrl: './alarms.component.html',
  styleUrls: ['./alarms.component.css']
})
export class AlarmsComponent implements OnInit, OnDestroy, OnChanges {

  public static readonly STR_INIT = AppSettings.STR_INIT;
  public static readonly STR_CARD_RELOADED = AppSettings.STR_CARD_RELOADED;
  public static readonly STR_CARD_SELECTED = AppSettings.STR_CARD_SELECTED;
  public static readonly STR_CARD_UPDATED = AppSettings.STR_CARD_UPDATED;
  public static readonly STR_STEP_RELOADED = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_SELECTED = AppSettings.STR_STEP_SELECTED;
  public static readonly STR_STEP_UPDATED = AppSettings.STR_STEP_UPDATED;

  public static readonly STR_NORIFY_FROM_PARENT = 'notifyFromParent';

  readonly c = 'AlarmsComponent';

  @Input() notifyFromParent: string;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  cardSubscription: Subscription;
  selectionSubscription: Subscription;

  selectedCardName: string;

  private rowHeaderPrefix: string;
  private rowHeaderIds: number[];
  private rowHeaderWidth: number;

  private colHeaderPrefix: string;
  private colHeaderIds: number[];
  private colWidths: number;

  // private data: number[][];

  private cellSel: AlarmServeritySelection;

  alarmServeritys: AlarmServerity[];

  rows_header: string[];
  rows_header_width: number[];
  cols_header: string[];
  col_widths: number[];
  spreadsheet_data: string[][];

  constructor(
    private translate: TranslateService
    , private settingsService: SettingsService
    , private cardService: CardService
    , private selectionService: SelectionService
  ) {
    translate.onLangChange.subscribe((event: LangChangeEvent) => {
      this.loadTranslations();
    });
   }

  ngOnInit() {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.loadSettings();

    this.cardSubscription = this.cardService.cardItem
      .subscribe(item => {
        console.log(this.c, f, 'cardSubscription', item);
        switch (item) {
          case CardServiceType.CARD_RELOADED: {
            this.btnClicked(AlarmsComponent.STR_CARD_RELOADED);
          } break;
          case CardServiceType.STEP_RELOADED: {
            this.btnClicked(AlarmsComponent.STR_STEP_RELOADED);
          } break;
          case CardServiceType.STEP_UPDATED: {
            this.btnClicked(AlarmsComponent.STR_STEP_UPDATED);
          } break;
        }
      });

    this.selectionSubscription = this.selectionService.selectionItem
      .subscribe(item => {
        console.log(this.c, f, 'selectionSubscription', item);
        switch (item) {
          case SelectionServiceType.CARD_SELECTED: {
            this.btnClicked(AlarmsComponent.STR_CARD_SELECTED);
          } break;
        }
      });

    // Create tesing data
    const card: Card = this.cardService.getCard([this.selectedCardName]);
    if (null != card) {
      card.alarms = this.buildTestingData(this.rowHeaderIds, this.colHeaderIds);
    }

    this.btnClicked(AlarmsComponent.STR_INIT);
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
    this.selectionSubscription.unsubscribe();
    this.cardSubscription.unsubscribe();
  }

  onParentChange(change: string): void {
    const f = 'onParentChange';
    console.log(this.c, f);
    console.log(this.c, f, 'change', change);

    switch (change) {
      // case StepEditSettings.STR_STEP_EDIT_ENABLE: {
      //   this.editEnableNewStep = true;
      // } break;
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    const f = 'ngOnChanges';
    // if ( changes[StepEditComponent.STR_NORIFY_FROM_PARENT] ) {
    //   this.onParentChange(changes[StepEditComponent.STR_NORIFY_FROM_PARENT].currentValue);
    // }
  }

  sendNotifyParent(str: string) {
    const f = 'sendNotifyParent';
    console.log(this.c, f, str);
    this.notifyParent.emit(str);
  }

  loadTranslations() {
    const f = 'loadTranslations';
    console.log(this.c, f);

    this.reloadData();
  }

  private loadSettings(): void {
    const f = 'loadSettings';
    console.log(this.c, f);

    this.rowHeaderPrefix = this.settingsService.getSetting(this.c, f, this.c, AlarmsSettings.STR_ROW_HEADER_PREFIX);
    this.rowHeaderIds = this.settingsService.getSetting(this.c, f, this.c, AlarmsSettings.STR_ROW_HEADER_IDS);
    this.rowHeaderWidth = this.settingsService.getSetting(this.c, f, this.c, AlarmsSettings.STR_ROW_HEADER_WIDTH);

    this.colHeaderPrefix = this.settingsService.getSetting(this.c, f, this.c, AlarmsSettings.STR_COL_HEADER_PREFIX);
    this.colHeaderIds = this.settingsService.getSetting(this.c, f, this.c, AlarmsSettings.STR_COL_HEADER_IDS);
    this.colWidths = this.settingsService.getSetting(this.c, f, this.c, AlarmsSettings.STR_COL_WIDTH);

    this.alarmServeritys = this.settingsService.getSetting(this.c, f, this.c, AlarmsSettings.STR_ALARM_SERVERITYS) as AlarmServerity[];
  }

  onSelection(items: any[]): void {
    const f = 'onSelection';
    console.log(this.c, f);
    // console.log(this.c, f, 'items', items);

    if ( undefined === this.cellSel ) {
      this.cellSel = new AlarmServeritySelection();
    }
    this.cellSel.x = items[0];
    this.cellSel.y = items[1];
    this.cellSel.x2 = items[2];
    this.cellSel.y2 = items[3];
  }

  private buildTestingData(rowHeaderIds: number[], colHeaderIds: number[]): number[][] {
    const f = 'onChange';
    console.log(this.c, f);
    const data = new Array<Array<number>>();
    for (let i = 0; i < rowHeaderIds.length ; ++i) {
      data[i] = new Array<number>();
      for (let j = 0 ; j < colHeaderIds.length ; ++ j ) {
        data[i][j] = 15;
      }
    }
    return data;
  }

  trackByIndex(index: number, value: number) {
    return index;
  }

  isIndeterminate(index: number, event): boolean {
    const f = 'isChecked';
    console.log(this.c, f);
    let selected = 0;
    let checked = 0;
    const card: Card = this.cardService.getCard([this.selectedCardName]);
    if (null != card) {
      const alarms: number[][] = card.alarms;
      if ( undefined !== this.cellSel ) {
        for ( let x = this.cellSel.x ; x <= this.cellSel.x2 ; ++x ) {
          for ( let y = this.cellSel.y ; y <= this.cellSel.y2 ; ++y ) {
            selected++;
            if ( this.isFlagOn(alarms[x][y], index) ) {
              checked++;
            }
          }
        }
      }
    }

    return (checked > 0 && (selected !== checked));
  }

  isChecked(index: number, event): boolean {
    const f = 'isChecked';
    console.log(this.c, f);
    let checked = 0;
    const card: Card = this.cardService.getCard([this.selectedCardName]);
    if (null != card) {
      const alarms: number[][] = card.alarms;
      if ( undefined !== this.cellSel ) {
        for ( let x = this.cellSel.x ; x <= this.cellSel.x2 ; ++x ) {
          for ( let y = this.cellSel.y ; y <= this.cellSel.y2 ; ++y ) {
            if ( this.isFlagOn(alarms[x][y], index) ) {
              checked++;
            }
          }
        }
      }
    }

    return (0 !== checked);
  }

  onChange(name: string, index: number, event): void {
    const f = 'onChange';
    console.log(this.c, f);
    console.log(this.c, f, name, event);
    console.log(this.c, f, name, event.target.value, event.target.checked );

    const val = Number.parseInt(event.target.value);

    const card: Card = this.cardService.getCard([this.selectedCardName]);

    if (null != card) {
      const alarms: number[][] = card.alarms;
      if ( undefined !== this.cellSel ) {
        for ( let x = this.cellSel.x ; x <= this.cellSel.x2 ; ++x ) {
          for ( let y = this.cellSel.y ; y <= this.cellSel.y2 ; ++y ) {
            if (event.target.checked) {
              alarms[x][y] = this.setFlagOn(alarms[x][y], val);
            } else {
              alarms[x][y] = this.setFlagOff(alarms[x][y], val);
            }
          }
        }
      }
    }



    this.reloadData();
  }

  private isFlagOn(val: number, flag: number): boolean {
    // tslint:disable-next-line:no-bitwise
    return ((val & ( 1 << flag)) !== 0);
  }

  private setFlagOn(val: number, flag: number): number {
    // tslint:disable-next-line:no-bitwise
    return ( val | ( 1 << flag ) );
  }

  private setFlagOff(val: number, flag: number): number {
    // tslint:disable-next-line:no-bitwise
    return ( val ^ ( 1 << flag ) );
  }

  private getCellStr(data: number[][], x: number, y: number): string {
    const f = 'getCellStr';
    console.log(this.c, f);
    let ret = '';
    if ( undefined !== data && undefined !== data[x] && undefined !== data[x][y] ) {
      for ( let i = 0 ; i < this.alarmServeritys.length ; ++i ) {
        if ( this.isFlagOn( data[x][y], this.alarmServeritys[i].index) ) {
          if ( 0 !== ret.length ) {
            ret += this.translate.instant(AlarmsSettings.STR_DELFAULT_CELL_COMMA);
          }
          ret += this.translate.instant(this.alarmServeritys[i].shortlabel);
        }
      }
      if ( 0 === ret.length ) {
        ret = this.translate.instant(AlarmsSettings.STR_DELFAULT_CELL_EMPTY);
      }
    } else {
      ret = x + ':' + y + ' ' + this.translate.instant(AlarmsSettings.STR_DELFAULT_CELL_INCORRECT);
    }
    return ret;
  }

  reloadData() {
    const f = 'onSelection';
    console.log(this.c, f);

    // Setup Col/Row Header
    this.rows_header = [];
    this.rows_header_width = [];
    this.rowHeaderIds.forEach(element => {
      this.rows_header.push(this.translate.instant(this.rowHeaderPrefix + element));
      this.rows_header_width.push(this.rowHeaderWidth);
    });

    this.cols_header = [];
    this.col_widths = [];
    this.colHeaderIds.forEach(element => {
      this.cols_header.push(this.translate.instant(this.colHeaderPrefix + element));
      this.col_widths.push(this.colWidths);
    });

    const card: Card = this.cardService.getCard([this.selectedCardName]);

    if (null != card) {
      const alarms: number[][] = card.alarms;
      this.spreadsheet_data = new Array<Array<string>>();
      for (let i = 0; i < this.rows_header.length ; ++i) {
        this.spreadsheet_data[i] = new Array<string>();
        for (let j = 0 ; j < this.cols_header.length ; ++ j ) {
          this.spreadsheet_data[i][j] = this.getCellStr(alarms, i, j);
        }
      }
    }
  }

  private init(): void {
    const f = 'init';
    console.log(this.c, f);
    this.selectedCardName = '';
  }

  btnClicked(btnLabel: string, event?: Event): void {
    const f = 'btnClicked';
    console.log(this.c, f);
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');
    switch (btnLabel) {
      case AlarmsComponent.STR_INIT: {
        this.init();
        this.reloadData();
      } break;
      case AlarmsComponent.STR_CARD_RELOADED: {
        this.selectedCardName = '';
      } break;
      case AlarmsComponent.STR_CARD_SELECTED: {
        this.selectedCardName = this.selectionService.getSelectedCardId();
      } break;
      case AlarmsComponent.STR_STEP_RELOADED: {
      } break;
      case AlarmsComponent.STR_STEP_UPDATED: {
      } break;
    }
  }
}
