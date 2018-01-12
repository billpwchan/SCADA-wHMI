import { Component, OnInit, OnDestroy, OnChanges, Input, Output, EventEmitter, SimpleChanges } from '@angular/core';
import { AppSettings } from '../../../app-settings';
import { SettingsService } from '../../../service/settings.service';
import { MatrixSettings, Selection, Matrix, MatrixConfig } from './matrix-settings';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { CardService } from '../../../service/card/card.service';
import { SelectionService } from '../../../service/card/selection.service';
import { Subscription } from 'rxjs/Subscription';
import { CardServiceType } from '../../../service/card/card-settings';
import { SelectionServiceType } from '../../../service/card/selection-settings';
import { Card } from '../../../model/Scenario';
import { DataScenarioHelper } from '../../../model/DataScenarioHelper';

@Component({
  selector: 'app-matrix',
  templateUrl: './matrix.component.html',
  styleUrls: ['./matrix.component.css']
})
export class MatrixComponent implements OnInit, OnDestroy, OnChanges {

  readonly c = 'MatrixComponent';

  @Input() notifyFromParent: string;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  @Input() matrixCfg: MatrixConfig;

  private preview: number[][];
  private updated: number[][];
  @Input()
  set updateMatrix(data: number[][]) {
    if ( null != data ) {
      this.preview = JSON.parse(JSON.stringify(data));
      this.updated = JSON.parse(JSON.stringify(data));
    } else {
      this.updated = this.preview = data;
    }
    this.reloadData();
  }
  @Output() onUpdatedMatrix = new EventEmitter<number[][]>();

  private rowHeaderPrefix: string;
  private rowHeaderIds: number[];
  private rowHeaderWidth: number;

  private colHeaderPrefix: string;
  private colHeaderIds: number[];
  private colWidths: number;

  private cellSel: Selection;

  private matrixes: Matrix[];

  private defVal: number;

  spreadsheet_height: number;
  spreadsheet_width: number;
  spreadsheet_visible_rows: number;

  rows_header: string[];
  rows_header_width: number[];
  cols_header: string[];
  col_widths: number[];
  spreadsheet_data: string[][];

  enableSpreadsheet: boolean;
  enableCheckbox: boolean;

  constructor(
    private translate: TranslateService
  ) {
    translate.onLangChange.subscribe((event: LangChangeEvent) => {
      this.loadTranslations();
    });
  }

  ngOnInit() {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.btnClicked(MatrixSettings.STR_INIT);
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
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

  onConfigChange(matrixConfig: MatrixConfig): void {
    const f = 'onConfigChange';
    console.log(this.c, f);

    this.loadConfigs(matrixConfig);
  }

  ngOnChanges(changes: SimpleChanges): void {
    const f = 'ngOnChanges';
    console.log(this.c, f);
    if ( changes[MatrixSettings.STR_NORIFY_FROM_PARENT] ) {
      this.onParentChange(changes[MatrixSettings.STR_NORIFY_FROM_PARENT].currentValue);
    } else if ( changes[MatrixSettings.STR_MATRIX_CFG] ) {
      this.onConfigChange(changes[MatrixSettings.STR_MATRIX_CFG].currentValue);
    }
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

  private loadConfigs(matrixConfig: MatrixConfig): void {
    const f = 'loadConfigs';
    console.log(this.c, f);
    this.spreadsheet_height = matrixConfig.spreadsheet_height;
    this.spreadsheet_width = matrixConfig.spreadsheet_width;
    this.spreadsheet_visible_rows = matrixConfig.spreadsheet_visible_rows;

    this.rowHeaderPrefix = matrixConfig.row_header_prefix;
    this.rowHeaderIds = matrixConfig.row_header_ids;
    this.rowHeaderWidth = matrixConfig.row_header_width;

    this.colHeaderPrefix = matrixConfig.col_header_prefix;
    this.colHeaderIds = matrixConfig.col_header_ids;
    this.colWidths = matrixConfig.col_width;

    this.defVal = matrixConfig.default_value;

    this.matrixes = matrixConfig.matrixes;
  }

  sendNotifyOnUpdated(data: number[][]) {
    const f = 'sendNotifyOnUpdated';
    console.log(this.c, f);
    console.log(this.c, f, data);
    this.onUpdatedMatrix.emit(data);
  }

  /**
   * @param {MouseEvent} items
   * @param {WalkontableCellCoords} coords
   * @param {Element} element
   */
  beforeOnCellMouseDown(items) {
    const f = 'beforeOnCellMouseDown';
    console.log(this.c, f);
    console.log(this.c, f, items);
    const event = items[0];
    const coords = items[1];
    if ( ! this.enableSpreadsheet ) {
      event.stopImmediatePropagation();
    }
  }

  onSelection(items: any[]): void {
    const f = 'onSelection';
    console.log(this.c, f);

    if ( undefined === this.cellSel ) {
      this.cellSel = new Selection();
    }
    this.cellSel.x  = items[0];
    this.cellSel.y  = items[1];
    this.cellSel.x2 = items[2];
    this.cellSel.y2 = items[3];
  }

  trackByIndex(index: number, value: number) {
    return index;
  }

  isIndeterminate(index: number, event): boolean {
    const f = 'isChecked';
    console.log(this.c, f);
    let selected = 0;
    let checked = 0;
    if ( null != this.updated ) {
      if ( this.updated.length > 0 && null != this.updated[0] && this.updated[0].length > 0  ) {
        if ( undefined !== this.cellSel ) {
          for ( let x = this.cellSel.x ; x <= this.cellSel.x2 ; ++x ) {
            for ( let y = this.cellSel.y ; y <= this.cellSel.y2 ; ++y ) {
              selected++;
              if ( DataScenarioHelper.isFlagOn(this.updated[x][y], index) ) {
                checked++;
              }
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
    if ( undefined !== this.cellSel ) {
      if ( 0 === this.isValidData() ) {
        for ( let x = this.cellSel.x ; x <= this.cellSel.x2 ; ++x ) {
          for ( let y = this.cellSel.y ; y <= this.cellSel.y2 ; ++y ) {
            if ( DataScenarioHelper.isFlagOn(this.updated[x][y], index) ) {
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

    if ( undefined !== this.cellSel ) {
      for ( let x = this.cellSel.x ; x <= this.cellSel.x2 ; ++x ) {
        for ( let y = this.cellSel.y ; y <= this.cellSel.y2 ; ++y ) {
          if (event.target.checked) {
            this.updated[x][y] = DataScenarioHelper.setFlagOn(this.updated[x][y], val);
          } else {
            this.updated[x][y] = DataScenarioHelper.setFlagOff(this.updated[x][y], val);
          }
        }
      }
    }
    this.reloadData();
    this.sendNotifyOnUpdated(this.updated);
  }

  private getCellStr(data: number[][], x: number, y: number): string {
    const f = 'getCellStr';
    console.log(this.c, f);
    let ret = '';
    if ( undefined !== data && undefined !== data[x] && undefined !== data[x][y] ) {
      for ( let i = 0 ; i < this.matrixes.length ; ++i ) {
        if ( DataScenarioHelper.isFlagOn( data[x][y], this.matrixes[i].index) ) {
          if ( 0 !== ret.length ) {
            ret += this.translate.instant(MatrixSettings.STR_DELFAULT_CELL_COMMA);
          }
          ret += this.translate.instant(this.matrixes[i].shortlabel);
        }
      }
      if ( 0 === ret.length ) {
        ret = this.translate.instant(MatrixSettings.STR_DELFAULT_CELL_EMPTY);
      }
    } else {
      ret = x + ':' + y + ' ' + this.translate.instant(MatrixSettings.STR_DELFAULT_CELL_INCORRECT);
    }
    return ret;
  }

  private getIsValidConfigurationStr(val: number): string {
    const f = 'getIsValidConfigurationStr';
    console.log(this.c, f);
    let ret = '';
    switch (val) {
      case 1: {
        ret = MatrixSettings.STR_CFG_ROW_HEADER_WIDTH_IS_NEGAVITE;
      } break;
      case 2: {
        ret = MatrixSettings.STR_CFG_ROW_HEADER_ID_IS_NULL_OR_INVALID;
      } break;
      case 3: {
        ret = MatrixSettings.STR_CFG_COL_WIDTH_IS_NEGAVITE;
      } break;
      case 4: {
        ret = MatrixSettings.STR_CFG_COL_HEADER_ID_IS_NULL_OR_INVALID;
      } break;
    }
    return ret;
  }

  private isValidConfiguration(): number {
    const f = 'isValidConfiguration';
    console.log(this.c, f);
    let ret = 0;
    if ( ! ( this.rowHeaderWidth > 0 ) ) {
      ret++;
    } else {
      if ( ! (null != this.rowHeaderIds && this.rowHeaderIds.length > 0 ) ) {
        ret++;
      } else {
        if ( ! ( this.colWidths > 0 ) ) {
          ret++;
        } else {
          if ( ! (null != this.colHeaderIds && this.colHeaderIds.length > 0 ) ) {
            ret++;
          }
        }
      }
    }
    return ret;
  }

  private getIsValidDataStr(val: number): string {
    const f = 'getIsValidDataStr';
    console.log(this.c, f);
    let ret = '';
    switch (val) {
      case 1: {
        ret = MatrixSettings.STR_DATA_IS_NULL;
      } break;
      case 2: {
        ret = MatrixSettings.STR_DATA_LENGTH_IS_ZERO;
      } break;
      case 3: {
        ret = MatrixSettings.STR_DATA_INDEX_ZERO_IS_NULL;
      } break;
      case 4: {
        ret = MatrixSettings.STR_DATA_INDEX_ZERO_LENGTH_IS_ZERO;
      } break;
    }
    return ret;
  }

  private isValidData(): number {
    const f = 'isValidData';
    console.log(this.c, f);
    let ret = 0;
    if ( null == this.updated ) {
      ret++;
    } else {
      if ( this.updated.length === 0 ) {
        ret++;
      } else {
        if ( null == this.updated[0] ) {
          ret++;
        } else {
          if ( this.updated[0].length === 0 ) {
            ret++;
          }
        }
      }
    }
    return ret;
  }

  reloadData() {
    const f = 'reloadData';
    console.log(this.c, f);

    // Setup Col/Row Header
    this.rows_header        = [];
    this.rows_header_width  = [];
    this.cols_header        = [];
    this.col_widths         = [];
    this.spreadsheet_data   = new Array<Array<string>>();

    const isvalidConfigurationId = this.isValidConfiguration();
    if ( 0 === isvalidConfigurationId ) {
      // Refresh Row Header and Col Header
      this.rowHeaderIds.forEach(element => {
        this.rows_header.push(this.translate.instant(this.rowHeaderPrefix + element));
        this.rows_header_width.push(this.rowHeaderWidth);
      });
      this.colHeaderIds.forEach(element => {
        this.cols_header.push(this.translate.instant(this.colHeaderPrefix + element));
        this.col_widths.push(this.colWidths);
      });
    } else {
      console.warn(this.c, f, this.getIsValidConfigurationStr(isvalidConfigurationId) );
    }

    const isValidDataId = this.isValidData();
    if ( 0 !== isValidDataId ) {
      console.warn(this.c, f, this.getIsValidDataStr(isvalidConfigurationId) );
      this.updated = this.createEmptyData(this.defVal);
    }

    // Refresh Data
    for (let i = 0; i < this.rows_header.length ; ++i) {
      this.spreadsheet_data[i] = new Array<string>();
      for (let j = 0 ; j < this.cols_header.length ; ++ j) {
        this.spreadsheet_data[i][j] = this.getCellStr(this.updated, i, j);
      }
    }
  }

  private createEmptyData(defVal: number): number[][] {
    const f = 'createEmptyData';
    console.log(this.c, f);
    const data = new Array<Array<number>>();
    for (let i = 0; i < this.rowHeaderIds.length ; ++i) {
      data[i] = new Array<number>();
      for (let j = 0 ; j < this.colHeaderIds.length ; ++ j ) {
        data[i][j] = defVal;
      }
    }
    return data;
  }

  private init(): void {
    const f = 'init';
    console.log(this.c, f);

    this.enableSpreadsheet = true;
    this.enableCheckbox = true;
  }

  btnClicked(btnLabel: string, event?: Event): void {
    const f = 'btnClicked';
    console.log(this.c, f);
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');
    switch (btnLabel) {
      case MatrixSettings.STR_INIT: {
        this.init();
        this.reloadData();
      } break;
    }
  }
}
