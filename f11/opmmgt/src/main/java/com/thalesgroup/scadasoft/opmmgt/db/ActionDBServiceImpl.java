package com.thalesgroup.scadasoft.opmmgt.db;

import java.util.Collection;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thalesgroup.scadasoft.opmmgt.api.ActionService;
import com.thalesgroup.scadasoft.opmmgt.util.UtilService;

@Service(value = "actionService")
@Transactional
public class ActionDBServiceImpl implements ActionService {

    /**
     * Class level logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ActionDBServiceImpl.class);

    // Database access
    private final ActionRepository actionRepository;
    
    @Resource
    private UtilService utilService;

    @Autowired
    public ActionDBServiceImpl(ActionRepository actionRepository) {
        LOGGER.info("Create ActionDBServiceImpl {} rep: {}", this, actionRepository);
        this.actionRepository = actionRepository;    
    }

    @Override
    public synchronized Collection<Action> getAllActions() {
        return this.actionRepository.findAll();
    }

    @Override
    public synchronized Action createAction(final Action Action) {
    	Action a = this.actionRepository.save(Action);
    	LOGGER.debug("Action createAction {}", a.toString());
    	if (a != null) {
    		this.utilService.setLastModifiedTime();
    	}
        return a;
    }

    @Override
    public synchronized Action updateAction(final Action action) {
        Action u = this.actionRepository.findOne(action.getId());
        if (u != null) {
            if (action.getName() != null) {
                u.setName(action.getName());
            }
            if (action.getDescription() != null) {
                u.setDescription(action.getDescription());
            }
            if (action.getCategory() != null) {
                u.setCategory(action.getCategory());
            }
            if (action.getAbbrev() != null) {
                u.setAbbrev(action.getAbbrev());
            }
            Action a = this.actionRepository.save(u);
            LOGGER.debug("Action updateAction {}", a.toString());
            if (a != null) {
            	this.utilService.setLastModifiedTime();
            }
            return u;
        }

        return null;
    }

    @Override
    public synchronized void deleteAction(final int id) {
        if (this.actionRepository.exists(id)) {

            this.actionRepository.delete(id);
            this.utilService.setLastModifiedTime();
        }
    }

    @Override
    public synchronized Action getAction(final int id) {
        Action u = this.actionRepository.findOne(id);
        return u;
    }

}
