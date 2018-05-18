window.SCADAGEN = window.SCADAGEN || {};
window.SCADAGEN.UIEVENTACTION = (function(){
	'use strict';

	return {
		'callJSByGWT': function(jsdata){
			if(!jsdata){
				console.error(
					'SCADAGEN.UIEVENTACTION.callJSByGWT',
					'Invalid data received'
				);
				return false;
			}
			if(
				window.SCADAGEN.PROJECT &&
				window.SCADAGEN.PROJECT.UIEVENTACTION &&
				window.SCADAGEN.PROJECT.UIEVENTACTION.callJSByGWT
			){
				// project specific handler is available
				console.log(
					'SCADAGEN.UIEVENTACTION.callJSByGWT',
					'Calling project specific handler with data: ' + jsdata
				);
				const handled = window.SCADAGEN.PROJECT.UIEVENTACTION.callJSByGWT(jsdata);
				if(handled){
					// handled by project specific handler
					return true;
				} // else... not handled by project specific handler, try to handle it myself
			} // else... project specific handler not available

			const json = JSON.parse(jsdata);
			console.log(
				'SCADAGEN.UIEVENTACTION.callJSByGWT',
				'json',
				json
			);

			console.log("json.OperationString2", json.OperationString2);
			if ( json.OperationString2 === "UIWidgetVerifyUIEventActionJSControl" ) {
				window.SCADAGEN.UIWidgetVerifyUIEventActionJSControl.js2gwt(JSON.parse(json.OperationString3));
				return true;
			} else if ( json.OperationString2 === "SessionStart" ) {
				window.SCADAGEN.KEEPALIVE.start(jsdata);
				return true;
			} else if ( json.OperationString2 === "SessionEnd" ) {
				window.SCADAGEN.KEEPALIVE.stop(jsdata);
				return true;
			} else if ( json.OperationString2 === "ScreenCapture" ) {
				// trigger the screen capture API call (assumed the browser extension was installed)
				thalesex_scrcap_capture();
				return true;
			} else if ( json.OperationString2 === "PrintCurPageCsv" ) {
				window.SCADAGEN.DOWNLOAD.Csv(jsdata);
				return true;
			}
			return false;
		}
	}
}());
