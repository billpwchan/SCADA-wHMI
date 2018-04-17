package com.thalesgroup.scadasoft.opmmgt.db;

import com.thalesgroup.scadasoft.opmmgt.api.FunctionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service(value = "functionService")
@Transactional
public class FunctionDBServiceImpl implements FunctionService {

    /**
     * Class level logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FunctionDBServiceImpl.class);

    // Database access
    private final FunctionRepository functionRepository;

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

        return this.functionRepository.save(Function);
    }

    @Override
    public synchronized Function updateFunction(final Function function) {
        Function u = this.functionRepository.findOne(function.getId());
        if (u != null) {
            u.setName(function.getName());
            u.setDescription(function.getDescription());
            u.setCategory(function.getCategory());
            u.setFamily(function.getFamily());
            this.functionRepository.save(u);
            return u;
        }

        return null;
    }

    @Override
    public synchronized void deleteFunction(final int id) {
        if (this.functionRepository.exists(id)) {
            this.functionRepository.delete(id);
        }
    }

    @Override
    public synchronized Function getFunction(final int id) {
        Function u = this.functionRepository.findOne(id);
        return u;
    }

}
