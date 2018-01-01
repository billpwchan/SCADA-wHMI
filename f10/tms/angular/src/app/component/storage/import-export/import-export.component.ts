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
import { ImportExportSettings, ImportFileType, ExportFileType } from './import-export-settings';
import { importType } from '@angular/compiler/src/output/output_ast';
import { Subscription } from 'rxjs/Subscription';
import { SelectionService } from '../../../service/card/selection.service';
import { SelectionServiceType } from '../../../service/card/selection-settings';

@Component({
  selector: 'app-import-export'
  , templateUrl: './import-export.component.html'
  , styleUrls: ['./import-export.component.css']
})
export class ImportExportComponent implements OnInit, OnDestroy, OnChanges {

  public static readonly STR_INIT = AppSettings.STR_INIT;
  public static readonly STR_CARD_RELOADED = AppSettings.STR_CARD_RELOADED;
  public static readonly STR_CARD_SELECTED = AppSettings.STR_CARD_SELECTED;
  public static readonly STR_CARD_UPDATED = AppSettings.STR_CARD_UPDATED;
  public static readonly STR_STEP_RELOADED = AppSettings.STR_STEP_RELOADED;
  public static readonly STR_STEP_SELECTED = AppSettings.STR_STEP_SELECTED;

  public static readonly STR_NORIFY_FROM_PARENT = 'notifyFromParent';

  private strFileName: string = CsvToCardSettings.STR_FILENAME;
  private strComma: string = CsvToCardSettings.STR_COMMA;
  private strEOL: string = CsvToCardSettings.STR_EOL;

  readonly c = ImportExportComponent.name;

  @Input() notifyFromParent: string;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  cardSubscription: Subscription;
  selectionSubscription: Subscription;

  btnDisabledExportCSV: boolean;
  btnDisabledImportCSV: boolean;

  disableImportMsg: boolean;
  disableExportMsg: boolean;

  importNumber: number;
  exportNumber: number;

  initCardsBeforeExport: boolean;

  importType: ImportFileType;
  importAccept: string;
  exportType: ExportFileType;
  exportTypeOpt: string;

  private exportSelectionOnly: boolean;
  private selectedCardIds: string[];

  constructor(
    private translate: TranslateService
    , private storageService: StorageService
    , private cardService: CardService
    , private selectionService: SelectionService
    , private settingsService: SettingsService
  ) {
  }

  ngOnInit() {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.loadSettings();

    this.cardSubscription = this.cardService.cardItem
    .subscribe( item => {
      console.log(this.c, f, 'storageSubscription', item);
      switch (item) {
        case CardServiceType.CARD_RELOADED: {
          this.btnClicked(ImportExportComponent.STR_CARD_RELOADED);
        } break;
      }
    });

    this.selectionSubscription = this.selectionService.selectionItem
    .subscribe( item => {
      console.log(this.c, f, 'storageSubscription', item);
      switch (item) {
        case SelectionServiceType.CARD_SELECTED: {
          this.selectedCardIds = this.selectionService.getSelectedCardIds();
          this.btnClicked(ImportExportComponent.STR_CARD_SELECTED);
        } break;
      }
    });

    this.btnClicked(ImportExportComponent.STR_INIT);
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
    this.cardSubscription.unsubscribe();
    this.selectionSubscription.unsubscribe();
  }

  ngOnChanges(changes: SimpleChanges): void {
    const f = 'ngOnChanges';
    console.log(this.c, f);
    console.log(this.c, f, 'changes', changes);
    if ( changes[ImportExportComponent.STR_NORIFY_FROM_PARENT] ) {
      switch (changes[ImportExportComponent.STR_NORIFY_FROM_PARENT].currentValue) {
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

    const component = ImportExportComponent.name;

    this.exportSelectionOnly = this.settingsService.getSetting(this.c, f, component, ImportExportSettings.STR_EXPORT_SELECTION_ONLY);

    this.importType = Number.parseInt(this.settingsService.getSetting(this.c, f, component, ImportExportSettings.STR_IMPORT_TYPE));
    this.importAccept = this.settingsService.getSetting(this.c, f, component, ImportExportSettings.STR_IMPORT_ACCEPT);
    this.exportType = Number.parseInt(this.settingsService.getSetting(this.c, f, component, ImportExportSettings.STR_EXPORT_TYPE));
    this.exportTypeOpt = this.settingsService.getSetting(this.c, f, component, ImportExportSettings.STR_EXPORT_TYPE_OPT);

    this.strFileName = this.settingsService.getSetting(this.c, f, component, ImportExportSettings.STR_EXPORT_FILENAME);
    this.strComma = this.settingsService.getSetting(this.c, f, component, ImportExportSettings.STR_CSV_COMMA);
    this.strEOL = this.settingsService.getSetting(this.c, f, component, ImportExportSettings.STR_CSV_EOL);
    this.initCardsBeforeExport = this.settingsService.getSetting(this.c, f, component, ImportExportSettings.STR_INIT_CARDS_BEFORE_EXPORT);
  }

  /**
   * Export Cards as a file
   *
   * @private
   * @memberof ImportExportComponent
   */
  private exportCards(cards: Card[], type: ExportFileType): void {
    const f = 'exportCards';
    console.log(this.c, f);

    const strCards = JSON.stringify(cards);
    let exportCards = JSON.parse(strCards);
    if ( this.initCardsBeforeExport ) {
      exportCards = this.cardService.initCards(exportCards);
    }

    console.log(this.c, f, 'type', type);
    let data: string;
    switch ( type ) {
      case ExportFileType.CSV: {
        data = new CardsToCsvPipe().transform(exportCards, [this.strComma, this.strEOL]);
      } break;
      case ExportFileType.JSON: {
        data = JSON.stringify(exportCards);
      } break;
    }

    console.log(this.c, f, 'this.exportTypeOpt', this.exportTypeOpt);
    const blob: Blob = new Blob([data], {type: this.exportTypeOpt});
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
   * Import file as a Cards
   *
   * @private
   * @param {string} str
   * @memberof ImportExportComponent
   */
  private importCards(str: string, type: ImportFileType): void {
    const f = 'importCards';
    console.log(this.c, f);

    let cards: Card[];

    console.log(this.c, f, 'type', type);
    switch ( type ) {
      case ImportFileType.CSV: {
        cards = new CsvToCardsPipe().transform(str, [this.strComma, this.strEOL]);
      } break;
      case ImportFileType.JSON: {
        cards = JSON.parse(str);
      } break;
    }

    if ( null != cards ) {

      const deleteIds: string[] = [];
      cards.forEach( item => {
        deleteIds.push(item.name);
      });
      this.cardService.deleteCards(deleteIds);

      this.cardService.addCards(cards);
      this.cardService.notifyUpdate(CardServiceType.CARD_RELOADED);
      this.cardService.notifyUpdate(CardServiceType.CARD_EDITED);

      this.importNumber = this.cardService.getCards().length;
      this.disableImportMsg = false;
    } else {
      console.warn(this.c, f, 'cards is null');
    }
  }

  private loadFile(event) {
    const f = 'loadFile';
    console.log(this.c, f);
    const file: File = event.target.files[0];
    const reader = new FileReader();
    reader.onload = (e: any) => {
//    console.log('name', e.target.name, 'size', e.target.size, 'result', e.target.result);
      this.importCards(e.target.result, this.importType);
    };
    reader.readAsText(file);
  }

  private init(): void {
    const f = 'init';
    console.log(this.c, f);

    this.btnDisabledExportCSV = true;
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
      case ImportExportComponent.STR_INIT: {
        this.init();
      } break;
      case 'exportcsv': {
        const cards: Card[] = this.cardService.getCards(
          this.exportSelectionOnly ? this.selectedCardIds : null
        );
        this.exportCards(cards, this.exportType);
      } break;
      case 'importcsv': {
      } break;
      case 'loadfile': {
        this.loadFile(event);
      } break;
      case 'disableImportMsg': {
        this.disableImportMsg = true;
      } break;
      case 'disableExportMsg': {
        this.disableExportMsg = true;
      } break;
    }

    if ( this.cardService.isRunning() ) {
      this.btnDisabledExportCSV = true;
      this.btnDisabledImportCSV = true;
    } else {
      if ( ! this.exportSelectionOnly
         || ( this.exportSelectionOnly
          && this.selectedCardIds != null
          && this.selectedCardIds.length > 0 ) ) {
        this.btnDisabledExportCSV = false;
      }
      this.btnDisabledImportCSV = false;
    }

    // this.sendNotifyParent(btnLabel);
  }
}

