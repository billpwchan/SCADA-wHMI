package com.thalesgroup.scadasoft.opmmgt.api;

import java.util.Collection;

import com.thalesgroup.scadasoft.opmmgt.db.Location;

public interface LocationService {

    Location createLocation(Location location);

    Collection<Location> getAllLocations();

    void deleteLocation(int id);

    Location updateLocation(Location location);

    Location getLocation(int id);

}
