package com.thalesgroup.scadasoft.opmmgt.api;

import com.thalesgroup.scadasoft.opmmgt.db.Function;

import java.util.Collection;

public interface FunctionService {

    Function createFunction(Function function);

    Collection<Function> getAllFunctions();

    void deleteFunction(int id);

    Function updateFunction(Function function);

    Function getFunction(int id);

}
