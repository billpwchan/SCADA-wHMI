import { Component, OnInit, ViewChild, OnDestroy, Inject, Directive, ElementRef } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router'
import { DOCUMENT } from '@angular/platform-browser';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';

import { ConfigService } from '../service/config.service';
import { ScsOlsService } from '../service/scs-ols.service';
import { OlsdataService } from '../service/olsdata.service';
import { RttTrendService } from '../service/rtt-trend.service';
import { ColorPickerService } from 'angular4-color-picker';

import { Olsdata } from '../rtt/olsdata';

import { RttTrendDef } from '../service/rtt-trend-def';

@Component({
    selector: 'app-rtt',
    templateUrl: './rtt.component.html',
    styleUrls: ['./rtt.component.css']
})
export class RttComponent implements OnInit, OnDestroy {

    // subscriptions
    private subRoute: any;
    private subOlslist: any;

    // stores data list returned from ScsOlslist
    public dataList: string[] = [];
    public olsList: Olsdata[] = [];

    // selected data
    public data: any;
    public ols: any;

    // public listServer = 'LstServer';
    // public listName = 'ptwdciset';
    public listServer = 'HisServer';
    public listName = 'TECSOperating';
    public archiveNameList: String[] = [];

    public selectedOlsdatas: Olsdata[] = [];

    public filter: Olsdata = new Olsdata();

    public yaxislabel1: string;
    public yaxislabel2: string;

    constructor(
        private configService: ConfigService,
        private route: ActivatedRoute,
        private scsOlsService: ScsOlsService,
        private olsdataService: OlsdataService,
        private translate: TranslateService,
        private rttTrendService: RttTrendService,
        private colorPickerService: ColorPickerService,
        @Inject(DOCUMENT) private document: any) {
            translate.onLangChange.subscribe((event: LangChangeEvent) => {
                this.loadTranslations();
            })
    }
    ngOnInit() {
        this.loadTranslations();
        this.loadConfig();
        this.loadData();
    }
    loadTranslations() {
        // this.messages['emptyMessage'] = this.translate.instant('No item to display');
        // this.messages['selectedMessage'] = this.translate.instant('selected');
        // this.messages['totalMessage'] = this.translate.instant('total');
    }
    loadConfig() {
        console.log('{schedule-table}', '[loadConfig]', 'translate current lang=', this.translate.currentLang);
        // this.cutoffTime = this.configService.config.getIn(['schedule_table', 'cutoff_time']);
        // console.log('{schedule-table}', '[loadConfig]', 'cutoff_time=', this.cutoffTime);
        this.archiveNameList = this.configService.config.getIn(['archive_names']);
    }
    loadData() {
        this.subRoute = this.route.queryParams.subscribe(params => {
        })
    }
    ngOnDestroy() {
        // this.cleanupSubscriptions()
    }

    public getOlsDataArr(): Promise<Olsdata[]> {
        return this.scsOlsService.readOlslist(this.listServer, this.listName)
        .then(dataList => this.olsList = dataList as Olsdata[])
    }

    public prepareOlsdataObj(): void {
        this.getOlsDataArr()
          .then(myOlsdata =>
            this.olsdataService.populateComplexPointName(myOlsdata));
    }

    public isSelectedOls(name: string): boolean {
      // console.log('selected ols name', name);
      const foundOls = this.selectedOlsdatas.filter(ols => ols.ID === name);
      return foundOls.length > 0;
    }

    public selectOls(ols: Olsdata): void {
      console.log('clicked ols: ', ols.ID);
      const index: number = this.selectedOlsdatas.lastIndexOf(ols);
      if (index === -1 && this.selectedOlsdatas.length < 8) {
        this.selectedOlsdatas.push(ols);
      }
    }

    public deselectOls(ols: Olsdata): void {
      console.log('dblclicked ols: ', ols.ID);
      const index: number = this.selectedOlsdatas.lastIndexOf(ols);
      console.log('index: ' + index);
      if (index !== -1) {
        this.selectedOlsdatas.splice(index, 1);
      }
    }

    public isEmptyList(someList: any[]): boolean {
      return someList.length === 0;
    }

    public isEmptyStr(str: String): boolean {
      return (!str || 0 === str.length);
    }


    // public isEmptyRequiredColumns(ols: Olsdata): boolean {
    //   return this.isEmptyStr(ols.Station) &&
    //           this.isEmptyStr(ols.System) &&
    //           this.isEmptyStr(ols.EquipmentLabel) &&
    //           this.isEmptyStr(ols.PointLabel);
    // }

    public displayTrend(): void {
      this.goToUrl();
    }

    public onReset(): void {
      this.olsList = [];
      this.selectedOlsdatas = [];
    }

    public getRandomNum(): number {
      return Math.floor(Math.random() * 1000);
    }

    public goToUrl(): void {
      // loop through the olsdata[]
      // extract the needed arguments from the olsdata[] lists:
      // - subscriptionInfo1, xaxisLabel, yaxisLabel, callerId
      // -- subscriptionInfo: env_name+eqpclass+line+"equipment"+java_class+pointname("trackno")
      const env = 'OCC';
      const archiveName = this.listName;
      const yaxisLabel1 = this.yaxislabel1;
      const yaxisLabel2 = this.yaxislabel2;
      const xaxisLabel = 'Time';
      let rttparams = '';
      let amper = '';
      this.selectedOlsdatas.forEach((element, index) => {
        const hvid = element.DB_point_Alias;
        const color = element.color.substring(1);
        const subinfoparam = env + '+' + hvid + '+' + archiveName + '+' + color;
        console.log('index:' + index);
        if (index === 0) {
          rttparams += RttTrendDef.HV_SUBSCRIBE_Q;
          rttparams += amper + RttTrendDef.YAXIS + (index + 1) + '=' + this.yaxislabel1;
        }
        if (index === 1 && this.yaxislabel2) {
          rttparams += amper + RttTrendDef.YAXIS + (index + 1) + '=' + this.yaxislabel2;
        }
        amper = '&';
        rttparams += amper + RttTrendDef.SUBINFO + (index + 1) + '=' + subinfoparam;
      });

      rttparams += amper + RttTrendDef.XAXIS_E + xaxisLabel + amper + RttTrendDef.CALLERID_E + (Math.floor(Math.random() * 1000));
      console.log('Rtt Params: ' + rttparams);

      this.rttTrendService.readTrendUrl(rttparams)
        .then(charturl => this.document.location.href = charturl);

    }
}
