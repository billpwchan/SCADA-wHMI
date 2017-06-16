window.SCADAGEN = window.SCADAGEN || {};
window.SCADAGEN.UIWidgetVerifyUIEventActionJSControl = (function(){
    'use strict';
	
	return {
		'js2gwt' : function (json) {

			var dumpmsg = "OperationAction: " + json.OperationAction
			+ " OperationType: " + json.OperationType
			+ " OperationString1: " + json.OperationString1
			+ " OperationString2: " + json.OperationString2;
			alert(dumpmsg);
			
			var jsonstring = JSON.stringify(json);

			window.SCADAGEN.UIEVENTACTION.callGWTByJS(jsonstring);
		}
	}
	
}());
