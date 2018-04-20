import { Component, OnInit } from '@angular/core';
import {GridOptions} from "ag-grid/main";
import {OpmService} from "../opm.service";

@Component({
  selector: 'app-action-editor',
  templateUrl: './action-editor.component.html',
  styleUrls: ['./action-editor.component.css']
})
export class ActionEditorComponent implements OnInit {

  public errorMessage: string;

  ngOnInit() {
    this.errorMessage = "";
  }

  public gridOptions: GridOptions;

  constructor(private opmService: OpmService) {
    this.gridOptions = <GridOptions>{};
    this.gridOptions.columnDefs = this.createColumnDefs();
    this.getActions();
  }

  private createColumnDefs() {
    return [
      {
        headerName: '#', width: 60, suppressSorting: true,
        suppressMenu: true, pinned: true, editable:false, field: "id",
        cellRenderer: function(params) {
          var eDiv = document.createElement('div');
          if (params.data.id > 0) {
            eDiv.innerHTML = '<button class="btn btn-xs btn-primary">Del</button>';
          } else {
            eDiv.innerHTML = '<button class="btn btn-xs btn-primary">Add</button>';
          }
          return eDiv;
        }
      },
      {headerName: "ID", field: "id", width: 60},
      {headerName: "Name", field: "name", editable: true, width: 100},
      {headerName: "Category", cellEditor: 'select', cellEditorParams:{values:[1,2,3,4,5,6,7,8]}, field: "category", editable: true, width: 100},
      {headerName: "Abbrev", field: "abbrev", editable: true, width: 100},
      {headerName: "Description", field: "description", editable: true, width: 300}
    ];
  }

  private getActions() {
    this.opmService.getActions()
      .subscribe(
        fcts => this.gridOptions.api.setRowData(fcts),
        error => this.errorMessage = <any>error);
  }

  public onCellClicked(event): void {
    console.log('onCellClicked: ' + event.rowIndex + ' ' + event.colDef.field);
    if (event.colDef.headerName === '#') {
      if (event.data.id > 0) {
        this.opmService.deleteAction(event.data.id).subscribe((res) => {
          this.getActions();
        },
          error => this.errorMessage = <any>error);
      } else {
        this.opmService.createAction(event.data).subscribe((res) => {
          this.getActions();
        },
          error => this.errorMessage = <any>error);
      }
    }
  }

  public onRowValueChanged(event): void {
    console.log('onRowValueChanged: ' + event.data);
    if (event.data.id > 0) {
      this.opmService.updateAction(event.data).subscribe((res) => {
        this.getActions();
      },
        error => this.errorMessage = <any>error);
    } else {
      this.opmService.createAction(event.data).subscribe((res) => {
        this.getActions();
      },
        error => this.errorMessage = <any>error);
    }
  }
}
