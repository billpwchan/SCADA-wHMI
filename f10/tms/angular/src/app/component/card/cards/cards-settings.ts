export class CardsSettings {

    public static readonly STR_CARD_DG_STATE_UNKNOW             = '&cards_dg_state_unknow';
    public static readonly STR_CARD_DG_STATE_STOPPED            = '&cards_dg_state_stopped';
    public static readonly STR_CARD_DG_STATE_STOPPED_PARTIAL    = '&cards_dg_state_stopped_partial';
    public static readonly STR_CARD_DG_STATE_STOP_RUNNING       = '&cards_dg_state_stop_running';
    public static readonly STR_CARD_DG_STATE_STOP_PAUSE         = '&cards_dg_state_stop_paused';
    public static readonly STR_CARD_DG_STATE_STOP_TERMINATED    = '&cards_dg_state_stop_terminated';
    public static readonly STR_CARD_DG_STATE_STARTED            = '&cards_dg_state_start';
    public static readonly STR_CARD_DG_STATE_STARTED_PARTIAL    = '&cards_dg_state_started_partial';
    public static readonly STR_CARD_DG_STATE_START_RUNNING      = '&cards_dg_state_started_running';
    public static readonly STR_CARD_DG_STATE_START_PAUSE        = '&cards_dg_state_start_paused';
    public static readonly STR_CARD_DG_STATE_START_TERMINATED   = '&cards_dg_state_start_terminated';

    public static readonly STR_CARD_DG_HEADER_SORTING_NON       = '&cards_dg_header_sorting_non';
    public static readonly STR_CARD_DG_HEADER_SORTING_ASC       = '&cards_dg_header_sorting_asc';
    public static readonly STR_CARD_DG_HEADER_SORTING_DES       = '&cards_dg_header_sorting_des';
}

export enum CardColumnIndex {
    NAME = 0
    , STATE
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
