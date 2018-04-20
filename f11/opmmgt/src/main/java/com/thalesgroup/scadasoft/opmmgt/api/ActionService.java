package com.thalesgroup.scadasoft.opmmgt.api;

import com.thalesgroup.scadasoft.opmmgt.db.Action;

import java.util.Collection;

public interface ActionService {

    Action createAction(Action action);

    Collection<Action> getAllActions();

    void deleteAction(int id);

    Action updateAction(Action action);

    Action getAction(int id);

}
