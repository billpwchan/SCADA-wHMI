import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatSnackBar } from '@angular/material';
import { Router } from '@angular/router';
import { LangChangeEvent, TranslateService } from '@ngx-translate/core';
import { Frame } from '@stomp/stompjs';
import { GridOptions, RowNode } from 'ag-grid';
// import * as Stomp from '@stomp/stompjs';
import * as c3 from 'c3';
import * as Highstocks from 'highcharts/highstock';
import { chart } from 'highcharts/highstock';
import * as HichartsExporting from 'highcharts/modules/exporting';
import * as moment from 'moment';
import 'rxjs/add/operator/map';
import * as _ from 'underscore';
import { RttAppDialogComponent } from '../rtt-app-dialog/rtt-app-dialog.component';
import { ConfigService } from '../service/config.service';
import { HighstockDataService } from '../service/highstock-data.service';
import { Olsdata } from '../service/olsdata';
import { ScsResponse, ScsRESTRequest } from '../service/scsrequest';
import { SelectedOlsdatasService } from '../service/selected-olsdatas.service';
import { StompSubscriptionService } from '../service/stomp-subscription.service';
import { MatSliderModule } from '@angular/material/slider';


HichartsExporting(Highstocks);

@Component({
  selector: 'app-rtt-app',
  templateUrl: './rtt-app.component.html',
  styleUrls: ['./rtt-app.component.css']
})
export class RttAppComponent implements AfterViewInit, OnInit, OnDestroy {

  @ViewChild('chartTarget') chartTarget: ElementRef;

  [key: string]: any;

  _gridOptions: GridOptions;
  rowData: any;
  columnDefs: any;
  entries = 0;
  message: string;
  serverName: string;
  listName: string;
  isFrozen = false;
  getRowNodeId;
  pendingUpdate: any[];
  selectedNodeID: string;
  // private stompClient;

  selectedOlsdatas: Olsdata[];
  axisOptions = {};
  colorOptions = {};
  tickInterval = {
    hours: 0,
    minutes: 0,
    seconds: 0
  };
  tickIntervalTime: number;
  tickIntervalTimeHis: number;
  allowShowYAxis2: boolean;

  defaultInterval;
  defaultStockInterval;
  devMode: boolean;
  chType: string;
  chartLib: number;
  backgroundColor;
  timeFormat;
  legendSeparator;
  highstockRefreshRate: number;
  c3RefreshRate: number;
  c3ReloadFlag: boolean;
  chartSize = 44;

  chart: c3.ChartAPI;
  chartObj = {
    'chart': this.chart,
    'redrawArgs': {
      length: null,
      columns: null,
      duration: null,
      to: null
    },
    'truncThreshold': undefined
  };
  oriRedrawArgsLength = {};


  highchart: Highstocks.ChartObject;
  myTooltip: Highstocks.TooltipOptions;

  // dev_mode Ag-Grid Row Style only
  private static getRowStyleScheduled(params: any) {
    let fstyle = 'normal';
    let fweight = 'normal';
    let textcolor = '#111111';
    const opacity = '1';
    let bgcolor = '#43a047'; // green 600

    if (params.data.AcknowledgeRequired === 1) {
      fweight = 'bold';
    }

    if (params.data.Severity === 2) {
      bgcolor = '#fdd835'; // yellow 600
    } else if (params.data.Severity === 3) {
      bgcolor = '#fb8c00'; // orange 600
    } else if (params.data.Severity === 4) {
      bgcolor = '#e53935'; // red 600
    }

    if (params.node.selected) {
      fstyle = 'italic';
      textcolor = 'blue';
      bgcolor = '#00701a'; // green dark
      if (params.data.Severity === 2) {
        bgcolor = '#c6a700'; // yellow dark
      } else if (params.data.Severity === 3) {
        bgcolor = '#c25e00'; // orange dark
      } else if (params.data.Severity === 4) {
        bgcolor = '#ab000d'; // red dark
      }
    }

    return {
      'background-color': bgcolor,
      'color': textcolor,
      'opacity': opacity,
      'font-weight': fweight,
      'font-style': fstyle
    };
  }

  constructor(
    private configService: ConfigService,
    private selectedOlsdatasService: SelectedOlsdatasService,
    private stompSubscriptionService: StompSubscriptionService,
    private highstockDataService: HighstockDataService,
    private translateService: TranslateService,
    public dialog: MatDialog,
    public snackBar: MatSnackBar,
    private router: Router,
  ) {
    translateService.onLangChange.subscribe((event: LangChangeEvent) => { });
    this.columnDefs = [
      {
        headerName: 'Time',
        field: 'SCSTime',
      },
      { headerName: 'ID', field: 'ID', maxWidth: 100 },
      { headerName: 'Value', field: 'Value', maxWidth: 100 },
      { headerName: 'Name', field: 'Name' }
    ];

    this.listName = this.configService.config.getIn(['serverName']);
    this.serverName = this.selectedOlsdatasService.getarchiveName();
    this.isFrozen = false;

    this.getRowNodeId = function (data) {
      return data.ID;
    };

    this._gridOptions = <GridOptions>{
      onGridSizeChanged: () => this._gridOptions.api.sizeColumnsToFit(),
      getRowStyle: RttAppComponent.getRowStyleScheduled
    };

    this.stompSubscriptionService.setCurrentSequence(-2);
    this.rowData = [];
    this.stompSubscriptionService.sub = null;
    this.pendingUpdate = [];
    this.initVariables();
  }

  initVariables() {
    this.devMode = this.configService.config.getIn(['dev_mode']);
    this.backgroundColor = this.configService.config.getIn(['background_color']);
    this.selectedOlsdatas = this.selectedOlsdatasService.getSelectedOlsdatas() ? this.selectedOlsdatasService.getSelectedOlsdatas() : [];
    this.tickInterval = this.selectedOlsdatasService.getTickInterval();
    this.tickIntervalTime = (this.tickInterval.hours * 3600 + this.tickInterval.minutes * 60 + this.tickInterval.seconds) * 1000;
    this.tickIntervalTimeHis = this.tickIntervalTime + this.configService.config.getIn(['his_time']) * 60 * 1000;
    this.allowShowYAxis2 = this.selectedOlsdatasService.getAllowShowYAxis2();
    this.timeFormat = this.configService.config.getIn(['time_format']);
    this.chartLib = this.configService.config.getIn(['chart_lib']);
    switch (this.selectedOlsdatasService.getselectedChart()) {
      case 'Line':
        this.chType = 'line';
        break;
      case 'Bar':
        this.chType = 'column';
        break;
      case 'Step':
        this.chType = 'line';
        break;
      default:
        break;
    }
  }

  ngOnInit() {
    this.serverName = this.configService.config.getIn(['serverName']);
    this.listName = this.selectedOlsdatasService.getarchiveName();
    this.stompSubscriptionService.initStomp().then(data => {
      if (data) {
        this.onSubscribeTopic();
      } else {
        // Hae to unsubscribeTopic then Subscribe Topic again.
        // Stomp subscription will usually not working first time cconnecting to a server
        this.onUnSubscribeTopic();
        this.onSubscribeTopic();
      }
    });
    this.message = '';
  }

  ngAfterViewInit() {
    // Retrieve general chart configuarations from config
    this.tickIntervalTime = (this.tickInterval.hours * 3600 + this.tickInterval.minutes * 60 + this.tickInterval.seconds) * 1000;
    this.tickIntervalTimeHis = this.tickIntervalTime + this.configService.config.getIn(['his_time']) * 60 * 1000;
    this.legendSeparator = this.configService.config.getIn(['legend_separator']);
    this.highstockRefreshRate = this.configService.config.getIn(['highstock_refreshRate']);
    this.c3RefreshRate = this.configService.config.getIn(['c3_refreshRate']);
    this.configService.config.getIn(['chart_lib']) === 1 ? this.initStocks() : this.initChart();
  }

  // c3 Only.
  private initChart() {
    this.chart = c3.generate({
      bindto: '#chart',
      transition: {
        duration: 700
      },
      interaction: {
        enabled: true
      },
      size: {
        height: 700,
        width: 1700
      },
      grid: {
        x: {
          show: true
        },
        y: {
          show: true
        }
      },
      data: {
        x: 'x',
        columns: [],
        labels: false,
        axes: {
          'Data Point 1': 'y',
        },
        type: 'line'
      },
      zoom: {
        enabled: this.isFrozen,
        rescale: true
      },
      subchart: {
        show: this.isFrozen
      },
      axis: {
        x: {
          type: 'timeseries',
          tick: {
            format: this.timeFormat,
            rotate: 80,
            culling: {
              max: 20
            },
            fit: false
          },
        },
        y: {
          label: {
            text: 'Y Axis 1',
            position: 'outer-middle'
          }
        },
        y2: {
          show: this.allowShowYAxis2,
          label: {
            text: 'Y Axis 2',
            position: 'outer-middle'
          }
        }
      },
      legend: {
        position: 'bottom'
      },
      line: {
        connectNull: true
      }
    });
    this.chartObj.chart = this.chart;
    this.chartObj.chart.axis.labels({
      x: this.configService.config.getIn(['xaxisLabel']),
      y: this.selectedOlsdatasService.getyaxis1().unit,
      y2: this.selectedOlsdatasService.getyaxis2() ? this.selectedOlsdatasService.getyaxis2().unit : ' '
    });
    if (!this.selectedOlsdatasService.getAllowAutoMinMax()) {
      // Manually assign min/max value if enableAutoMinMax options is not selected
      this.chartObj.chart.axis.min({
        y: Number(this.selectedOlsdatasService.getyaxis1().min),
        y2: this.selectedOlsdatasService.getyaxis2() ? Number(this.selectedOlsdatasService.getyaxis2().min) : this.chartObj.chart.axis.min()['y2']
      });
      this.chartObj.chart.axis.max({
        y: Number(this.selectedOlsdatasService.getyaxis1().max),
        y2: this.selectedOlsdatasService.getyaxis2() ? Number(this.selectedOlsdatasService.getyaxis2().max) : this.chartObj.chart.axis.max()['y2']
      });
    } else {
      // In some cases, auto min max is not feasible.
    }
    // Change chart type on run-time
    this.chartObj.chart.transform(this.selectedOlsdatasService.getselectedChart().toLocaleLowerCase());
    // To let the xAxis move by its own (Via using transparent time series xAxis[0])
    this.initDefaultInterval();
  }

  // For generate c3JS chart only
  private initStocks() {
    // Highstock Global Options
    Highstocks.setOptions({
      lang: {
        rangeSelectorZoom: this.translateService.instant('CHART.ZOOM'),
        noData: this.translateService.instant('CHART.NODATA')
      }
    });
    // Highstock Chart Options
    const options: Highstocks.Options = {
      time: {
        timezoneOffset: Number(this.configService.config.getIn(['timezone_offset'])) * 60,
      },
      title: {
        text: this.translateService.instant('CHART.TITLE')
      },
      chart: {
        type: this.chType,
        backgroundColor: this.backgroundColor,
        animation: {
          duration: 500,
        },
        zoomType: 'xy',
        alignTicks: false,
        height: (9 / 16 * 70) + '%',
        panning: true,
        panKey: 'shift'
      },
      credits: {
        enabled: true,
        text: 'Thales',
        href: 'https://www.thales.com/'
      },
      noData: {
        style: {
          fontWeight: 'bold',
          fontSize: '15px',
          color: '#303030'
        }
      },
      navigator: {
        enabled: true,
        adaptToUpdatedData: true,
        handles: {
          enabled: this.isFrozen
        },
        series: {
          type: this.chType,
          fillOpacity: 0.05,
          lineWidth: 1,
          marker: {
            enabled: false
          }
        }
      },
      scrollbar: {
        enabled: this.isFrozen,
        barBackgroundColor: 'gray',
        barBorderRadius: 7,
        barBorderWidth: 0,
        buttonBackgroundColor: 'gray',
        buttonBorderWidth: 0,
        buttonArrowColor: 'orange',
        buttonBorderRadius: 7,
        rifleColor: 'orange',
        trackBackgroundColor: 'white',
        trackBorderWidth: 1,
        trackBorderColor: 'silver',
        trackBorderRadius: 7
      },
      subtitle: {
        // tslint:disable-next-line:max-line-length
        text: this.translateService.instant('CHART.SUBTITLE_1') + this.serverName + ' ' + this.translateService.instant('CHART.SUBTITLE_2') + this.listName
      },
      loading: {
        hideDuration: 1000,
        showDuration: 1000,
      },
      rangeSelector: {
        allButtonsEnabled: false,
        buttons: [{
          type: 'all',
          text: 'All'
        }],
        enabled: false,
        selected: 0,
        inputEnabled: false
      },
      xAxis: {
        type: 'datetime',
        range: this.tickIntervalTime,
        tickPixelInterval: 100,
        crosshair: true,
        ordinal: false,
        showEmpty: true,
        title: {
          text: this.configService.config.getIn(['xaxisLabel'])
        },
        dateTimeLabelFormats: {
          millisecond: this.timeFormat,
          second: this.timeFormat,
          minute: this.timeFormat,
          hour: this.timeFormat,
          day: this.timeFormat,
          week: this.timeFormat,
          month: this.timeFormat,
          year: this.timeFormat
        },
        plotLines: [{ // mark the weekend
          color: '#b27cef',
          width: 0,
          value: Date.now(),
          dashStyle: 'longdashdot'
        }]
      },
      yAxis: [{
        opposite: false,
        showEmpty: true,
        crosshair: true,
        title: {
          text: this.selectedOlsdatasService.getyaxis1().unit,
          style: {
            color: Highstocks.getOptions().colors[1]
          }
        },
        labels: {
          format: '{value}',
          style: {
            color: Highstocks.getOptions().colors[1]
          }
        }
      },
      {
        visible: this.allowShowYAxis2,
        opposite: true,
        showEmpty: true,
        crosshair: true,
        title: {
          text: this.allowShowYAxis2 ? this.selectedOlsdatasService.getyaxis2().unit : ' ',
          style: {
            color: Highstocks.getOptions().colors[0]
          }
        },
        labels: {
          format: '{value}',
          style: {
            color: Highstocks.getOptions().colors[0]
          }
        }
      }],
      legend: {
        layout: 'vertical',
        backgroundColor: this.backgroundColor,
        align: 'center',
        borderColor: 'black',
        borderWidth: 2,
        verticalAlign: 'bottom',
        floating: false,
        maxheight: 400,
        navigation: {
          activeColor: '#3E576F',
          animation: true,
          arrowSize: 12,
          inactiveColor: '#CCC',
          style: {
            fontWeight: 'bold',
            color: '#333',
            fontSize: '12px'
          }
        }
      },
      exporting: {
        enabled: false,
        scale: 2,
        sourceWidth: 1920,
        sourceHeight: 1080,
        buttons: {
          contextButton: {
            menuItems: ['printChart', 'separator', 'downloadJPEG', 'downloadSVG']
          }
        }
      },
      boost: {
        enabled: true,
      },
      plotOptions: {
        series: {
          events: {
            legendItemClick: function () { return false; }
          }
        }
      },
      tooltip: {
        animation: true,
        crosshairs: true,
        enabled: true,
        shared: true,
        dateTimeLabelFormats: {
          millisecond: '%Y %A, %b %e, %H:%M:%S.%L',
          second: '%Y %A, %b %e, %H:%M:%S',
          minute: '%Y %A, %b %e, %H:%M',
          hour: '%Y %A, %b %e, %H:%M',
          day: '%Y %A, %b %e, %Y',
          week: 'Week from %A, %b %e, %Y',
          month: '%B %Y',
          year: '%Y'
        },
        formatter: function () {
          let pointFormat = ''
          let lastFlag = false;
          this.points.forEach(point => {
            if (point.key === 'last') {
              lastFlag = true;
              pointFormat += '<span style="color:' + point.series.color + '">\u25CF</span> ' + point.series.name + ': <b>' + point.y + '</b><br/>';
            } else {
              pointFormat += '<span style="color:' + point.series.color + '">\u25CF</span> ' + point.series.name + ': <b>' + point.y + '</b><br/>';
            }
          });
          if (!lastFlag) {
            pointFormat =
              '<span style="font-size: 10px"><b>' + Highstocks.dateFormat('%Y %A, %b %e, %H:%M:%S.%L', this.points[0].x) + '</b></span><br/>' + pointFormat;
          }
          return pointFormat;
        }
      }
    };
    this.highchart = chart(this.chartTarget.nativeElement, options);
    this.highchart.showLoading();
    // Transparent xAxis timeseries.
    this.highchart.addSeries({
      allowPointSelect: false,
      enableMouseTracking: false,
      showInNavigator: true,
      name: ' ',
      yAxis: 0,
      color: '#ffffff',
      marker: {
        enabled: false
      }
    });
    this.highchart.redraw();
    // Add series for each selectedOls
    this.selectedOlsdatas.forEach((data, index) => {
      let labelName = '';
      const nameArr = data.Name.split('|');
      this.configService.config.getIn(['configurable_Label']).forEach(label => {
        labelName += nameArr[label] + this.legendSeparator;
      });
      labelName = labelName.slice(0, -1) + ' (Y' + data.attachedAxis + ')';

      this.highchart.addSeries({
        name: labelName,
        yAxis: data.attachedAxis === '1' ? 0 : 1,
        color: data.color,
        showInNavigator: true,
        marker: {
          enabled: true,
          radius: 3
        },
        step: this.chType === 'line' && this.selectedOlsdatasService.getselectedChart() === 'Step',
        shadow: true,
        tooltip: {
          valueDecimals: 2
        }
      });
    });

    if (!this.selectedOlsdatasService.getAllowAutoMinMax()) {
      this.highchart.yAxis[0].setExtremes(Number(this.selectedOlsdatasService.getyaxis1().min),
        Number(this.selectedOlsdatasService.getyaxis1().max), false);
      if (this.allowShowYAxis2) {
        this.highchart.yAxis[1].setExtremes(Number(this.selectedOlsdatasService.getyaxis2().min),
          Number(this.selectedOlsdatasService.getyaxis2().max), false);
      }
    }
    setTimeout(() => this.highchart.hideLoading(), 1000);
    this.highstockDataService.tickInterval = this.isFrozen ?
      this.tickIntervalTime + this.configService.config.getIn(['his_time']) * 60 * 1000 : this.tickIntervalTime;
    this.initDefaultInterval();
  }

  private initDefaultInterval() {
    if (this.chartLib === 1) {
      // Add previous missing points (i.e 5 mins interval before moment.now())
      for (let i = 0; i < this.tickIntervalTime / this.highstockRefreshRate; i++) {
        this.highchart.series[0].addPoint([
          moment.now() - this.tickIntervalTime + i * this.highstockRefreshRate, 0
        ], false, this.highchart.series[0].data.length !== 0 ?
            moment(this.highchart.series[0].data[0].x).isBefore(moment(new Date()).subtract(this.tickIntervalTime / 1000, 'seconds')) : false);
      }
      this.defaultStockInterval = setInterval(() => {
        this.tickIntervalTimeHis = this.isFrozen ? this.tickIntervalTime + this.configService.config.getIn(['his_time']) * 60 * 1000 : this.tickIntervalTime;
        for (let i = 0; i < this.highchart.series.length; i++) {
          if (i !== 0) {
            if (!this.isFrozen && this.highstockDataService.dataList[i - 1] &&
              this.highstockDataService.dataList[i - 1].length !== 0 &&
              this.highstockDataService.dataList[i - 1][this.highstockDataService.dataList[i - 1].length - 1]) {  // Update last point.
              this.highstockDataService.dataList[i - 1][this.highstockDataService.dataList[i - 1].length - 1].x = moment.now();
            }
            if (this.highstockDataService.dataList[i - 1]) {
              this.highchart.series[i].setData(this.highstockDataService.dataList[i - 1], false, true, true); // Pass by reference. (Last option has to be true)
            }
          }
          while (this.highchart.series[i].data.length !== 0 &&
            (!this.highchart.series[i].data[0] ||
              moment(this.highchart.series[i].data[0].x).isBefore(moment(new Date()).subtract(this.tickIntervalTimeHis / 1000, 'seconds')))) {
            this.highchart.series[i].removePoint(0, false);
          }
        }
        if (!this.isFrozen) { this.highstockDataService.cleanData(); }

        if (!this.isFrozen) {   // Update xAxis range & update transparent timeseries data
          this.highchart.series[0].addPoint([moment.now(), 0], false, this.highchart.series[0].data.length !== 0 ?
            moment(this.highchart.series[0].data[0].x).isBefore(moment(new Date()).subtract(this.tickIntervalTime / 1000, 'seconds')) : false);
          this.highchart.xAxis[0].setExtremes(
            Number(moment(new Date()).subtract(this.tickIntervalTime / 1000, 'seconds').format('x')),
            Number(moment(new Date()).format('x')), false, false
          );
          this.hideNavigator();   // Use CSS rule to hide the below subchart
        }
        this.highchart.redraw();
        if (this.isFrozen) { clearInterval(this.defaultStockInterval); }
      }, this.highstockRefreshRate)
    } else {
      // This function is used for moving x-axis horizontally. c3JS only
      this.defaultInterval = setInterval(() => {
        if (!this.isFrozen) {
          this.tickIntervalTimeHis = this.isFrozen ? this.tickIntervalTime + this.configService.config.getIn(['his_time']) * 60 : this.tickIntervalTime;
          const minX = moment.now() - this.tickIntervalTimeHis; // Additional 30 mins historical data
          const maxX = moment.now();
          this.chartObj.chart.axis.range({ max: { x: maxX }, min: { x: minX } });
          this.chartObj.chart.zoom([moment.now() - this.tickIntervalTimeHis, moment.now()]);
        }
      }, this.c3RefreshRate);
    }
  }

  private hideNavigator() {
    const selectedElement = document.getElementsByClassName('highcharts-navigator')[0];
    document.body.appendChild(selectedElement);
    selectedElement.setAttribute('Visibility', 'hidden');
    let elements = document.getElementsByClassName('highcharts-navigator-series');
    for (let i = 0; i < elements.length; i++) {
      const ele = elements[i];
      document.body.appendChild(ele);
      ele.setAttribute('Visibility', 'hidden');
    }
    elements = document.getElementsByClassName('highcharts-navigator-xaxis');
    for (let i = 0; i < elements.length; i++) {
      const ele = elements[i];
      document.body.appendChild(ele);
      ele.setAttribute('Visibility', 'hidden');
    }
  }

  private redrawGraph(subscribedList: any[]) {
    if (subscribedList.length === 0) { return; }
    if (this.chartLib === 1) {
      // Following for HighChart  only.
      this.tickIntervalTimeHis = this.isFrozen ? this.tickIntervalTime + this.configService.config.getIn(['his_time']) * 60 * 1000 : this.tickIntervalTime;
      subscribedList.forEach(item => {
        let seriesIndex = -1;
        this.selectedOlsdatas.forEach((data, index) => {
          if (data.Name === item.Name) {
            seriesIndex = index;
          }
        });
        // To add point only if it is within 30 mins range. SCSTime is in format (w/ millisecond)
        if (moment(Number(item.SCSTime)).isAfter(moment(new Date()).subtract(this.tickIntervalTimeHis / 1000, 'seconds'))) {
          if (this.highstockDataService.dataList[seriesIndex].length === 0) { // Empty List. Adding first point ot this series.
            this.highstockDataService.dataList[seriesIndex].push({ x: Number(item.SCSTime), y: Number(item.Value) });
            this.highstockDataService.dataList[seriesIndex].push({ x: moment.now(), y: Number(item.Value), name: 'last' });
          } else {
            this.highstockDataService.dataList[seriesIndex].pop();
            this.highstockDataService.dataList[seriesIndex].push({ x: Number(item.SCSTime), y: Number(item.Value) });
            this.highstockDataService.dataList[seriesIndex].push({ x: moment.now(), y: Number(item.Value), name: 'last' });
            this.highstockDataService.dataList[seriesIndex].sort((a, b) => a.x - b.x);
          }
        }
      });
    } else {
      this.tickIntervalTimeHis = this.isFrozen ? this.tickIntervalTime + this.configService.config.getIn(['his_time']) * 60 * 1000 : this.tickIntervalTime;
      const date = moment.now();
      const dataCols: any[] = [['x']];
      const minX = date - this.tickIntervalTime;
      const maxX = date;
      const redrawArgs = this.chartObj.redrawArgs;
      // Remove out-dated points
      const xValues = this.chartObj.chart.x();
      console.log(xValues);
      Object.keys(xValues).forEach((key) => {
        if (xValues[key].length >= this.configService.config.getIn(['c3_maxPoint_unload'])) {
          this.c3ReloadFlag = true;
          this.onModal();
          setTimeout(() => this.onModal(), this.configService.config.getIn(['c3_modal_timeout']) * 1000);
          // initChart has to be the first, or the subscription will not work correctly.
          this.onReset();
          this.chartObj.chart.unload();
          if (this.chart !== null) {
            this.chart.destroy();
          }
          setTimeout(() => {
            this.initChart();
            this.initVariables();
            this.chartObj.chart.unload();
            this.chart.unload();
            this.chartObj = {
              'chart': this.chart,
              'redrawArgs': {
                length: null,
                to: null,
                columns: null,
                duration: null
              },
              'truncThreshold': undefined
            };
            this.onSubscribeTopic();
          }, 2000);
          setTimeout(() => this.onUnfreeze(), 2000);
        }
        return;
      });
      if (!this.chartObj.truncThreshold) {
        this.chartObj.truncThreshold = maxX;
      }
      redrawArgs.duration = 500;
      subscribedList.forEach(item => {
        dataCols[0].push(Number(item.SCSTime));
        let labelName = '';
        const nameArr = item.Name.split('|');
        this.configService.config.getIn(['configurable_Label']).forEach(label => {
          labelName += nameArr[label] + this.legendSeparator;
        });
        labelName = labelName.slice(0, -1) + ' (Y' + item.attachedAxis + ')';

        let flag = false;
        dataCols.forEach(col => {
          if (col[0] && col[0] === labelName) {
            col.push(Number(item.Value));
            flag = true;
          }
        })
        if (!flag) {
          dataCols.push([labelName, Number(item.Value)]);
        }
        this.axisOptions[labelName] = (!item.attachedAxis || item.attachedAxis === '1') ? 'y' : 'y2';
        this.colorOptions[labelName] = item.color;
      });
      // Update c3JS chart with dataCols (Added data). Refer to redrawArgs parameters
      redrawArgs.columns = dataCols;
      this.chartObj.chart.data.axes(this.axisOptions);
      this.chartObj.chart.data.colors(this.colorOptions);
      this.chartObj.chart.zoom([moment.now() - this.tickIntervalTimeHis, moment.now()]);
      this.chartObj.chart.unzoom();
      this.chartObj.chart.flow(redrawArgs);
    }
  }

  public onFreeze() {
    this.isFrozen = true;
    if (this.chartLib === 1) {
      clearInterval(this.defaultStockInterval);
      this.initStocks();
      this.onUnSubscribeTopic();
      this.onSubscribeTopic();
    } else {
      // initChart has to be the first, or the subscription will not work correctly.
      this.onReset();
      this.initChart();
      this.initVariables();
      this.chartObj.chart.unload();
      this.chart.unload();
      this.chartObj = {
        'chart': this.chart,
        'redrawArgs': {
          length: null,
          to: null,
          columns: null,
          duration: null
        },
        'truncThreshold': undefined
      };
      this.onSubscribeTopic();
    }
  }

  public onUnfreeze() {
    this.isFrozen = false;
    if (this.chartLib === 1) {
      clearInterval(this.defaultStockInterval);
      this.highstockDataService.initDataList();
      this.initStocks();
      this.onUnSubscribeTopic();
      this.onSubscribeTopic();
    } else {
      this.onReset();
      this.initChart();
      this.initVariables();
      this.chartObj.chart.unload();
      this.chart.unload();
      this.chartObj = {
        'chart': this.chart,
        'redrawArgs': {
          length: null,
          to: null,
          columns: null,
          duration: null
        },
        'truncThreshold': undefined
      };
      this.onSubscribeTopic();
    }
    // check if we have pending update
    for (let i = 0; i < this.pendingUpdate.length; i++) {
      if (this.pendingUpdate[i].sequence === this.stompSubscriptionService.currentSequence + 1) {
        this.stompSubscriptionService.currentSequence = this.pendingUpdate[i].sequence;
        this.redrawGraph(this.mapSubscribedSelectedOlsDataScsResponse(this.pendingUpdate[i]));
        const response = this.pendingUpdate[i];
        // update grid
        const transaction: any = {};
        if (response.deleted !== undefined) {
          transaction.remove = response.deleted;
          this.entries -= response.deleted.length;
        }
        if (response.updated !== undefined) {
          transaction.update = response.updated;
        }
        if (response.created !== undefined) {
          transaction.add = response.created;
          this.entries += response.created.length;
        }
        // this._gridOptions.api.updateRowData(transaction);
      }
    }
    if (this.selectedNodeID != null) {
      this._gridOptions.api.ensureNodeVisible((node: RowNode) => this.selectedNodeID === node.data.ID, 'top');
    } else {
      this._gridOptions.api.ensureIndexVisible(0, 'top');
    }
    this.pendingUpdate = [];
  }

  onExport() {
    this._gridOptions.api.exportDataAsCsv();
  }

  onServerKey(event) {
    this.serverName = event.target.value;
  }

  onListKey(event) {
    this.listName = event.target.value;
  }

  onUnSubscribeTopic() {
    this.rowData = [];
    this.pendingUpdate = [];
    this.entries = 0;
    this.message = '';
    this.stompSubscriptionService.currentSequence = -2;
    this.stompSubscriptionService.unSubscribe();
  }

  onSubscribeTopic() {
    // Both subscriptions are necessary to retreve data. Cannot have only one subscription
    // The first one is for retrieving historical data before moment.now()
    this.stompSubscriptionService.onSubscribeResponse().then((stomp_response_subscription: any) => {
      this.stompSubscriptionService.onSubscribeScsResponse(stomp_response_subscription, 'response_subscription').then((o: any) => {
        this.message = '';
        // Initial Mapping between retrieved ScsResponse & Selected OlsList
        if (o.component === 'OlsTopicComponent') {
          this.stompSubscriptionService.setCurrentSequence(o.response.sequence);
          // if (o.response.currentData !== undefined && this.isFrozen) {
          if (o.response.currentData !== undefined && this.isFrozen) {
            this.rowData = o.response.currentData;
            this.entries = this.rowData.length;
            const sortedMapSelectedList = _.sortBy(this.mapSelectedOlsDataScsResponse(o.response), 'SCSTime');
            this.selectedOlsdatas.forEach(data => {
              let redrawList = [];
              sortedMapSelectedList.forEach(item => {
                if (Number(item.SCSTime) >= moment.now() - Number(this.configService.config.getIn(['his_time']) * 60 * 1000)
                  && data.Name === item.Name) {
                  redrawList.push(item);
                }
              });
              this.redrawGraph(redrawList);
              redrawList = [];
            });
          } else if (this.c3ReloadFlag) { // For c3 memory leakage, need to destroy & redraw
            this.rowData = o.response.currentData;
            this.entries = this.rowData.length;
            const sortedMapSelectedList = _.sortBy(this.mapSelectedOlsDataScsResponse(o.response), 'SCSTime');
            this.selectedOlsdatas.forEach(data => {
              let redrawList = [];
              sortedMapSelectedList.forEach(item => {
                if (Number(item.SCSTime) >= moment.now() - Number(this.tickIntervalTime)
                  && data.Name === item.Name) {
                  redrawList.push(item);
                }
              });
              console.log('Special Memory Part', redrawList);
              this.redrawGraph(redrawList);
              redrawList = [];
            });
            this.c3ReloadFlag = false;
          } else {
            this.entries = 0;
          }
        }
      })
    });
    // The second one is for subscribing future incoming data after moment.now()
    this.stompSubscriptionService.onSubscribeTopic(this.serverName, this.listName).then((stomp_subscription: any) => {
      // this.stompSubscriptionService.onSubscribeScsResponse(stomp_subscription, 'sub').then((o: any) => {
      // });
      this.stompSubscriptionService.sub = stomp_subscription.subscribe((stompframe: Frame) => {
        const o = JSON.parse(stompframe.body) as ScsResponse;
        const seq = o.response.sequence;
        if (this.isFrozen) {
          // connection has been requested
          this.pendingUpdate.push(o.response);
          return;
        }

        // Receive a new message with no pending update
        if (this.stompSubscriptionService.currentSequence + 1 === seq) {
          this.stompSubscriptionService.currentSequence = seq;
          this.updateGrid(o.response);
          this.redrawGraph(this.mapSubscribedSelectedOlsDataScsResponse(o.response));
          return;
        }
        // Push data into pending Update. Will plot on the chart later
        if (this.stompSubscriptionService.currentSequence < 0) {
          // connection has been requested
          this.pendingUpdate.push(o.response);
          return;
        }
        // There are pending update exist. Need to plot pending update first before plotting this newly received data
        if (this.stompSubscriptionService.currentSequence + 1 !== seq) {
          // check if we have pending update
          for (let i = 0; i < this.pendingUpdate.length; i++) {
            if (this.pendingUpdate[i].sequence === this.stompSubscriptionService.currentSequence + 1) {
              this.stompSubscriptionService.currentSequence = this.pendingUpdate[i].sequence;
              this.updateGrid(this.pendingUpdate[i]);
              this.redrawGraph(this.mapSubscribedSelectedOlsDataScsResponse(o.response));
            }
          }
          this.pendingUpdate = [];
          // Unexpected sequence error. Normally due to poor network connection or other issues.
          // Have to restart the subscription process.
          if (this.stompSubscriptionService.currentSequence + 1 !== seq) {
            this.openSnackBar(this.translateService.instant('GENERAL_STOMP_CONNECTION_ERR_MSG'));
            this.stompSubscriptionService.currentSequence = -2;
            this.requestPublish();
            return;
          }
        }
        this.stompSubscriptionService.currentSequence = seq;
        this.updateGrid(o.response);
        this.redrawGraph(this.mapSubscribedSelectedOlsDataScsResponse(o.response));
      });
    });

    this.requestPublish();
  }

  private requestPublish() {
    const req = new ScsRESTRequest('OlsTopicComponent', 'GetCurrentOlsTopicData', 'Olstest');
    req.parameters = {};
    req.parameters.listName = this.listName;
    req.parameters.listServer = this.serverName;
    this.stompSubscriptionService.stompPublish('/app/scsrequest', req);
  }

  // Map historical data (before moment.now()) to required format
  private mapSelectedOlsDataScsResponse(scsResponse) {
    const returnList = [];
    this.selectedOlsdatas.forEach(Data => {
      scsResponse.currentData.forEach(responseData => {
        if (responseData.Name === Data.Name) {
          const adjustedTime = this.extractSCSTime(responseData);
          returnList.push({
            'SCSTime': adjustedTime,
            'Value': responseData.Value,
            'Name': responseData.Name,
            'color': Data.color,
            'attachedAxis': Data.attachedAxis
          });
        }
      });
    });

    return returnList;
  }

  // Map subscribed data (after moment.now()) to required format
  private mapSubscribedSelectedOlsDataScsResponse(scsResponse) {
    const returnList = [];
    this.selectedOlsdatas.forEach(Data => {
      scsResponse.created.forEach(item => {
        if (item.Name === Data.Name) {
          const adjustedTime = this.extractSCSTime(item);
          returnList.push({
            'SCSTime': adjustedTime,
            'Value': item.Value,
            'Name': item.Name,
            'color': Data.color,
            'attachedAxis': Data.attachedAxis
          });
        }
      })
    })
    return returnList;
  }

  // This method is used for convert SCSTime in millisecond format.
  private extractSCSTime(item: any) {
    const minute = item.SCSTime.substr(0, item.SCSTime.indexOf(' ')) as string;
    const milliSec = item.SCSTime.substr((item.SCSTime.indexOf(' ') as number) + 1) as string;
    const adjustedTime = minute + '0'.repeat((6 - milliSec.length)) + milliSec.substr(0, (-3 + milliSec.length));
    return adjustedTime;
  }

  // dev_mode grid
  private updateGrid(response: any) {
    // update grid
    const transaction: any = {};
    if (response.deleted !== undefined) {
      transaction.remove = response.deleted;
      this.entries -= response.deleted.length;
    }
    if (response.updated !== undefined) {
      transaction.update = response.updated;
    }
    if (response.created !== undefined) {
      transaction.add = response.created;
      this.entries += response.created.length;
    }
    // this._gridOptions.api.updateRowData(transaction);

    // if (this.selectedNodeID != null) {
    //   this._gridOptions.api.ensureNodeVisible((node: RowNode) => this.selectedNodeID === node.data.ID, 'top');
    // } else {
    //   this._gridOptions.api.ensureIndexVisible(0, 'top');
    // }
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(RttAppDialogComponent, {
      data: {
        yAxis1: this.selectedOlsdatasService.getyaxis1(),
        yAxis2: this.selectedOlsdatasService.getyaxis2(),
        tickInterval: this.selectedOlsdatasService.getTickInterval(),
        chType: this.selectedOlsdatasService.getselectedChart(),
        chartTypeList: this.translateService.instant('CHART_TYPES'),
        powerUnitList: this.selectedOlsdatasService.getPointUnitList(),
        powerUnitDisplayList: this.selectedOlsdatasService.getPointUnitList().map((item) => this.translateService.instant(item.toString())),
        allowAutoMinMax: this.selectedOlsdatasService.getAllowAutoMinMax(),
        allowShowYAxis2: this.selectedOlsdatasService.getAllowShowYAxis2()
      }
    }).afterClosed().subscribe(result => {
      if (!result) { return; }
      this.selectedOlsdatasService.setyaxis1(result.data.yAxis1);
      this.selectedOlsdatasService.setyaxis2(result.data.allowShowYAxis2 ? result.data.yAxis2 : {});
      this.selectedOlsdatasService.setTickInterval(result.data.tickInterval);
      this.selectedOlsdatasService.setselectedChart(result.data.chType);
      this.selectedOlsdatasService.setAllowAutoMinMax(result.data.allowAutoMinMax);
      this.onReset();
      this.initVariables();
      this.chartLib === 1 ? this.initStocks() : this.initChart();
      this.onSubscribeTopic();
    }, failure => {
      this.openSnackBar(failure.message);
    });
  }

  private openSnackBar(msg: string): void {
    this.snackBar.open(msg, this.translateService.instant('BUTTON.DISMISS'), {
      duration: 10000,
    });
  }

  onModal() {
    document.getElementById('myModal').style.display === 'block' ?
      document.getElementById('myModal').style.display = 'none' : document.getElementById('myModal').style.display = 'block';
  }

  onReset() {
    this.chartLib === 1 ? clearInterval(this.defaultStockInterval) : clearInterval(this.defaultInterval);
    this.highstockDataService.initDataList();
    this.stompSubscriptionService.unSubscribe();
  }

  ngOnDestroy() {
    this.onReset();
  }

  onReselect(): void {
    this.onReset();
    this.router.navigateByUrl('rtt');
  }
}
