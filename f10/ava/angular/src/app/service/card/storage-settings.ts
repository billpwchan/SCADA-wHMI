export class StorageSettings {

    // Variable
    public static readonly STR_USE_LOCAL_STORAGE = 'use_local_storage';
    public static readonly STR_LOCAL_STORAGE_NAME = 'local_storage_name';

    public static readonly STR_REMOTE_URL = 'remote_url';
    public static readonly STR_REMOTE_FILENAME_PREFIX = 'remote_filename_prefix';
    public static readonly STR_REMOTE_FILENAME_EXTENSION = 'remote_filename_extension';

    public static readonly STR_UPLOAD_URL = 'upload_url';
    public static readonly STR_DOWNLOAD_URL = 'download_url';

    public static readonly STR_DOWNLOAD_METHOD = 'download_method';

    // Const Variable
    public static readonly STR_OPERATION = 'operation';
    public static readonly STR_PATH = 'path';
    public static readonly STR_DATA = 'data';

    public static readonly STR_GETFILE = 'getfile';
    public static readonly STR_GETFILELIST = 'getfilelist';
    public static readonly STR_POSTFILE = 'postfile';
    public static readonly STR_DELTEFILE = 'deletefile';

}

export enum StorageResponse {
  SAVE_SUCCESS = 0
  , SAVE_FAILED
  , LOAD_SUCCESS
  , LOAD_FAILED
}
