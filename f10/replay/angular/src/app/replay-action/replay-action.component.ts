import { Component, OnInit, Input } from '@angular/core';
import { Record } from '../model/record';
import { TranslateService } from '@ngx-translate/core';
import { SettingsService } from '../service/settings.service';
import { ReplayService, WorkingState } from '../service/replay.service';

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

  public replayState: WorkingState;
  public replayStateStr = '';

  public replayReady = false;
  public replayRunning = false;

  public currentSpeed = 1;

  constructor(
    private translate: TranslateService
    , private settingsService: SettingsService
    , private replayService: ReplayService
  ) { }

  ngOnInit() {
    const f = 'ngOnInit';
    this.speeds = this.settingsService.getSetting(this.c, f, this.c, this.STR_SPEEDS);

    if (this.speeds && this.speeds.length > 0) {
      this.currentSpeed = this.speeds[0];
    } else {
      this.currentSpeed = 1;
    }

    this.btn1Caption = this.settingsService.getSetting(this.c, f, this.c, this.STR_BTN1_CAPTION);
    this.btn2Caption = this.settingsService.getSetting(this.c, f, this.c, this.STR_BTN2_CAPTION);

    this.btn1Action = this.settingsService.getSetting(this.c, f, this.c, this.STR_BTN1_ACTION);
    this.btn2Action = this.settingsService.getSetting(this.c, f, this.c, this.STR_BTN2_ACTION);

    this.getReplayState();
  }

  onSpeedChange(speed: number) {
    const f = 'onSpeedChange';
    console.log(this.c, f);

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

  getReplayState(): void {
    const f = 'getReplayState';
    console.log(this.c, f);

    this.replayService.getReplayState().subscribe( state => {
      console.log(this.c, f, 'update state', state);
      this.replayState = state;
      this.replayStateStr = 'ReplayState_' + state;

      if (this.replayState === WorkingState.WAITFORINIT) {
        this.replayReady = false;
        this.replayRunning = false;
      } else if (this.replayState === WorkingState.READY) {
        this.replayReady = true;
        this.replayRunning = false;
      } else if (this.replayState === WorkingState.RUNNING || this.replayState === WorkingState.FREEZE) {
        this.replayReady = true;
        this.replayRunning = true;
      }
    });
  }

  getReplaySpeed(): void {
    const f = 'getReplaySpeed';
    console.log(this.c, f);

    this.replayService.getReplayState().subscribe( speed => {
      console.log(this.c, f, 'update speed', speed);
      this.currentSpeed = speed;
    });
  }
}
