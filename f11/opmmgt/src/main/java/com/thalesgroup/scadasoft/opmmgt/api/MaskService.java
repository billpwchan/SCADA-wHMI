package com.thalesgroup.scadasoft.opmmgt.api;

import com.thalesgroup.scadasoft.opmmgt.db.Mask;

import java.util.Collection;

public interface MaskService {

    Mask createMask(Mask mask);

    Collection<Mask> getAllMasks();

    void deleteMask(int id);

    Mask updateMask(Mask mask);

    Mask getMask(int id);

}
