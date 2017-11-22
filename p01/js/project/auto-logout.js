window.SCADAGEN = window.SCADAGEN || {};
window.SCADAGEN.AUTOLOGOUT = (function(){
    'use strict';
    const localStorageId = 'SCADAGEN_AUTOLOGOUT';
    const idleWarningElementId = 'idle-warning';

    // Verbosity levels
    const LOG_ERR = 0;
    const LOG_INFO = 1;
    const LOG_DEBUG = 2;
    let verbosity = LOG_ERR;

    const defaultSettings = {'enabled': false, 'idleThresholdMs': 15 * 60 * 1000, 'warnThresholdMs': 10 * 60 * 1000};
    const settings = {};
    const copySettings = (src, dest, template) => {for (let i in template) {dest[i] = src[i];}};

    // auto logout functions
    let showIdleWarning = undefined;
    let removeIdleWarning = undefined;
    let logout = undefined;

    // detect user activities
    const activityMonitor = function(){
        let currentSettings = {};

        let checkIntervalMs = undefined;
        let lastActiveRef = undefined;
        let inWarning = false;
        let inIdleReach = false;

        let timer = undefined;
        let started = false;

        const checkIdle = () => {
            const curTimeRef = new Date();
            const diffTimeMs = curTimeRef - lastActiveRef;

            if (!inWarning) {
                if (diffTimeMs > currentSettings.warnThresholdMs) {
                    const nextTimeoutMs = Math.min(checkIntervalMs, currentSettings.idleThresholdMs - diffTimeMs);
                    console.log(
                        '[SCADAGEN_AUTOLOGOUT]',
                        '[checkIdle]',
                        'Reached warning threshold',
                        'curTimeRef:', curTimeRef,
                        'lastActiveRef:', lastActiveRef,
                        'diffTimeMs:', diffTimeMs,
                        'nextTimeoutMs:', nextTimeoutMs
                    );
                    inWarning = true;
                    timer = setTimeout(checkIdle, nextTimeoutMs);
                    if (undefined !== showIdleWarning) { showIdleWarning(); }
                } else {
                    // else... not yet reached warning threshold
                    const timeToWarnThresholdMs = currentSettings.warnThresholdMs - diffTimeMs;
                    const nextTimeoutMs = Math.min(checkIntervalMs, timeToWarnThresholdMs);
                    if (verbosity >= LOG_DEBUG) {
                        console.log(
                            '[SCADAGEN_AUTOLOGOUT]',
                            '[checkIdle]',
                            'Current idle statistcs',
                            'curTimeRef:', curTimeRef,
                            'lastActiveRef:', lastActiveRef,
                            'diffTimeMs:', diffTimeMs,
                            'nextTimeoutMs:', nextTimeoutMs,
                            'timeToWarnThresholdMs:', timeToWarnThresholdMs
                        );
                    }
                    timer = setTimeout(checkIdle, nextTimeoutMs);
                }
            } else {
                // else... already showing warning
                if (diffTimeMs > currentSettings.idleThresholdMs) {
                    console.log(
                        '[SCADAGEN_AUTOLOGOUT]',
                        '[checkIdle]',
                        'Reached idle threshold',
                        'curTimeRef:', curTimeRef,
                        'lastActiveRef:', lastActiveRef,
                        'diffTimeMs:' + diffTimeMs
                    );
                    inWarning = false;
                    inIdleReach = true;
                    timer = undefined;
                    logout();
                } else {
                    // else... warning is shown, but not yet reach the idle threshold
                    const timeToIdleThresholdMs = currentSettings.idleThresholdMs - diffTimeMs;
                    const nextTimeoutMs = Math.min(checkIntervalMs, timeToIdleThresholdMs);
                    if (verbosity >= LOG_INFO) {
                        console.log(
                            '[SCADAGEN_AUTOLOGOUT]',
                            '[checkIdle]',
                            'Current warning statistcs',
                            'curTimeRef:', curTimeRef,
                            'lastActiveRef:', lastActiveRef,
                            'diffTimeMs:', diffTimeMs,
                            'nextTimeoutMs:', nextTimeoutMs,
                            'timeToIdleThresholdMs:', timeToIdleThresholdMs
                        );
                    }
                    timer = setTimeout(checkIdle, nextTimeoutMs);
                }
            } // else... not yet shown the warning
        };
        const registerCallbacks = () => {
            let lastX = undefined;
            let lastY = undefined;
            const timerResetFunc = function() {
                if (inWarning) {
                    inWarning = false;
                    if (undefined !== timer) {
                        clearTimeout(timer); timer = undefined;
                        if (undefined !== removeIdleWarning) { removeIdleWarning(); }
                        timer = setTimeout(checkIdle, checkIntervalMs);
                    } // else... should not happen
                }
                lastActiveRef = new Date();
            };
            $(document).mousemove(function (e) {
                if (!started || inIdleReach) {return;}
                if (e.offsetX !== lastX || e.offsetY !== lastY) {
                    lastX = e.offsetX;
                    lastY = e.offsetY;
                    if (verbosity >= LOG_DEBUG) {console.log("[auto-logout] lastActiveRef reset to current time [mousemove]");}
                    timerResetFunc();
                } // else... mouse not moved
            });
            $(document).keypress(function (e) {
                if (!started || inIdleReach) {return;}
                if (verbosity >= LOG_DEBUG) {console.log("[auto-logout] lastActiveRef reset to current time [keypress]");}
                timerResetFunc();
            });
        };
        const unregisterCallbacks = () => {
            $(document).mousemove(undefined);
            $(document).keypress(undefined);
        };

        return {
            'start': (settings) => {
                if (started) { return false; }
                copySettings(settings, currentSettings, settings);
                checkIntervalMs = Math.min(currentSettings.idleThresholdMs, currentSettings.warnThresholdMs) / 2;
                console.log(
                    '[SCADAGEN_AUTOLOGOUT]',
                    '[activityMonitor]',
                    '[start]',
                    'checkIntervalMs:', checkIntervalMs
                );
                lastActiveRef = new Date();
                inWarning = inIdleReach = false;
                started = true;
                registerCallbacks();
                checkIdle();
                return true;
            },
            'stop': () => {
                if (!started) { return false; }
                started = false;
                unregisterCallbacks();
                if (undefined !== timer) {
                    clearTimeout(timer); timer = undefined;
                    if (inWarning) {removeIdleWarning();}
                    inWarning = inIdleReach = false;
                }
                currentSettings = {};
                checkIntervalMs = undefined;
                return true;
            }
        };
    }();

    // manage auto logout settings
    let settingTimer = undefined;
    const monitorSettings = () => {
        const rawSettings = localStorage[localStorageId];
        if (undefined === rawSettings) {
            if (verbosity >= LOG_DEBUG) { console.log('[SCADAGEN_AUTOLOGOUT]', '[monitorSettings]', 'No Auto Logout Configured'); }
        } else {
            const currentSettings = JSON.parse(rawSettings);
            let invalidSettings = false;
            let settingsChanged = false;
            for (var i in defaultSettings) {
                // check to ensure the parameters are all in correct types
                if (typeof(defaultSettings[i]) !== typeof(currentSettings[i])) {
                    invalidSettings = true;
                    console.error(
                        '[SCADAGEN_AUTOLOGOUT]',
                        '[monitorSettings]',
                        'Invalid setting:',
                        i, currentSettings[i], typeof(currentSettings[i]),
                        'rawSettings:', rawSettings,
                        'currentSettings:', currentSettings,
                        'defaultSettings:', defaultSettings
                    );
                    break;
                }
                if (settings[i] !== currentSettings[i]) {
                    if (verbosity >= LOG_INFO) {
                        console.log(
                            '[SCADAGEN_AUTOLOGOUT]',
                            '[monitorSettings]',
                            'Changed setting:',
                            i,
                            'Old Value:', settings[i],
                            'New Value:', currentSettings[i]
                        );
                    }
                    settingsChanged = true;
                }
            }
            if (!invalidSettings && settingsChanged) {
                copySettings(currentSettings, settings, defaultSettings);
                activityMonitor.stop();
                if (settings.enabled) {
                    if (verbosity >= LOG_INFO) {
                        console.log(
                            '[SCADAGEN_AUTOLOGOUT]',
                            '[monitorSettings]',
                            'Starting activityMonitor with settings:', settings
                        );
                    }
                    activityMonitor.start(settings);
                } else {
                    if (verbosity >= LOG_DEBUG) {
                        console.log(
                            '[SCADAGEN_AUTOLOGOUT]',
                            '[monitorSettings]',
                            'Auto Logout disabled'
                        );
                    }
                }
                return;
            } // else... keep current settings
        }
    };

    return {
        'start': (
            settingReloadIntervalMs,
            logoutFunc,
            showIdleFunc = undefined,
            removeIdleFunc = undefined,
            verbose = LOG_ERR
        ) => {
            logout = logoutFunc;
            showIdleWarning = showIdleFunc;
            removeIdleWarning = removeIdleFunc;
            verbosity = verbose;
            if (verbosity >= LOG_INFO) {console.log('[SCADAGEN_AUTOLOGOUT]', '[start]', 'Starting...');}
            if (undefined === settingTimer) {
                settingTimer = setInterval(monitorSettings, settingReloadIntervalMs);
                if (undefined === settingTimer) {
                    console.error('[SCADAGEN_AUTOLOGOUT]', '[start]', 'Failed in starting timer');
                    return false;
                }
                if (verbosity >= LOG_INFO) {console.log('[SCADAGEN_AUTOLOGOUT]', '[start]', 'Started');}
                return true;
            } else {
                console.error('[SCADAGEN_AUTOLOGOUT]', '[start]', 'settingTimer is already started');
                return false;
            }
        },
        'stop': () => {
            if (verbosity >= LOG_INFO) {console.log('[SCADAGEN_AUTOLOGOUT]', '[stop]', 'Stopping...');}
            if (undefined === settingTimer) {
                console.error('[SCADAGEN_AUTOLOGOUT]', '[stop]', 'settingTimer is not started.');
                return false;
            }
            clearInterval(settingTimer);
            settingTimer = undefined;
            if (verbosity >= LOG_INFO) {console.log('[SCADAGEN_AUTOLOGOUT]', '[stop]', 'Stopped.');}
            return true;
        }
    };
}());
