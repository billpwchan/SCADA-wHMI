import { AppSettings } from '../../../app-settings';
import { Env } from '../alarm-summary/alarm-summary-settings';

export class MatrixSettings {

  public static readonly STR_MATRIX_CONFIG              = 'matrixConfig';

  public static readonly STR_ON_UPDATED_MATRIX          = 'onUpdateMatrix';

  public static readonly STR_SPREADSHEET_HEIGHT         = 'spreadsheet_height';
  public static readonly STR_SPREADSHEET_WIDTH          = 'spreadsheet_width';
  public static readonly STR_SPREADSHEET_VISIBLE_ROW    = 'spreadsheet_visible_rows';

  public static readonly STR_COL_HEADER_PREFIX          = 'col_header_prefix';
  public static readonly STR_COL_HEADER_IDS             = 'col_header_ids';
  public static readonly STR_COL_WIDTH                  = 'col_width';

  public static readonly STR_ROW_HEADER_PREFIX          = 'row_header_prefix';
  public static readonly STR_ROW_HEADER_IDS             = 'row_header_ids';
  public static readonly STR_ROW_HEADER_WIDTH           = 'row_header_width';

  public static readonly STR_DELFAULT_CELL_EMPTY        = '&matrix_default_cell_empty';
  public static readonly STR_DELFAULT_CELL_COMMA        = '&matrix_default_cell_comma';
  public static readonly STR_DELFAULT_CELL_INCORRECT    = '&matrix_default_cell_incorrect';

  public static readonly STR_MATRIXES                   = 'matrixes';

  public static readonly STR_SINGLE_BIT_ONLY            = 'single_bit_only';

  public static readonly STR_DEFAULT_VALUE              = 'default_value';

  public static readonly STR_CFG_ROW_HEADER_WIDTH_IS_NEGAVITE       = '&matrix_cfg_row_header_width_is_negavite';
  public static readonly STR_CFG_ROW_HEADER_ID_IS_NULL_OR_INVALID   = '&matrix_cfg_row_header_id_is_null_or_invalid';
  public static readonly STR_CFG_COL_WIDTH_IS_NEGAVITE              = '&matrix_cfg_col_width_is_negavite';
  public static readonly STR_CFG_COL_HEADER_ID_IS_NULL_OR_INVALID   = '&matrix_cfg_col_header_id_is_null_or_invalid';

  public static readonly STR_DATA_IS_NULL                     = '&matrix_data_is_null';
  public static readonly STR_DATA_LENGTH_IS_ZERO              = '&matrix_data_index_length_is_zero';
  public static readonly STR_DATA_INDEX_ZERO_IS_NULL          = '&matrix_data_index_zero_length_is_null';
  public static readonly STR_DATA_INDEX_ZERO_LENGTH_IS_ZERO   = '&matrix_data_index_zero_length_is_zero';
}

export class Selection {
  x: number;
  x2: number;
  y: number;
  y2: number;
}

export class Matrix {
  index: number;
  label: string;
  shortlabel: string;
  title: string;
}

export class MatrixConfig {
  public spreadsheetHeight: number;
  public spreadsheetWidth: number;
  public spreadsheetVisibleRows: number;
  public colHeaderPrefix: string;
  public colHeaderIds: number[];
  public colWidth: number;
  public rowHeaderPrefix: string;
  public rowHeaderIds: number[];
  public rowHeaderWidth: number;
  public defVal: number;
  public matrixes: Matrix[];
  public singleBitOnly: boolean;
  public envs: Env[];
}
