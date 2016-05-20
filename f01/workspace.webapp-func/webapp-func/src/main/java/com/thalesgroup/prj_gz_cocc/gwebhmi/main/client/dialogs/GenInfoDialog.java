package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.dialogs;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.component.Button;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;

/**
 * Dialog used to display messages to the HMI user; Adapted from InfoDialog
 */
public class GenInfoDialog {
    /** to add a styled background */
    private final PopupPanel glass_;
    
    /** The popup to display */
    private final PopupPanel popup_;
    
    /** The message displayed in the dialog panel */
    private final Label message_;
    
    /** Dialog button */
    private final Button dialogBtn_;
    
    /** Focus anchor (hidden) */
    private final Anchor focusAnchor_;
    
    private HandlerRegistration dialogBtnHandlerReg_;
    
    /** To display message's details if any */
    private final DisclosurePanel detailsPanel_;
    
    /** Optional details to be displayed */
    private final Label messageDetails_;
    
    /** Header of the dialog */
    private final Label header_;
    
    //private final boolean soaFwConnectionIssueOccurred_ = false;
    //private final boolean interactWithSoaFwConnectionStatus_;
    
    /**
     * Constructor.
     */
    public GenInfoDialog() {
        // Background
    	glass_ = new PopupPanel();
        glass_.addStyleName("infoDialog-glass");
        
        // Dialog
        popup_ = new PopupPanel(false);
        popup_.setStylePrimaryName("infoDialog-panel");
        
        // Vertical Panel that Contains the Header, Message, etc.
        final VerticalPanel popupContents = new VerticalPanel();
        
        // Header
        header_ = new Label();
        header_.addStyleName("header-label");
        popupContents.add(header_);
        
        // Message
        message_ = new HTML("");
        message_.addStyleName("message");
        popupContents.add(message_);
        
        // Optional Details
        detailsPanel_ = new DisclosurePanel(Dictionary.getWording("info_dialog_details_header"));
        detailsPanel_.setOpen(false);
        detailsPanel_.setVisible(false);
        messageDetails_ = new HTML("");
        messageDetails_.addStyleName("details");
        detailsPanel_.setContent(messageDetails_);
        popupContents.add(detailsPanel_);
        
        // (Close) Button
        dialogBtn_ = new Button();
        dialogBtn_.setVisible(false);
        popupContents.add(dialogBtn_);

        // Anchor on which the Focus will be Set
        focusAnchor_ = new Anchor();
        popupContents.add(focusAnchor_);

        // Include the Vertical Panel to the Popup Panel Represents the Dialog Box 
        popup_.add(popupContents);
        
        // Hide the Dialog Box by default
        hide();
    }
    
    /**
     * Displays the dialog with predefined messages
     * @param inHdrKey Dictionary Key to the Header Message
     * @param inMsgKey Dictionary Key to the Dialog Box Message
     * @param inDisplayBtn Indicates whether the Close Button should be Displayed
     */
    private void show(final String inHeaderKey, final String inMessageKey, final boolean inDisplayBtn) {
    	// Initialize Header and Close Button
    	header_.setText(Dictionary.getWording(inHeaderKey));
    	message_.setText(Dictionary.getWording(inMessageKey));
        
    	// Remove Previous Handler, if any
    	if (dialogBtnHandlerReg_ != null) {
            dialogBtnHandlerReg_.removeHandler();
        }
    	
    	if(inDisplayBtn) {
    		// Show Close Button
	        dialogBtnHandlerReg_ = dialogBtn_.addClickHandler(new ClickHandler() {
	            @Override
	            public void onClick(final ClickEvent event) {
	                hide();
	            }
	        });
	        dialogBtn_.setVisible(true);
    	} else {
    		// Hide Close Button
            dialogBtn_.setText("");
            dialogBtn_.setVisible(false);
    	}

    	// Show the Dialog
    	show();
    }
    
    /**
     * Displays the dialog without any buttons
     * @param inHdrKey Dictionary Key to the Header Message
     * @param inMsgKey Dictionary Key to the Dialog Box Message
     */
    public void showDialogWithoutButton(final String inHdrKey, final String inMsgKey) {
    	show(inHdrKey, inMsgKey, false);
    }

    /**
     * Displays the dialog with close button
     * @param inHdrKey Dictionary Key to the Header Message
     * @param inMsgKey Dictionary Key to the Dialog Box Message
     * @param inBtnMsgKey Dictionary Key to the Button Text
     */
    public void showDialogWithButton(final String inHdrKey, final String inMsgKey, final String inBtnMsgKey) {
    	dialogBtn_.setText(Dictionary.getWording(inBtnMsgKey));
    	show(inHdrKey, inMsgKey, true);
    }

    /**
     * Displays the dialog
     */
    public void show() {
        show("");
    }
    
    /**
     * Displays the dialog
     * @param detailMessage Detail message
     */
    public void show(final String detailMessage) {
        messageDetails_.setText(detailMessage);
        detailsPanel_.setVisible(messageDetails_.getText().length() > 0);

        glass_.show();
        popup_.center();
        focusAnchor_.setFocus(true);
    }
    
    /**
     * Hides the dialog.
     */
    public void hide() {
        if (dialogBtnHandlerReg_ != null) {
            dialogBtnHandlerReg_.removeHandler();
            dialogBtnHandlerReg_ = null;
        }
        detailsPanel_.setVisible(false);
        popup_.hide();
        glass_.hide();
    }
    
    /**
     * Indicates if the popup is visible
     * 
     * @return true if visible
     */
    public boolean isVisible() {
        return glass_.isShowing() || popup_.isShowing();
    }
}
