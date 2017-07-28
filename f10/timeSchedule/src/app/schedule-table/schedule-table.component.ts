import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router'
import { SelectComponent } from 'ng2-select';
import { DatatableComponent } from '../../../node_modules/@swimlane/ngx-datatable/src/components/datatable.component';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { Schedule } from '../type/schedule';
import { ScheduleItem, ScheduleItemFilter } from '../type/schedule-item';
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
    public selectScheduleDisabled = false;
    public activeItem;
    // properties for ngx-datatable
    public messages = {};
    public sortingColumn = [];

    // subscriptions
    private subRoute: any;
    private subSchedules: any;
    private subScheduleItems: any;
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
    public selectedScheduleItem: ScheduleItem;
    public selectedItemDisplay = '';
    public selectedOnTime = '';
    public selectedOffTime = '';
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

    public pendingOnTimeIsEnabled = false;
    public pendingOffTimeIsEnabled = false;

    public onTimeNotAvailable = true;
    public offTimeNotAvailable = true;

    public newOnTime = '';
    public newOffTime = '';

    public inputIsModified = false;
    public newOnTimeValid = true;
    public newOffTimeValid = true;
    public inputTimeValid = true;

    public newScheduleTitleValid = false;

    public maxTitleLength = 40;

    // string to represent unavailable task
    private unavailableOnOffTime = 'N/A';
    // string to represent inhibited time
    private inhibitedOnOffTime = '';
    // schedule task cut off time
    public cutoffTime: string;

    // ScheduleItem filter list including online/offline filters
    private schItemFilters: ScheduleItemFilter [];

    // offline filter config
    private offlineFilters: Map<string, string>;

    // offline sort config
    private offlineSort: any;

    public runningSchedules = Array<Schedule>();
    public runningSchedulesStr = '';

    private subGetRunningSchedules: any;

    public oneshotStarted = false;

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

        this.offlineFilters = this.configService.config.getIn(['schedule_table', 'filter']);
        console.log('{schedule-table}', '[loadConfig]', 'filter=', this.offlineFilters);

        this.offlineSort = this.configService.config.getIn(['schedule_table', 'sort']);
        console.log('{schedule-table}', '[loadConfig]', 'sort=', this.offlineSort);

        this.maxTitleLength = this.configService.config.getIn(['schedule_table', 'max_title_length']);
        console.log('{schedule-table}', '[loadData]', 'maxTitleLength =', this.maxTitleLength);

    }
    loadData() {
        if (this.subRoute) {
            this.subRoute.unsubscribe();
        }
        this.subRoute = this.route.queryParams.subscribe(params => {
            this.cleanupScheduleSubscriptions();
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

            let runtimeFilters = params['filter'];
            console.log('{schedule-table}', '[loadData]', 'runtimeFilters =', runtimeFilters);

            let runtimeSort = params['sort'];
            console.log('{schedule-table}', '[loadData]', 'runtimeSort =', runtimeSort);

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
                    this.oneshotStarted = this.scheduleService.isOneshotScheduleStarted();
                    this.updateAddDeleteRenameScheduleButton();
                    this.cancelRenameSchedule();
                } else {
                    console.log('{schedule-table}', '[loadData]', 'getSchedulesByPeriodic return 0 schedule');
                }
            })

            console.log('{schedule-table}', '[loadData]', 'schedules', this.schedules);
            this.subScheduleItems = this.scheduleService.getScheduleItemsByPeriodic(this.displayPeriodicSchedules).subscribe(scheduleItems => {
                this.cachedSchItems = [...scheduleItems];
                this.clearSelectedRow();
                this.clearSchItemFilters();

                this.setDefaultSort(runtimeSort);

                if (this.selectedSchedule) {
                    this.addSchItemsFilter('scheduleKey', this.selectedSchedule.id);
                }

                this.addOfflineFilters();

                this.addRuntimeFilters(runtimeFilters);

                this.scheduleItems = this.filterSchItems(scheduleItems);
            });
            console.log('{schedule-table}', '[loadData]', 'scheduleItems', this.scheduleItems);

            this.getRunningSchedules();
        })
    }
    ngOnDestroy() {

    }
    private cleanupScheduleSubscriptions() {        
        if (this.subSchedules) {
            this.subSchedules.unsubscribe();
        }
        if (this.subScheduleItems) {
            this.subScheduleItems.unsubscribe();
        }
        if (this.subGetRunningSchedules) {
            this.subGetRunningSchedules.unsubscribe();
        }
    }
    // ng2-select callback
    private get disabledV(): string {
        return this._disabledV;
    }
    // ng2-select callback
    private set disabledV(value: string) {
        this._disabledV = value;
        this.selectScheduleDisabled = this._disabledV === '1';
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

        // clear equipment selection
        this.clearSelectedRow();
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
        this.resetModified();
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

    private clearSchItemFilters() {
        this.schItemFilters = [];
    }
    private addSchItemsFilter(attributeName: string, filterPattern: string) {
        let scheduleFilter = new ScheduleItemFilter();
        scheduleFilter.attributeName = attributeName;
        scheduleFilter.matchPattern = new RegExp(filterPattern);   
        this.schItemFilters.push(scheduleFilter);
         console.log('{schedule-table}', '[addSchItemsFilter]', 'this.schItemFilters.length', this.schItemFilters.length);
    }
    private addOfflineFilters() {
        console.log('{schedule-table}', '[addOfflineFilters]');
        if (this.offlineFilters) {
            console.log('{schedule-table}', '[addOfflineFilters]', 'this.offlineFilters', this.offlineFilters);
            this.offlineFilters.forEach((value: string, key: string) => {
                console.log('{schedule-table}', '[addOfflineFilters]', 'filter', key, value);
                let filter = new ScheduleItemFilter();
                filter.attributeName = key;
                filter.matchPattern = new RegExp(value);
                this.schItemFilters.push(filter);
                console.log('{schedule-table}', '[addOfflineFilters]', 'this.schItemFilters.length', this.schItemFilters.length);
            })
        } else {
            console.log('{schedule-table}', '[addOfflineFilters]', 'this.offlineFilters is null');
        }
    }
    private addRuntimeFilters(runtimeFilters) {
        console.log('{schedule-table}', '[addRuntimeFilters]');
        if (runtimeFilters) {
            console.log('{schedule-table}', '[addRuntimeFilters]', 'runtimeFilters', runtimeFilters);
            let obj = JSON.parse(runtimeFilters)
            Object.keys(obj).forEach(key => {
                console.log('{schedule-table}', '[addRuntimeFilters]', 'key', key, 'value', obj[key]);
                let filter = new ScheduleItemFilter();
                filter.attributeName = key;
                filter.matchPattern = new RegExp(obj[key]);
                this.schItemFilters.push(filter);
                console.log('{schedule-table}', '[addRuntimeFilters]', 'this.schItemFilters.length', this.schItemFilters.length);
            })
        } else {
            console.log('{schedule-table}', '[addRuntimeFilters]', 'runtimeFilters is null');
        }
    }

    private filterSchItems(scheduleItems): ScheduleItem[] {
        let filteredScheduleItems = Array<ScheduleItem>();
        if (this.schItemFilters) {
            console.log('{schedule-table}', '[filterSchItems]', 'this.schItemFilters.length', this.schItemFilters.length);
            for (const r of scheduleItems) {
                let matching = true;
                let filterCnt = 0;
                for (const f of this.schItemFilters) {
                    console.log('{schedule-table}', '[filterSchItems]', 'filter', f.attributeName, f.matchPattern);
                    const attribute: string = r[f.attributeName];
                    if (attribute) {
                        //console.log('{schedule-table}', '[filterSchItems]', 'attribute', attribute);
                        if (!attribute.toString().match(f.matchPattern)) {
                            matching = false;
                            console.log('{schedule-table}', '[filterSchItems]', 'attribute', attribute, 'not matching', f.matchPattern);
                            break;
                        } else {
                            console.log('{schedule-table}', '[filterSchItems]', 'attribute', attribute, 'matching', f.matchPattern);
                        }
                    } else {
                        console.log('{schedule-table}', '[filterSchItems]', 'attribute not defined', f.attributeName);
                        continue;
                    }
                }
                if (matching) {
                    filteredScheduleItems.push(r);
                }
            }
        } else {
            filteredScheduleItems = scheduleItems;
        }
        return filteredScheduleItems;
    }

    private setDefaultSort(runtimeSort) {
        console.log('{schedule-table}', '[setDefaultSort]');
        this.sortingColumn = [];
        if (runtimeSort) {
            console.log('{schedule-table}', '[setDefaultSort]', 'runtimeSort', runtimeSort);
            let obj = JSON.parse(runtimeSort);
            if (Array.isArray(obj)) {
                this.sortingColumn = obj;
            } else {
                this.sortingColumn = [obj];
            }
            console.log('{schedule-table}', '[setDefaultSort]', 'sortingColumn', this.sortingColumn);
        } else if (this.offlineSort) {
            console.log('{schedule-table}', '[setDefaultSort]', 'runtimeSort is null. offlineSort', this.offlineSort);

            if (this.offlineSort.get('prop') && this.offlineSort.get('dir')) {
                let obj = new Object();
                obj['prop'] = this.offlineSort.get('prop');
                obj['dir'] = this.offlineSort.get('dir');
                this.sortingColumn = [obj];
            }

            console.log('{schedule-table}', '[setDefaultSort]', 'sortingColumn', this.sortingColumn);
        } else {
            console.log('{schedule-table}', '[setDefaultSort]', 'runtimeSort and offlineSort are null. No default sort is applied');
        }
    }
    private clearSelectedRow() {
        this.selectedRow = [];
        this.selectedScheduleItem = null;
        this.selectedItemDisplay = '';
        this.resetModified();
    }
    public addSchedule() {
        console.log('{schedule-table}', '[addSchedule]');
        this.addScheduleClicked = true;
        this.unusedSchedules = this.scheduleService.getUnusedSchedules();
        if (this.unusedSchedules && this.unusedSchedules.length > 0) {
            this.newSchedule = this.unusedSchedules[0];
        }
        this.disableScheduleAction();
    }
    public disableScheduleAction() {
        this.addScheduleEnabled = false;
        this.deleteScheduleEnabled = false;
        this.renameScheduleEnabled = false;
        this.selectScheduleDisabled = true;
    }
    public applyAddSchedule() {
        console.log('{schedule-table}', '[applyAddSchedule]');
        this.addScheduleClicked = false;

        if (this.newSchedule) {
            this.scheduleService.addSchedule(this.newSchedule.id).subscribe(
                res => {
                    this.loadData();
                }
            )
        }
        this.updateAddDeleteRenameScheduleButton();
    }
    public cancelAddSchedule() {
        console.log('{schedule-table}', '[cancelRenameSchedule]');
        this.addScheduleClicked = false;
        this.updateAddDeleteRenameScheduleButton();
    }
    public deleteSchedule() {
        console.log('{schedule-table}', '[deleteSchedule]');
        this.disableScheduleAction();
        if (this.selectedSchedule.periodic && !this.selectedSchedule.titleReadOnly) {
            this.scheduleService.deleteSchedule(this.selectedSchedule.id).subscribe(
                res => {
                    this.loadData();
                }
            )
        }
    }
    public renameSchedule() {
        console.log('{schedule-table}', '[renameSchedule] clicked');
        this.renameScheduleClicked = true;
        this.disableScheduleAction();
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
        this.updateAddDeleteRenameScheduleButton();
    }
    public updateAddDeleteRenameScheduleButton() {
        console.log('{schedule-table}', '[updateAddDeleteRenameScheduleButton]', ' visibleUserDefinedCnt', this.visibleUserDefinedCnt);
        this.addScheduleEnabled = this.visibleUserDefinedCnt < this.maxUserDefinedScheduleCount;
        let isAssigned = this.scheduleService.isScheduleAssigned(this.selectedSchedule.id);
        let isRunning = this.scheduleService.isScheduleRunning(this.selectedSchedule.id);

        console.log('{schedule-table}', '[updateAddDeleteRenameScheduleButton]', 'isAssigned', isAssigned);
        console.log('{schedule-table}', '[updateAddDeleteRenameScheduleButton]', 'isRunning', isRunning);
        this.deleteScheduleEnabled = !isRunning && !isAssigned && !this.selectedSchedule.titleReadOnly && (this.visibleUserDefinedCnt > 0);
        this.renameScheduleEnabled = !this.selectedSchedule.titleReadOnly;
        this.selectScheduleDisabled = false;
    }
    public cancelRenameSchedule() {
        console.log('{schedule-table}', '[cancelRenameSchedule]', this.newScheduleTitle);
        this.renameScheduleClicked = false;
        this.newScheduleTitle = '';
        this.updateAddDeleteRenameScheduleButton();
    }
    public startOneShot() {
        console.log('{schedule-table}', '[startOneShot]');
        this.scheduleService.startOneshotSchedule(this.selectedSchedule.id);
        this.oneshotStarted = true;
    }
    public stopOneShot() {
         console.log('{schedule-table}', '[stopOneShot]');
        this.scheduleService.stopSchedule(this.selectedSchedule.id);
        this.oneshotStarted = false;
    }

    public updateOnTimeValue(newOnTimeValue) {
        console.log('{schedule-table}', '[updateOnTimeValue]', newOnTimeValue);
        this.newOnTime = newOnTimeValue;
        this.checkInputModified();
        this.checkInputTimeIsValid();
    }
    public updateOffTimeValue(newOffTimeValue) {
        console.log('{schedule-table}', '[updateOffTimeValue]', newOffTimeValue);
        this.newOffTime = newOffTimeValue;
        this.checkInputModified();
        this.checkInputTimeIsValid();
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
        //let selectedItem: ScheduleItem = this.selectedRow[0];

        if (this.pendingOnTimeIsEnabled && !this.selectedScheduleItem.enableFlag1) {
            this.inputIsModified = true;
            console.log('{schedule-table}', '[checkInputModified]', 'this.pendingOnTimeIsEnabled && (selectedItem.enableFlag1===0)', this.pendingOnTimeIsEnabled, this.selectedScheduleItem.enableFlag1);
        } else if (!this.pendingOnTimeIsEnabled && this.selectedScheduleItem.enableFlag1) {
            this.inputIsModified = true;
            console.log('{schedule-table}', '[checkInputModified]', '!this.pendingOnTimeIsEnabled && (selectedItem.enableFlag1===1)', this.pendingOnTimeIsEnabled, this.selectedScheduleItem.enableFlag1);
        } else if (this.pendingOffTimeIsEnabled && !this.selectedScheduleItem.enableFlag2) {
            this.inputIsModified = true;
            console.log('{schedule-table}', '[checkInputModified]', 'this.pendingOffTimeIsEnabled && (selectedItem.enableFlag2===0)', this.pendingOffTimeIsEnabled, this.selectedScheduleItem.enableFlag2);
        } else if (!this.pendingOffTimeIsEnabled && this.selectedScheduleItem.enableFlag2) {
            this.inputIsModified = true;
            console.log('{schedule-table}', '[checkInputModified]', '!this.pendingOffTimeIsEnabled && (selectedItem.enableFlag2===1)', this.pendingOffTimeIsEnabled, this.selectedScheduleItem.enableFlag2);
        } else if (this.newOnTime !== this.selectedScheduleItem.onTime) {
            this.inputIsModified = true;
            console.log('{schedule-table}', '[checkInputModified]', 'this.newOnTime', this.newOnTime, ' !== selectedItem.onTime', this.selectedScheduleItem.onTime);
        } else if (this.newOffTime !== this.selectedScheduleItem.offTime) {
            this.inputIsModified = true;
            console.log('{schedule-table}', '[checkInputModified]', 'this.newOffTime', this.newOffTime, ' !== selectedItem.offTime', this.selectedScheduleItem.offTime);
        } else {
            this.inputIsModified = false;
        }
        console.log('{schedule-table}', '[checkInputModified]', 'inputIsModified', this.inputIsModified);
    }
    public checkInputTimeIsValid() {
        let regexp = /^\d\d:\d\d$/;
        if (this.newOnTime.match(regexp)) {
            this.newOnTimeValid = true;
        } else {
            this.newOnTimeValid = false;
        }
        if (this.newOffTime.match(regexp)) {
            this.newOffTimeValid = true;
        } else {
            this.newOffTimeValid = false;
        }
        if (this.newOnTimeValid && this.newOffTimeValid) {
            this.inputTimeValid = true;
        } else {
            this.inputTimeValid = false;
        }
        console.log('{schedule-table}', '[checkInputTimeIsValid]', 'inputTimeValid', this.inputTimeValid, 'newOnTime', this.newOnTime, 'newOffTime', this.newOffTime);
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

    public resetModified() {
        if (this.selectedRow && this.selectedRow[0]) {
            let scheduleItem: ScheduleItem = this.selectedRow[0];
            this.selectedScheduleItem = this.selectedRow[0];

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
            this.selectedOnTime = scheduleItem.onTime;
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

            this.selectedOffTime = scheduleItem.offTime;
            this.newOffTime = scheduleItem.offTime;
        }
        this.inputIsModified = false;
        this.newOnTimeValid = true;
        this.newOffTimeValid = true;
        this.inputTimeValid = true;
    }

    public checkBeforeCutoffTime(hour: number, minute: number): boolean {
        let cutoffHr = +this.cutoffTime.split(':')[0];
        let cutoffMin = +this.cutoffTime.split(':')[1];
        if (hour < cutoffHr || (hour === cutoffHr && minute < cutoffMin)) {
            return true;
        }
        return false;
    }

    public updateNewScheduleTitle(newScheduleTitle) {
        console.log('{schedule-table}', '[updateNewScheduleTitle]', newScheduleTitle);
        if (newScheduleTitle && newScheduleTitle.length > 0) {
            let found = false;
            for (let s of this.schedules) {
                if (s.text === newScheduleTitle) {
                    found = true;
                    break;
                }
            }
            if (found) {
                this.newScheduleTitleValid = false;
            } else {
                this.newScheduleTitleValid = true;
            }
        } else {
            this.newScheduleTitleValid = false;
        }
    }

    public getRunningSchedules() {
        console.log('{schedule-table}', '[getRunningSchedules]');
        this.subGetRunningSchedules = this.scheduleService.getRunningSchedules().subscribe(
            schedules => {
                console.log('{schedule-table}', '[getRunningSchedules] return', schedules);
                if (schedules !== undefined) {
                    this.runningSchedules = schedules;
                    if (schedules && schedules.length > 0) {
                        let cnt = 0;
                        for (let s of schedules) {
                            if (cnt > 0) {
                                this.runningSchedulesStr = this.runningSchedulesStr + ', ' + s.text;
                            } else {
                                this.runningSchedulesStr = s.text;
                            }
                            cnt++;
                            console.log('{schedule-table}', '[getRunningSchedules] runningSchedulesStr', this.runningSchedulesStr);
                        }
                    } else {
                        let str = 'No schedule is running';
                        let translatedStr = this.translate.instant(str);
                        if (translatedStr) {
                            this.runningSchedulesStr = translatedStr;
                        } else {
                            this.runningSchedulesStr = str;
                        }
                    }
                }
            }
        )
    }
}

