/**
 * A model for an individual Scenario Step
 */
export class ScenarioStep {
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
    public step: number;
    public steps: ScenarioStep[] = [];
    constructor(
        public name: string
        , public state: number
    ){}
}