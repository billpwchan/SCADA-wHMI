import { Component, OnInit, Input } from '@angular/core';
import { AvaCond } from '../../types/ava-cond';

@Component({
  selector: 'app-ava-trigger-cond',
  templateUrl: './ava-trigger-cond.component.html',
  styleUrls: ['./ava-trigger-cond.component.css']
})
export class AvaTriggerCondComponent implements OnInit {
  @Input() avaCond: AvaCond;

  constructor() { }

  ngOnInit() {
  }

}
