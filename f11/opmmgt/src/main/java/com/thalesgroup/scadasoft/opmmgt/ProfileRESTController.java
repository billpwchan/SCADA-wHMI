package com.thalesgroup.scadasoft.opmmgt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thalesgroup.scadasoft.opmmgt.api.ActionService;
import com.thalesgroup.scadasoft.opmmgt.api.FunctionService;
import com.thalesgroup.scadasoft.opmmgt.api.LocationService;
import com.thalesgroup.scadasoft.opmmgt.api.MaskService;
import com.thalesgroup.scadasoft.opmmgt.api.ProfileService;
import com.thalesgroup.scadasoft.opmmgt.db.Action;
import com.thalesgroup.scadasoft.opmmgt.db.Function;
import com.thalesgroup.scadasoft.opmmgt.db.Location;
import com.thalesgroup.scadasoft.opmmgt.db.Mask;
import com.thalesgroup.scadasoft.opmmgt.db.Profile;
import com.thalesgroup.scadasoft.opmmgt.util.UtilService;

@RestController
@RequestMapping("/opm")
public class ProfileRESTController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileRESTController.class);

    private final CounterService counterService;

    @Resource
    private ProfileService profileService;
    
    @Resource
    private LocationService locationService;
    
    @Resource
    private FunctionService functionService;
    
    @Resource
    private ActionService actionService;
    
    @Resource
    private MaskService maskService;
    
    @Resource
    private UtilService utilService;
    
    Map<Character, Action> actionsMap = null;

    @Value("${opm.dumpPermissionWithEmptyMaskValue:true}")
    private boolean dumpPermissionWithEmptyMaskValue;
    
    @Autowired
    ProfileRESTController(final CounterService counterService) {
        this.counterService = counterService;
    }

    @CrossOrigin()
    @RequestMapping(value = "/profiles", method = RequestMethod.POST)
    public Profile createProfile(@RequestBody final Profile Profile) {
        this.counterService.increment("com.thalesgroup.scadasoft.profilemgt.restcall.createProfile");
        return this.profileService.createProfile(Profile);
    }

    @CrossOrigin()
    @RequestMapping(value = "/profiles", method = RequestMethod.GET)
    public Collection<Profile> getAllProfiles() {
        this.counterService.increment("com.thalesgroup.scadasoft.profilemgt.restcall.getAllProfiles");
        return this.profileService.getAllProfiles();
    }
    
    @CrossOrigin()
    @RequestMapping(value = "/dump", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public String dumpAllProfiles() {
    	LOGGER.debug("start dumpAllProfiles");
        this.counterService.increment("com.thalesgroup.scadasoft.profilemgt.restcall.dumpAllProfiles");
        StringBuilder sbuilder = new StringBuilder();
        getHVActions();
        
        LOGGER.debug("dumpHVOPMHeader");
        dumpHVOPMHeader(sbuilder);
        Collection<Profile> profList = this.profileService.getAllProfiles();

        LOGGER.debug("dumpHVOPMProfiles");
        sbuilder.append("  <!-- List of ROLES -->\n");
        for(Profile prof : profList) {
        	if (prof.getMasks().size() > 0) {
        		dumpHVOPMProfile(sbuilder, prof);
        	}
        }
        
        sbuilder.append("  <!-- List of PERMISSIONS -->\n");
        for(Profile prof : profList) {
        	if (prof.getMasks().size() > 0) {
	        	sbuilder.append("  <!-- PERMISSIONS for ").append(prof.getName()).append(" -->\n");
	        	List<Mask> masks = sort(prof.getMasks());
	        	for(Mask m : masks) {
	        		if ((m.getMask1().isEmpty() && this.dumpPermissionWithEmptyMaskValue) || (!m.getMask1().isEmpty() && ! "0".equals(m.getMask1()))) {
	        			dumpHVOPMPermission(sbuilder, prof, m, m.getMask1(), 1);
	        		}
	        		if ((m.getMask2().isEmpty() && this.dumpPermissionWithEmptyMaskValue) || (!m.getMask2().isEmpty() && ! "0".equals(m.getMask2()))) {
	        			dumpHVOPMPermission(sbuilder, prof, m, m.getMask2(), 2);
	        		}
	        		if ((m.getMask3().isEmpty() && this.dumpPermissionWithEmptyMaskValue) || (!m.getMask3().isEmpty() && ! "0".equals(m.getMask3()))) {
	        			dumpHVOPMPermission(sbuilder, prof, m, m.getMask3(), 3);
	        		}
	        		if ((m.getMask4().isEmpty() && this.dumpPermissionWithEmptyMaskValue) || (!m.getMask4().isEmpty() && ! "0".equals(m.getMask4()))) {
	        			dumpHVOPMPermission(sbuilder, prof, m, m.getMask4(), 4);
	        		}
	        	}
        	}
        }

        LOGGER.debug("dumpHVOPMFooter");
        dumpHVOPMFooter(sbuilder);
        LOGGER.debug("finish dumpAllProfiles");
        return sbuilder.toString();
    }

	@CrossOrigin()
    @RequestMapping(value = "/profiles/{id}", method = RequestMethod.GET)
    public Profile getProfile(@PathVariable(value = "id") final int id) {
        this.counterService.increment("com.thalesgroup.scadasoft.profilemgt.restcall.getProfile");
        return this.profileService.getProfile(id);
    }

    @CrossOrigin()
    @RequestMapping(value = "/profiles/{id}", method = RequestMethod.DELETE)
    public void deleteProfile(@PathVariable(value = "id") final int id) {
        this.counterService.increment("com.thalesgroup.scadasoft.profilemgt.restcall.deleteProfile");
        this.profileService.deleteProfile(id);
    }

    @CrossOrigin()
    @RequestMapping(value = "/profiles/{id}", method = RequestMethod.PUT)
    public Profile updateProfile(@PathVariable(value = "id") final int id, @RequestBody final Profile Profile) {
        this.counterService.increment("com.thalesgroup.scadasoft.profilemgt.restcall.updateProfile");
        Profile.setId(id);

        return this.profileService.updateProfile(Profile);
    }

    // code to dump data in HV xml format
    private void dumpHVOPMHeader(StringBuilder sbuilder) {
    	sbuilder.append("<?xml version='1.0' encoding='UTF-8'?>\n");
    	sbuilder.append("<tns:maestroHMIConfiguration\n");
    	sbuilder.append("     xmlns:tns='http://www.thalesgroup.com/hv/mwt/conf/opm/permission'\n");
    	sbuilder.append("     xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'\n");
    	sbuilder.append("     xsi:schemaLocation='http://www.thalesgroup.com/hv/mwt/conf/opm/permission mwt-opm.xsd'>\n\n");

    	sbuilder.append("  <!-- Validation configuration -->\n");
    	sbuilder.append("  <validationConfiguration enabled='true' checkAlarmSourceID='true'\n");
    	sbuilder.append("                           checkEventSourceID='true' displayLogOnCheck='true' />\n\n");
    	
    }
    
    private void dumpHVOPMProfile(StringBuilder sbuilder, Profile prof) {
		List<Mask> masks = sort(prof.getMasks());
    	sbuilder.append("  <role id='").append(prof.getName()).append("'>\n");
    	for(Mask m : masks) {
    		if ((m.getMask1().isEmpty() && this.dumpPermissionWithEmptyMaskValue) || (!m.getMask1().isEmpty() && ! "0".equals(m.getMask1()))) {
    			sbuilder.append("    <permissionAllowList>permission_").append(prof.getName()).append("_").append(m.getId()).append(1).append("</permissionAllowList>\n");
    		}
    		if ((m.getMask2().isEmpty() && this.dumpPermissionWithEmptyMaskValue) || (!m.getMask2().isEmpty() && ! "0".equals(m.getMask2()))) {
    			sbuilder.append("    <permissionAllowList>permission_").append(prof.getName()).append("_").append(m.getId()).append(2).append("</permissionAllowList>\n");
    		}
    		if ((m.getMask3().isEmpty() && this.dumpPermissionWithEmptyMaskValue) || (!m.getMask3().isEmpty() && ! "0".equals(m.getMask3()))) {
    			sbuilder.append("    <permissionAllowList>permission_").append(prof.getName()).append("_").append(m.getId()).append(3).append("</permissionAllowList>\n");
    		}
    		if ((m.getMask4().isEmpty() && this.dumpPermissionWithEmptyMaskValue) || (!m.getMask4().isEmpty() && ! "0".equals(m.getMask4()))) {
    			sbuilder.append("    <permissionAllowList>permission_").append(prof.getName()).append("_").append(m.getId()).append(4).append("</permissionAllowList>\n");
    		}
    	}
    	sbuilder.append("  </role>\n\n");
	}
    
    private void dumpHVOPMPermission(StringBuilder sbuilder, Profile prof, Mask m, String mask, int index) {
    	sbuilder.append("  <permission id='permission_").append(prof.getName()).append("_").append(m.getId()).append(index).append("'>\n");
    	
    	// dump mode
    	sbuilder.append("    <criterion key='mode' xsi:type='tns:criterionList'>\n");
    	sbuilder.append("      <value>").append(index).append("</value>\n");
		sbuilder.append("    </criterion>\n");
		
    	// dump location
    	if (m.getLocation() != null) {
    		sbuilder.append("    <criterion key='location' xsi:type='tns:criterionList'>\n");
    		sbuilder.append("      <value>").append(m.getLocation().getName()).append("</value>\n");
    		sbuilder.append("    </criterion>\n");
    	} else {
    		sbuilder.append("    <criterion key='location' xsi:type='tns:criterionAll'/>\n");
    	}
    	// dump function
    	if (m.getFunction() != null) {
    		sbuilder.append("    <criterion key='function' xsi:type='tns:criterionList'>\n");
    		sbuilder.append("      <value>").append(m.getFunction().getName()).append("</value>\n");
    		sbuilder.append("    </criterion>\n");
    	} else {
    		sbuilder.append("    <criterion key='function' xsi:type='tns:criterionAll'/>\n");
    	}
    	
    	// dump mask
    	if ("1".equals(mask)) {
    		sbuilder.append("    <criterion key='action' xsi:type='tns:criterionAll'/>\n");
    	} else {
    		sbuilder.append("    <criterion key='action' xsi:type='tns:criterionList'>\n");
    		int i;
    		for(i = 0; i < mask.length(); i++) {
    			sbuilder.append("      <value>").append(getHVAction(mask.charAt(i))).append("</value>\n");
    		}
    		sbuilder.append("    </criterion>\n");
    	}

    	sbuilder.append("  </permission>\n");
	}
    
    // 'M' => VIEW_ENTITY
    // 'C' => COMMAND_ENTITY
    // 'D' => VIEW_ENTITY_TYPE_ALARM
    // 'A' => COMMAND_ENTITY_TYPE_ALARM
    private void getHVActions() {
    	Collection<Action> actions = this.actionService.getAllActions();
    	actionsMap = new HashMap<Character, Action>();
    	for (Action a : actions) {
    		String abbrev = a.getAbbrev();
    		if (abbrev != null && !abbrev.isEmpty()) {
        		actionsMap.put(new Character(abbrev.charAt(0)), a);
    		}
    	}
	}
    
    private String getHVAction(char c) {
    	String retVal = "ACTION '" + c + "' (abbrev) NOT FOUND !!";
    	if (actionsMap.containsKey(c)) {
    		retVal = actionsMap.get(c).getName();
    	}
    	
		return retVal;
	}

	private void dumpHVOPMFooter(StringBuilder sbuilder) {
    	sbuilder.append("</tns:maestroHMIConfiguration>");
    }
	
	@CrossOrigin()
    @RequestMapping(value = "/dumpcfg/def", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String dumpCfgDefinition() {
        this.counterService.increment("com.thalesgroup.scadasoft.profilemgt.restcall.dumpCfgDefinition");
        StringBuilder sbuilder = new StringBuilder();
        
        Collection<Location> locList = this.locationService.getAllLocations();
        sbuilder.append("Define Locations\n{\n");
        for(Location l : locList) {
        	sbuilder.append("  Define Location \"").append(l.getName()).append("\"\n  {\n");
        	sbuilder.append("    Category = ").append(l.getCategory()).append("\n");
        	sbuilder.append("    Description = \"").append(l.getDescription()).append("\"\n");
        	sbuilder.append("  }\n");
        }
        sbuilder.append("}\n\n");
        
        Collection<Function> funcList = this.functionService.getAllFunctions();
        sbuilder.append("Define Functions\n{\n");
        for(Function f : funcList) {
        	sbuilder.append("  Define Function \"").append(f.getName()).append("\"\n  {\n");
        	sbuilder.append("    Category = ").append(f.getCategory()).append("\n");
        	sbuilder.append("    Description = \"").append(f.getDescription()).append("\"\n");
        	sbuilder.append("    Family = \"").append(f.getFamily()).append("\"\n");
        	sbuilder.append("  }\n");
        }
        sbuilder.append("}\n\n");
        
        Collection<Action> actList = this.actionService.getAllActions();
        sbuilder.append("Define Actions\n{\n");
        for(Action a : actList) {
        	sbuilder.append("  Define Action \"").append(a.getName()).append("\"\n  {\n");
        	sbuilder.append("    Category = ").append(a.getCategory()).append("\n");
        	sbuilder.append("    Abbrev = \"").append(a.getAbbrev()).append("\"\n");
        	sbuilder.append("    Description = \"").append(a.getDescription()).append("\"\n");
        	sbuilder.append("  }\n");
        }
        sbuilder.append("}\n");
        
        return sbuilder.toString();
    }
	
	@CrossOrigin()
    @RequestMapping(value = "/dumpcfg/prof", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String dumpCfgProfile() {
        this.counterService.increment("com.thalesgroup.scadasoft.profilemgt.restcall.dumpCfgProfile");
        StringBuilder sbuilder = new StringBuilder();
        
        Collection<Profile> profList = this.profileService.getAllProfiles();
        for(Profile prof : profList) {
        	if (prof.getMasks().size() > 0) {
        		sbuilder.append("Define Profile \"").append(prof.getName()).append("\"\n{\n");
        		for(Mask m: prof.getMasks()) {
        			if (m.getFunction() != null) {
        	    		sbuilder.append("  F").append(m.getFunction().getCategory());
        	    		
        	    	} else {
        	    		sbuilder.append("  F*");
        	    	}
        			if (m.getLocation() != null) {
        	    		sbuilder.append(" L").append(m.getLocation().getCategory());
        	    		
        	    	} else {
        	    		sbuilder.append(" L*");
        	    	}
        			sbuilder.append(" = ").append(m.getMask1());
        			if (m.getMask2() != null) {
        				sbuilder.append(" ").append(m.getMask2());
        			}
        			if (m.getMask3() != null) {
        				sbuilder.append(" ").append(m.getMask3());
        			}
        			if (m.getMask4() != null) {
        				sbuilder.append(" ").append(m.getMask4());
        			}
        			
        			sbuilder.append("\n");
        		}
        		sbuilder.append("}\n\n");
        	}
        }
        
        return sbuilder.toString();
    }
	
	@CrossOrigin()
    @RequestMapping(value = "/lastUpdateTime", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String lastUpdateTime() {
		return Long.toString(this.utilService.getLastUpdateTime().getTime());
	}

	private List<Mask> sort(Set<Mask> masks) {
		List<Mask> retVal = new ArrayList<Mask>(masks);
		Collections.sort(retVal, new Comparator<Mask>() {
			@Override
			public int compare(Mask o1, Mask o2) {
				int retVal = 0;
				if (o1.getId() < o2.getId()) {
					retVal =-1;
				} else if (o1.getId() == o2.getId()) {
					retVal = 0;
				} else {
					retVal = 1;
				}
				return retVal;
			}
		});
		return retVal;
	}
	
}
