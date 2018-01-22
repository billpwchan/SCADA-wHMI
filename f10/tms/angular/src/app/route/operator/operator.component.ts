import { Component, OnInit } from '@angular/core';
import { StorageService } from '../../service/card/storage.service';

@Component({
  selector: 'app-operator',
  templateUrl: './operator.component.html',
  styleUrls: ['./operator.component.css']
})
export class OperatorComponent implements OnInit {

  c: string = OperatorComponent.name;

  notifyCards: string;
  notifyCardController: string;

  notifySteps: string;
  notifyStepController: string;

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
