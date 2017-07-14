import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router'
import { SelectComponent } from 'ng2-select';
import { DatatableComponent } from '../../../node_modules/@swimlane/ngx-datatable/src/components/datatable.component';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { Schedule } from '../type/schedule';
import { ScheduleItem } from '../type/schedule-item';
import { ConfigService } from '../service/config.service';
import { ScheduleService } from '../service/schedule.service';
@Component({
    selector: 'app-sch-table',
    templateUrl: './schedule-table.component.html',
    styleUrls: ['./schedule-table.component.css']
})
export class ScheduleTableComponent implements OnInit, OnDestroy {
    // properties for ng2-select
    public _disabledV = '0';
    public disabled = false;
    public activeItem;
    // properties for ngx-datatable
    public messages = {};
    // subscriptions
    private subRoute: any;
    private subSchedules: any;
    private subScheduleItems: any;
    // scheduleType filter
    //private scheduleType: string;
    // current schedule periodic
    public scheduleIsPeriodic: boolean = false;
    // stores schedules returned from ScheduleService
    public schedules: Schedule[] = [];
    // stores scheduleItems filtered by type
    public scheduleItems: ScheduleItem[] = [];
    // cache copy of scheduleItems
    private cachedSchItems = [];
    @ViewChild(DatatableComponent) table: DatatableComponent;
    // selected schedule
    public selectedSchedule: Schedule;
    // selected row in table
    public selectedRow = [];
    // flag to display 'Add' button
    public addScheduleEnabled = false;
    // flag to display 'Delete' button
    public deleteScheduleEnabled = false;
    // flag to display 'Rename' button
    public renameScheduleEnabled = false;
    // place holder to store title typed by operator
    public newScheduleTitle: string;
    // flags to display button to enable/disable task
    public displayEnableOnTime = false;
    public displayDisableOnTime = false;
    public displayEnableOffTime = false;
    public displayDisableOffTime = false;
    public renameScheduleClicked = false;

    // string to represent unavailable task
    private unavailableOnOffTime = 'N/A';
    // string to represent inhibited time
    private inhibitedOnOffTime = '';
    // schedule task cut off time
    public cutoffTime: string;

    constructor(
        private configService: ConfigService,
        private route: ActivatedRoute,
        private scheduleService: ScheduleService,
        private translate: TranslateService) {
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
        this.messages['emptyMessage'] = this.translate.instant('No item to display');
        this.messages['selectedMessage'] = this.translate.instant('selected');
        this.messages['totalMessage'] = this.translate.instant('total');
    }
    loadConfig() {
        console.log('{schedule-table}', '[loadConfig]', 'translate current lang=', this.translate.currentLang);
        
        this.cutoffTime = this.configService.config.getIn(['schedule_table', 'cutoff_time']);
        console.log('{schedule-table}', '[loadConfig]', 'cutoff_time=', this.cutoffTime);

        this.unavailableOnOffTime = this.configService.config.getIn(['schedule_table', 'unavailable_on_off_time']);
        console.log('{schedule-table}', '[loadConfig]', 'unavailable_on_off_time=', this.unavailableOnOffTime);

        console.log('{schedule-table}', '[loadConfig]', 'inhibited_on_off_time=', this.inhibitedOnOffTime);
        this.inhibitedOnOffTime = this.configService.config.getIn(['schedule_table', 'inhibited_on_off_time']);
    }
    loadData() {
        this.cleanupSubscriptions();
        this.subRoute = this.route.queryParams.subscribe(params => {
            let p = params['periodic'];
            if (p === 'true') {
                this.scheduleIsPeriodic = true;
            } else {
                this.scheduleIsPeriodic = false;
            }
            console.log('{schedule-table}', '[loadData]', 'periodic =', this.scheduleIsPeriodic);

            let clientName = params['clientName'] || this.configService.config.getIn(['default_client_name']);
            console.log('{schedule-table}', '[loadData]', 'clientName =', clientName);

            this.scheduleService.setClientName(clientName);

            this.subSchedules = this.scheduleService.getSchedulesByPeriodic(this.scheduleIsPeriodic).subscribe(schedules => {
                this.schedules = schedules;
                if (this.schedules.length > 0) {
                    console.log('{schedule-table}', '[loadData]', 'getSchedulesByPeriodic return schedule count=', schedules.length);
                    this.activeItem = [this.schedules[0]];
                    this.selectedSchedule = schedules[0];
                    // this.scheduleIsPeriodic = this.selectedSchedule.periodic;
                    this.addScheduleEnabled = !this.selectedSchedule.titleReadOnly;
                    this.deleteScheduleEnabled = !this.selectedSchedule.titleReadOnly;
                    this.renameScheduleEnabled = !this.selectedSchedule.titleReadOnly;
                    this.cancelRenameSchedule();
//                    this.clearSelectedRow();
//                    this.filterSchItemsBySchedule();
                } else {
                    console.log('{schedule-table}', '[loadData]', 'getSchedulesByPeriodic return 0 schedule');
                }
            })

            console.log('{schedule-table}', '[loadData]', 'schedules', this.schedules);
            console.log('{schedule-table}', '[loadData]', 'schedules', this.schedules);
            this.subScheduleItems = this.scheduleService.getScheduleItemsByPeriodic(this.scheduleIsPeriodic).subscribe(scheduleItems => {
                this.scheduleItems = scheduleItems;
                this.cachedSchItems = [...this.scheduleItems];
                this.clearSelectedRow();
                this.filterSchItemsBySchedule();
            });
            console.log('{schedule-table}', '[loadData]', 'scheduleItems', this.scheduleItems);
        })
    }
    ngOnDestroy() {
        // this.cleanupSubscriptions()
    }
    private cleanupSubscriptions() {
        if (this.subRoute) {
            this.subRoute.unsubscribe();
        }
        if (this.subSchedules) {
            this.subSchedules.unsubscribe();
        }
        if (this.subScheduleItems) {
            this.subScheduleItems.unsubscribe();
        }
    }
    // ng2-select callback
    private get disabledV(): string {
        return this._disabledV;
    }
    // ng2-select callback
    private set disabledV(value: string) {
        this._disabledV = value;
        this.disabled = this._disabledV === '1';
    }
    // ng2-select callback
    public selected(event: any): void {
        console.log('{schedule-table}', '[ng2-select selected]', 'Selected value is: ', event.text);
        const val = event.id;
        console.log('{schedule-table}', '[ng2-select selected]', 'selectedSchedule', event.id);
        this.selectedSchedule = this.schedules.find(s => s.id === val);
        this.addScheduleEnabled = !this.selectedSchedule.titleReadOnly;
        this.deleteScheduleEnabled = !this.selectedSchedule.titleReadOnly;
        this.renameScheduleEnabled = !this.selectedSchedule.titleReadOnly;
        this.cancelRenameSchedule();
        console.log('{schedule-table}', '[ng2-select selected]', 'titleReadOnly', this.selectedSchedule.titleReadOnly);
        console.log('{schedule-table}', '[ng2-select selected]', 'renameScheduleEnabled', this.renameScheduleEnabled);
        // filter our data
        const temp = this.cachedSchItems.filter(function(d) {
            console.log('{schedule-table}', '[ng2-select selected]', 'filtering', d.scheduleKey, val);
            return d.scheduleKey.indexOf(val) !== -1 || !val;
        });
        // update the rows
        this.scheduleItems = temp;
        console.log('{schedule-table}', '[ng2-select selected]', 'filtered rows', temp);
        // Whenever the filter changes, always go back to the first page
        // this.table.offset = 0;
    }
    // ng2-select callback
    public removed(value: any): void {
        console.log('{schedule-table}', '[ng2-select removed]', 'Removed value is: ', value);
    }
    // ng2-select callback
    public typed(value: any): void {
        console.log('{schedule-table}', '[ng2-select typed]', 'New search input: ', value);
        //this.newScheduleTitle = value;
    }
    // ng2-select callback
    public refreshValue(value: any): void {
        console.log('{schedule-table}', '[ng2-select typed]', 'refresh value ', value);
    }
    // ngx-datatable callback
    public onSelect(selectedRow) {
        console.log('{schedule-table}', '[ngx-datatable onSelect]', 'Select Event', selectedRow, this.selectedRow);
        const scheduleItem: ScheduleItem = this.selectedRow[0];
        if (scheduleItem.onTime !== this.unavailableOnOffTime) {
            if (scheduleItem.enableFlag1) {
                this.displayEnableOnTime = false;
                this.displayDisableOnTime = true;
            } else {
                this.displayEnableOnTime = true;
                this.displayDisableOnTime = false;
            }
        } else {
            this.displayEnableOnTime = false;
            this.displayDisableOnTime = false;
        }
        if (scheduleItem.offTime !== this.unavailableOnOffTime) {
            if (scheduleItem.enableFlag2) {
                this.displayEnableOffTime = false;
                this.displayDisableOffTime = true;
            } else {
                this.displayEnableOffTime = true;
                this.displayDisableOffTime = false;
            }
        } else {
            this.displayEnableOffTime = false;
            this.displayDisableOffTime = false;
        }
      }
    // ngx-datatable callback
    public onActivate(event) {
        console.log('{schedule-table}', '[ngx-datatable onActivate]', 'Activate Event', event);
      }
    // ngx-datatable callback
    updateRowPosition() {
        const ix = this.getSelectedIx();
        const arr = [ ...this.scheduleItems ];
        arr[ix - 1] = this.scheduleItems[ix];
        arr[ix] = this.scheduleItems[ix - 1];
        this.scheduleItems = arr;
    }
    // ngx-datatable callback
    getSelectedIx() {
        return this.selected[0]['$$index'];
    }
    // click event handler for save button
    updateOnOffTime(newOnTime: string, newOffTime: string) {
        console.log('{schedule-table}', '[updateOnOffTime]', 'on/off time ', newOnTime, newOffTime);
        const scheduleItem: ScheduleItem = this.selectedRow[0];
        console.log('{schedule-table}', '[updateOnOffTime]', 'scheduleItem=', scheduleItem);

        if (newOnTime && this.selectedRow.length === 1) {
            if (scheduleItem && scheduleItem.taskName1) {
                scheduleItem.onTime = newOnTime;
                const hh = newOnTime.split(':')[0];
                const mm = newOnTime.split(':')[1];
                const dgtime = scheduleItem.filter1.split(' ');
                dgtime[4] = hh;
                dgtime[5] = mm;
                const filter = dgtime.join(' ');
                console.log('{schedule-table}', '[updateOnOffTime]', 'onTime=', hh, mm, 'filter=', filter);
                this.scheduleService.setFilter(scheduleItem.taskName1, filter);
            }
        }
        if (newOffTime && this.selectedRow.length === 1) {
            if (scheduleItem && scheduleItem.taskName2) {
                scheduleItem.offTime = newOffTime;
                const hh = newOffTime.split(':')[0];
                const mm = newOffTime.split(':')[1];
                const dgtime = scheduleItem.filter2.split(' ');
                dgtime[4] = hh;
                dgtime[5] = mm;
                const filter = dgtime.join(' ');
                console.log('{schedule-table}', '[updateOnOffTime]', 'offTime=', hh, mm, 'filter=', filter);
                this.scheduleService.setFilter(scheduleItem.taskName2, filter);
            }
        }
    }
    // click event handler for clear button
    private clearOnOffTime() {
    }
    private filterSchItemsBySchedule() {
        this.scheduleItems = [];
        for (const r of this.cachedSchItems) {
            if (this.selectedSchedule && r.scheduleKey === this.selectedSchedule.id) {
                this.scheduleItems.push(r);
            }
        }
    }
    private clearSelectedRow() {
        this.selectedRow = [];
    }
    public addSchedule() {
        console.log('{schedule-table}', '[addSchedule]');
    }
    public deleteSchedule() {
        console.log('{schedule-table}', '[deleteSchedule]');
    }
    public renameSchedule() {
        console.log('{schedule-table}', '[renameSchedule] clicked');
        this.renameScheduleClicked = true;
        this.newScheduleTitle = this.selectedSchedule.text;
    }
    public applyRenameSchedule() {
        console.log('{schedule-table}', '[applyRenameSchedule]', this.newScheduleTitle);
        this.renameScheduleClicked = false;
        
        if (this.newScheduleTitle && this.newScheduleTitle.length > 0) {
            this.selectedSchedule.text = this.newScheduleTitle;
            this.scheduleService.setScheduleTitle(this.selectedSchedule.id, this.newScheduleTitle);
            // rebuild schedules
            const tempSch: Schedule[] = [];
            const selectedId = 0;
            for (const sch of this.schedules) {
                if (sch.id === this.selectedSchedule.id) {
                    sch.text = this.newScheduleTitle;
                    this.activeItem = [sch];
                }
                tempSch.push(sch);
            }
            this.schedules = [];
            this.schedules = tempSch;
        }
        this.newScheduleTitle = '';
    }
    public cancelRenameSchedule() {
        console.log('{schedule-table}', '[cancelRenameSchedule]', this.newScheduleTitle);
        this.renameScheduleClicked = false;
        this.newScheduleTitle = '';
    }
    private setOnTimeEnable() {
        console.log('{schedule-table}', '[setOnTimeEnable]');
        // if (this.selectedRow[0].taskName1) {
        //     this.scheduleService.enableTask(this.selectedRow[0].taskName1);
        // }
        this.displayEnableOnTime = false;
        this.displayDisableOnTime = true;
    }
    private setOnTimeDisable() {
        console.log('{schedule-table}', '[setOnTimeDisable]');
        // if (this.selectedRow[0].taskName1) {
        //     this.scheduleService.disableTask(this.selectedRow[0].taskName1);
        // }
        this.displayEnableOnTime = true;
        this.displayDisableOnTime = false;
    }
    private setOffTimeEnable() {
        console.log('{schedule-table}', '[setOffTimeEnable]');
        // if (this.selectedRow[0].taskName2) {
        //     this.scheduleService.enableTask(this.selectedRow[0].taskName2);
        // }
        this.displayEnableOffTime = false;
        this.displayDisableOffTime = true;
    }
    private setOffTimeDisable() {
        console.log('{schedule-table}', '[setOffTimeDisable]');
        // if (this.selectedRow[0].taskName2) {
        //     this.scheduleService.disableTask(this.selectedRow[0].taskName2);
        // }
        this.displayEnableOffTime = true;
        this.displayDisableOffTime = false;
    }
    public startOneShot() {
        console.log('{schedule-table}', '[startOneShot]');
        this.scheduleService.startOneshotSchedule(this.selectedSchedule.id);
    }
    public stopOneShot() {
         console.log('{schedule-table}', '[stopOneShot]');
        this.scheduleService.stopSchedule(this.selectedSchedule.id);
    }
}
