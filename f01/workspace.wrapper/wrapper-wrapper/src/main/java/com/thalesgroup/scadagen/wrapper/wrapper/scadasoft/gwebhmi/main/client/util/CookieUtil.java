package com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.util;

import java.util.Date;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;

public class CookieUtil {

    private static final String dualScreenChoiceCookieName_ = "MwtDualScreen";
    // Dual screen cookie persists 1 month (30*24*3600*1000 ms)
    private static final Long cookiePersistenceDuration_ = 2592000000L;

    /**
     * Enum used to read/write dual screen choice from a cookie.
     */
    public static enum DualScreenChoice {
        /** Guess what ... */
        SINGLE_SCREEN("1"), /** Guess what ... */
        DUAL_SCREEN("2"), /** Guess what ... */
        MOBILE_SCREEN("3");

        private String value_;

        DualScreenChoice(final String value) {
            value_ = value;
        }

        /**
         * Enable to know if a value match the dual screen choice.
         * 
         * @param value
         *            value toe check
         * @return true if the passed value match the dual screen choice, false
         *         otherwise.
         */
        public static boolean isDualScreen(final String value) {
            return DUAL_SCREEN.value_.equals(value);
        }

        /**
         * Enable to know if a value match the mobile choice.
         * 
         * @param value
         *            value toe check
         * @return true if the passed value match the mobile choice, false
         *         otherwise.
         */
        public static boolean isMobile(final String value) {
            return MOBILE_SCREEN.value_.equals(value);
        }

        /**
         * Returns the value associated to a dual screen choice.
         * 
         * @return result
         */
        public String getValue() {
            return value_;
        }
    };

    public static void setDualScreenCookie(final DualScreenChoice screenChoice) {
        final Date cookieExpireDate = new Date(System.currentTimeMillis() + cookiePersistenceDuration_);
        Window.Location.getHost();
        String cookieValue = DualScreenChoice.SINGLE_SCREEN.getValue();
        if (screenChoice == DualScreenChoice.DUAL_SCREEN) {
            cookieValue = DualScreenChoice.DUAL_SCREEN.getValue();
        } else if (screenChoice == DualScreenChoice.MOBILE_SCREEN) {
            cookieValue = DualScreenChoice.MOBILE_SCREEN.getValue();
        }
        Cookies.setCookie(dualScreenChoiceCookieName_, cookieValue, cookieExpireDate, null, "/", false);
    }

    /**
     * Enable to know if the HMI user set the dualscreen checkbox when he logged
     * in.
     *
     * @return true for dualscreen, false for single screen / no choice (ie no
     *         cookie found)
     */
    public static boolean isDualScreenSetFromCookie() {
        boolean ret = false;
        final String choice = Cookies.getCookie(dualScreenChoiceCookieName_);
        if (choice != null && DualScreenChoice.isDualScreen(choice)) {
            ret = true;
        }
        return ret;
    }

    /**
     * Enable to know if the HMI user set the dualscreen checkbox when he logged
     * in.
     *
     * @return true for dualscreen, false for single screen / no choice (ie no
     *         cookie found)
     */
    public static boolean isMobileScreenSetFromCookie() {
        boolean ret = false;
        final String choice = Cookies.getCookie(dualScreenChoiceCookieName_);
        if (choice != null && DualScreenChoice.isMobile(choice)) {
            ret = true;
        }
        return ret;
    }
}
