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
      , public status: boolean
    ){}
  }

/**
 * A model for an individual Scenario Card
 */
export class ScenarioCard {
    // name: string;
    // state: boolean;
    public scenarioSteps: ScenarioStep[] = [];
    constructor(
        public name: string
        , public state: boolean
    ){}
}