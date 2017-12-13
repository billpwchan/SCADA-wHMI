import { Component, OnInit, Output, EventEmitter, OnDestroy, OnChanges, Input, SimpleChanges } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { StorageService } from '../../../service/card/storage.service';
import { AppSettings } from '../../../app-settings';
import { CardService } from '../../../service/card/card.service';
import { CardsToCsvPipe } from '../../../pipe/csv/cards-to-csv.pipe';
import { Card } from '../../../model/Scenario';
import { CsvToCardsPipe } from '../../../pipe/csv/csv-to-cards.pipe';
import { CardServiceType } from '../../../service/card/card-settings';
import { SettingsService } from '../../../service/settings.service';
import { AppComponent } from '../../../app.component';
import { CsvToCardSettings } from '../../../pipe/csv/csv-to-card-settings';
import { CsvInterpretSettings } from './csv-interpret-settings';

@Component({
  selector: 'app-csv-interpret'
  , templateUrl: './csv-interpret.component.html'
  , styleUrls: ['./csv-interpret.component.css']
})
export class CsvInterpretComponent implements OnInit, OnDestroy, OnChanges {

  public static readonly STR_INIT = AppSettings.STR_INIT;
  public static readonly STR_CARD_RELOADED = AppSettings.STR_CARD_RELOADED;
  public static readonly STR_CARD_SELECTED = AppSettings.STR_CARD_SELECTED;
  public static readonly STR_STEP_RELOADED = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_SELECTED = AppSettings.STR_STEP_SELECTED;

  public static readonly STR_NORIFY_FROM_PARENT = 'notifyFromParent';

  private strFileName: string = CsvToCardSettings.STR_FILENAME;
  private strComma: string = CsvToCardSettings.STR_COMMA;
  private strEOL: string = CsvToCardSettings.STR_EOL;

  readonly c = CsvInterpretComponent.name;

  @Input() notifyFromParent: string;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  btnDisabledExportCSV: boolean;
  btnDisabledImportCSV: boolean;

  disableImportMsg: boolean;
  disableExportMsg: boolean;

  importNumber: number;
  exportNumber: number;

  initCardsBeforeExport: boolean;

  constructor(
    private translate: TranslateService
    , private storageService: StorageService
    , private cardService: CardService
    , private settingsService: SettingsService
  ) {
  }

  ngOnInit() {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.loadSettings();

    this.btnClicked(CsvInterpretComponent.STR_INIT);
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
  }

  ngOnChanges(changes: SimpleChanges): void {
    const f = 'ngOnChanges';
    console.log(this.c, f);
    console.log(this.c, f, 'changes', changes);
    if ( changes[CsvInterpretComponent.STR_NORIFY_FROM_PARENT] ) {
      switch (changes[CsvInterpretComponent.STR_NORIFY_FROM_PARENT].currentValue) {
      }
    }
  }

  sendNotifyParent(str: string) {
    const f = 'sendNotifyParent';
    console.log(this.c, f, str);
    this.notifyParent.emit(str);
  }

  private loadSettings() {
    const f = 'loadSettings';
    console.log(this.c, f);

    const component = CsvInterpretComponent.name;
    this.strFileName = this.settingsService.getSetting(this.c, f, component, CsvInterpretSettings.STR_FILED_FILENAME);
    this.strComma = this.settingsService.getSetting(this.c, f, component, CsvInterpretSettings.STR_FILED_COMMA);
    this.strEOL = this.settingsService.getSetting(this.c, f, component, CsvInterpretSettings.STR_FILED_EOL);
    this.initCardsBeforeExport = this.settingsService.getSetting(this.c, f, component, CsvInterpretSettings.STR_INIT_CARDS_BEFORE_EXPORT);
  }

  /**
   * Export Cards as a CSV file
   *
   * @private
   * @memberof CsvInterpretComponent
   */
  private exportCardsAsCsv(): void {
    const f = 'exportCardsAsCsv';
    console.log(this.c, f);

    const cards: Card[] = this.cardService.getCards();
    const strCards = JSON.stringify(cards);
    let exportCards = JSON.parse(strCards);
    if ( this.initCardsBeforeExport ) {
      exportCards = this.cardService.initCards(exportCards);
    }
    const data: string = new CardsToCsvPipe().transform(exportCards, [this.strComma, this.strEOL]);

    const blob: Blob = new Blob([data], {type: 'text/csv'});
    const fileName: string = this.strFileName;
    const objectUrl: string = URL.createObjectURL(blob);
    const a: HTMLAnchorElement = document.createElement('a') as HTMLAnchorElement;

    a.href = objectUrl;
    a.download = fileName;
    document.body.appendChild(a);
    a.click();

    document.body.removeChild(a);
    URL.revokeObjectURL(objectUrl);

    this.exportNumber = this.cardService.getCards().length;
    this.disableExportMsg = false;
  }

  /**
   * Import CSV file as a Cards
   *
   * @private
   * @param {string} csv
   * @memberof CsvInterpretComponent
   */
  private importCsvAsCards(csv: string): void {
    const f = 'importCsvAsCards';
    console.log(this.c, f);

    const cards: Card[] = new CsvToCardsPipe().transform(csv, [this.strComma, this.strEOL]);
    if ( null != cards ) {
      this.cardService.setCards(cards);
      this.cardService.notifyUpdate(CardServiceType.CARD_RELOADED);

      this.importNumber = this.cardService.getCards().length;
      this.disableImportMsg = false;
    }
  }

  private loadFile(event) {
    const f = 'loadFile';
    console.log(this.c, f);
    const file: File = event.target.files[0];
    const reader = new FileReader();
    reader.onload = (e: any) => {
//    console.log('name', e.target.name, 'size', e.target.size, 'result', e.target.result);
      this.importCsvAsCards(e.target.result);
    };
    reader.readAsText(file);
  }

  private init(): void {
    const f = 'init';
    console.log(this.c, f);

    this.btnDisabledExportCSV = false;
    this.btnDisabledImportCSV = false;

    this.disableImportMsg = true;
    this.importNumber = 0;
    this.disableExportMsg = true;
    this.exportNumber = 0;
  }

  // Button Handler
  btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(this.c, f);
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');

    switch (btnLabel) {
      case CsvInterpretComponent.STR_INIT: {
        this.init();
      } break;
      case 'exportcsv': {
        this.exportCardsAsCsv();
      } break;
      case 'importcsv': {
      } break;
      case 'loadfile': {
        this.loadFile(event);
      } break;
    }

    this.sendNotifyParent(btnLabel);
  }
}

