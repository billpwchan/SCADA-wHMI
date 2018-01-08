export class CardServiceSetting {

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

export enum CardServiceType {
  UNKNOW = 0
  , CARD_RELOADED
  , CARD_EDITED
  , CARD_UPDATED
  , STEP_RELOADED
  , STEP_EDITED
  , STEP_UPDATED
}
