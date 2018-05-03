package com.thalesgroup.scadasoft.opmmgt.db;

import java.util.Collection;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thalesgroup.scadasoft.opmmgt.api.ProfileService;
import com.thalesgroup.scadasoft.opmmgt.util.UtilService;

@Service(value = "profileService")
@Transactional
public class ProfileDBServiceImpl implements ProfileService {

    /**
     * Class level logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileDBServiceImpl.class);

    // Database access
    private final ProfileRepository profileRepository;

    @Resource
    private UtilService utilService;

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
    	Profile p = this.profileRepository.save(Profile);
    	if (p != null) {
        	this.utilService.setLastModifiedTime();
        }
        return p;
    }

    @Override
    public synchronized Profile updateProfile(final Profile profile) {
        Profile u = this.profileRepository.findOne(profile.getId());
        if (u != null) {
            u.setName(profile.getName());
            u.setDescription(profile.getDescription());
            Profile p = this.profileRepository.save(u);
            if (p != null) {
            	this.utilService.setLastModifiedTime();
            }
            return u;
        }

        return null;
    }

    @Override
    public synchronized void deleteProfile(final int id) {
        if (this.profileRepository.exists(id)) {

            this.profileRepository.delete(id);
            this.utilService.setLastModifiedTime();
            
        }
    }

    @Override
    public synchronized Profile getProfile(final int id) {
        Profile u = this.profileRepository.findOne(id);
        return u;
    }

}
