import { Injectable } from '@angular/core';
import { Olsdata } from './olsdata';
import { ConfigService } from './config.service';

import { TranslateService, LangChangeEvent } from '@ngx-translate/core';

@Injectable()
export class OlsdataService {
  [key: string]: any;

  constructor(
    private configService: ConfigService,
    private translate: TranslateService
  ) {
    translate.onLangChange.subscribe((event: LangChangeEvent) => {
      this.loadTranslations();
    })
  }

  loadTranslations(): void {
    // By default should load preferred / default language.
    console.log('{schedule-table}', '[loadConfig]', 'translate current lang=', this.translate.currentLang);
  }

  public populateComplexPointName(olsArr: Olsdata[]): void {
    if (!olsArr) { return; }

    for (let index = 0; index < olsArr.length; index++) {
      const o = olsArr[index];
      const nameArr: string[] = o.Name.split('|');
      if (nameArr.length < 5 && this.configService.config.getIn(['legacy_mode'])) {
        o.color = '#ff0000';
      } else {
        if (nameArr.length < 5) {
          olsArr = null;
          break;
        }
        /**
         * CbType
         * ReportType
         * Station
         * System
         * EqpType
         * EquipmentLabel
         * EquipmentShortLabel
         * PointLabel
         * PointUnit
         * DB_point_alias
         * Spare 1
         * Spare 2
         * Spare 3
         * Spare 4
         * Spare 5
        **/
        // console.log('checking Name:' + o.Name);
        // console.log('checking Name arr:' + nameArr);
        // there is an empty slot in nameArr[0]
        // o.CBType = nameArr[1];
        // o.ReportType = nameArr[2];
        // o.Station = nameArr[3];
        // o.System = nameArr[4];
        // o.EqpType = nameArr[5];
        // o.EquipmentLabel = nameArr[6];
        // o.EquipmentShortLabel = nameArr[7];
        // o.PointLabel = nameArr[8];
        // o.PointUnit = nameArr[9];
        o.DB_point_Alias = nameArr[10];
        // initialize default color
        o.color = '#ff0000';
        o.attachedAxis = '1';
        const column_idx_list = [];

        for (let i = 1; i <= 19; i++) {
          if (this.configService.config.getIn(['col_idx_' + i.toString()])) {
            column_idx_list.push(this.configService.config.getIn(['col_idx_' + i.toString()]));
          }
        }

        for (let varIndex = 0; varIndex < 19; varIndex++) {
          const varName = 'column' + (varIndex + 1).toString();
          o[varName] = nameArr[column_idx_list[varIndex]];
        }
      }
    }
  }
}
