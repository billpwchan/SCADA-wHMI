package com.thalesgroup.scadasoft.opmmgt.db;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



public interface LocationRepository extends JpaRepository<Location, Integer> {

    List<Location> findByName(String name);
    List<Location> findByCategory(Integer category);
}