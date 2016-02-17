package com.thalesgroup.scadasoft.gwebhmi.security.client;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.admin.client.model.benchmark.BenchmarkAnnotation;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.Constants;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.security.AuthenticationError;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.component.Button;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.scadasoft.gwebhmi.security.client.CookieUtil.DualScreenChoice;

public class ScsLoginPanel extends Composite {

    // URL used by spring security to check login credentials */
    private static final String SPRING_SEC_PROCESSING_URL = "j_spring_security_check";

    private final boolean handleErrorMsg_;

    /**
     * Callback to be executed when the user clicks on the login button
     */
    public interface LoginCallback {
        /**
         * executed when the user clicks on the login button
         * 
         * @param userName
         *            User name
         * @param password
         *            Password
         * @param isDualScreen
         *            dual screen mode?
         */
        void execute(String userName, String password, boolean isDualScreen);
    }

    private static final String CSS_BASE_CLASS = "mwt-LoginPanel";

    private TextBox usernameTextBox_;
    private TextBox passwordTextBox_;
    private CheckBox dualScreenCheckBox_;
    private CheckBox mobileScreenCheckBox_;
    private Button loginBtn_;
    private Label msgArea_;
    private FlowPanel innerPanel_;
    private FlowPanel outerPanel_;
    private Label headerLabel_;
    private String checkedUsername_;
    private KeyPressHandler keyPresshdlr_ = new KeyPressHandler() {
        @Override
        public void onKeyPress(final KeyPressEvent event) {
            if (event.getCharCode() == KeyCodes.KEY_ENTER) {
                login();
            }
        }
    };

    // private FormElement formElement_;

    private boolean checkUserInput() {
        checkedUsername_ = usernameTextBox_.getText().trim();
        return !checkedUsername_.equals("");
    }

    /**
     * Constructor.
     */
    public ScsLoginPanel() {
        this(SPRING_SEC_PROCESSING_URL, true);
    }

    /**
     * Constructor.
     *
     * @param actionUrl
     *            Url to which the login form is posted
     */
    public ScsLoginPanel(final String actionUrl) {
        this(actionUrl, true);
    }

    /**
     * Constructor.
     *
     * @param handleErrorMsg
     *            if true the login panel display message according to
     *            {@code Constants.AUTH_ERR_PARAM_NAME} url parameter.
     */
    public ScsLoginPanel(final boolean handleErrorMsg) {
        this(SPRING_SEC_PROCESSING_URL, handleErrorMsg);
    }

    /**
     * Builds a login panel which posts the login form to the given action url.
     *
     * @param actionUrl
     *            Url to which the login form is posted
     * @param handleErrorMsg
     *            if true the login panel display message according to
     *            {@code Constants.AUTH_ERR_PARAM_NAME} url parameter.
     */
    public ScsLoginPanel(final String actionUrl, final boolean handleErrorMsg) {
        handleErrorMsg_ = handleErrorMsg;
        buildLoginPanel();
        usernameTextBox_.getElement().setAttribute("name", "j_username");
        passwordTextBox_.getElement().setAttribute("name", "j_password");
        loginBtn_.getElement().setAttribute("type", "submit");

        formPanel_ = new FormPanel();
        formPanel_.setAction(actionUrl);
        formPanel_.setMethod(FormPanel.METHOD_POST);
        // Removing target attribute prevent 302 HTTP responses to be stored
        // within a hidden iframe
        formPanel_.getElement().removeAttribute("target");
        initWidget(formPanel_);
        formPanel_.add(outerPanel_);
    }

    private FormPanel formPanel_;

    /**
     * Unit-test for using specific username/password credentials
     * 
     * @param username
     *            username
     * @param password
     *            password
     * @param dualScreen
     *            select dual screen mode
     */
    @BenchmarkAnnotation
    public void unitTestSetLoginPassword(final String username, final String password, final boolean dualScreen) {
        usernameTextBox_.setText(username);
        passwordTextBox_.setText(password);
        dualScreenCheckBox_.setValue(dualScreen);

        if (dualScreenCheckBox_.getValue()) {
            CookieUtil.setDualScreenCookie(DualScreenChoice.DUAL_SCREEN);
        } else {
            CookieUtil.setDualScreenCookie(DualScreenChoice.SINGLE_SCREEN);
        }
    }

    /**
     * Unit-test for form submission
     */
    @BenchmarkAnnotation
    public void unitTestLoginSubmit() {
        formPanel_.submit();
    }

    private void buildLoginPanel() {
        outerPanel_ = new FlowPanel();
        outerPanel_.getElement().setId("outerPanel");
        outerPanel_.addStyleName(CSS_BASE_CLASS);

        headerLabel_ = new Label();
        headerLabel_.addStyleName("header-label");
        headerLabel_.setVisible(false);
        outerPanel_.add(headerLabel_);

        innerPanel_ = new FlowPanel();
        innerPanel_.addStyleName("inner-panel");
        outerPanel_.add(innerPanel_);

        // For invalid login
        msgArea_ = new Label();
        msgArea_.addStyleName("msg-area");
        msgArea_.setVisible(false);
        innerPanel_.add(msgArea_);

        // Username
        final Label usernameLabel = new Label(Dictionary.getWording("login_username"));
        usernameLabel.addStyleName("username-label");
        innerPanel_.add(usernameLabel);
        usernameTextBox_ = new TextBox();
        usernameTextBox_.addStyleName("username-textbox");
        usernameTextBox_.addKeyPressHandler(keyPresshdlr_);
        usernameTextBox_.setFocus(true);
        usernameTextBox_.getElement().setAttribute("autocomplete", "off");
        innerPanel_.add(usernameTextBox_);

        // Password
        final Label passwordLabel = new Label(Dictionary.getWording("login_password"));
        usernameLabel.addStyleName("password-label");
        innerPanel_.add(passwordLabel);
        passwordTextBox_ = new PasswordTextBox();
        passwordTextBox_.addStyleName("password-textbox");
        passwordTextBox_.addKeyPressHandler(keyPresshdlr_);
        passwordTextBox_.getElement().setAttribute("autocomplete", "off");

        innerPanel_.add(passwordTextBox_);

        // Dual screen choice
        dualScreenCheckBox_ = new CheckBox(Dictionary.getWording("login_dualscreen"));
        dualScreenCheckBox_.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(final ValueChangeEvent<Boolean> event) {
                if (dualScreenCheckBox_.getValue()) {
                    CookieUtil.setDualScreenCookie(DualScreenChoice.DUAL_SCREEN);
                    mobileScreenCheckBox_.setValue(false);
                } else {
                    CookieUtil.setDualScreenCookie(DualScreenChoice.SINGLE_SCREEN);
                }
            }
        });
        dualScreenCheckBox_.setName("dualscreen");
        dualScreenCheckBox_.addStyleName("dualscreen-checkbox");
        innerPanel_.add(dualScreenCheckBox_);

        // Mobile screen choice

        mobileScreenCheckBox_ = new CheckBox(Dictionary.getWording("login_mobile"));
        mobileScreenCheckBox_.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (mobileScreenCheckBox_.getValue()) {
                    CookieUtil.setDualScreenCookie(DualScreenChoice.MOBILE_SCREEN);
                    dualScreenCheckBox_.setValue(false);
                } else {
                    if (dualScreenCheckBox_.getValue()) {
                        CookieUtil.setDualScreenCookie(DualScreenChoice.DUAL_SCREEN);
                    } else {
                        CookieUtil.setDualScreenCookie(DualScreenChoice.SINGLE_SCREEN);
                    }
                }
            }
        });
        mobileScreenCheckBox_.setName("mobile");
        mobileScreenCheckBox_.addStyleName("dualscreen-checkbox");
        innerPanel_.add(mobileScreenCheckBox_);
        // Login button
        loginBtn_ = new Button(Dictionary.getWording("login_connection"));
        loginBtn_.addStyleName("login-button");
        innerPanel_.add(loginBtn_);

        // Invisible hr tag used to include the floating login button
        innerPanel_.getElement().appendChild(DOM.createElement("hr"));
    }

    private void login() {
        if (checkUserInput()) {
            msgArea_.setText("");
            msgArea_.setVisible(false);

            // Form-based login panel
            /*
             * if (formElement_ != null) { formElement_.submit(); }
             */
            if (formPanel_ != null) {
                formPanel_.submit();
            }
        } else {
            msgArea_.setText(Dictionary.getWording("login_missing_username"));
            msgArea_.setVisible(true);
        }
    }

    /**
     * Set the header text of the login panel
     *
     * @param headerText
     *            Header text
     */
    public void setHeaderText(final String headerText) {
        if (headerText != null) {
            headerLabel_.setText(headerText);
            headerLabel_.setVisible(true);
        }
    }

    /**
     * Display an error message in the login panel
     * 
     * @param message
     *            Message to be displayed
     */
    public void displayErrorMessage(final String message) {
        if (message != null) {
            msgArea_.setText(message);
            msgArea_.setVisible(true);
        }
    }

    /**
     * Display an error message in the login panel
     */
    public void hideErrorMessage() {
        msgArea_.setText("");
        msgArea_.setVisible(false);
    }

    @Override
    protected void onLoad() {
        /*
         * usernameTextBox_.setFocus(true);
         * 
         * final String choice = Cookies.getCookie(dualScreenChoiceCookieName_);
         * if (choice != null && DualScreenChoice.isDualScreen(choice)) {
         * dualScreenCheckBox_.setValue(true); } else {
         * dualScreenCheckBox_.setValue(false); }
         * 
         * if (handleErrorMsg_ == true) { handleErrorMessages(); }
         */

        mobileScreenCheckBox_.setValue(CookieUtil.isMobileScreenSetFromCookie());
        dualScreenCheckBox_.setValue(CookieUtil.isDualScreenSetFromCookie());

        if (handleErrorMsg_ == true) {
            handleErrorMessages();
        }
    }

    protected void handleErrorMessages() {

        final String authErrCode = Window.Location.getParameter(Constants.AUTH_ERR_PARAM_NAME);
        if (authErrCode != null && !authErrCode.isEmpty()) {
            try {
                final int code = Integer.parseInt(authErrCode);
                final AuthenticationError error = AuthenticationError.getEnum(code);

                switch (error) {
                case AUTH_SERVICE_NOK:
                    displayErrorMessage(Dictionary.getWording("login_err_auth_service_nok"));
                    break;
                case BAD_CREDENTIALS:
                    displayErrorMessage(Dictionary.getWording("login_err_bad_credentials"));
                    break;
                case DB_SERVICE_NOK:
                    displayErrorMessage(Dictionary.getWording("login_err_db_service_nok"));
                    break;
                case MWT_SERVER_NOK:
                    displayErrorMessage(Dictionary.getWording("login_err_mwt_server_nok"));
                    break;
                default:
                    break;
                }
            } catch (final NumberFormatException e) {
                // ignore invalid error code
            }
        }
    }

}
