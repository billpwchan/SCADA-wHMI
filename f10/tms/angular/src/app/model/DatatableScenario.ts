/**
 * A model for an ngx-datatable Scenario List
 */
export class DatatableCard {
  constructor(
    public name: string
    , public state: string
    , public updated: Date
  ) {}
}

/**
 * A model for an ngx-datatable Scenario Step List
 */
export class DatatableStep {
  constructor(
    public step: string
    , public env: string
    , public location: string
    , public system: string
    , public equipment: string
    , public point: string
    , public init: string
    , public value: string
    , public delay: string
    , public execute: boolean
    , public status: string
    , public num: string
    , public current: string
    , public updated: Date
  ) {}
}
