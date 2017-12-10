import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { StorageService } from '../../service/card/storage.service';
import { AppSettings } from '../../app-settings';
import { CardService } from '../../service/card/card.service';
import { CardsToCsvPipe } from '../../pipe/cards-to-csv.pipe';
import { Card } from '../../model/Scenario';
import { CsvToCardsPipe } from '../../pipe/csv-to-cards.pipe';
import { CardServiceType } from '../../service/card/card-settings';
import { SettingsService } from '../../service/settings.service';
import { AppComponent } from '../../app.component';
import { CsvToCardSettings } from '../../pipe/csv-to-card-settings';

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

  private strFileName: string = CsvToCardSettings.STR_FILENAME;
  private strComma: string = CsvToCardSettings.STR_COMMA;
  private strEOL: string = CsvToCardSettings.STR_EOL;

  readonly c = CsvInterpretComponent.name;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  disableImportMsg: boolean;
  disableExportMsg: boolean;

  importNumber: number;
  exportNumber: number;

  constructor(
    private translate: TranslateService
    , private storageService: StorageService
    , private cardService: CardService
    , private settingsService: SettingsService
  ) {
  }

  private static readonly STR_FILED_FILENAME = 'FileName';
  private static readonly STR_FILED_COMMA = 'Comma';
  private static readonly STR_FILED_EOL = 'EOL';

  ngOnInit() {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.loadSettings();

    this.btnClicked(CsvInterpretComponent.STR_INIT);
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
    this.strFileName = this.settingsService.getSetting(this.c, f, component, CsvInterpretComponent.STR_FILED_FILENAME);
    this.strComma = this.settingsService.getSetting(this.c, f, component, CsvInterpretComponent.STR_FILED_COMMA);
    this.strEOL = this.settingsService.getSetting(this.c, f, component, CsvInterpretComponent.STR_FILED_EOL);
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
    const data: string = new CardsToCsvPipe().transform(cards, [this.strComma, this.strEOL]);

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

    let cards: Card[] = new CsvToCardsPipe().transform(csv, [this.strComma, this.strEOL]);
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

  // Button Handler
  private btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(f, 'btnLabel[' + btnLabel + ']');

    switch (btnLabel) {
      case CsvInterpretComponent.STR_INIT: {
        this.disableImportMsg = true;
        this.importNumber = 0;
        this.disableExportMsg = true;
        this.exportNumber = 0;
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

