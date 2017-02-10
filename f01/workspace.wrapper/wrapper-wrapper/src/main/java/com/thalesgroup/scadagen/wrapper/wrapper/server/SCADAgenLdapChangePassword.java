package com.thalesgroup.scadagen.wrapper.wrapper.server;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.CommunicationException;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.security.authentication.encoding.LdapShaPasswordEncoder;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.action.ChangePasswordAction;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.security.server.ChangePasswordException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.security.server.IPasswordChange;
import com.thalesgroup.scadagen.wrapper.wrapper.shared.SCADAgenChangePasswordAction;


/**
 * Change the user password in a ldap directory.
 */
public class SCADAgenLdapChangePassword implements IPasswordChange {
    
    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(SCADAgenLdapChangePassword.class);
    private static final String LOG_PREFIX = "[SCADAgenLdapChangePassword] ";

    private static final String SEARCH_FILTER_DEFAULT = "(uid={0})";

    private LdapTemplate ldapTemplate_;
    
    private LdapShaPasswordEncoder pwdEncoder_;
    
    private String searchBase_;
    private String searchFilter_ = SEARCH_FILTER_DEFAULT;
    
    /**
     * Sets the password encoder
     * @param pwdEncoder password encoder
     */
    public void setPwdEncoder(final LdapShaPasswordEncoder pwdEncoder) {
        this.pwdEncoder_ = pwdEncoder;
    }
    
    /**
     * Sets the spring ldap template
     * @param ldapTemplate ldap template
     */
    public void setLdapTemplate(final LdapTemplate ldapTemplate) {
        this.ldapTemplate_ = ldapTemplate;
    }
    
    /**
     * Sets the searchBase
     * @param searchBase   searchBase
     */
    public void setSearchBase(final String searchBase) {
        this.searchBase_ = searchBase;
    }
    /**
     * Get Search Filter (RFC 2254 Representation)
     * @return Search Filter (RFC 2254 Representation)
     */
    public String getSearchFilter() {
        return searchFilter_;
    }
    /**
     * Set Search Filter (RFC 2254 Representation)
     * @param searchFilter Search Filter (RFC 2254 Representation)
     */
    public void setSearchFilter(final String searchFilter) {
        searchFilter_ = searchFilter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changePassword(final String userId, final ChangePasswordAction action) throws ChangePasswordException {
        
    	String targetUserId = userId;
    	if ( action instanceof SCADAgenChangePasswordAction ) {
    		logger.info(LOG_PREFIX + "action instanceof SCADAgenChangePasswordAction IS TRUE");
    		String tmp = ((SCADAgenChangePasswordAction)action).getUserId();
    		if ( null != tmp ) {
    			targetUserId = tmp;
    		} else {
    			logger.info(LOG_PREFIX + "action.getUserId() IS NULL, using currentPassword");
    		}
    	} else {
    		logger.info(LOG_PREFIX + "action instanceof SCADAgenChangePasswordAction IS FALSE");
    	}
    	
        final String oldPassword = action.getOldPassword();
        final String newPassword = action.getNewPassword();
        
        try {
        	final List<DirContextOperations> contexts = getUserDn(targetUserId);
        
	        if (contexts.size() == 0) {
	            throw new ChangePasswordException("pwdChgNoMatch");
	        } else if (contexts.size() > 1) {
	            throw new ChangePasswordException("pwdChgTooManyMatch");
	        } else {
	            final DirContextOperations ctx = contexts.get(0);
	            
	            final String directoryPassword = new String((byte[]) ctx.getObjectAttribute("userpassword"));
	            
	            if (!pwdEncoder_.isPasswordValid(directoryPassword, oldPassword, null)) {
	                throw new ChangePasswordException("pwdChgWrongPwd");
	            }
	            
	            final String encPwd = pwdEncoder_.encodePassword(newPassword, null);
	            ctx.setAttributeValue("userPassword", encPwd);
	            
	            ldapTemplate_.modifyAttributes(ctx);
	        }
        } catch (CommunicationException e) {
        	String m = e.getMessage();
        	logger.error(LOG_PREFIX + m, e);
        }
    }
    
    @SuppressWarnings("unchecked")
    protected List<DirContextOperations> getUserDn(final String userUid) {
        
        final String filter =
            searchFilter_ != null ? searchFilter_.replaceAll("\\{0\\}", userUid) : SEARCH_FILTER_DEFAULT.replaceAll(
                "\\{0\\}", userUid);
            
        final String base = searchBase_ != null ? searchBase_ : "";
        
        logger.info("Search for user : base=[{}] - filter=[{}]", new Object[] { base, filter });

        return ldapTemplate_.search(base, filter, new AbstractContextMapper() {

            @Override
            protected DirContextOperations doMapFromContext(final DirContextOperations ctx) {
                return ctx;
            }
        });
    }
}
