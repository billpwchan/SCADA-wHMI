package com.thalesgroup.scadasoft.opmmgt.api;

import com.thalesgroup.scadasoft.opmmgt.db.Profile;

import java.util.Collection;

public interface ProfileService {

    Profile createProfile(Profile profile);

    Collection<Profile> getAllProfiles();

    void deleteProfile(int id);

    Profile updateProfile(Profile profile);

    Profile getProfile(int id);

}
