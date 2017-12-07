/**
 * A model for an ngx-datatable Scenario List
 */
export class DatatableScenarioCard {
  constructor(
    public name: string
    , public state: string
  ) {}
}

/**
 * A model for an ngx-datatable Scenario Step List
 */
export class DatatableScenarioStep {
  constructor(
    public step: string
    , public location: string
    , public system: string
    , public equipment: string
    , public point: string
    , public value: string
    , public delay: string
    , public status: string
  ) {}
}
