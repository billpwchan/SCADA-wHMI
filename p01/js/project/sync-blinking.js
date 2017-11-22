window.SCADAGEN = window.SCADAGEN || {};
window.SCADAGEN.SYNCBLINKING = (function(){
	'use strict';
	const funcName = '[SCADAGEN_SYNCBLINKING]';

	// Verbosity levels
	const LOG_ERR = 0;
	const LOG_INFO = 1;
	const LOG_DEBUG = 2;

	return {
		start: (properties, verbosity = LOG_ERR) => {
			const count = properties.length;
			const propertyNames = [];
			const propertyTimeoutMs = [];
			const propertyValues = [];

			if (verbosity >= LOG_DEBUG) {
				console.debug(funcName, 'Property count:', count);
			}

			try {
				for (let i = 0; i < count; ++i) {
					const property = properties[i];
					propertyNames[i] = property[0];
					propertyTimeoutMs[i] = property[1];
					propertyValues[i] = ((property.length >= 3) ? property[2]:'none');
					if (verbosity >= LOG_DEBUG) {
						console.debug(
							funcName,
							'[' + i +']',
							propertyNames[i] + ':',
							propertyTimeoutMs[i],
							'[' + propertyValues[i] + ']'
						);
					}
				}
			} catch (err) {
				if (verbosity >= LOG_ERR) {
					console.error(
						funcName,
						'Error in parsing parameter:',
						err
					);
				}
				return false;
			}

			const set = function(name, value = '') {
				if (verbosity >= LOG_DEBUG) {
					console.debug(
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
					if (verbosity >= LOG_ERR) {
						console.error(
							funcName,
							'Error in setting style property:',
							err
						);
					}
					return false;
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
			iterator();
			return true;
		}
	};
}());
