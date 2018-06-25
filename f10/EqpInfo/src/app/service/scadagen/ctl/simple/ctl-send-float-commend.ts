import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpErrorResponse, HttpClient } from '@angular/common/http';
import { UtilsHttpModule } from '../../common/utils-http.module';
import { EnvironmentMappingService } from '../../envs/environment-mapping.service';
import { CtlSettings } from '../../ctl/ctl-settings';

export class CtlSendFloatCommand {

  readonly c = 'CtlSendFloatCommand';

  // Observable source
  private source = new BehaviorSubject<string>(null);

  // Observable dbmPollingItem stream
  item = this.source.asObservable();

  private cfg;

  constructor(
    private httpClient: HttpClient
    , private utilsHttp: UtilsHttpModule
    , private environmentMappingService: EnvironmentMappingService
  ) { }

  // Service command
  ctlChanged(obj: any) {
    const f = 'dbmChanged';
    console.log(this.c, f);
    console.log(this.c, f, obj);
    this.source.next(obj);
  }

  setCfg(cfg) {
    this.cfg = cfg;
  }

  sendCommand(key: string, env: string, names: string[], value: number): void {
    const f = 'sendCommand';
    console.log(this.c, f);

    console.log(this.c, f, 'key', key);
    console.log(this.c, f, 'env', env);
    console.log(this.c, f, 'names', names);
    console.log(this.c, f, 'value', value);

    const urls: string [] = [];
    names.forEach( name => {
      urls.push(name);
    });

    const connAddr = this.environmentMappingService.getEnv(env);
    console.log(this.c, f, 'connAddr', connAddr, 'env', env);

    const urlSendCmd = connAddr + CtlSettings.STR_URL_SEND_FLOAT_CMD + JSON.stringify(urls) + CtlSettings.STR_ATTR_VALUE + value;
    console.log(this.c, f, 'urlSendCmd', urlSendCmd);

    let url = urlSendCmd;
    if (null != this.cfg) {
      if (null != this.cfg['sendAnyway']) {
        url += CtlSettings.STR_ATTR_SEND_ANYWAY + this.cfg['sendAnyway'];
        console.log(this.c, f, 'url', url);
      }
      if (null != this.cfg['bypassRetCond']) {
        url += CtlSettings.STR_ATTR_SEND_ANYWAY + this.cfg['bypassRetCond'];
        console.log(this.c, f, 'url', url);
      }
      if (null != this.cfg['bypassInitCond']) {
        url +=  CtlSettings.STR_ATTR_SEND_ANYWAY + this.cfg['bypassInitCond'];
        console.log(this.c, f, 'url', url);
      }
    }
    console.log(this.c, f, 'url', url);

    this.httpClient.get(
      url
    )
      .subscribe(
        (res: any[]) => {
          console.log(this.c, f, res);
          if (null != res) {
            const dbvalue = res[CtlSettings.STR_RESPONSE][CtlSettings.STR_FIRST_MESSAGE];
            this.ctlChanged(dbvalue);
          } else {
            console.warn(this.c, f, 'res IS NULL, url[' + url + ']');
          }
        }
        , (err: HttpErrorResponse) => { this.utilsHttp.httpClientHandlerError(f, err); }
        , () => { this.utilsHttp.httpClientHandlerComplete(f); }
    );
  }
}
