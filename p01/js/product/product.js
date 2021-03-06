window.SCADAGEN_LOADER = window.SCADAGEN_LOADER || (function(){
	'use strict';

	return {
		start: function(){
			
			// initialize uieventaction.js
			const strJsUIEventAction = 'resources/js/product/uieventaction.js';
			console.log('Loading ', strJsUIEventAction);
			$.getScript(
				strJsUIEventAction,
				() => {
					console.log(strJsUIEventAction, 'Loaded');
					
					// initialize UIWidgetVerifyUIEventActionJSControl.js
					const strJsUIWidgetVerifyUIEventActionJSControl = 'resources/js/product/UIWidgetVerifyUIEventActionJSControl.js';
					console.log('Loading ', strJsUIWidgetVerifyUIEventActionJSControl);
					$.getScript(
						strJsUIWidgetVerifyUIEventActionJSControl,
						() => {
							console.log(strJsUIWidgetVerifyUIEventActionJSControl, 'Loaded');
						}
						,
						() => {
							console.log(strJsUIWidgetVerifyUIEventActionJSControl, 'Load Failed');
						}
					);	
					
					// initialize keep_alive.js
					const strJsKeepAlive = 'resources/js/product/keep_alive.js';
					console.log('Loading ', strJsKeepAlive);
					$.getScript(
						strJsKeepAlive,
						() => {
							console.log(strJsKeepAlive, 'Loaded');
						}
						,
						() => {
							console.log(strJsKeepAlive, 'Load Failed');
						}
					);
					
					// initialize thalesex_scrcap_ui.js
					const strJsScreenCapture = 'resources/js/product/thalesex_scrcap_ui.js';
					console.log('Loading ', strJsScreenCapture);
					$.getScript(
						strJsScreenCapture,
						() => {
							console.log(strJsScreenCapture, 'Loaded');
						}
						,
						() => {
							console.log(strJsScreenCapture, 'Load Failed');
						}
					);
					
					// initialize download_csv.js
					const strJsDownload = 'resources/js/product/download_csv.js';
					console.log('Loading ', strJsDownload);
					$.getScript(
						strJsDownload,
						() => {
							console.log(strJsDownload, 'Loaded');
						}
						,
						() => {
							console.log(strJsDownload, 'Load Failed');
						}
					);
				}
				,
				() => {
					console.log(strJsUIEventAction, 'Load Failed');
				}
			);
		}
	}
}());

window.SCADAGEN_LOADER.start();

