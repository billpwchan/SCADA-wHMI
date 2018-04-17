package com.thalesgroup.scadasoft.opmmgt;

import java.util.Collection;


import com.thalesgroup.scadasoft.opmmgt.api.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thalesgroup.scadasoft.opmmgt.db.Location;

import javax.annotation.Resource;

@RestController
@RequestMapping("/opm")
public class LocationRESTController {

    private final CounterService counterService;

    @Resource
    private LocationService locationService;

    @Autowired
    LocationRESTController(final CounterService counterService) {
        this.counterService = counterService;
    }

    @CrossOrigin()
    @RequestMapping(value = "/locations", method = RequestMethod.POST)
    public Location createLocation(@RequestBody final Location Location) {
        this.counterService.increment("com.thalesgroup.scadasoft.locationmgt.restcall.createLocation");
        return this.locationService.createLocation(Location);
    }

    @CrossOrigin()
    @RequestMapping(value = "/locations", method = RequestMethod.GET)
    public Collection<Location> getAllLocations() {
        this.counterService.increment("com.thalesgroup.scadasoft.locationmgt.restcall.getAllLocations");
        return this.locationService.getAllLocations();
    }

    @CrossOrigin()
    @RequestMapping(value = "/locations/{id}", method = RequestMethod.GET)
    public Location getLocation(@PathVariable(value = "id") final int id) {
        this.counterService.increment("com.thalesgroup.scadasoft.locationmgt.restcall.getLocation");
        return this.locationService.getLocation(id);
    }

    @CrossOrigin()
    @RequestMapping(value = "/locations/{id}", method = RequestMethod.DELETE)
    public void deleteLocation(@PathVariable(value = "id") final int id) {
        this.counterService.increment("com.thalesgroup.scadasoft.locationmgt.restcall.deleteLocation");
        this.locationService.deleteLocation(id);
    }

    @CrossOrigin()
    @RequestMapping(value = "/locations/{id}", method = RequestMethod.PUT)
    public Location updateLocation(@PathVariable(value = "id") final int id, @RequestBody final Location Location) {
        this.counterService.increment("com.thalesgroup.scadasoft.locationmgt.restcall.updateLocation");
        Location.setId(id);

        return this.locationService.updateLocation(Location);
    }

}
