import { Component, OnInit, Input } from '@angular/core';
import { Record } from '../model/record';
import { TranslateService } from '@ngx-translate/core';
import { SettingsService } from '../service/settings.service';
import { ReplayService } from '../service/replay.service';

@Component({
  selector: 'app-replay-action',
  templateUrl: './replay-action.component.html',
  styleUrls: ['./replay-action.component.css']
})
export class ReplayActionComponent implements OnInit {
  readonly c = 'ReplayActionComponent';
  readonly STR_SPEEDS = 'speeds';
  readonly STR_BTN1_CAPTION = 'btn1Caption';
  readonly STR_BTN2_CAPTION = 'btn2Caption';
  readonly STR_BTN1_ACTION = 'btn1Action';
  readonly STR_BTN2_ACTION = 'btn2Action';

  public speeds: number[] = [ 1, 2, 5, 10, 20 ];

  public btn1Caption = '';
  public btn2Caption = '';
  public btn1Action: any;
  public btn2Action: any;

  constructor(
    private translate: TranslateService
    , private settingsService: SettingsService
    , private replayService: ReplayService
  ) { }

  ngOnInit() {
    const f = 'ngOnInit';
    this.speeds = this.settingsService.getSetting(this.c, f, this.c, this.STR_SPEEDS);

    this.btn1Caption = this.settingsService.getSetting(this.c, f, this.c, this.STR_BTN1_CAPTION);
    this.btn2Caption = this.settingsService.getSetting(this.c, f, this.c, this.STR_BTN2_CAPTION);

    this.btn1Action = this.settingsService.getSetting(this.c, f, this.c, this.STR_BTN1_ACTION);
    this.btn2Action = this.settingsService.getSetting(this.c, f, this.c, this.STR_BTN2_ACTION);
  }

  onSpeedChange(speed: number) {
    const f = 'onSpeedChange';
    this.replayService.setReplaySpeed(+speed);
  }

  onClickBtn1() {
    const f = 'onClickBtn1';
    console.log(this.c, f);

    this.replayService.execAction(this.btn1Action);
  }

  onClickBtn2() {
    const f = 'onClickBtn2';
    console.log(this.c, f);

    this.replayService.execAction(this.btn2Action);
  }

}
