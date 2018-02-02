import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-refresh',
  templateUrl: './refresh.component.html',
  styleUrls: ['./refresh.component.css']
})
export class RefreshComponent implements OnInit {

  readonly c = 'RefreshComponentComponent';

  btnEnableRefresh: boolean;

  constructor() { }

  ngOnInit() {
    const f = 'ngOnInit';
    console.log(this.c, f);
    this.btnEnableRefresh = true;
  }

  btnClicked(btnLabel: string, event?: Event) {
    const f = 'btnClicked';
    console.log(this.c, f);
    console.log(this.c, f, 'btnLabel[' + btnLabel + ']');
    switch (btnLabel) {
      case 'refresh': {
        window.location.reload();
      } break;
    }
  }

}
