package com.thalesgroup.scadasoft.opmmgt.db;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FunctionRepository extends JpaRepository<Function, Integer> {

    List<Function> findByName(String name);
    List<Function> findByFamily(String family);
    List<Function> findByCategory(Integer category);
}