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
import { OlsAvaSupService, AvaSupPoint } from '../../../service/scs/ava/ols-ava-sup.service';
import { DbmReadAvaSupService } from '../../../service/scs/ava/dbm-read-ava-sup.service';

@Component({
  selector: 'app-matrix',
  templateUrl: './matrix.component.html',
  styleUrls: ['./matrix.component.css']
})
export class MatrixComponent implements OnInit, OnDestroy, OnChanges {

  readonly c = 'MatrixComponent';

  @Input() notifyFromParent: string;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  private cfg: MatrixConfig;
  @Input() config: MatrixConfig;

  private preview: number[][];
  private updated: number[][];
  @Input()
  set updateMatrix(data: Map<number, Map<number, number>>) {
    const f = 'updateMatrix';
    console.log(this.c, f);

    if ( null != data ) {

      if ( null == this.updated ) {
        this.updated = new Array<Array<number>>();
      }

      for ( let i = 0 ; i < this.cfg.rowHeaderIds.length ; ++i ) {
        for ( let j = 0 ; j < this.cfg.colHeaderIds.length ; ++j ) {
          if ( null == this.updated[i] ) {
            this.updated[i] = new Array<number>();
          }
          const row: number = this.cfg.rowHeaderIds[i];
          const col: number = this.cfg.colHeaderIds[j];
          console.log(this.c, f, 'row', row, 'col', col);
          this.updated[i][j] = 0;
          const func: Map<number, number> = data.get(row);
          if ( null != func ) {
            const level = func.get(col);
            if ( null != level ) {
              this.updated[i][j] = level;
            } else {
              console.warn(this.c, f, 'no data for row', row, 'col', col);
            }
          } else {
            console.warn(this.c, f, 'no data for row', row);
          }
        }
      }

      console.log(this.c, f, 'this.updated', this.updated);
      this.generateMatrix();
      this.reloadData();
    } else {
      console.warn(this.c, f, 'data IS INVALID');
    }
  }
  @Output() onUpdatedMatrix = new EventEmitter<Map<number, Map<number, number>>>();

  private cellSel: Selection;

  rows_header: string[];
  rows_header_width: number[];
  cols_header: string[];
  col_widths: number[];
  spreadsheet_data: string[][];

  enableSpreadsheet: boolean;
  enableCheckbox: boolean;

  constructor(
    private translate: TranslateService
    , private settingsService: SettingsService
  ) {
    const f = 'constructor';
    console.log(this.c, f);

    translate.onLangChange.subscribe((event: LangChangeEvent) => {
      this.loadTranslations();
    });

    const matrixConfig: MatrixConfig = this.loadCfg();
    if ( null != matrixConfig ) {
      this.cfg = matrixConfig;
    } else {
      console.warn(this.c, f, 'loadMatrixCfgs IS INVALID');
    }
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

  onConfigChange(cfg: MatrixConfig): void {
    const f = 'onConfigChange';
    console.log(this.c, f);

    this.cfg = cfg;
  }

  ngOnChanges(changes: SimpleChanges): void {
    const f = 'ngOnChanges';
    console.log(this.c, f);
    if ( changes[MatrixSettings.STR_NORIFY_FROM_PARENT] ) {
      this.onParentChange(changes[MatrixSettings.STR_NORIFY_FROM_PARENT].currentValue);
    }
    if ( changes[MatrixSettings.STR_MATRIX_CONFIG] ) {
      this.onConfigChange(changes[MatrixSettings.STR_MATRIX_CONFIG].currentValue);
    }
  }

  sendNotifyParent(str: string) {
    const f = 'sendNotifyParent';
    console.log(this.c, f, str);
    this.notifyParent.emit(str);
  }

  private loadCfg(): MatrixConfig {
    const f = 'loadMatrixCfgs';
    console.log(this.c, f);

    const c = 'MatrixComponent';
    const cfg: MatrixConfig = new MatrixConfig();

    cfg.spreadsheetHeight = this.settingsService.getSetting(this.c, f, c, MatrixSettings.STR_SPREADSHEET_HEIGHT);
    cfg.spreadsheetWidth = this.settingsService.getSetting(this.c, f, c, MatrixSettings.STR_SPREADSHEET_WIDTH);
    cfg.spreadsheetVisibleRows = this.settingsService.getSetting(this.c, f, c, MatrixSettings.STR_SPREADSHEET_VISIBLE_ROW);

    cfg.rowHeaderPrefix = this.settingsService.getSetting(this.c, f, c, MatrixSettings.STR_ROW_HEADER_PREFIX);
    cfg.rowHeaderIds = this.settingsService.getSetting(this.c, f, c, MatrixSettings.STR_ROW_HEADER_IDS);
    cfg.rowHeaderWidth = this.settingsService.getSetting(this.c, f, c, MatrixSettings.STR_ROW_HEADER_WIDTH);

    cfg.colHeaderPrefix = this.settingsService.getSetting(this.c, f, c, MatrixSettings.STR_COL_HEADER_PREFIX);
    cfg.colHeaderIds = this.settingsService.getSetting(this.c, f, c, MatrixSettings.STR_COL_HEADER_IDS);
    cfg.colWidth = this.settingsService.getSetting(this.c, f, c, MatrixSettings.STR_COL_WIDTH);

    cfg.defVal = this.settingsService.getSetting(this.c, f, c, MatrixSettings.STR_DEFAULT_VALUE);

    cfg.matrixes = this.settingsService.getSetting(this.c, f, c, MatrixSettings.STR_MATRIXES) as Matrix[];

    return cfg;
  }

  loadTranslations() {
    const f = 'loadTranslations';
    console.log(this.c, f);

    // this.reloadData();
  }

  sendNotifyOnUpdated(data: Map<number, Map<number, number>>) {
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
    // const f = 'isChecked';
    // console.log(this.c, f);
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
    this.generateMatrix();
    this.reloadData();

    const data: Map<number, Map<number, number>> = new Map<number, Map<number, number>>();
    for ( let i = 0 ; i < this.cfg.rowHeaderIds.length ; ++i ) {
      const row: number = this.cfg.rowHeaderIds[i];
      data.set(row, new Map<number, number>());
      const data2: Map<number, number> = data.get(row);
      for ( let j = 0 ; j < this.cfg.colHeaderIds.length ; ++j ) {
        const col: number = this.cfg.colHeaderIds[j];
        data2.set(col, this.updated[i][j]);
      }
    }
    this.sendNotifyOnUpdated(data);
  }

  private getCellStr(data: number[][], x: number, y: number): string {
    const f = 'getCellStr';
    console.log(this.c, f);
    let ret = '';
    if ( undefined !== data && undefined !== data[x] && undefined !== data[x][y] ) {
      for ( let i = 0 ; i < this.cfg.matrixes.length ; ++i ) {
        if ( DataScenarioHelper.isFlagOn( data[x][y], this.cfg.matrixes[i].index) ) {
          if ( 0 !== ret.length ) {
            ret += this.translate.instant(MatrixSettings.STR_DELFAULT_CELL_COMMA);
          }
          ret += this.translate.instant(this.cfg.matrixes[i].shortlabel);
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
    if ( ! ( this.cfg.rowHeaderWidth > 0 ) ) {
      ret++;
    } else {
      if ( ! (null != this.cfg.rowHeaderIds && this.cfg.rowHeaderIds.length > 0 ) ) {
        ret++;
      } else {
        if ( ! ( this.cfg.colWidth > 0 ) ) {
          ret++;
        } else {
          if ( ! (null != this.cfg.colHeaderIds && this.cfg.colHeaderIds.length > 0 ) ) {
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
    // Refresh Data
    for (let i = 0; i < this.rows_header.length ; ++i) {
      this.spreadsheet_data[i] = new Array<string>();
      for (let j = 0 ; j < this.cols_header.length ; ++ j) {
        this.spreadsheet_data[i][j] = this.getCellStr(this.updated, i, j);
      }
    }
  }

  generateMatrix() {
    const f = 'generateMatrix';
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
      this.cfg.rowHeaderIds.forEach(element => {
        this.rows_header.push(this.translate.instant(this.cfg.rowHeaderPrefix + element));
        this.rows_header_width.push(this.cfg.rowHeaderWidth);
      });
      this.cfg.colHeaderIds.forEach(element => {
        this.cols_header.push(this.translate.instant(this.cfg.colHeaderPrefix + element));
        this.col_widths.push(this.cfg.colWidth);
      });
    } else {
      console.warn(this.c, f, this.getIsValidConfigurationStr(isvalidConfigurationId) );
    }

    const isValidDataId = this.isValidData();
    if ( 0 !== isValidDataId ) {
      console.warn(this.c, f, this.getIsValidDataStr(isvalidConfigurationId) );
      this.updated = this.createEmptyData(this.cfg.defVal);
    }

  }

  private createEmptyData(defVal: number): number[][] {
    const f = 'createEmptyData';
    console.log(this.c, f);
    const data = new Array<Array<number>>();
    for (let i = 0; i < this.cfg.rowHeaderIds.length ; ++i) {
      data[i] = new Array<number>();
      for (let j = 0 ; j < this.cfg.colHeaderIds.length ; ++ j ) {
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
        this.generateMatrix();
        this.reloadData();
      } break;
    }
  }
}
