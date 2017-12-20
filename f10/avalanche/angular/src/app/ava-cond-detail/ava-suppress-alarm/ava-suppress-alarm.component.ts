import { Component, OnInit, Input } from '@angular/core';
import { AvaCond } from '../../types/ava-cond';

@Component({
  selector: 'app-ava-suppress-alarm',
  templateUrl: './ava-suppress-alarm.component.html',
  styleUrls: ['./ava-suppress-alarm.component.css']
})
export class AvaSuppressAlarmComponent implements OnInit {
  @Input() avaCond: AvaCond;

  constructor() { }

  ngOnInit() {
  }

}
