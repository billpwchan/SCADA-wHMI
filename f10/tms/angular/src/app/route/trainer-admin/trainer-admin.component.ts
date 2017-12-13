import { Component, OnInit } from '@angular/core';
import { StorageService } from '../../service/card/storage.service';
import { StepEditControllerSettings } from '../../component/step-edit-controller/step-edit-controller-settings';
import { StepEditSettings } from '../../component/step-edit/step-edit-settings';

@Component({
  selector: 'app-trainer-admin',
  templateUrl: './trainer-admin.component.html',
  styleUrls: ['./trainer-admin.component.css']
})
export class TrainerAdminComponent implements OnInit {

  c: string = TrainerAdminComponent.name;

  notifyCards: string;
  notifyCardController: string;
  notifyCardEdit: string;

  notifySteps: string;
  notifyStepController: string;
  notifyStepEdit: string;

  notifyStorage: string;
  notifyCsvInterpret: string;

  constructor(
    private storageService: StorageService
  ) { }

  ngOnInit(): void {
    const f = 'ngOnInit';
    console.log(this.c, f);

    this.storageService.loadCard();
  }

  getNotification(evt: string): void {
    const f = 'getNotification';
    // Do something with the notification (evt) sent by the child!
    console.log(this.c, f, 'evt', evt);

    switch ( evt ) {
      case StepEditControllerSettings.STR_STEP_EDIT_ENABLE: {
        this.notifyStepEdit = StepEditSettings.STR_STEP_EDIT_ENABLE;
      } break;
    }
  }

}
