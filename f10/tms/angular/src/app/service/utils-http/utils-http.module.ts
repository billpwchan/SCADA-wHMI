import { NgModule, Injectable } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';

// @Injectable()

@NgModule({
  imports: [CommonModule],
  declarations: []
})

export class UtilsHttpModule {

  readonly c = 'UtilsHttpModule';

  httpClientHandlerError(func: string, err: HttpErrorResponse, mgs?: string): void {
    const f = 'httpClientHandlerError';
    console.warn(this.c, f, 'call from', func, 'error', err);
    console.warn(this.c, f, 'call from', func, 'error.error', err.error);
    if (err.error instanceof Error) {
      console.warn(this.c, f, 'call from', func, 'Client-side error occured.');
    } else {
      console.warn(this.c, f, 'call from', func, 'Server-side error occured.');
    }
  }

  httpClientHandlerComplete(func: string, msg?: string): void {
    const f = 'httpClientHandlerComplete';
    console.log(this.c, f, 'call from', func, 'Complete msg', msg);
  }
 }
