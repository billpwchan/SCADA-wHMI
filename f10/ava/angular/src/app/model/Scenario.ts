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

export enum PhasesType {
  SINGLE_EV = 0
  , LENGTH
}

/**
 * A model for an individual Equipment
 */
export class Equipment {
  constructor(
    public connAddr: string
    , public univname: string
    , public fullpath: string
    , public classId: number
    , public geo: number
    , public func: number
    , public eqplabel: string
    , public pointlabel: string
    , public value: number
    , public valuelabel: string
  ) {}
}

/**
 * A model for an individual Step
 */
export class Step {
  public equipment: Equipment;
  constructor(
    public step: number
  ) {}
}

/**
 * A model for an individual Card
 */
export class Card {
  // Equipment Variable
  public steps: Step[] = new Array<Step>();
  public alarms: number[][];
  constructor(
    public univname: string
    , public index: number
    , public name: string
    , public state: boolean
    , public status: boolean
  ) {}
}
