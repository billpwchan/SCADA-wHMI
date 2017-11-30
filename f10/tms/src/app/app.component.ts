import { Component } from '@angular/core';
import { Observable, Subscription } from 'rxjs/Rx';
import { TranslateService } from '@ngx-translate/core';
import { OnInit } from '@angular/core/src/metadata/lifecycle_hooks';

import { HttpClient, HttpParams, HttpHeaders, HttpErrorResponse } from '@angular/common/http';

import { ScenarioCard, ScenarioStep } from '../model/Scenario'; 
import { DatatableScenarioCard, DatatableScenarioStep } from '../model/DatatableScenario';
import { Console } from '@angular/core/src/console';

const 
strUrlfsstorage: string = 'http://127.0.0.1:8080'
, strUrlUpstreamFile: string = 'upstreamFile_result'
, strUrlDownstramFile: string = 'downstreamFile_result'
, STR_FILEPATH: string = 'filepath'
, STR_DATA: string = 'data'

, STR_TMS_FILENAME: string = 'tms.csv'
, STR_METHOD: string = 'method'
, STR_DOWNLOAD: string = 'download'
, STR_STREAM: string = 'stream'

, STR_TMS_FILENAME_JSON: string = 'tms.json'

, STR_NAME_CARD: string = 'card'
, STR_NAME_STEP: string = 'step'

, INT_STOP: number = 0
, INT_RUNNING: number = 1
, INT_PAUSE: number = 2

, STR_STOP: string ="Stop"
, STR_RUNNING: string = "Running"
, STR_PAUSE: string = "PAUSE"
;

var cards: ScenarioCard[] = [];

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
  
 columns_card = [
    { prop: 'name' }
    ,{ name: 'State' }
  ];
  rows_card = new Array<DatatableScenarioCard>();
  selected_card = new Array<DatatableScenarioCard>();
  
  columns_step = [
    { prop: 'step' }
    ,{ name: 'Location' }
    ,{ name: 'System' }
    ,{ name: 'Equipment' }
    ,{ name: 'Point' }
    ,{ name: 'Value' }
    ,{ name: 'Delay' }
    ,{ name: 'Status' }
  ];
  rows_step = new Array<DatatableScenarioStep>();
  selected_step = new Array<DatatableScenarioStep>();

  private getState(state: number): string {
    let res: string = STR_PAUSE;
    switch(state) {
      case INT_STOP: res = STR_STOP; break;
      case INT_RUNNING: res = STR_RUNNING; break;
    }
    return res;
  }

  private reloadScenarioStep(): void {
    const func: string = "reloadScenarioStep";
    console.log(func);

    // Rset ScenarioStep
    this.rows_step = [];

    if ( this.selected_card.length > 0 ) {
        this.selected_card.forEach((item, index) => {
          console.log(func,'name',item.name,'index',index);
          
          for ( let i = 0 ; i < cards.length ; ++i ) {
            let card: ScenarioCard = cards[i];
            console.log(func,'scenarioCard.name',card.name);
            if ( card.name === item.name ) {
              let steps = card.steps;
              if ( steps.length > 0 ) {
                steps.forEach((item, index)=>{
                  let dtStep: DatatableScenarioStep = new DatatableScenarioStep(
                    ""+item.step
                    , "&location"+item.location
                    , "&system"+item.system
                    , item.equipment
                    , item.point
                    , "&equipment"+item.value
                    , ""+item.delay
                    , item.status?STR_RUNNING:STR_STOP
                  );
                  this.rows_step.push(dtStep);
                });
              }
              else {
                console.log(func,'scenarioCard.scenarioSteps IS EMPTY');
              }
            }
          }
      });
    }
    this.rows_step = [...this.rows_step]
  }

  private newScenarioStep(): void {
    const func: string = "newScenarioStep";
    console.log(func);
    if ( this.selected_card.length > 0 ) {
      this.selected_card.forEach((item, index) => {
        console.log(func,'name',item.name,'index',index);
        for ( let i = 0 ; i < cards.length ; ++i ) {
          console.log(func,'cards[i].name',cards[i].name);
          if ( cards[i].name === item.name ) {
            console.log(func,'new','name',item.name,'index',index);
            let key = cards[i].steps.length;       
            cards[i].steps.push(new ScenarioStep(
              key
              , key
              , key
              , 'Breaker ('+key+')'
              , 'Point # '+key
              , key
              , key
              , INT_STOP)
            );
          }
        }
      });
    }
    else {
      console.log(func,'selected_card IS EMPTY');
    }
    this.reloadScenarioStep();
  }

  private getSelectedScenarioStep(): ScenarioStep {
    const func: string = "getSelectedScenarioStep";
    console.log(func);
    let step: ScenarioStep = null;
    let card: ScenarioCard = this.getSelectedScenarioCard();
    if ( null != card ) {
      for ( let x = 0 ; x < this.selected_step.length ; ++x ) {
        let dtStep: DatatableScenarioStep = this.selected_step[x];
        console.log(func,"datatableScenarioStep.step",dtStep.step,"x",x);
        for ( let y = 0 ; y < card.steps.length ; ++y ) {
          console.log(func,"datatableScenarioStep.step",dtStep.step,"y",y);
          if ( ""+card.steps[y].step === dtStep.step ) {
            console.log(func,"y",y);
            step = card.steps[y];
            break;
          }
        }
      }
    }
    else {
      console.log(func,'scenarioCard IS EMPTY');
    }
    return step;
  }

  private getSelectedScenarioCard(): ScenarioCard {
    const func: string = "getSelectedScenarioCard";
    console.log(func);
    let card: ScenarioCard = null;
    if ( this.selected_card.length > 0 ) {
      this.selected_card.forEach((item, index) => {
        console.log(func,'name',item.name,'index',index);
        for ( let i = 0 ; i < cards.length ; ++i ) {
          card = cards[i];
          break;
        }
      });
    }
    return card;
  }

  private deleteScenarioStep(): void {
    const func: string = "deleteScenarioStep";
    console.log(func);
    if ( this.selected_step.length > 0 ) {
      if ( this.selected_card.length > 0 ) {
        this.selected_card.forEach((item, index) => {
          console.log(func,'name',item.name,'index',index);
          for ( let i = 0 ; i < cards.length ; ++i ) {
            let card:ScenarioCard = cards[i];
            console.log(func,'card.name',card.name);
            if ( card.name === item.name ) {
              console.log(func,'delete','name',item.name,'index',index);
  
              for ( let x = 0 ; x < this.selected_step.length ; ++x ) {
                let dtStep: DatatableScenarioStep = this.selected_step[x];
                console.log(func,'delete','dtStep.step',dtStep.step,'x',x);
                for ( let y = 0 ; y < card.steps.length ; ++y ) {
                  let step: ScenarioStep = card.steps[y];
                  console.log(func,'delete','dtStep.step',dtStep.step,'index',index);
                  if ( ""+step.step === dtStep.step ) {
                    console.log(func,'delete','y',y);
                    card.steps.splice(y,1);
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

  private subscription: Subscription;
  private curExecCard: ScenarioCard;
  private executeCard(firstStep: boolean = false): void {
    const func: string = "executeCard";
    console.log(func);
    console.log(func,"firstStep[",firstStep,"]");

    if ( firstStep ) {

      if ( this.selected_card.length > 0 ) {
        this.selected_card.forEach((item, index) => {
          console.log(func,'name',item.name,'index',index);
          for ( let i = 0 ; i < cards.length ; ++i ) {
            let card:ScenarioCard = cards[i];
            console.log(func,'card.name',card.name);
            if ( card.name === item.name ) {
              console.log(func,'name',item.name,'index',index);
              this.curExecCard = card;
            }
          }
        });
      }

      // NOK
      // this.curExecCard = this.getSelectedScenarioCard();
    }

    if ( null != this.curExecCard ) {
      // Start time and set it to disable
      if ( firstStep ) {
        this.curExecCard.state = INT_RUNNING;
        this.curExecCard.step = 0;
      }
      console.log(func,"this.curExecCard.name[",this.curExecCard.name,"]");
      console.log(func,"this.curExecCard.state[",this.curExecCard.state,"]");
      console.log(func,"this.curExecCard.step[",this.curExecCard.step,"]");
      console.log(func,"this.curExecCard.steps.length[",this.curExecCard.steps.length,"]");
      console.log(func,"this.curExecCard.steps[",this.curExecCard.steps,"]");
      if ( this.curExecCard.step < this.curExecCard.steps.length ) {
        console.log(func,"start");
        this.curExecCard.steps[this.curExecCard.step].status = INT_RUNNING;

        console.log(func,"reloadScenarioStep");
        this.reloadScenarioStep();
        
        // Fire control
        //...
        
        // Asnyc Return
        let timeout = this.curExecCard.steps[this.curExecCard.step].delay;

        this.subscription = Observable.interval(1000*timeout).map((x) => {
          console.log(func,"map");

        }).subscribe((x) => {
          console.log(func,"subscribe");
          
          this.curExecCard.steps[this.curExecCard.step].status = INT_STOP;
          this.curExecCard.step++;

          console.log(func,"unsubscribe");
          this.subscription.unsubscribe();

          console.log(func,"reloadScenarioStep");
          this.reloadScenarioStep();
          
          console.log(func,"executeCard");
          this.executeCard();
        });
      }
      else {
        console.log(func,"step IS END");

        this.curExecCard.state = INT_STOP;
      }
    }
    else {
      console.log(func,"curExecCard IS NULL");
    }
  }

  private stopCard(): void {
    const func: string = "startCard";
    console.log(func);

    let card: ScenarioCard = this.getSelectedScenarioCard();
    if ( null != card ) {
      let steps: ScenarioStep [] = card.steps;
      for ( let y = 0 ; y < card.steps.length ; ++y ) {
        let step: ScenarioStep = card.steps[y];
        console.log(func,'y',y);
        step.status = INT_RUNNING;
      }
      this.reloadScenarioStep();
    }
    else {
      console.log(func,"card IS NULL");
    }
  }

  private startScenarioStep(): void {
    const func: string = "startScenarioStep";
    console.log(func);

    if ( this.selected_step.length > 0 ) {
      if ( this.selected_card.length > 0 ) {
        this.selected_card.forEach((item, index) => {
          console.log(func,'name',item.name,'index',index);
          for ( let i = 0 ; i < cards.length ; ++i ) {
            let card:ScenarioCard = cards[i];
            console.log(func,'card.name',card.name);
            if ( card.name === item.name ) {
              console.log(func,'name',item.name,'index',index);
  
              for ( let x = 0 ; x < this.selected_step.length ; ++x ) {
                let dtStep: DatatableScenarioStep = this.selected_step[x];
                console.log(func,'dtStep.step',dtStep.step,'x',x);
                for ( let y = 0 ; y < card.steps.length ; ++y ) {
                  let step: ScenarioStep = card.steps[y];
                  console.log(func,'dtStep.step',dtStep.step,'index',index);
                  if ( ""+step.step === dtStep.step ) {
                    console.log(func,'y',y);
                    step.status = INT_RUNNING;
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
    if ( null != cards ) {
      if ( this.selected_step.length > 0 ) {
        if ( this.selected_card.length > 0 ) {
          this.selected_card.forEach((item, index) => {
            console.log(func,'name',item.name,'index',index);
            for ( let i = 0 ; i < cards.length ; ++i ) {
              let card:ScenarioCard = cards[i];
              console.log(func,'card.name',card.name);
              if ( card.name === item.name ) {
                console.log(func,'delete','name',item.name,'index',index);
  
                for ( let x = 0 ; x < this.selected_step.length ; ++x ) {
                  let dtStep: DatatableScenarioStep = this.selected_step[x];
                  console.log(func,'delete','dtStep.step',dtStep.step,'x',x);
                  for ( let y = 0 ; y < card.steps.length ; ++y ) {
                    let step: ScenarioStep = card.steps[y];
                    console.log(func,'delete','dtStep.step',dtStep.step,'index',index);
                    if ( ""+step.step === dtStep.step ) {
                      console.log(func,'delete','y',y);
                      step.status = INT_STOP;
                    }
                  }
                }
              }
            }
          });
        }
      }
    }
    else {
      console.log(func,"cards IS EMPTY");
    }
    this.reloadScenarioStep();
  }

  private reloadScenarioCard(): void {
    const func: string = "reloadScenarioCard";
    console.log(func);
    this.rows_card = [];
    if ( null != cards ) {
      cards.forEach((item, index) => {
        let name = item.name;
        let state = item.state ? STR_RUNNING : STR_STOP;
        console.log(func,"index["+index+"] name["+name+"] state["+state+"]");
        this.rows_card.push(new DatatableScenarioCard(name, state));
      })
      this.rows_card = [...this.rows_card]
    }
    else {
      console.log(func,"cards IS EMPTY");
    }
  }

  private newScenario() {
    const func: string = "newScenario";
    console.log(func);
    let name: string = 'Scenario # '+cards.length;
    cards.push(new ScenarioCard(name, INT_STOP));
    this.reloadScenarioCard();
  }

  private deleteScenario() {
    const func: string = "deleteScenario";
    console.log(func);
    if ( this.selected_card.length > 0 ) {
      this.selected_card.forEach((item, index) => {
        console.log(func,'name',item.name,'index',index);
        for ( let i = 0 ; i < cards.length ; ++i ) {
          console.log(func,'cards[i].name',cards[i].name);
          if ( cards[i].name === item.name ) {
            console.log(func,'remove','name',item.name,'index',index);
            cards.splice(i,1);
          }
        }
      });
    }
    else {
      console.log(func,'selected_card IS EMPTY');
    }
    this.reloadScenarioCard();
  }

  private startScenario(): void {
    const func: string = "startScenario";
    console.log(func);
    if ( this.selected_card.length > 0 ) {
      this.selected_card.forEach((item, index) => {
        console.log(func,'name',item.name,'index',index);
        for ( let i = 0 ; i < cards.length ; ++i ) {
          console.log(func,'cards[i].name',cards[i].name);
          if ( cards[i].name === item.name ) {
            console.log(func,'start','name',item.name,'index',index);
            cards[i].state=INT_RUNNING;
          }
        }
      });
    }
    else {
      console.log(func,'selected_card IS EMPTY');
    }
    this.reloadScenarioCard();
  }

  private stopScenario(): void {
    const func: string = "stopScenario";
    console.log(func);
    if ( this.selected_card.length > 0 ) {
      this.selected_card.forEach((item, index) => {
        console.log(func,'name',item.name,'index',index);
        for ( let i = 0 ; i < cards.length ; ++i ) {
          console.log(func,'cards[i].name',cards[i].name);
          if ( cards[i].name === item.name ) {
            console.log(func,'end','name',item.name,'index',index);
            cards[i].state=INT_STOP;
          }
        }
      });
    }
    else {
      console.log(func,'selected_card IS EMPTY');
    }
    this.reloadScenarioCard();
  }

  public saveScenario(): void {
    const func: string = "saveScenario";
    console.log(func);
    let url: string = strUrlfsstorage+'/'+strUrlUpstreamFile;
    let filepath: string = STR_TMS_FILENAME_JSON;
    let strcards: string = JSON.stringify(cards);
    this.postData(url,filepath,strcards);
  }

  public reloadScenario(): void {
    const func: string = "reloadScenario";
    console.log(func);

    // Reset the scenario cards
    cards = [];

    let url = strUrlfsstorage+'/'+strUrlDownstramFile
    +"?"+STR_FILEPATH+"="+STR_TMS_FILENAME_JSON
    +"&"+STR_METHOD+"="+STR_STREAM;

    // Handle the data recerived
    {
      this.httpClient.get(
        url
      )
        .subscribe(
          res => {
            console.log(res);

            console.log(func, "cards[",cards,"]");
            
            cards = JSON.parse(res[STR_DATA]);
            
            console.log(func, "cards[",cards,"]");
            
            this.reloadScenarioCard();
            this.reloadScenarioStep();
  
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
  }

  private getData(url: string): string {
    const func: string="getData";
    console.log(func);
    this.httpClient.get(
        url
      )
        .subscribe(
          res => {
            console.log(res);
            return res;
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
    return null;
  }

  private downloadData(): void {
    const func: string="downloadData";
    console.log(func);
    let url = strUrlfsstorage+'/'+strUrlDownstramFile
    +"?"+STR_FILEPATH+"="+STR_TMS_FILENAME
    +"&"+STR_METHOD+"="+STR_DOWNLOAD;
    let res = this.getData(url);
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

  private postData(url: string, filePath: string, filedata: string) {
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

  private postFile(url: string, filePath: string, filedata: File) {
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
    const func: string="onRowActivate";
    console.log(func,'name',name,'event',event);
  }
  private onRowSelect(name: string, event: Event) {
    const func: string="onRowSelect";
    console.log(func,'name',name,'event',event,);
    if ( name === STR_NAME_CARD ) {
      this.reloadScenarioStep();
    }
  }

  // Button Handler
  private btnClicked(btnlabel: string, event?: Event) {
    const func: string="btnClicked";
    console.log(func,'btnlabel[',btnlabel,']');
    
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
      this.executeCard(true);
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
      this.saveScenario();
    }
    else if ( btnlabel === 'reloadscenario' ) {
      this.reloadScenario();
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