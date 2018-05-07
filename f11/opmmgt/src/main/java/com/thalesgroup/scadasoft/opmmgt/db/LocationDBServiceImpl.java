package com.thalesgroup.scadasoft.opmmgt.db;

import java.util.Collection;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thalesgroup.scadasoft.opmmgt.api.LocationService;
import com.thalesgroup.scadasoft.opmmgt.util.UtilService;

@Service(value = "locationService")
@Transactional
public class LocationDBServiceImpl implements LocationService {

    /**
     * Class level logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LocationDBServiceImpl.class);

    // Database access
    private final LocationRepository locationRepository;
    
    @Resource
    private UtilService utilService;

    @Autowired
    public LocationDBServiceImpl(LocationRepository locationRepository) {
        LOGGER.info("Create LocationDBServiceImpl {} rep: {}", this, locationRepository);
        this.locationRepository = locationRepository;
    }

    @Override
    public synchronized Collection<Location> getAllLocations() {
        return this.locationRepository.findAll();
    }

    @Override
    public synchronized Location createLocation(final Location Location) {
    	Location loc = this.locationRepository.save(Location);
    	if (loc != null) {
    		this.utilService.setLastModifiedTime();
    	}
        return loc;
    }

    @Override
    public synchronized Location updateLocation(final Location location) {
        Location u = this.locationRepository.findOne(location.getId());
        if (u != null) {
            if (location.getName() != null) {
                u.setName(location.getName());
            }
            if (location.getDescription() != null) {
                u.setDescription(location.getDescription());
            }
            if (location.getCategory() != null) {
                u.setCategory(location.getCategory());
            }
            Location loc = this.locationRepository.save(u);
            if (loc != null) {
            	this.utilService.setLastModifiedTime();
            }
            return u;
        }

        return null;
    }

    @Override
    public synchronized void deleteLocation(final int id) {
        if (this.locationRepository.exists(id)) {

            this.locationRepository.delete(id);
            this.utilService.setLastModifiedTime();
        }
    }

    @Override
    public synchronized Location getLocation(final int id) {
        Location u = this.locationRepository.findOne(id);
        return u;
    }

}
