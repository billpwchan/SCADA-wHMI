import { Injectable } from '@angular/core';
import { StompRService, StompState } from '@stomp/ng2-stompjs';
import { Frame } from '@stomp/stompjs';
import { CookieService } from 'ngx-cookie-service';
import { Subscription } from 'rxjs/Subscription';
import { ConfigService } from './config.service';
import { ScsResponse } from './scsrequest';



@Injectable()
export class StompSubscriptionService {
  private cookieTokenValue = 'UNKNOWN';
  private cookieTokenExists = false;
  public currentSequence: number;
  sub: Subscription;
  private response_subscription: Subscription;


  constructor(private cookieService: CookieService,
    private stompService: StompRService,
    private configService: ConfigService,
  ) { }

  public initStomp() {
    const cookieName = (<any>window).token_cookie_name;
    this.cookieTokenExists = this.cookieService.check(cookieName);
    if (this.cookieTokenExists) {
      this.cookieTokenValue = this.cookieService.get(cookieName);
    }
    this.stompService.config = {
      // Which server?
      // url: 'ws://' + (<any>window).backend_endpoint + '/scswsgw',
      url: 'ws://' + this.configService.config.getIn(['backend_endpoint']) + '/scswsgw',
      // Headers
      // Typical keys: login, passcode, host
      headers: {
        // login: 'guest',  passcode: 'guest'
        'tokenKey': this.configService.config.getIn(['token_cookie_name'])
      },
      // How often to heartbeat?
      // Interval in milliseconds, set to 0 to disable
      heartbeat_in: 20000, // Typical value 0 - disabled
      heartbeat_out: 20000, // Typical value 20000 - every 20 seconds

      reconnect_delay: 5000, // Wait in milliseconds before attempting auto reconnect
      debug: false // Will log diagnostics on console
    };
    this.stompService.initAndConnect();
    return new Promise((resolve) => {
      this.stompService.state
        .map((state: number) => StompState[state])
        .subscribe((status: string) => {
          if (status === 'CONNECTED') {
            resolve(true);
          } else {
            console.log('Failed to connect');
            resolve(false);
          }
        });
    });
  }

  public onSubscribeResponse() {
    return new Promise((resolve) => {
      this.currentSequence = -1;
      const stomp_response_subscription = this.stompService.subscribe('/user/queue/scsresponse');
      resolve(stomp_response_subscription);
    });
  }

  public onSubscribeTopic(serverName, listName) {
    return new Promise((resolve) => {
      const stomp_response = this.stompService.subscribe('/topic/' + serverName + '.' + listName);
      resolve(stomp_response);
    });
  }

  public onSubscribeScsResponse(response_subscription, title) {
    return new Promise((resolve) => {
      if (title === 'sub') {
        this.sub = response_subscription.subscribe((stompframe: Frame) => {
          const o = JSON.parse(stompframe.body) as ScsResponse;
          resolve(o);
        });
      } else {
        this.response_subscription = response_subscription.subscribe((stompframe: Frame) => {
          const o = JSON.parse(stompframe.body) as ScsResponse;
          resolve(o);
        });
      }
    });
  }

  public unSubscribe() {
    if (this.sub != null) {
      this.sub.unsubscribe();
      this.sub = null;
    }
    if (this.response_subscription != null) {
      this.response_subscription.unsubscribe();
      this.response_subscription = null;
    }
  }

  public stompPublish(requestTitle, req) {
    this.stompService.publish(requestTitle, JSON.stringify(req));

  }

  public setCurrentSequence(curSeq: number) {
    this.currentSequence = curSeq;
  }

  public getCurrentSequence(): number {
    return this.currentSequence;
  }



}
