## README for RTT Configuration
-  **"backend_endpoint"**: "127.0.0.1:12443",     *Sample: ws://stomp endpoint/scswsgw*
-  **"token_cookie_name"**: "mwt.app.Token",
-  **"scs_ols_url"**: "http://localhost:12443/scs/service/OlsComponent/",
-  **"i18n"**: {
    "default_lang": "en-US",  *Change this value to manually adjust translation language*
    "resolve_by_browser_lang": true,  
    "use_culture_lang": true,
    "resolve_by_browser_cookie": false,
    "use_cookie_name": "iscs.webapp.Lang"
  },
-  **"rtt_url"**: "http://localhost:8090/rttapp/",  *Legacy mode only (RTTApp)*
-  **"env"**: "OCC",  *legacy mode only (RTTApp)*
-  **"serverName"**: "HisServer", 
-  **"xaxisLabel"**: "Time",  *xAxis label for both chart libs*
-  **"his_time"**: 30,  *Maximum duration of historical data retrieved (mins)*
-  **"time_format"**: "%Y-%m-%d %H:%M:%S",  *time format setting*
-  **"timezone_offset"**: -8,     *Highstock timezone offset setting*
-  **"max_point_allowed"**: 7,    *Maximum allowed # points for plotting*
-  **"legacy_mode"**: false,  *Enable/Disable legacy mode (RTTApp)*
-  **"dev_mode"**: boolean     *Allow to show Ag-Grid for debug/dev purpose in the chart webpage*
-  **"chart_lib"**: number       *1=>HighStock; 2=>3JS*
-  **"highstock_refreshRate"**: 500,  *Refresh rate for Highstock Graph (ms)*
-  **"c3_refreshRate"**: 1000,  *Refresh rate for c3JS Graph (ms)*
-  **"c3_maxPoint_unload"**: 20,  *Maximum allowed points on the graph until a forced graph refresh*
-  **"c3_modal_timeout"**: 10,  *C3JS Timeout settings (Refer to C3JS documentation)*
-  **"max_duration"**: [5, 0, 0], *Maximum allowed duration for charts*
-  **"highstock_chartSize"**: "44%",
-  **"background_color"**: "#FFFFFF",
-  **"highstock_chartSize"**: "41%",  *The percentage of height of width*
-  **"configurable_Label"**: number[]  *http://lxdv16.hk.thales:23000/scs/service/OlsComponent/ReadOlsList?listServer=HisServer&listName=TECSTemperature*
-  **"legend_separator"**: string *Separator for separating configurable_label*
-  **"color_list"**: ["#ff0000", "#002aff", "#9400ff", "#00fff6", ...],   *Default color list for user selected equipment*
-  **"col_idx_1":** "3", *Filter column selection. Based on http://lxdv16.hk.thales:23000/scs/service/OlsComponent/ReadOlsList?listServer=HisServer&listName=TECSTemperature.*
-  **"col_idx_2":** "4",
-  **"col_idx_3":** "5",
-  **"col_idx_4":** "7",
-  **"col_idx_5":** "8",


####Put all translation for Ag-grid content under this line