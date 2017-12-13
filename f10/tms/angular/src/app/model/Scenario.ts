import { Subscription } from 'rxjs/Subscription';


export enum ExecType {
  UNKNOW = 0
  , DACSIM
}

/**
 * A model for an individual EV
 */
export class Execution {
  constructor(
    public execType: ExecType
    , public name: string
    , public value: number
  ) {}
}

export enum PhaseType {
  UNKNOW = 0
  , START
  , STOP
}

/**
 * A model for an individual Equipment
 */
export class Equipment {
  public phaseStop: Execution[] = new Array<Execution>();
  public phaseStart: Execution[] = new Array<Execution>();
  constructor(
    public connAddr: string
    , public univname: string
    , public classId: number
    , public geo: number
    , public func: number
    , public eqplabel: string
    , public pointlabel: string
    , public valuelabel: string
  ) {}
}

export enum StepType {
  UNKNOW = 0
  , STOP
  , START
  , STOP_RUNNING
  , START_RUNNING
  , STOP_FAILED
  , START_FAILED
}

/**
 * A model for an individual Step
 */
export class Step {
  constructor(
    public step: number
    , public state: StepType
    , public delay: number
    , public equipment: Equipment
  ) {}
}

export enum CardType {
  UNKNOW = 0
  , STOP
  , START
  , STOP_RUNNING
  , START_RUNNING
  , START_PAUSE
  , STOP_PAUSE
}

/**
 * A model for an individual Card
 */
export class Card {
  // Equipment Variable
  public steps: Step[] = new Array<Step>();
  public timer: Subscription = null;
  constructor(
    public name: string
    , public state: CardType
    , public step: number
  ) {}
}
