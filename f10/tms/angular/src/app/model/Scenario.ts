import { Subscription } from "rxjs/Subscription";


export enum ExecType {
  DACSIM = 0
  , UNKNOW
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
  START = 0
  , STOP 
  , UNKNOW
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
  ) {}
}

export enum StepType {
  STOP = 0
  , START = 1
  , STOP_RUNNING = 2
  , START_RUNNING = 3
  , STOP_FAILED = 4
  , START_FAILED = 5
  , UNKNOW = 6
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
  STOP = 0
  , START = 1
  , STOP_RUNNING = 2
  , START_RUNNING = 3
  , START_PAUSE = 4
  , STOP_PAUSE = 5
  , UNKNOW = 6
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
