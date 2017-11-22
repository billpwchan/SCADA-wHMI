window.SCADAGEN = window.SCADAGEN || {};
window.SCADAGEN.KEEPALIVE = (function(){
    'use strict';

	return {
		'start' : function(jsdata) {
			
			console.log('window.SCADAGEN.KEEPALIVE jsdata', jsdata);
			
			var json = JSON.parse(jsdata);
			
			var x = setInterval(function(){
				
				console.log('window.SCADAGEN.KEEPALIVE Date', new Date().toLocaleTimeString());
				console.log('window.SCADAGEN.KEEPALIVE json.OperationString5', json.OperationString5);
				console.log('window.SCADAGEN.KEEPALIVE json.OperationString6', json.OperationString6);
				console.log('window.SCADAGEN.KEEPALIVE json.OperationString7', json.OperationString7);
				console.log('window.SCADAGEN.KEEPALIVE json.OperationString8', json.OperationString8);
			
				var myObject = {};
				myObject.OperationType = 'action';
				myObject.OperationAction = 'dbm';
				myObject.OperationString1 = 'WriteStringValue';
				myObject.OperationString2 = json.OperationString5;
				myObject.OperationString3 = json.OperationString6;
				myObject.OperationString4 = json.OperationString7;
				
				var jsonstring = JSON.stringify(myObject);
				
				console.log('window.SCADAGEN.KEEPALIVE jsonstring', jsonstring);

				window.SCADAGEN.UIEVENTACTION.callGWTByJS(jsonstring);

			}, json.OperationString8);

		}

	}
	
}());
