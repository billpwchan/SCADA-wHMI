import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { StorageService } from '../../service/card/storage.service';
import { AppSettings } from '../../app-settings';
import { CardService } from '../../service/card/card.service';
import { CardsToCsvPipe } from '../../pipe/cards-to-csv.pipe';
import { Card } from '../../model/Scenario';
import { CsvToCardsPipe } from '../../pipe/csv-to-cards.pipe';

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
    , private storageService: StorageService
    , private cardService: CardService
  ) {
  }

  ngOnInit() {
  }

  sendNotifyParent(str: string) {
    const f = 'sendNotifyParent';
    console.log(this.c, f, str);
    this.notifyParent.emit(str);
  }

  private exportCardAsCsv(): void {
    const f = 'exportCardAsCsv';
    console.log(this.c, f);

    const cards: Card[] = this.cardService.getCards();
    const data: string = new CardsToCsvPipe().transform(cards);

    const blob: Blob = new Blob([data], {type: 'text/csv'});
    const fileName: string = 'my-test.csv';
    const objectUrl: string = URL.createObjectURL(blob);
    const a: HTMLAnchorElement = document.createElement('a') as HTMLAnchorElement;

    a.href = objectUrl;
    a.download = fileName;
    document.body.appendChild(a);
    a.click();        

    document.body.removeChild(a);
    URL.revokeObjectURL(objectUrl);
  }

  loadFile(event) {
    const f = 'loadFile';
    console.log(this.c, f);
    const files: File[] = event.target.files;
    const file = files[0];

    console.log(files.length);

    const reader = new FileReader();
    reader.onload = (e: any) => {

//      console.log('csv name', e.target.name);
//      console.log('csv size', e.target.size);
      const result = e.target.result;

      console.log(f, 'csv result', result);


    };

    reader.readAsText(file);
  }

  // Button Handler
  private btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(f, 'btnLabel[' + btnLabel + ']');

    switch (btnLabel) {
      case 'exportcsv': {
        this.exportCardAsCsv();
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

