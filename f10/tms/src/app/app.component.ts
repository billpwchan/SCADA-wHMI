import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { OnInit } from '@angular/core/src/metadata/lifecycle_hooks';

import { HttpClient, HttpParams, HttpHeaders, HttpErrorResponse } from '@angular/common/http';

import { ScenarioCard, ScenarioStep } from '../model/Scenario'; 
import { DatatableScenarioCard, DatatableScenarioStep } from '../model/DatatableScenario';

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

var scenarioCards: ScenarioCard[] = [];

@Component({
  selector: 'app-root'
  , templateUrl: './app.component.html'
  , styleUrls: [
    './app.component.css'
  ]
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
  
 columns_scenariocard = [
    { prop: 'name' }
    ,{ name: 'State' }
  ];
  rows_scenariocard = new Array<DatatableScenarioCard>();
  selected_scenariocard = new Array<DatatableScenarioCard>();
  
  columns_scenariostep = [
    { prop: 'step' }
    ,{ name: 'Location' }
    ,{ name: 'System' }
    ,{ name: 'Equipment' }
    ,{ name: 'Point' }
    ,{ name: 'Value' }
    ,{ name: 'Delay' }
    ,{ name: 'Status' }
  ];
  rows_scenariostep = new Array<DatatableScenarioStep>();
  selected_scenariostep = new Array<DatatableScenarioStep>();

  private reloadScenarioStep(): void {
    const func: string = "reloadScenarioStep";
    console.log(func);
    if ( this.selected_scenariocard.length > 0 ) {
        this.selected_scenariocard.forEach((item, index) => {
          console.log(func,'name',item.name,'index',index);
          for ( let i = 0 ; i < scenarioCards.length ; ++i ) {
            console.log(func,'scenarioCards[i].name',scenarioCards[i].name);
            if ( scenarioCards[i].name === item.name ) {
              console.log(func,'new','name',item.name,'index',index);
              this.rows_scenariostep = [];
              scenarioCards.forEach((item, index) => {
                let steps = item.scenarioSteps;
                steps.forEach((item, index)=>{
                  let datatableScenarioStep: DatatableScenarioStep = new DatatableScenarioStep(
                    ""+item.step
                    , "&location"+item.location
                    , "&system"+item.system
                    , item.equipment
                    , item.point
                    , "&equipment"+item.value
                    , ""+item.delay
                    , item.status?"Running":"Stop"
                  );
                  this.rows_scenariostep.push(datatableScenarioStep);
                });
              })
            }
          }
      });
    }
    this.rows_scenariostep = [...this.rows_scenariostep]
  }

  private newScenarioStep(): void {
    const func: string = "newScenarioStep";
    console.log(func);
    if ( this.selected_scenariocard.length > 0 ) {
      this.selected_scenariocard.forEach((item, index) => {
        console.log(func,'name',item.name,'index',index);
        for ( let i = 0 ; i < scenarioCards.length ; ++i ) {
          console.log(func,'scenarioCards[i].name',scenarioCards[i].name);
          if ( scenarioCards[i].name === item.name ) {
            console.log(func,'new','name',item.name,'index',index);
            let key = scenarioCards[i].scenarioSteps.length;       
            scenarioCards[i].scenarioSteps.push(new ScenarioStep(
              key
              , key
              , key
              , 'Breaker ('+key+')'
              , 'Point # '+key
              , key
              , key
              , false)
            );
          }
        }
      });
    }
    else {
      console.log(func,'selected_scenariolist IS EMPTY');
    }
    this.reloadScenarioStep();
  }

  private deleteScenarioStep(): void {
    const func: string = "deleteScenarioStep";
    console.log(func);
    if ( this.selected_scenariostep.length > 0 ) {
      if ( this.selected_scenariocard.length > 0 ) {
        this.selected_scenariocard.forEach((item, index) => {
          console.log(func,'name',item.name,'index',index);
          for ( let i = 0 ; i < scenarioCards.length ; ++i ) {
            let scenarioCard:ScenarioCard = scenarioCards[i];
            console.log(func,'scenarioCard.name',scenarioCard.name);
            if ( scenarioCard.name === item.name ) {
              console.log(func,'delete','name',item.name,'index',index);
  
              for ( let x = 0 ; x < this.selected_scenariostep.length ; ++x ) {
                let datatableScenarioStep: DatatableScenarioStep = this.selected_scenariostep[x];
                console.log(func,'delete','datatableScenarioStep.step',datatableScenarioStep.step,'x',x);
                for ( let y = 0 ; y < scenarioCard.scenarioSteps.length ; ++y ) {
                  let scenarioStep: ScenarioStep = scenarioCard.scenarioSteps[y];
                  console.log(func,'delete','datatableScenarioStep.step',datatableScenarioStep.step,'index',index);
                  if ( ""+scenarioStep.step === datatableScenarioStep.step ) {
                    console.log(func,'delete','y',y);
                    scenarioCard.scenarioSteps.splice(y,1);
                  }
                }
              }
            }
          }
        });
      }
    }
    this.reloadScenarioStep();
  }

  private startScenarioStep(): void {
    const func: string = "startScenarioStep";
    console.log(func);
    if ( this.selected_scenariostep.length > 0 ) {
      if ( this.selected_scenariocard.length > 0 ) {
        this.selected_scenariocard.forEach((item, index) => {
          console.log(func,'name',item.name,'index',index);
          for ( let i = 0 ; i < scenarioCards.length ; ++i ) {
            let scenarioCard:ScenarioCard = scenarioCards[i];
            console.log(func,'scenarioCard.name',scenarioCard.name);
            if ( scenarioCard.name === item.name ) {
              console.log(func,'delete','name',item.name,'index',index);
  
              for ( let x = 0 ; x < this.selected_scenariostep.length ; ++x ) {
                let datatableScenarioStep: DatatableScenarioStep = this.selected_scenariostep[x];
                console.log(func,'delete','datatableScenarioStep.step',datatableScenarioStep.step,'x',x);
                for ( let y = 0 ; y < scenarioCard.scenarioSteps.length ; ++y ) {
                  let scenarioStep: ScenarioStep = scenarioCard.scenarioSteps[y];
                  console.log(func,'delete','datatableScenarioStep.step',datatableScenarioStep.step,'index',index);
                  if ( ""+scenarioStep.step === datatableScenarioStep.step ) {
                    console.log(func,'delete','y',y);
                    scenarioStep.status = true;
                  }
                }
              }
            }
          }
        });
      }
    }
    this.reloadScenarioStep();
  }

  private stopScenarioStep(): void {
    const func: string = "stopScenarioStep";
    console.log(func);
    if ( this.selected_scenariostep.length > 0 ) {
      if ( this.selected_scenariocard.length > 0 ) {
        this.selected_scenariocard.forEach((item, index) => {
          console.log(func,'name',item.name,'index',index);
          for ( let i = 0 ; i < scenarioCards.length ; ++i ) {
            let scenarioCard:ScenarioCard = scenarioCards[i];
            console.log(func,'scenarioCard.name',scenarioCard.name);
            if ( scenarioCard.name === item.name ) {
              console.log(func,'delete','name',item.name,'index',index);

              for ( let x = 0 ; x < this.selected_scenariostep.length ; ++x ) {
                let datatableScenarioStep: DatatableScenarioStep = this.selected_scenariostep[x];
                console.log(func,'delete','datatableScenarioStep.step',datatableScenarioStep.step,'x',x);
                for ( let y = 0 ; y < scenarioCard.scenarioSteps.length ; ++y ) {
                  let scenarioStep: ScenarioStep = scenarioCard.scenarioSteps[y];
                  console.log(func,'delete','datatableScenarioStep.step',datatableScenarioStep.step,'index',index);
                  if ( ""+scenarioStep.step === datatableScenarioStep.step ) {
                    console.log(func,'delete','y',y);
                    scenarioStep.status = false;
                  }
                }
              }
            }
          }
        });
      }
    }
    this.reloadScenarioStep();
  }

  private reloadScenarioCard(): void {
    const func: string = "reloadScenarioCard";
    console.log(func);
    this.rows_scenariocard = [];
    scenarioCards.forEach((item, index) => {
      let name = item.name;
      let state = item.state ? "Runnning" : "Stop";
      console.log(func,"index["+index+"] name["+name+"] state["+state+"]");
      this.rows_scenariocard.push(new DatatableScenarioCard(name, state));
    })
    this.rows_scenariocard = [...this.rows_scenariocard]
  }

  private newScenario() {
    const func: string = "newScenario";
    console.log(func);
    let name: string = 'Scenario # '+scenarioCards.length;
    scenarioCards.push(new ScenarioCard(name, false));
    this.reloadScenarioCard();
  }

  private deleteScenario() {
    const func: string = "deleteScenario";
    console.log(func);
    if ( this.selected_scenariocard.length > 0 ) {
      this.selected_scenariocard.forEach((item, index) => {
        console.log(func,'name',item.name,'index',index);
        for ( let i = 0 ; i < scenarioCards.length ; ++i ) {
          console.log(func,'scenarioCards[i].name',scenarioCards[i].name);
          if ( scenarioCards[i].name === item.name ) {
            console.log(func,'remove','name',item.name,'index',index);
            scenarioCards.splice(i,1);
          }
        }
      });
    }
    else {
      console.log(func,'selected_scenariolist IS EMPTY');
    }
    this.reloadScenarioCard();
  }

  private startScenario(): void {
    const func: string = "startScenario";
    console.log(func);
    if ( this.selected_scenariocard.length > 0 ) {
      this.selected_scenariocard.forEach((item, index) => {
        console.log(func,'name',item.name,'index',index);

        for ( let i = 0 ; i < scenarioCards.length ; ++i ) {
          console.log(func,'scenarioCards[i].name',scenarioCards[i].name);
          if ( scenarioCards[i].name === item.name ) {
            console.log(func,'start','name',item.name,'index',index);
            scenarioCards[i].state=true;
          }
        }
      });
    }
    else {
      console.log(func,'selected_scenariolist IS EMPTY');
    }
    this.reloadScenarioCard();
  }

  private stopScenario(): void {
    const func: string = "stopScenario";
    console.log(func);
    if ( this.selected_scenariocard.length > 0 ) {
      this.selected_scenariocard.forEach((item, index) => {
        console.log(func,'name',item.name,'index',index);
        for ( let i = 0 ; i < scenarioCards.length ; ++i ) {
          console.log(func,'scenarioCards[i].name',scenarioCards[i].name);
          if ( scenarioCards[i].name === item.name ) {
            console.log(func,'end','name',item.name,'index',index);
            scenarioCards[i].state=false;
          }
        }
      });
    }
    else {
      console.log(func,'selected_scenariolist IS EMPTY');
    }
    this.reloadScenarioCard();
  }

  private getData(method: string): void {
    const func: string="getData";
    console.log(func);
    console.log(func,'method',method);

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
            console.log(func,"PUT call in error", err.error);
            if (err.error instanceof Error) {
              console.log(func,"Client-side error occured.");
            } else {
              console.log(func,"Server-side error occured.");
            }
          }
          , () => {
            console.log(func,"The GET observable is now completed.");
          }
        );
  }

  private downloadData(): void {
    const func: string="downloadData";
    console.log(func);
    this.getData(STR_DOWNLOAD);
  }

  private putData(url: string, filePath: string, filedata: File): void {
    const func: string="putData";
    console.log(func);
    console.log(func,'url['+url+']');
    console.log(func,'STR_FILEPATH['+STR_FILEPATH+'] filePath['+filePath+']');
    console.log(func,'STR_DATA['+STR_DATA+'] data['+filedata+']');

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
              console.log(func,"Client-side error occured.");
            } else {
              console.log(func,"Server-side error occured.");
            }
          }
          , () => {
            console.log(func,"The PUT observable is now completed.");
          }
      );
  }

  private postData(url: string, filePath: string, filedata: File) {
    const func: string="postData";
    console.log(func);
    console.log(func,'url['+url+']');
    console.log(func,'STR_FILEPATH['+STR_FILEPATH+'] filePath['+filePath+']');
    console.log(func,'STR_DATA['+STR_DATA+'] data['+filedata+']');

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
            console.log(func,"Client-side error occured.");
          } else {
            console.log(func,"Server-side error occured.");
          }
        }
        , () => {
          console.log(func,"The PUT observable is now completed.");
        }
      );
  }
  
  private loadFile(event) {
    const func: string="loadFile";
    console.log(func);
    let files : File[] = event.target.files;
    let file = files[0];

    console.log(files.length);

    const reader = new FileReader();
    reader.onload = (e: any) => {

//      console.log('csv name', e.target.name);
//      console.log('csv size', e.target.size);
      let result = e.target.result;

      console.log(func,'csv result', result);

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


  private onRowActivate(name: string, event: Event) {
    console.log('onRowActivate','name',name,'event',event);
  }
  private onRowSelect(name: string, event: Event) {
    console.log('onRowSelect','name',name,'event',event,);
  }

  // Button Handler
  private btnClicked(btnlabel: string, event?: Event) {
    const func: string="btnClicked";
    console.log(func,'btnlabel[', btnlabel, ']');
    
    if ( btnlabel === 'new' ) {
      this.newScenario();
    }
    else if ( btnlabel === 'modify' ) {
      
    }
    else if ( btnlabel === 'delete' ) {
      this.deleteScenario();
    }
    else if ( btnlabel === 'copy' ) {
      
    }
    else if ( btnlabel === 'start' ) {
      this.startScenario();
    }
    else if ( btnlabel === 'pause' ) {
      
    }
    else if ( btnlabel === 'resume' ) {
      
    }
    else if ( btnlabel === 'stop' ) {
      this.stopScenario();
    }
    else if ( btnlabel === 'reset' ) {
      
    }
    else if ( btnlabel === 'addedstep' ) {
      this.newScenarioStep();
    }
    else if ( btnlabel === 'deletestep' ) {
      this.deleteScenarioStep();
    }
    else if ( btnlabel === 'savescenario' ) {
      
    }
    else if ( btnlabel === 'reloadscenario' ) {
      
    }
    else if ( btnlabel === 'startstep' ) {
      this.startScenarioStep();
    }
    else if ( btnlabel === 'stopstep' ) {
      this.stopScenarioStep();
    }   
    else if ( btnlabel === 'exportstart' ) {
      
    }
    else if ( btnlabel === 'exportcstop' ) {
      
    }
    else if ( btnlabel === 'exportcsv' ) {
      this.downloadData();
    }
    else if ( btnlabel === 'importcsv' ) {
      
    }
    else if ( btnlabel === 'loadfile' ) {
      this.loadFile(event);
    }

  }
}
