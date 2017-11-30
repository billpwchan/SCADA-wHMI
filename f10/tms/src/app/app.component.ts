import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { OnInit } from '@angular/core/src/metadata/lifecycle_hooks';

import { HttpClient, HttpParams, HttpHeaders, HttpErrorResponse } from '@angular/common/http';

const 
strUrlfsstorage: string = 'http://127.0.0.1:8080'
, strUrlUpstreamFile: string = 'upstreamFile_result'
, strUrlDownstramFile: string = 'downstreamFile_result'
, STR_FILEPATH: string = 'filepath'
, STR_DATA: string = 'data'

, STR_TMS_FILENAME = 'tms.csv'
, STR_METHOD = 'method'
, STR_DOWNLOAD = 'download'
, STR_STREAM = 'stream';

@Component({
  selector: 'app-root'
  , templateUrl: './app.component.html'
  , styleUrls: ['./app.component.css']
  // , template: `<div>{{ 'HELLO' | translate:param }}</div>`
})
export class AppComponent implements OnInit {

  ngOnInit(): void {
    throw new Error("Method not implemented.");
  }

  param = {value: 'world'};
  
  constructor(
    private translate: TranslateService
    , private httpClient: HttpClient
  ) {
      // this language will be used as a fallback when a translation isn't found in the current language
      translate.setDefaultLang('en');

        // the lang to use, if the lang isn't available, it will use the current loader to get them
      translate.use('en');
  }

  title = 'app';
  
  rows_scenariolist = [
    { name: 'Scenario # 1', state: 'Stop' }
    ,{ name: 'Scenario # 2', state: 'Stop' }
    ,{ name: 'Scenario # 3', state: 'Stop' }
    ,{ name: 'Scenario # 4', state: 'Stop' }
    ,{ name: 'Scenario # 5', state: 'Stop' }
  ];
  columns_scenariolist = [
    { prop: 'name' }
    ,{ name: 'State' }
  ];

  rows_steplist = [
    { step: '1', location: 'Location # 1', system: 'POW', equipment: 'Breaker (301)', point: 'Point # 1', value: 'Open', delay: '5', status: 'Stop' }
    ,{ step: '2', location: 'Location # 2', system: 'POW', equipment: 'Breaker (501)', point: 'Point # 2', value: 'Open', delay: '5', status: 'Stop' }
  ];
  columns_steplist = [
    { prop: 'step' }
    ,{ name: 'Location' }
    ,{ name: 'System' }
    ,{ name: 'Equipment' }
    ,{ name: 'Point' }
    ,{ name: 'Value' }
    ,{ name: 'Delay' }
    ,{ name: 'Status' }
  ];

  private getData(method){
    this.httpClient.get(
        strUrlfsstorage+'/'+strUrlDownstramFile
        +"?"+STR_FILEPATH+"="+STR_TMS_FILENAME
        +"&"+STR_METHOD+"="+method
      )
        .subscribe(
          res => {
            console.log(res);
          },
          (err: HttpErrorResponse) => {

            console.log("PUT call in error", err.error);

            if (err.error instanceof Error) {
              console.log("Client-side error occured.");
            } else {
              console.log("Server-side error occured.");
            }
          }
          , () => {
            console.log("The PUT observable is now completed.");
          }
        );
  }

  private putData(url: string, filePath: string, filedata: File) {
    console.log('url['+url+']');
    console.log('STR_FILEPATH['+STR_FILEPATH+'] filePath['+filePath+']');
    console.log('STR_DATA['+STR_DATA+'] data['+filedata+']');

    let bodydata = {};
    bodydata[STR_FILEPATH]=filePath;
    bodydata[STR_DATA]=filedata;

    // const bodydata = JSON.stringify({filepath: filePath, data: filedata});
    // console.log('bodydata['+bodydata+']');

    this.httpClient.put(
      url
      , bodydata
    )
      .subscribe(
          res => {
            console.log(res);
          },
          (err: HttpErrorResponse) => {
            if (err.error instanceof Error) {
              console.log("Client-side error occured.");
            } else {
              console.log("Server-side error occured.");
            }
          }
      );
  }

  private postData(url: string, filePath: string, filedata: File) {

    console.log('url['+url+']');
    console.log('STR_FILEPATH['+STR_FILEPATH+'] filePath['+filePath+']');
    console.log('STR_DATA['+STR_DATA+'] data['+filedata+']');

    let bodydata = {};
    bodydata[STR_FILEPATH]=filePath;
    bodydata[STR_DATA]=filedata;

    // const bodydata = JSON.stringify({filepath: filePath, data: filedata});
    // console.log('bodydata['+bodydata+']');

//  let headers = new HttpHeaders().set('header1', 'hvalue1'); // create header object
//  headers = headers.append('header2', hvalue2); // add a new header, creating a new object

//  let params = new HttpParams().set('filepath', filePath); // create params object
//  params = params.append('param2', value2); // add a new param, creating a new object

    this.httpClient.post(
      url
      , bodydata
//    ,{headers: headers, params: params}
    )
      .subscribe(
        res => {
          console.log(res);
        },
        (err: HttpErrorResponse) => {
          if (err.error instanceof Error) {
            console.log("Client-side error occured.");
          } else {
            console.log("Server-side error occured.");
          }
        }
      );
  }
  
  private loadFile(event) {
    let files : File[] = event.target.files;
    let file = files[0];

    console.log(files.length);

    const reader = new FileReader();
    reader.onload = (e: any) => {

//      console.log('csv name', e.target.name);
//      console.log('csv size', e.target.size);
      let result = e.target.result;

      console.log('csv result', result);

      this.postData(strUrlfsstorage+'/'+strUrlUpstreamFile, STR_TMS_FILENAME, result);
    };

    reader.readAsText(file);
  }

  private btnDisabledNew: boolean = false;
  private btnDisabledModify: boolean = false;
  private btnDisabledDelete: boolean = false;
  private btnDisabledCopy: boolean = false;

  private btnDisabledStart: boolean = false;
  private btnDisabledPause: boolean = false;
  private btnDisabledResume: boolean = false;
  private btnDisabledStop: boolean = false;
  private btnDisabledReset: boolean = false;


  private btnDisabledAddedStep: boolean = false;
  private btnDisabledDeleteStep: boolean = false;
  private btnDisabledSaveScenario: boolean = false;
  private btnDisabledReloadScenario: boolean = false;

  private btnDisabledStartStep: boolean = false;
  private btnDisabledStopStep: boolean = false;

  private btnDisabledExportCSV: boolean = false;
  private btnDisabledImportCSV: boolean = false;

  private btnClicked(btnlabel: string, event?: Event) {

    console.log('btnlabel[', btnlabel, ']');
    
    if ( btnlabel === 'new' ) {

    }
    else if ( btnlabel === 'modify' ) {
      
    }
    else if ( btnlabel === 'delete' ) {
      
    }
    else if ( btnlabel === 'copy' ) {
      
    }
    else if ( btnlabel === 'start' ) {
      
    }
    else if ( btnlabel === 'pause' ) {
      
    }
    else if ( btnlabel === 'resume' ) {
      
    }
    else if ( btnlabel === 'stop' ) {
      
    }
    else if ( btnlabel === 'reset' ) {
      
    }
    else if ( btnlabel === 'addedstep' ) {
      
    }
    else if ( btnlabel === 'deletestep' ) {
      
    }
    else if ( btnlabel === 'savescenario' ) {
      
    }
    else if ( btnlabel === 'reloadscenario' ) {
      
    }
    else if ( btnlabel === 'exportstart' ) {
      
    }
    else if ( btnlabel === 'exportcstop' ) {
      
    }
    else if ( btnlabel === 'exportcsv' ) {
      this.getData(STR_DOWNLOAD);
    }
    else if ( btnlabel === 'importcsv' ) {
      
    }
    else if ( btnlabel === 'loadfile' ) {
      this.loadFile(event);
    }

  }
}
