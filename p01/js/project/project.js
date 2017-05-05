"use strict";

window.uieventaction = {
	callJSByGWT : function(jsdata) {
		var json = JSON.parse(jsdata);
		if ( json.OperationString1 == "CallJSByGWT" ) {
			var json2 = JSON.parse(json.OperationString2);
			js2gwt(json2);
		}
	}
}

function js2gwt(json) {

	var dumpmsg = "OperationAction: " + json.OperationAction
	+ " OperationType: " + json.OperationType
	+ " OperationString1: " + json.OperationString1
	+ " OperationString2: " + json.OperationString2;
	alert(dumpmsg);
	
	var jsonstring = JSON.stringify(json);

	window.callGWTByJS(jsonstring);
}

function getFunctionName() {
	const caller = (new Error().stack).split('\n')[2].split(' ')[5];
	return caller;
}

function getCurrentTimeString() {
	const curTime = new Date();
	return '' + 
		curTime.getFullYear() + '-' + (curTime.getMonth() + 1) + '-' + curTime.getDate() + ' ' +
		curTime.getHours() + ':' + curTime.getMinutes() + ':' + curTime.getSeconds() + '.' +
		curTime.getMilliseconds();
}

function periodicStyleProperties(properties, verbose = false) {
	const funcName = '[' + getFunctionName() + ']';
	const count = properties.length;
	const propertyNames = [];
	const propertyTimeoutMs = [];
	const propertyValues = [];

	if (verbose) {
		console.debug(
			getCurrentTimeString(),
			funcName,
			'Property count:',
			count
		);
	}

	try {
		for (var i = 0; i < count; ++i) {
			const property = properties[i];
			propertyNames[i] = property[0];
			propertyTimeoutMs[i] = property[1];
			propertyValues[i] = ((property.length >= 3) ? property[2]:'none');
			if (verbose) {
				console.debug(
					getCurrentTimeString(),
					funcName,
					'[' + i +']',
					propertyNames[i] + ':',
					propertyTimeoutMs[i],
					'[' + propertyValues[i] + ']'
				);
			}
		}
	} catch (err) {
		console.error(
			getCurrentTimeString(),
			funcName,
			'Error in parsing parameter:',
			err
		);
		return null;
	}

	const set = function(name, value = '') {
		if (verbose) {
			console.debug(
				getCurrentTimeString(),
				funcName,
				'Setting style property:',
				'"' + name + '"',
				'->',
				'"' + value + '"'
			);
		}
		try {
			document.body.style.setProperty(name, value);
		} catch (err) {
			console.warn(
				getCurrentTimeString(),
				funcName,
				'Error in setting style property:',
				err
			);
		}
	}

	var index = 0;
	const iterator = function() {
		const name = propertyNames[index];
		const timeoutMs = propertyTimeoutMs[index];
		const value = propertyValues[index];
		index = (index + 1) % count;

		set(name, value);
		setTimeout(
			function() {
				set(name, '');
				iterator();
			},
			timeoutMs
		);
	}
	return iterator;
}

const gdgBlinkProperties = [];
gdgBlinkProperties[gdgBlinkProperties.length] = ['--gdg-blinking-off', 1000];
gdgBlinkProperties[gdgBlinkProperties.length] = ['--gdg-blinking-off', 1000, ''];
var gdgBlink = periodicStyleProperties(gdgBlinkProperties);
document.addEventListener('DOMContentLoaded', gdgBlink, false);

const symbolBlinkProperties = [];
symbolBlinkProperties[symbolBlinkProperties.length] = ['--symbol-blinking-off', 500];
symbolBlinkProperties[symbolBlinkProperties.length] = ['--symbol-blinking-off', 500, ''];
var symbolBlink = periodicStyleProperties(symbolBlinkProperties);
document.addEventListener('DOMContentLoaded', symbolBlink, false);