export class ImportExportSettings {

    public static readonly STR_EXPORT_SELECTION_ONLY = 'export_selection_only';

    public static readonly STR_IMPORT_TYPE = 'import_type';
    public static readonly STR_IMPORT_ACCEPT = 'import_accept';

    public static readonly STR_EXPORT_TYPE = 'export_type';
    public static readonly STR_EXPORT_TYPE_OPT = 'export_type_opt';

    public static readonly STR_EXPORT_FILENAME = 'export_filename';

    public static readonly STR_EXPORT_CARD_NAME = 'export_filename_by_first_card_name';
    public static readonly STR_EXPORT_FILENAME_PREFIX = 'export_filename_prefix';
    public static readonly STR_EXPORT_FILENAME_EXTENSION = 'export_filename_extension';

    public static readonly STR_CSV_COMMA = 'csv_comma';
    public static readonly STR_CSV_QUOTE = 'csv_quote';
    public static readonly STR_CSV_EOL = 'csv_eol';

    public static readonly STR_INIT_CARDS_BEFORE_EXPORT = 'init_cards_before_export';
}

export enum ImportFileType {
    UNKNOW = 0
    , CSV
    , JSON
}

export enum ExportFileType {
    UNKNOW = 0
    , CSV
    , JSON
}
