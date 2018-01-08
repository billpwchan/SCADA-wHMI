/**
 * A model for an ngx-datatable Scenario List
 */
export class DatatableCard {
  constructor(
    public name: string
    , public state: string
    , public status: string
    , public updated: Date
  ) {}
}

/**
 * A model for an ngx-datatable Scenario Step List
 */
export class DatatableStep {
  constructor(
    public step: string
    , public location: string
    , public system: string
    , public equipment: string
    , public point: string
    , public value: string
    , public num: string
    , public updated: Date
  ) {}
}
