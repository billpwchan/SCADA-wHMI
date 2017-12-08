import { Subscription } from "rxjs/Subscription";

/**
 * A model for an individual Value
 */
export class Value {
  constructor(
    public stop: number
    , public start: number
  ) {}
}

/**
 * A model for an individual EV
 */
export class EV {
  constructor(
    public name: string
    , public value: Value
  ) {}
}

/**
 * A model for an individual Equipment
 */
export class Equipment {
  public ev: EV[] = new Array<EV>();
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
  stop = 0
  , start = 1
  , stop_running = 2
  , start_running = 3
  , stop_failed = 4
  , start_failed = 5
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

/**
 * A model for an individual Card
 */
export class Card {
  // Equipment Variable
  public steps: Step[] = new Array<Step>();
  public timer: Subscription = null;
  constructor(
    public name: string
    , public state: number
    , public step: number
    , public type: number
  ) {}
}
