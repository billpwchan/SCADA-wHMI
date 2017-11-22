import { Injectable } from '@angular/core';
import { Headers, Http, Response } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import { Observable } from 'rxjs/Rx';
import { ConfigService } from './config.service';
import { RttTrendDef } from './rtt-trend-def';

@Injectable()
export class RttTrendService {
  private static headers = new Headers ({
    'Content-Type': 'Content-Type:text/html;charset=UTF-8'
  });
  private urlRtt = this.configService.config.getIn(['rtt_url']);
  constructor(
      private http: Http,
      private configService: ConfigService
  ) { }

    public readTrendUrl(params: string): Promise<string> {
        console.log('{RttTrendService}', '[readTrendUrl] begin');
        const url = this.urlRtt + params;
        console.log('url: ' + url);

        return this.http.get(url).toPromise().then(
          this.readUrl
        ).catch(this.handleError);
    }

    private readUrl(res: Response) {
        const urlBody = res.text();
        console.log(console.log('{RttTrendService}', '[readUrl]', urlBody));
        return urlBody;
    }

    private handleError(error: Response | any) {
        console.error('{RttTrendService}', '[handleError]', error);
        return Promise.reject(error.message || error);
    }
}
