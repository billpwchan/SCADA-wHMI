import { Component, OnInit } from '@angular/core';
import { StorageService } from '../../service/card/storage.service';

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

  nodifySteps: string;
  notifyStepController: string;
  nodifyStepEdit: string;

  notifyStorage: string;
  notifyCsvInterpret: string;

  constructor(
    private storageService: StorageService
  ) { }

  ngOnInit() {
    this.storageService.loadCard();
  }

  getNotification(evt) {
    const f = 'getNotification';
    // Do something with the notification (evt) sent by the child!
    console.log(this.c, f, 'evt', evt);
  }

}
