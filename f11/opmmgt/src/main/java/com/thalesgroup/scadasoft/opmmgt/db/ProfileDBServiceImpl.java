package com.thalesgroup.scadasoft.opmmgt.db;

import com.thalesgroup.scadasoft.opmmgt.api.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service(value = "profileService")
@Transactional
public class ProfileDBServiceImpl implements ProfileService {

    /**
     * Class level logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileDBServiceImpl.class);

    // Database access
    private final ProfileRepository profileRepository;


    @Autowired
    public ProfileDBServiceImpl(ProfileRepository profileRepository) {
        LOGGER.info("Create ProfileDBServiceImpl {} rep: {}", this, profileRepository);
        this.profileRepository = profileRepository;
    }

    @Override
    public synchronized Collection<Profile> getAllProfiles() {
        return this.profileRepository.findAll();
    }

    @Override
    public synchronized Profile createProfile(final Profile Profile) {

        return this.profileRepository.save(Profile);
    }

    @Override
    public synchronized Profile updateProfile(final Profile profile) {
        Profile u = this.profileRepository.findOne(profile.getId());
        if (u != null) {
            u.setName(profile.getName());
            u.setDescription(profile.getDescription());
            this.profileRepository.save(u);
            return u;
        }

        return null;
    }

    @Override
    public synchronized void deleteProfile(final int id) {
        if (this.profileRepository.exists(id)) {

            this.profileRepository.delete(id);
        }
    }

    @Override
    public synchronized Profile getProfile(final int id) {
        Profile u = this.profileRepository.findOne(id);
        return u;
    }

}
