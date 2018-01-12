
export class DacSimSettings {
    public static readonly STR_URL_DACSIM_WRITEEXTVAR = '/scs/service/DacSimComponent/WriteExtVar?eivList=';
}

export enum DacSimExecType {
    START = 0
    , STOP
}

export class EIV {
    constructor(
        public name: string
        , public value: number
    ) {}
}

export enum ExecResult {
    INIT = 0
    , SENT = 1
    , FINISH = 2
    , FAILED = 3
}

export class DacSimExecution {
    constructor(
        public execType: DacSimExecType
        , public cardId: string
        , public stepId: number
        , public connAddr: string
        , public eivs: EIV[]
        , public ret: ExecResult
    ) {}
}
