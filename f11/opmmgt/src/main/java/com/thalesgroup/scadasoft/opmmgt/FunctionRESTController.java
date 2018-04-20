package com.thalesgroup.scadasoft.opmmgt;

import com.thalesgroup.scadasoft.opmmgt.api.FunctionService;
import com.thalesgroup.scadasoft.opmmgt.db.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;

@RestController
@RequestMapping("/opm")
public class FunctionRESTController {

    private final CounterService counterService;

    @Resource
    private FunctionService functionService;

    @Autowired
    FunctionRESTController(final CounterService counterService) {
        this.counterService = counterService;
    }

    @CrossOrigin()
    @RequestMapping(value = "/functions", method = RequestMethod.POST)
    public Function createFunction(@RequestBody final Function Function) {
        this.counterService.increment("com.thalesgroup.scadasoft.functionmgt.restcall.createFunction");
        return this.functionService.createFunction(Function);
    }

    @CrossOrigin()
    @RequestMapping(value = "/functions", method = RequestMethod.GET)
    public Collection<Function> getAllFunctions() {
        this.counterService.increment("com.thalesgroup.scadasoft.functionmgt.restcall.getAllFunctions");
        return this.functionService.getAllFunctions();
    }

    @CrossOrigin()
    @RequestMapping(value = "/functions/{id}", method = RequestMethod.GET)
    public Function getFunction(@PathVariable(value = "id") final int id) {
        this.counterService.increment("com.thalesgroup.scadasoft.functionmgt.restcall.getFunction");
        return this.functionService.getFunction(id);
    }

    @CrossOrigin()
    @RequestMapping(value = "/functions/{id}", method = RequestMethod.DELETE)
    public void deleteFunction(@PathVariable(value = "id") final int id) {
        this.counterService.increment("com.thalesgroup.scadasoft.functionmgt.restcall.deleteFunction");
        this.functionService.deleteFunction(id);
    }

    @CrossOrigin()
    @RequestMapping(value = "/functions/{id}", method = RequestMethod.PUT)
    public Function updateFunction(@PathVariable(value = "id") final int id, @RequestBody final Function Function) {
        this.counterService.increment("com.thalesgroup.scadasoft.functionmgt.restcall.updateFunction");
        Function.setId(id);

        return this.functionService.updateFunction(Function);
    }

}
