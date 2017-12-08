export class CardsSetting {
    
}

export enum CardExistsResult {
  UNKNOW = 0
  , CARD_NOT_FOUND = 1
  , CARD_FOUND = 2
}

export enum StepExistsResult {
  UNKNOW = 0
  , CARD_NOT_FOUND = 1
  , STEP_NOT_FOUND = 2
  , STEP_FOUND = 3
}

export enum CardExecType {
  START = 0
  , STOP = 1 
  , PAUSE = 2
  , UNKNOW = 3
}
