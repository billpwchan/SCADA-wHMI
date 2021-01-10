import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import 'rxjs/add/operator/toPromise';

@Injectable()
export class AggridTranslateService {

  private static url = './assets/i18n-agGrid/agGridText.json';

  constructor(private http: HttpClient) { }

  public load(): Promise<any> {
    return this.http.get(AggridTranslateService.url).toPromise().then(response => response, failure => null).catch(this.handleError);
  }

  private handleError(error: any): Promise<any> {
    console.error('{Ag-Grid Translate}', '[handleError]', error);
    return Promise.reject(error.message || error);
  }
}
