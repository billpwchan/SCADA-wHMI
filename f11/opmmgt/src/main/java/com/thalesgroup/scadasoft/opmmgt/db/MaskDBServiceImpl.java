package com.thalesgroup.scadasoft.opmmgt.db;

import com.thalesgroup.scadasoft.opmmgt.api.MaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service(value = "maskService")
@Transactional
public class MaskDBServiceImpl implements MaskService {

    /**
     * Class level logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MaskDBServiceImpl.class);

    // Database access
    private final MaskRepository maskRepository;
    private final FunctionRepository functionRepository;
    private final LocationRepository locationRepository;
    private final ProfileRepository profileRepository;

    @Autowired
    public MaskDBServiceImpl(MaskRepository maskRepository, FunctionRepository functionRepository,
                             LocationRepository locationRepository, ProfileRepository profileRepository)
    {
        LOGGER.info("Create MaskDBServiceImpl {} rep: {}", this, maskRepository);
        this.maskRepository = maskRepository;
        this.functionRepository = functionRepository;
        this.profileRepository = profileRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public synchronized Collection<Mask> getAllMasks() {
        return this.maskRepository.findAll();
    }
    
    private void connectToEntities(Mask mask) {
    	if (mask.getFunction() != null) {
    		Function fct = null;		
    		if (mask.getFunction().getId() != null && mask.getFunction().getId() >= 0) {
    			fct = functionRepository.getOne(mask.getFunction().getId());
    		}
    		
    		if (fct == null && mask.getFunction().getCategory() != null) {
    			List<Function> fctList = functionRepository.findByCategory(mask.getFunction().getCategory());
    			if (fctList.size() > 0) {
    				fct = fctList.get(0);
    			}
    		} 
    		
    		if (fct == null && mask.getFunction().getName() != null) {
    			List<Function> fctList = functionRepository.findByName(mask.getFunction().getName());
    			if (fctList.size() > 0) {
    				fct = fctList.get(0);
    			}
    		}
    		
            mask.setFunction(fct);
        }
        if (mask.getLocation() != null) {
            Location loc = null;
  
            if (mask.getLocation().getId() != null && mask.getLocation().getId() >= 0) {
    			loc = locationRepository.getOne(mask.getLocation().getId());
    		} 
            
            if (loc == null && mask.getLocation().getCategory() != null) {
    			List<Location> locList = locationRepository.findByCategory(mask.getLocation().getCategory());
    			if (locList.size() > 0) {
    				loc = locList.get(0);
    			}
    		}
            
            if (loc == null && mask.getLocation().getName() != null) {
    			List<Location> locList = locationRepository.findByName(mask.getLocation().getName());
    			if (locList.size() > 0) {
    				loc = locList.get(0);
    			}
    		}
            
            mask.setLocation(loc);
        }
        if (mask.getProfile() != null) {
            Profile prof = null;
            
            if (mask.getProfile().getId() != null) {
            	prof = profileRepository.getOne(mask.getProfile().getId());
    		} else if (mask.getProfile().getName() != null) {
    			List<Profile> profList = profileRepository.findByName(mask.getProfile().getName());
    			if (profList.size() > 0) {
    				prof = profList.get(0);
    			}
    		}
  
            mask.setProfile(prof);
        }
    }

    @Override
    public synchronized Mask createMask(final Mask mask) {
        Mask m = null;
        try {
            connectToEntities(mask);
            m = this.maskRepository.save(mask);
        } catch(Exception e) {
            LOGGER.error("CreateMask failure: {}", e.getMessage());
        }
        return m;
    }

    @Override
    public synchronized Mask updateMask(final Mask mask) {
        Mask u = this.maskRepository.findOne(mask.getId());
        connectToEntities(mask);
        if (u != null) {
            u.setMask1(mask.getMask1());
            u.setMask2(mask.getMask2());
            u.setMask3(mask.getMask3());
            u.setMask4(mask.getMask4());

            if (mask.getFunction() != null) {
                Function fct = functionRepository.getOne(mask.getFunction().getId());
                u.setFunction(fct);
            }
            if (mask.getLocation() != null) {
                Location loc = locationRepository.getOne(mask.getLocation().getId());
                u.setLocation(loc);
            }
            if (mask.getProfile() != null) {
                Profile prof = profileRepository.getOne(mask.getProfile().getId());
                u.setProfile(prof);
            }

            this.maskRepository.save(u);
            return u;
        }

        return null;
    }

    @Override
    public synchronized void deleteMask(final int id) {

            this.maskRepository.delete(id);

    }

    @Override
    public synchronized Mask getMask(final int id) {
        Mask u = this.maskRepository.findOne(id);
        return u;
    }

}
