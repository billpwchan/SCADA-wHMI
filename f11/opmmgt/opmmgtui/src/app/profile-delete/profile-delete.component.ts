import { Component, OnInit } from '@angular/core';
import {OpmService} from "../opm.service";
import {Router, ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-profile-delete',
  templateUrl: './profile-delete.component.html',
  styleUrls: ['./profile-delete.component.css']
})
export class ProfileDeleteComponent implements OnInit {

  private errorMessage: string;

  constructor(private route: ActivatedRoute, private router: Router, private opmService: OpmService) {
    const id: number = route.snapshot.params['id'];
    this.opmService.deleteProfile(id).subscribe(
      res => this.router.navigate(['/profiles']),
      error => this.errorMessage = <any>error._body);
  }

  ngOnInit() {
    this.errorMessage = "";
  }

}
