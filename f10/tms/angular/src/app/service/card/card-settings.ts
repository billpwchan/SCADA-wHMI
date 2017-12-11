export class CardsSetting {
    
}

export enum CardExistsResult {
  UNKNOW = 0
  , CARD_NOT_FOUND
  , CARD_FOUND
}

export enum StepExistsResult {
  UNKNOW = 0
  , CARD_NOT_FOUND
  , STEP_NOT_FOUND
  , STEP_FOUND
}

export enum CardExecType {
  START = 0
  , STOP
  , PAUSE
  , RESUME
  , UNKNOW
}

export enum CardServiceType {
  CARD_RELOADED = 0
  , CARD_UPDATED
  , STEP_RELOADED
  , STEP_UPDATED
  , UNKNOW
}
