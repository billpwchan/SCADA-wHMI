import { Component, OnInit } from '@angular/core';
import { AvaCond } from '../types/ava-cond';
import { AVACONDS } from './mock-ava-cond';
import { AvaCondService } from '../services/ava-cond.service';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';

@Component({
  selector: 'app-ava-cond',
  templateUrl: './ava-cond.component.html',
  styleUrls: ['./ava-cond.component.css']
})
export class AvaCondComponent implements OnInit {
  public avaConds: AvaCond[];

  public selectedAvaCond: AvaCond = new AvaCond();

  public conditionNameTranslationPrefix = '';
  public enableStatusTranslationPrefix = '';
  public triggerStatusTranslationPrefix = '';

  constructor(private avaCondService: AvaCondService,
              public translate: TranslateService) {
                translate.onLangChange.subscribe((event: LangChangeEvent) => {
                  this.loadTranslations();
                });
              }

  ngOnInit() {
    this.getAvaConds();
  }

  getAvaConds(): void {
    this.avaCondService.getAvaConds().subscribe((values) => {
      this.avaConds = values;
    });
  }

  public loadTranslations() {
  }

  // ngx-datatable callback
  public onSelect(event) {
    console.log('{AvaCondComponent}', '[ngx-datatable onSelect]', 'Select Event', event);
    this.selectedAvaCond = event.selected[0];
  }

  // ngx-datatable callback
  public onActivate(event) {
    console.log('{AvaCondComponent}', '[ngx-datatable onActivate]', 'Activate Event', event);
  }

  // ngx-datatable callback
  public onPage(event: any) {
    console.log('{AvaCondComponent}', '[ngx-datatable onPage]', 'Page Event', event);
  }

  // ngx-datatable callback
  public onSort(event: any) {
    console.log('{AvaCondComponent}', '[ngx-datatable onSort]', 'Sort Event', event);

  }
}
