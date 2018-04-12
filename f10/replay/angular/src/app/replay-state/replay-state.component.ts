import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { TranslateService } from '@ngx-translate/core';
import { ReplayService, WorkingState } from '../service/replay.service';


@Component({
  selector: 'app-replay-state',
  templateUrl: './replay-state.component.html',
  styleUrls: ['./replay-state.component.css']
})



export class ReplayStateComponent implements OnInit {
  readonly c = 'ReplayStateComponent';

  public replayState: WorkingState;
  public replayStateStr = '';

  public replayReady = false;
  public replayRunning = false;

  constructor(
    private translate: TranslateService
    , private replayService: ReplayService
  ) {}

  ngOnInit() {
    const f = 'ngOnInit';
    this.getReplayState();
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
}
