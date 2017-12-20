import { Component, OnInit, Input } from '@angular/core';
import { AvaCond } from '../types/ava-cond';

@Component({
  selector: 'app-ava-cond-detail',
  templateUrl: './ava-cond-detail.component.html',
  styleUrls: ['./ava-cond-detail.component.css']
})
export class AvaCondDetailComponent implements OnInit {
  @Input() avaCond: AvaCond;

  constructor() { }

  ngOnInit() {
  }

}
