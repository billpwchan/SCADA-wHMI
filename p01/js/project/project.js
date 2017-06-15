window.EAL_PROJECT = window.EAL_PROJECT || (function(){
	'use strict';
	// auto logout settings
	const autologout = {
		'param': {
			'settingReloadIntervalMs': 5 * 1000,
			'verbosity': 1,
			'idleWarningElementId': 'idle-warning'
		},
		'action': {
			'showIdleWarning': () => {
				console.log('[EAL_PROJECT]', '[showIdleWarning]');
				const divWarn = document.createElement('div');
				divWarn.id = autologout.param.idleWarningElementId;
				divWarn.innerHTML =
					'<div class="gwt-PopupPanel infoDialog-glass"></div>' +
					'<div class="infoDialog-panel" style="left: 815px; top: 451px; visibility: visible; position: absolute; overflow: visible;">' +
						'<div class="popupContent">' +
							'<table cellspacing="0" cellpadding="0">' +
								'<tbody>' +
									'<tr><td align="left" style="vertical-align: top;"><div class="gwt-Label header-label">HMI Idle</div></td></tr>' +
									'<tr><td align="left" style="vertical-align: top;"><div class="gwt-HTML message">HMI is idling and will soon be automatically logged out. Move the mouse to resume.</div></td></tr>' +
								'</tbody>' +
							'</table>' +
						'</div>' +
					'</div>';
				document.body.appendChild(divWarn);
			},
			'removeIdleWarning': () => {
				console.log('[EAL_PROJECT]', '[removeIdleWarning]');
				const element = document.getElementById(autologout.param.idleWarningElementId);
				element.parentNode.removeChild(element);
			},
			'logout': () => {
				const j2gData = {
					  'OperationType': 'action'
					, 'OperationAction': 'opm'
					, 'OperationString1': 'OpmLogout'
					, 'OperationString2': 'UIOpmSCADAgen'
				};
				const j2gString = JSON.stringify(j2gData);
				console.log('[EAL_PROJECT]', '[logout]', j2gString);
				window.callGWTByJS(j2gString);
			}
		}
	};

	// sync. blinking settings
	const gdgBlinkProperties = [];
	gdgBlinkProperties[gdgBlinkProperties.length] = ['--gdg-blinking-off', 1000];
	gdgBlinkProperties[gdgBlinkProperties.length] = ['--gdg-blinking-off', 1000, ''];

	const symbolBlinkProperties = [];
	symbolBlinkProperties[symbolBlinkProperties.length] = ['--symbol-blinking-off', 500];
	symbolBlinkProperties[symbolBlinkProperties.length] = ['--symbol-blinking-off', 500, ''];
	const syncblinking_verbosity = 1;

	return {
		start: function(){
			// initialize sync. blinking
			$.getScript(
				'resources/js/project/sync-blinking.js',
				() => {
					$(document).ready(() => {
						window.SCADAGEN.SYNCBLINKING.start(
							gdgBlinkProperties,
							syncblinking_verbosity
						);
						window.SCADAGEN.SYNCBLINKING.start(
							symbolBlinkProperties,
							syncblinking_verbosity
						);
					});
				}
			);

			// initialize auto logout monitoring
			$.getScript(
				'resources/js/project/auto-logout.js',
				() => {
					$(document).ready(
						window.SCADAGEN.AUTOLOGOUT.start(
							autologout.param.settingReloadIntervalMs,
							autologout.action.logout,
							autologout.action.showIdleWarning,
							autologout.action.removeIdleWarning,
							autologout.param.verbosity
						)
					);
				}
			);
		}
	}
}());

window.EAL_PROJECT.start();

