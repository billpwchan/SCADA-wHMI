package com.thalesgroup.scadasoft.opmmgt.db;

import java.util.Collection;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thalesgroup.scadasoft.opmmgt.api.FunctionService;
import com.thalesgroup.scadasoft.opmmgt.util.UtilService;

@Service(value = "functionService")
@Transactional
public class FunctionDBServiceImpl implements FunctionService {

    /**
     * Class level logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FunctionDBServiceImpl.class);

    // Database access
    private final FunctionRepository functionRepository;
    
    @Resource
    private UtilService utilService;

    @Autowired
    public FunctionDBServiceImpl(FunctionRepository functionRepository) {
        LOGGER.info("Create FunctionDBServiceImpl {} rep: {}", this, functionRepository);
        this.functionRepository = functionRepository;
    }

    @Override
    public synchronized Collection<Function> getAllFunctions() {
        return this.functionRepository.findAll();
    }

    @Override
    public synchronized Function createFunction(final Function Function) {
    	Function f = this.functionRepository.save(Function);
    	
    	if (f != null) {
    		this.utilService.setLastModifiedTime();
    	}
        return f;
    }

    @Override
    public synchronized Function updateFunction(final Function function) {
        Function u = this.functionRepository.findOne(function.getId());
        if (u != null) {
            u.setName(function.getName());
            u.setDescription(function.getDescription());
            u.setCategory(function.getCategory());
            u.setFamily(function.getFamily());
            Function f = this.functionRepository.save(u);
            if (f != null) {
        		this.utilService.setLastModifiedTime();
        	}
            return u;
        }

        return null;
    }

    @Override
    public synchronized void deleteFunction(final int id) {
        if (this.functionRepository.exists(id)) {
            this.functionRepository.delete(id);
            this.utilService.setLastModifiedTime();
        }
    }

    @Override
    public synchronized Function getFunction(final int id) {
        Function u = this.functionRepository.findOne(id);
        return u;
    }

}
