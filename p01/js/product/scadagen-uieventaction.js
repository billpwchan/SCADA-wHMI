window.SCADAGEN = window.SCADAGEN || {};
window.SCADAGEN.UIEVENTACTION = (function(){
    'use strict';

	return {
		'callJSByGWT' : function(jsdata) {
			var json = JSON.parse(jsdata);
			// If 'callJSByGWT' called by UIEventAction, json.OperationString1 is already "CallJSByGWT"
			//if ( json.OperationString1 === "CallJSByGWT" ) {
				console.log("json.OperationString2", json.OperationString2);
				if ( json.OperationString2 === "UIWidgetVerifyUIEventActionJSControl" ) {
					
					window.SCADAGEN.UIWidgetVerifyUIEventActionJSControl.js2gwt(JSON.parse(json.OperationString3));
					
				} else if ( json.OperationString2 === "SessionStart" ) {
					
					console.log(jsdata);
					
				}
			//}
		}
	}
	
}());
