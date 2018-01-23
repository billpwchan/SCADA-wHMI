import { Component, OnInit, OnDestroy, OnChanges, EventEmitter, Output, SimpleChanges, Input } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Card } from '../../../model/Scenario';
import { DatatableCard } from '../../../model/DatatableScenario';
import { AppSettings } from '../../../app-settings';
import { CardsSettings } from './../cards/cards-settings';
import { FormGroup, FormControl } from '@angular/forms';
import { Subscription } from 'rxjs/Subscription';
import { SettingsService } from '../../../service/settings.service';
import { RenameSettings } from './rename-settings';

@Component({
  selector: 'app-rename',
  templateUrl: './rename.component.html',
  styleUrls: ['./rename.component.css']
})
export class RenameComponent implements OnInit, OnDestroy, OnChanges {

  public static readonly STR_INIT = AppSettings.STR_INIT;

  public static readonly STR_NORIFY_FROM_PARENT = AppSettings.STR_NOTIFY_FROM_PARENT;

  readonly c: string = 'RenameComponent';

  @Input() notifyFromParent: string;
  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  private preview: string[];
  private updated: string[];
  @Input()
  set updateNames(data: string[]) {
    const f = 'updateNames';
    console.log(this.c, f);
    if ( null != data ) {

      this.updated = data;
      console.log(this.c, f, 'this.updated', this.updated);

    } else {
      console.warn(this.c, f, 'data IS INVALID');
    }
  }
  @Input()
  set updateName(data: string) {
    const f = 'updateName';
    console.log(this.c, f);
    if ( null != data ) {
      this.name = data;
      this.txtModifyName = this.name;
    }
  }
  @Input()
  set renameEnable(data: Date) {
    const f = 'renameEnable';
    this.divRenameEnable = true;
  }
  @Output() onUpdatedName = new EventEmitter<string>(null);

  name: string;

  // Modify Card
  divRenameEnable: boolean;
    txtModifyName: string;
    btnModifySaveDisable: boolean;
    btnModifyCancelDisable: boolean;

  txtNewAddNameTooShortInvalid: boolean;
  txtNewAddNameTooLongInvalid: boolean;
  txtNewAddNameDuplicatedInvalid: boolean;

  txtModifyNameTooShortInvalid: boolean;
  txtModifyNameTooLongInvalid: boolean;
  txtModifyNameDuplicatedInvalid: boolean;

  private cardNameMin = NaN;
  private cardNameMax = NaN;

  constructor(
    private translate: TranslateService
    , private settingsService: SettingsService
  ) {
  }

  ngOnInit(): void {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.loadSettings();

    this.btnClicked(RenameComponent.STR_INIT);
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
  }

  onParentChange(change: string): void {
    const f = 'onParentChange';
    console.log(this.c, f);
    console.log(this.c, f, 'change', change);

    if ( change ) {
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    const f = 'ngOnChanges';
    console.log(this.c, f);
    console.log(this.c, f, 'changes', changes);
    if ( changes[RenameComponent.STR_NORIFY_FROM_PARENT] ) {
      this.onParentChange(changes[RenameComponent.STR_NORIFY_FROM_PARENT].currentValue);
    }
  }

  sendNotifyParent(str: string) {
    const f = 'sendNotifyParent';
    console.log(this.c, f, str);
    this.notifyParent.emit(str);
  }

  private loadSettings(): void {
    const f = 'loadSettings';
    console.log(this.c, f);
    this.cardNameMin = Number.parseInt(this.settingsService.getSetting(this.c, f, this.c, RenameSettings.STR_NAME_MIN));
    this.cardNameMax = Number.parseInt(this.settingsService.getSetting(this.c, f, this.c, RenameSettings.STR_NAME_MAX));
  }

  private nameExists(newName: string): boolean {
    const f = 'nameExists';
    console.log(this.c, f);
    console.log(this.c, f, 'newName[' + newName + ']');

    let ret = false;
    for ( let i = 0 ; i < this.updated.length ; ++i ) {
      const name = this.updated[i];
      if ( name === newName ) {
        ret = true;
      }
    }
    return ret;
  }

  onChange(name: string, event?: Event): void {
    const f = 'onChange';
    console.log(this.c, f);
    console.log(this.c, f, 'name[' + name + ']');

    if ( 'txtModifyName' === name ) {
      const newName = this.txtModifyName;
      const curName = this.name;

      this.btnModifySaveDisable = false;

      this.txtModifyNameTooShortInvalid = false;
      this.txtModifyNameTooLongInvalid = false;
      this.txtModifyNameDuplicatedInvalid = false;

      if ( newName.length <= this.cardNameMin ) {
        this.btnModifySaveDisable = true;
        this.txtModifyNameTooShortInvalid = true;

      } else if ( newName.length >= this.cardNameMax ) {
        this.btnModifySaveDisable = true;
        this.txtModifyNameTooLongInvalid = true;

      } else if ( newName === curName ) {
        this.btnModifySaveDisable = true;

      } else if ( this.nameExists(newName) ) {
        this.btnModifySaveDisable = true;
        this.txtModifyNameDuplicatedInvalid = true;

      }
    }
  }

  private init(): void {
    const f = 'init';
    console.log(this.c, f);

    // Modify Card
    this.divRenameEnable = false;
      this.txtModifyName = '';
      this.btnModifySaveDisable = false;
      this.btnModifyCancelDisable = false;

    this.txtNewAddNameTooShortInvalid = false;
    this.txtNewAddNameTooLongInvalid = false;
    this.txtNewAddNameDuplicatedInvalid = false;

    this.txtModifyNameTooShortInvalid = false;
    this.txtModifyNameTooLongInvalid = false;
    this.txtModifyNameDuplicatedInvalid = false;

  }

  btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(this.c, f);
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');
    switch (btnLabel) {
      case RenameComponent.STR_INIT: {
        this.init();
      } break;
      case 'modify': {
        this.txtModifyName = this.name;
        this.divRenameEnable = true;
        this.btnModifySaveDisable = true;
      } break;
      case 'modifysave': {
        this.onUpdatedName.emit(this.txtModifyName);
        this.init();
      } break;
      case 'modifysavecancel': {
        this.divRenameEnable = false;
      } break;
    }
  }
}
