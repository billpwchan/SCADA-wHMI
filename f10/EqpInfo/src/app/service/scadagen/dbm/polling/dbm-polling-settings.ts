import { Subscription } from 'rxjs/Subscription';

export class DbmPollingSettings {

  public static readonly STR_INTERVAL = 'interval';
}

export class DbmNotify {
  constructor(
    public connAddr: string
    , public key: string
  ) {}
}

export class EqpSubInfo {
  public classId: number;
  public unviname: string;
}

export class SubInfo {
  public timer: Subscription;
  public eqpSubInfo: EqpSubInfo[];
}

export enum DbmPollingServiceType {

}

export class DbmPollingCfg {
  public interval: number;
  public envs: any;
}
