/**
 * A model for an individual Scenario Step
 */
export class ScenarioStep {
    // step: number;
    // location: number;
    // system: number;
    // equipment: string;
    // point: string;
    // value: number;
    // delay: number;
    // status: boolean;
    constructor(
      public step:number
      , public location
      , public system: number
      , public equipment: string
      , public point: string
      , public value: number
      , public delay: number
      , public status: number

      , public evenv: string
      , public evname: string
      , public evvalue: number
    ){}
  }

/**
 * A model for an individual Scenario Card
 */
export class ScenarioCard {
    // name: string;
    // state: boolean;
    public step: number;
    public steps: ScenarioStep[] = [];
    constructor(
        public name: string
        , public state: number
    ){}
}