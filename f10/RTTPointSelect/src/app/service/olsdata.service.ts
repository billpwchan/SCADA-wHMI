import { Injectable } from '@angular/core';
import { Olsdata } from '../rtt/olsdata';
import { ConfigService } from '../service/config.service';

@Injectable()
export class OlsdataService {

  constructor(
    private configService: ConfigService
  ) {}

  public populateComplexPointName(olsArr: Olsdata[]): void {
    // console.log('populatingComplexPointName. Data size: ' + olsArr.length);
    // const olsObjArr = [] ;
    olsArr.map(o => {
      // const olsObj: Olsdata = new Olsdata(o);
      // console.log('check:' + olsObj.ID + ', ' + o.ID);
      const nameArr: string[] = o.Name.split('|');
      // console.log('splitted length: ' + olsObj.Name + ', ' + nameArr.length);
        if (nameArr.length < 5) {
          o.color = '#ff0000';
        } else {
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
          console.log('checking Name:' + o.Name);
          console.log('checking Name arr:' + nameArr);
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

          const column_idx_1 = this.configService.config.getIn(['col_idx_1']);
          const column_idx_2 = this.configService.config.getIn(['col_idx_2']);
          const column_idx_3 = this.configService.config.getIn(['col_idx_3']);
          const column_idx_4 = this.configService.config.getIn(['col_idx_4']);
          const column_idx_5 = this.configService.config.getIn(['col_idx_5']);
          const column_idx_6 = this.configService.config.getIn(['col_idx_6']);

          o.column1 = nameArr[column_idx_1];
          o.column2 = nameArr[column_idx_2];
          o.column3 = nameArr[column_idx_3];
          o.column4 = nameArr[column_idx_4];
          o.column5 = nameArr[column_idx_5];
          o.column6 = nameArr[column_idx_6];
          console.log('checking value: ' + o.column1);
          // olsObj.setComplexPointName(
          //   nameArr[1],
          //   nameArr[2],
          //   nameArr[3],
          //   nameArr[4],
          //   nameArr[5],
          //   nameArr[6],
          //   nameArr[7],
          //   nameArr[8],
          //   nameArr[9],
          //   nameArr[10]
          // )
        }
    })
  }
}
