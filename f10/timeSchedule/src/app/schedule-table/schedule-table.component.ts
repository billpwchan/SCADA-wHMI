import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router'
import { SelectComponent } from 'ng2-select';
import { DatatableComponent } from '../../../node_modules/@swimlane/ngx-datatable/src/components/datatable.component';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { Schedule } from '../type/schedule';
import { ScheduleItem } from '../type/schedule-item';
import { ConfigService } from '../service/config.service';
import { ScheduleService } from '../service/schedule.service';
import { UtilService } from '../service/util.service';
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
    // display periodic
    public displayPeriodicSchedules: boolean = false;
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
    public selectedItemDisplay = '';
    // flag to display 'Add' button
    public addScheduleEnabled = false;
    // flag to display 'Delete' button
    public deleteScheduleEnabled = false;
    // flag to display 'Rename' button
    public renameScheduleEnabled = false;
    // userdefined schedules visible count
    public visibleUserDefinedCnt = 0;
    public maxUserDefinedScheduleCount = 0;

    public unusedSchedules = [];
    public newSchedule: Schedule;

    // place holder to store title typed by operator
    public newScheduleTitle: string;
    // flags to display button to enable/disable task
    public displayEnableOnTime = false;
    public displayDisableOnTime = false;
    public displayEnableOffTime = false;
    public displayDisableOffTime = false;
    public addScheduleClicked = false;
    public renameScheduleClicked = false;

    // public onTimeIsEnabled = false;
    // public offTimeIsEnabled = false;
    public pendingOnTimeIsEnabled = false;
    public pendingOffTimeIsEnabled = false;

    public onTimeNotAvailable = true;
    public offTimeNotAvailable = true;

    public newOnTime = '';
    public newOffTime = '';

    public inputIsModified = false;

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

        this.inhibitedOnOffTime = this.configService.config.getIn(['schedule_table', 'inhibited_on_off_time']);
        console.log('{schedule-table}', '[loadConfig]', 'inhibited_on_off_time=', this.inhibitedOnOffTime);

        this.maxUserDefinedScheduleCount = this.configService.config.getIn(['schedule_table', 'max_userdefined_schedule_count']);
        console.log('{schedule-table}', '[loadConfig]', 'max-userdefined-schedule-count=', this.maxUserDefinedScheduleCount);
    }
    loadData() {
        this.cleanupSubscriptions();
        this.subRoute = this.route.queryParams.subscribe(params => {
            let p = params['periodic'];
            if (p === 'true') {
                this.displayPeriodicSchedules = true;
            } else {
                this.displayPeriodicSchedules = false;
            }
            console.log('{schedule-table}', '[loadData]', 'periodic =', this.displayPeriodicSchedules);

            let clientName = params['clientName'] || this.configService.config.getIn(['default_client_name']);
            console.log('{schedule-table}', '[loadData]', 'clientName =', clientName);

            this.scheduleService.setClientName(clientName);

            this.subSchedules = this.scheduleService.getSchedulesByPeriodic(this.displayPeriodicSchedules).subscribe(schedules => {
                this.schedules = schedules;
                if (schedules && schedules.length > 0) {
                    console.log('{schedule-table}', '[loadData]', 'getSchedulesByPeriodic return schedule count=', schedules.length);
                    // display first schedule as active and selected schedules 
                    this.activeItem = [this.schedules[0]];
                    this.selectedSchedule = schedules[0];

                    this.visibleUserDefinedCnt = 0;
                    for (let s of schedules) {
                        if (!s.titleReadOnly && s.visibility) {
                            this.visibleUserDefinedCnt++;
                        }
                    }
                    this.updateAddDeleteRenameScheduleButton();
                    this.cancelRenameSchedule();
//                    this.clearSelectedRow();
//                    this.filterSchItemsBySchedule();
                } else {
                    console.log('{schedule-table}', '[loadData]', 'getSchedulesByPeriodic return 0 schedule');
                }
            })

            console.log('{schedule-table}', '[loadData]', 'schedules', this.schedules);
            console.log('{schedule-table}', '[loadData]', 'schedules', this.schedules);
            this.subScheduleItems = this.scheduleService.getScheduleItemsByPeriodic(this.displayPeriodicSchedules).subscribe(scheduleItems => {
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
        this.updateAddDeleteRenameScheduleButton();
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
        this.selectedItemDisplay = this.translate.instant('Location_' + scheduleItem.geoCat) + '-' +
                                    this.translate.instant('System_' + scheduleItem.funcCat) + '-' +
                                    this.translate.instant(scheduleItem.eqtLabel)  + '-' + 
                                    this.translate.instant(scheduleItem.eqtDescription);
        if (scheduleItem.onTime !== this.unavailableOnOffTime) {
            this.onTimeNotAvailable = false;
        } else {
            this.onTimeNotAvailable = true;
        }
        if (scheduleItem.enableFlag1) {
            this.pendingOnTimeIsEnabled = true;
        } else {
            this.pendingOnTimeIsEnabled = false;
        }
        this.newOnTime = scheduleItem.onTime;
        if (scheduleItem.offTime !== this.unavailableOnOffTime) {
            this.offTimeNotAvailable = false;
        } else {
            this.offTimeNotAvailable = true;
        }
        if (scheduleItem.enableFlag2) {
            this.pendingOffTimeIsEnabled = true;
        } else {
            this.pendingOffTimeIsEnabled = false;
        }
        this.newOffTime = scheduleItem.offTime;
        this.inputIsModified = false;
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
        this.selectedItemDisplay = '';
    }
    public addSchedule() {
        console.log('{schedule-table}', '[addSchedule]');
        this.addScheduleClicked = true;
        this.unusedSchedules = this.scheduleService.getUnusedSchedules();
        if (this.unusedSchedules && this.unusedSchedules.length > 0) {
            this.newSchedule = this.unusedSchedules[0];
        }
    }
    public applyAddSchedule() {
        console.log('{schedule-table}', '[applyAddSchedule]');
        this.addScheduleClicked = false;

        if (this.newSchedule) {
            this.scheduleService.addSchedule(this.newSchedule.id).subscribe(
                res => {
                    //this.schedules.push(this.newSchedule);
                    this.loadData();
                }
            )
        }
    }
    public cancelAddSchedule() {
        console.log('{schedule-table}', '[cancelRenameSchedule]');
        this.addScheduleClicked = false;
    }
    public deleteSchedule() {
        console.log('{schedule-table}', '[deleteSchedule]');
        if (this.selectedSchedule.periodic && !this.selectedSchedule.titleReadOnly) {
            this.scheduleService.deleteSchedule(this.selectedSchedule.id).subscribe(
                res => {
                    // let newSchedules = Array<Schedule>();
                    // for (let s of this.schedules) {
                    //     if (this.selectedSchedule.id !== s.id) {
                    //         newSchedules.push(s);
                    //     }
                    // }
                    // this.schedules = newSchedules;
                    this.loadData();
                }
            )
        }
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
    public updateAddDeleteRenameScheduleButton() {
        console.log('{schedule-table}', '[loadData]', 'updateAddDeleteRenameScheduleButton visibleUserDefinedCnt', this.visibleUserDefinedCnt);
        this.addScheduleEnabled = this.visibleUserDefinedCnt < this.maxUserDefinedScheduleCount;
        this.deleteScheduleEnabled = !this.selectedSchedule.titleReadOnly && (this.visibleUserDefinedCnt > 0);
        this.renameScheduleEnabled = !this.selectedSchedule.titleReadOnly;
    }
    public cancelRenameSchedule() {
        console.log('{schedule-table}', '[cancelRenameSchedule]', this.newScheduleTitle);
        this.renameScheduleClicked = false;
        this.newScheduleTitle = '';
    }
    public startOneShot() {
        console.log('{schedule-table}', '[startOneShot]');
        this.scheduleService.startOneshotSchedule(this.selectedSchedule.id);
    }
    public stopOneShot() {
         console.log('{schedule-table}', '[stopOneShot]');
        this.scheduleService.stopSchedule(this.selectedSchedule.id);
    }

    public updateOnTimeValue(newOnTimeValue) {
        console.log('{schedule-table}', '[updateOnTimeValue]', newOnTimeValue);
        this.newOnTime = newOnTimeValue;
        this.checkInputModified();
    }
    public updateOffTimeValue(newOffTimeValue) {
        console.log('{schedule-table}', '[updateOffTimeValue]', newOffTimeValue);
        this.newOffTime = newOffTimeValue;
        this.checkInputModified();
    }

    public updateOnTimeIsEnabled(onTimeIsEnabled) {
        console.log('{schedule-table}', '[updateOnTimeIsEnabled]', onTimeIsEnabled);
        this.pendingOnTimeIsEnabled = onTimeIsEnabled;
        this.checkInputModified();
    }
    public updateOffTimeIsEnabled(offTimeIsEnabled) {
        console.log('{schedule-table}', '[updateOffTimeIsEnabled]', offTimeIsEnabled);
        this.pendingOffTimeIsEnabled = offTimeIsEnabled;
        this.checkInputModified();
    }
    public checkInputModified() {
        let selectedItem: ScheduleItem = this.selectedRow[0];

        if (this.pendingOnTimeIsEnabled && !selectedItem.enableFlag1) {
            this.inputIsModified = true;
            console.log('{schedule-table}', '[checkInputModified]', 'this.pendingOnTimeIsEnabled && (selectedItem.enableFlag1===0)', this.pendingOnTimeIsEnabled, selectedItem.enableFlag1);
        } else if (!this.pendingOnTimeIsEnabled && selectedItem.enableFlag1) {
            this.inputIsModified = true;
            console.log('{schedule-table}', '[checkInputModified]', '!this.pendingOnTimeIsEnabled && (selectedItem.enableFlag1===1)', this.pendingOnTimeIsEnabled, selectedItem.enableFlag1);
        } else if (this.pendingOffTimeIsEnabled && !selectedItem.enableFlag2) {
            this.inputIsModified = true;
            console.log('{schedule-table}', '[checkInputModified]', 'this.pendingOffTimeIsEnabled && (selectedItem.enableFlag2===0)', this.pendingOffTimeIsEnabled, selectedItem.enableFlag2);
        } else if (!this.pendingOffTimeIsEnabled && selectedItem.enableFlag2) {
            this.inputIsModified = true;
            console.log('{schedule-table}', '[checkInputModified]', '!this.pendingOffTimeIsEnabled && (selectedItem.enableFlag2===1)', this.pendingOffTimeIsEnabled, selectedItem.enableFlag2);
        } else if (this.newOnTime !== selectedItem.onTime) {
            this.inputIsModified = true;
            console.log('{schedule-table}', '[checkInputModified]', 'this.newOnTime', this.newOnTime, ' !== selectedItem.onTime', selectedItem.onTime);
        } else if (this.newOffTime !== selectedItem.offTime) {
            this.inputIsModified = true;
            console.log('{schedule-table}', '[checkInputModified]', 'this.newOffTime', this.newOffTime, ' !== selectedItem.offTime', selectedItem.offTime);
        } else {
            this.inputIsModified = false;
        }
        console.log('{schedule-table}', '[checkInputModified]', 'inputIsModified', this.inputIsModified);
    }
    // click event handler for save button
    public saveModified() {
        let selectedItem: ScheduleItem = this.selectedRow[0];
        if (this.pendingOnTimeIsEnabled && !selectedItem.enableFlag1) {
            this.scheduleService.enableTask(this.selectedRow[0].taskName1);
        } else if (!this.pendingOnTimeIsEnabled && selectedItem.enableFlag1) {
            this.scheduleService.disableTask(this.selectedRow[0].taskName1);
        }

        if (this.pendingOffTimeIsEnabled && !selectedItem.enableFlag2) {
            this.scheduleService.enableTask(this.selectedRow[0].taskName2);
        } else if (!this.pendingOffTimeIsEnabled && selectedItem.enableFlag2) {
            this.scheduleService.disableTask(this.selectedRow[0].taskName2);
        }

        if (this.newOnTime !== selectedItem.onTime) {
            if (this.newOnTime && this.selectedRow.length === 1 && this.newOnTime !== selectedItem.onTime) {
                if (selectedItem && selectedItem.taskName1) {
                    selectedItem.onTime = this.newOnTime;
                    const hh = this.newOnTime.split(':')[0];
                    const mm = this.newOnTime.split(':')[1];
                    const dgtime = selectedItem.filter1.split(' ');
                    dgtime[4] = hh;
                    dgtime[5] = mm;

                    if (this.displayPeriodicSchedules) {
                        if (this.checkBeforeCutoffTime(+hh, +mm)) {
                            dgtime[0] = this.scheduleService.getRunNextDayGroupId(this.selectedSchedule.id);
                        } else {
                            dgtime[0] = this.scheduleService.getRunDayGroupId(this.selectedSchedule.id);
                        }
                    } else {
                        if (UtilService.isTimeExpired(+hh, +mm)) {
                            dgtime[0] = this.scheduleService.getRunNextDayGroupId(this.selectedSchedule.id);
                        } else {
                            dgtime[0] = this.scheduleService.getRunDayGroupId(this.selectedSchedule.id);
                        }
                    }
                    const filter = dgtime.join(' ');
                    console.log('{schedule-table}', '[saveModified]', 'onTime=', hh, mm, 'filter=', filter);
                    this.scheduleService.setFilter(selectedItem.taskName1, filter);
                }
            }
        }

        if (this.newOffTime !== selectedItem.offTime) {
            if (this.newOffTime && this.selectedRow.length === 1 && this.newOffTime !== selectedItem.offTime) {
                if (selectedItem && selectedItem.taskName2) {
                    selectedItem.offTime = this.newOffTime;
                    const hh = this.newOffTime.split(':')[0];
                    const mm = this.newOffTime.split(':')[1];
                    const dgtime = selectedItem.filter2.split(' ');
                    dgtime[4] = hh;
                    dgtime[5] = mm;
                    if (this.checkBeforeCutoffTime(+hh, +mm)) {
                        dgtime[0] = this.scheduleService.getRunNextDayGroupId(this.selectedSchedule.id);
                    } else {
                        dgtime[0] = this.scheduleService.getRunDayGroupId(this.selectedSchedule.id);
                    }
                    const filter = dgtime.join(' ');
                    console.log('{schedule-table}', '[saveModified]', 'offTime=', hh, mm, 'filter=', filter);
                    this.scheduleService.setFilter(selectedItem.taskName2, filter);
                }
            }
        }
    }

    public checkBeforeCutoffTime(hour: number, minute: number): boolean {
        let cutoffHr = +this.cutoffTime.split(':')[0];
        let cutoffMin = +this.cutoffTime.split(':')[1];
        if (hour < cutoffHr || (hour === cutoffHr && minute < cutoffMin)) {
            return true;
        }
        return false;
    }
}
