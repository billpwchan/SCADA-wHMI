package com.thalesgroup.scadasoft.opmmgt;

import com.thalesgroup.scadasoft.opmmgt.api.MaskService;
import com.thalesgroup.scadasoft.opmmgt.db.Mask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;

@RestController
@RequestMapping("/opm")
public class MaskRESTController {

    private final CounterService counterService;

    @Resource
    private MaskService maskService;

    @Autowired
    MaskRESTController(final CounterService counterService) {
        this.counterService = counterService;
    }

    @CrossOrigin()
    @RequestMapping(value = "/masks", method = RequestMethod.POST)
    public Mask createMask(@RequestBody final Mask Mask) {
        this.counterService.increment("com.thalesgroup.scadasoft.maskmgt.restcall.createMask");
        return this.maskService.createMask(Mask);
    }

    @CrossOrigin()
    @RequestMapping(value = "/masks", method = RequestMethod.GET)
    public Collection<Mask> getAllMasks() {
        this.counterService.increment("com.thalesgroup.scadasoft.maskmgt.restcall.getAllMasks");
        return this.maskService.getAllMasks();
    }

    @CrossOrigin()
    @RequestMapping(value = "/masks/{id}", method = RequestMethod.GET)
    public Mask getMask(@PathVariable(value = "id") final int id) {
        this.counterService.increment("com.thalesgroup.scadasoft.maskmgt.restcall.getMask");
        Mask m = this.maskService.getMask(id);
        return m;
    }

    @CrossOrigin()
    @RequestMapping(value = "/masks/{id}", method = RequestMethod.DELETE)
    public void deleteMask(@PathVariable(value = "id") final int id) {
        this.counterService.increment("com.thalesgroup.scadasoft.maskmgt.restcall.deleteMask");
        this.maskService.deleteMask(id);
    }

    @CrossOrigin()
    @RequestMapping(value = "/masks/{id}", method = RequestMethod.PUT)
    public Mask updateMask(@PathVariable(value = "id") final int id, @RequestBody final Mask Mask) {
        this.counterService.increment("com.thalesgroup.scadasoft.maskmgt.restcall.updateMask");
        Mask.setId(id);

        return this.maskService.updateMask(Mask);
    }

}
