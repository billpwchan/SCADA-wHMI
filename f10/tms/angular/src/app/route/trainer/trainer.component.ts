import { Component, OnInit } from '@angular/core';
import { StorageService } from '../../service/card/storage.service';

@Component({
  selector: 'app-trainer',
  templateUrl: './trainer.component.html',
  styleUrls: ['./trainer.component.css']
})
export class TrainerComponent implements OnInit {

  c: string = TrainerComponent.name;

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
