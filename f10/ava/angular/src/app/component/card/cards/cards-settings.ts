export class CardsSettings {

    public static readonly STR_CARD_DG_STATE_DISABLED           = '&cards_dg_state_disabled';
    public static readonly STR_CARD_DG_STATE_ENABLED            = '&cards_dg_state_enabled';
    public static readonly STR_CARD_DG_STATUS_NOT_TRIGGERED     = '&cards_dg_status_not_triggered';
    public static readonly STR_CARD_DG_STATUS_TRIGGERED         = '&cards_dg_status_triggered';

    public static readonly STR_CARD_DG_HEADER_SORTING_NON       = '&cards_dg_header_sorting_non';
    public static readonly STR_CARD_DG_HEADER_SORTING_ASC       = '&cards_dg_header_sorting_asc';
    public static readonly STR_CARD_DG_HEADER_SORTING_DES       = '&cards_dg_header_sorting_des';
}

export enum CardColumnIndex {
    NAME = 0
    , STATE
    , STATUS
    , LENGTH
    , OUTOFRANGE
}

export enum SortingDirection {
    NON_ORDING = 0
    , ASC_ORDING
    , DES_ORDING
    , LENGTH
    , OUTOFRANGE
}
