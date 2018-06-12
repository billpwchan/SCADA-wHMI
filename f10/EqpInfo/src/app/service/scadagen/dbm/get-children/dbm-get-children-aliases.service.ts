import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { AppSettings } from '../../../../app-settings';
import { UtilsHttpModule } from '../../common/utils-http.module';
import { DbmSettings } from '../../../scadagen/dbm/dbm-settings';
import { DbmGetChildrenAliasesResult } from './dbm-get-children-aliases-settings';
import { DbmGetChildrenAliases } from './dbm-get-children-aliases';
import { Subscription } from 'rxjs/Subscription';
import { EnvironmentMappingService } from '../../envs/environment-mapping.service';

@Injectable()
export class DbmGetChildrenAliasesService {

  readonly c = 'DbmGetChildrenAliasesService';

  // Observable source
  private dbmSource = new BehaviorSubject<DbmGetChildrenAliasesResult>(null);

  // Observable cardItem stream
  dbmItem = this.dbmSource.asObservable();

  data: Map<string, Map<string, string[]>> = new Map<string, Map<string, string[]>>();

  private dbmGetChildrenAliases: DbmGetChildrenAliases;
  private dbmGetChildrenAliasesSubscription: Subscription;

  // Service command
  dbmChanged(result: DbmGetChildrenAliasesResult) {
    const f = 'dbmChanged';
    console.log(this.c, f);
    this.dbmSource.next(result);
  }

  constructor(
    private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
    , private environmentMappingService: EnvironmentMappingService
  ) {
    this.dbmGetChildrenAliases = new DbmGetChildrenAliases(httpClient, utilsHttp, environmentMappingService);
    this.dbmGetChildrenAliasesSubscription = this.dbmGetChildrenAliases.dbmItem
    .subscribe( res => {
        const f2 = 'dbmGetChildrenAliasesSubscription';
        console.log(this.c, f2);
        if ( null != res ) {
          this.dbmChanged(res);
        }
      });
  }

  readData(alias: string, dbAddress: string) {
    const f = 'readData';
    console.log(this.c, f);

    this.dbmGetChildrenAliases.readData(alias, dbAddress);
  }
}
