import { Component, OnInit } from '@angular/core';
import {GridOptions} from "ag-grid/main";
import {OpmService} from "../opm.service";

@Component({
  selector: 'app-location-editor',
  templateUrl: './location-editor.component.html',
  styleUrls: ['./location-editor.component.css']
})
export class LocationEditorComponent implements OnInit {

  public errorMessage: string;

  ngOnInit() {
    this.errorMessage = "";
  }

  public gridOptions: GridOptions;

  constructor(private opmService: OpmService) {
    this.gridOptions = <GridOptions>{};
    this.gridOptions.columnDefs = this.createColumnDefs();
    this.getLocations();
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
      {headerName: "Category", field: "category", editable: true, width: 100},
      {headerName: "Description", field: "description", editable: true, width: 300}
    ];
  }

  private getLocations() {
    this.opmService.getLocations()
      .subscribe(
        locs => this.gridOptions.api.setRowData(locs),
        error => this.errorMessage = <any>error);
  }

  public onCellClicked(event): void {
    console.log('onCellClicked: ' + event.rowIndex + ' ' + event.colDef.field);
    if (event.colDef.headerName === '#') {
      if (event.data.id > 0) {
        this.opmService.deleteLocation(event.data.id).subscribe((res) => {
          this.getLocations();
        },
        error => this.errorMessage = <any>error._body);
    } else {
        this.opmService.createLocation(event.data).subscribe((res) => {
          this.getLocations();
        },
        error => this.errorMessage = <any>error._body);
      }
    }
  }

  public onRowValueChanged(event): void {
    console.log('onRowValueChanged: ' + event.data);
    if (event.data.id > 0) {
      this.opmService.updateLocation(event.data).subscribe((res) => {
        this.getLocations();
      },
      error => this.errorMessage = <any>error._body);
    } else {
      this.opmService.createLocation(event.data).subscribe((res) => {
        this.getLocations();
      },
      error => this.errorMessage = <any>error._body);
    }
  }
}
