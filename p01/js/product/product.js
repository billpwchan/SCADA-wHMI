window.SCADAGEN_LOADER = window.SCADAGEN_LOADER || (function(){
	'use strict';

	return {
		start: function(){
			
			// initialize 
			
			
			const strJsSCADAgenUIWidgetVerifyUIEventActionJSControl = 'resources/js/product/scadagen-UIWidgetVerifyUIEventActionJSControl.js';
			console.log('Loading ', strJsSCADAgenUIWidgetVerifyUIEventActionJSControl);
			$.getScript(
				strJsSCADAgenUIWidgetVerifyUIEventActionJSControl,
				() => {
					console.log(strJsSCADAgenUIWidgetVerifyUIEventActionJSControl, 'Loaded');
				}
				,
				() => {
					console.log(strJsSCADAgenUIWidgetVerifyUIEventActionJSControl, 'Load Failed');
				}
			);	
			
			const strJsSCADAgenUIEventAction = 'resources/js/product/scadagen-uieventaction.js';
			console.log('Loading ', strJsSCADAgenUIEventAction);
			$.getScript(
				strJsSCADAgenUIEventAction,
				() => {
					$(document).ready(() => {
						console.log(strJsSCADAgenUIEventAction, 'Loaded');
						/*
						window.SCADAGEN.SYNCBLINKING.start(
							gdgBlinkProperties,
							syncblinking_verbosity
						);
						window.SCADAGEN.SYNCBLINKING.start(
							symbolBlinkProperties,
							syncblinking_verbosity
						);
						*/
					});
				}

			);
			
		
			

		}
	}
}());

window.SCADAGEN_LOADER.start();

