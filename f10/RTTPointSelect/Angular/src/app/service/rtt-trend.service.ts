import { Injectable } from '@angular/core';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import { ConfigService } from './config.service';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { text } from '@angular/core/src/render3/instructions';

@Injectable()
export class RttTrendService {
  private static headers = new HttpHeaders({
    'Content-Type': 'Content-Type:text/html;charset=UTF-8'
  });
  private urlRtt = this.configService.config.getIn(['rtt_url']);
  constructor(
    private http: HttpClient,
    private configService: ConfigService
  ) { }

  public readTrendUrl(params: string): Promise<string> {
    let url = this.urlRtt + params;
    url = url.replace(/\\n/g, '\\n')
      .replace(/\\'/g, '\\\'')
      .replace(/\\&/g, '\\&')
      .replace(/\\r/g, '\\r')
      .replace(/\\t/g, '\\t')
      .replace(/\\b/g, '\\b')
      .replace(/\\f/g, '\\f');

    url.replace(/[\u0000-\u0019]+/g, '');

    // Must specify the responseType, or it will cause Json.parse error.
    return this.http.get(url, {responseType: 'text'}).toPromise().then(res => res).catch(this.handleError);
  }

  private handleError(error: any) {
    console.error('{RttTrendService}', '[handleError]', error);
    return Promise.reject(error.message || error);
  }
}
