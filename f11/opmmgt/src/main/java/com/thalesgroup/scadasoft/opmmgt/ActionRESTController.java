package com.thalesgroup.scadasoft.opmmgt;

import com.thalesgroup.scadasoft.opmmgt.api.ActionService;
import com.thalesgroup.scadasoft.opmmgt.db.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;

@RestController
@RequestMapping("/opm")
public class ActionRESTController {

    private final CounterService counterService;

    @Resource
    private ActionService actionService;

    @Autowired
    ActionRESTController(final CounterService counterService) {
        this.counterService = counterService;
    }

    @CrossOrigin()
    @RequestMapping(value = "/actions", method = RequestMethod.POST)
    public Action createAction(@RequestBody final Action Action) {
        this.counterService.increment("com.thalesgroup.scadasoft.actionmgt.restcall.createAction");
        return this.actionService.createAction(Action);
    }

    @CrossOrigin()
    @RequestMapping(value = "/actions", method = RequestMethod.GET)
    public Collection<Action> getAllActions() {
        this.counterService.increment("com.thalesgroup.scadasoft.actionmgt.restcall.getAllActions");
        return this.actionService.getAllActions();
    }

    @CrossOrigin()
    @RequestMapping(value = "/actions/{id}", method = RequestMethod.GET)
    public Action getAction(@PathVariable(value = "id") final int id) {
        this.counterService.increment("com.thalesgroup.scadasoft.actionmgt.restcall.getAction");
        return this.actionService.getAction(id);
    }

    @CrossOrigin()
    @RequestMapping(value = "/actions/{id}", method = RequestMethod.DELETE)
    public void deleteAction(@PathVariable(value = "id") final int id) {
        this.counterService.increment("com.thalesgroup.scadasoft.actionmgt.restcall.deleteAction");
        this.actionService.deleteAction(id);
    }

    @CrossOrigin()
    @RequestMapping(value = "/actions/{id}", method = RequestMethod.PUT)
    public Action updateAction(@PathVariable(value = "id") final int id, @RequestBody final Action Action) {
        this.counterService.increment("com.thalesgroup.scadasoft.actionmgt.restcall.updateAction");
        Action.setId(id);

        return this.actionService.updateAction(Action);
    }

}
