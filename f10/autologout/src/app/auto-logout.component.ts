import { Component, OnInit } from '@angular/core';
import { LocalStorageService } from 'angular-2-local-storage';

import { Config } from './type/config';
import { ConfigService } from './service/config.service';
import { I18nService } from './service/i18n.service';

@Component({
  selector: 'app-auto-logout',
  templateUrl: './auto-logout.component.html'
})
export class AutoLogoutComponent implements OnInit {
  private config: Config = undefined;

  // auto logout settings
  public enabled: boolean;
  public idleThresholdMinute: string;
  public warnThresholdMinute: string;

  // hmi states
  private modified: boolean;

  public constructor(
    private localStorage: LocalStorageService,
    private configService: ConfigService,
    private i18nService: I18nService
  ) {
    console.log('{AutoLogoutComponent}', '[constructor]');
    this.config = configService.config;
  }

  public ngOnInit(): void {
    console.log('{AutoLogoutComponent}', '[ngOnInit]');
    this.onReset();
  }

  public onKeyup(): void {
    console.log('{AutoLogoutComponent}', '[onKeyup]');
    this.modified = true;
  }

  public onClick(): void {
    this.enabled = !this.enabled;
    this.modified = true;
    console.log(
      '{AutoLogoutComponent}', '[onClick]',
      'enabled:', this.enabled
    );
  }

  public onSave(): boolean {
    console.log(
      '{AutoLogoutComponent}', '[onSave]',
      'enabled:', this.enabled,
      'idleThresholdMinute:', this.idleThresholdMinute,
      'warnThresholdMinute:', this.warnThresholdMinute
    );

    const idleThresholdMs = Number(this.idleThresholdMinute) * 60 * 1000;
    const warnThresholdMs = Number(this.warnThresholdMinute) * 60 * 1000;
    if (isNaN(idleThresholdMs) || isNaN(warnThresholdMs)) {
      console.error(
        '{AutoLogoutComponent}', '[onSave]',
        'Invalid values:',
        'idleThresholdMinute:', this.idleThresholdMinute,
        'warnThresholdMinute:', this.warnThresholdMinute
      );
      return false;
    }

    const setting = { };
    setting[this.config.storage.enabledName] = this.enabled;
    setting[this.config.storage.idleThresholdMsName] = idleThresholdMs;
    setting[this.config.storage.warnThresholdMsName] = warnThresholdMs;
    const result = this.localStorage.set(this.config.storage.idName, setting);
    if (!result) {
      console.error(
        '{AutoLogoutComponent}', '[onSave]',
        'Failed to save setting:', setting,
        'localStorage:', this.config.storage.idName
      );
      return false;
    }

    console.log(
      '{AutoLogoutComponent}', '[onSave]',
      'Saved setting:', setting,
      'localStorage:', this.config.storage.idName
    );
    this.onReset();
    return true;
  }

  public onReset(): void {
    this.enabled = this.config.defaultValue.enabled;
    this.idleThresholdMinute = Math.floor(this.config.defaultValue.idleThresholdMs / 60 / 1000).toString();
    this.warnThresholdMinute = Math.floor(this.config.defaultValue.warnThresholdMs / 60 / 1000).toString();
    this.modified = false;

    const setting = this.localStorage.get(this.config.storage.idName);
    console.log(
      '{AutoLogoutComponent}', '[onReset]',
      'setting:', setting
    );

    if (null === setting) { return; }
    if (
      undefined !== setting[this.config.storage.enabledName] &&
      'boolean' === typeof(setting[this.config.storage.enabledName])
    ) {
      this.enabled = setting[this.config.storage.enabledName];
    }
    if (
      undefined !== setting[this.config.storage.idleThresholdMsName] &&
      'number' === typeof(setting[this.config.storage.idleThresholdMsName])
    ) {
      this.idleThresholdMinute = Math.floor(setting[this.config.storage.idleThresholdMsName] / 60 / 1000).toString();
    }
    if (
      undefined !== setting[this.config.storage.warnThresholdMsName] &&
      'number' === typeof(setting[this.config.storage.warnThresholdMsName])
    ) {
      this.warnThresholdMinute = Math.floor(setting[this.config.storage.warnThresholdMsName] / 60 / 1000).toString();
    }
  }

  public isIdleThresholdValid(): boolean {
    const time = Number(this.idleThresholdMinute);
    if (isNaN(time)) { return false; }
    if (time < 1) { return false; }
    return true;
  }

  public isWarnThresholdValid(): boolean {
    const time = Number(this.warnThresholdMinute);
    if (isNaN(time)) { return false; }
    if (time < 1) { return false; }

    const idleTime = Number(this.idleThresholdMinute);
    if (!isNaN(idleTime) && time > idleTime) { return false; }
    return true;
  }

  public isModified(): boolean {
    return this.modified;
  }
}
