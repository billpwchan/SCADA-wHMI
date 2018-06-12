import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { UtilsHttpModule } from '../../common/utils-http.module';
import { AppSettings } from '../../../../app-settings';
import { DbmSettings } from '../../../scadagen/dbm/dbm-settings';
import { GetInstancesByClassName } from './dbm-get-instance-by-class-name';
import { Subscription } from 'rxjs/Subscription';
import { EnvironmentMappingService } from '../../envs/environment-mapping.service';

@Injectable()
export class DbmGetInstancesByClassNameService {

  readonly c = 'DbmGetInstancesByClassNameService';

  // Observable source
  private dbmSource = new BehaviorSubject<string>('');

  // Observable cardItem stream
  dbmItem = this.dbmSource.asObservable();

  private getInstancesByClassName: GetInstancesByClassName;

  private getInstancesByClassNameSubscription: Subscription;

  // Service command
  dbmChanged(str: string) {
    const f = 'dbmChanged';
    console.log(this.c, f);
    this.dbmSource.next(str);
  }

  constructor(
    private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
    , private environmentMappingService: EnvironmentMappingService
  ) {
    this.getInstancesByClassName = new GetInstancesByClassName(httpClient, utilsHttp, environmentMappingService);
    this.getInstancesByClassNameSubscription = this.getInstancesByClassName.dbmItem
    .subscribe( res => {
      const f = 'getInstancesByClassNameSubscription';
      console.log(this.c, f);
      this.dbmChanged(res);
    });
  }

  readData(env: string, className: string) {
    const f = 'readData';
    console.log(this.c, f);

    this.getInstancesByClassName.readData(env, className);
  }

}
