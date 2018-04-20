package com.thalesgroup.scadasoft.opmmgt.db;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ActionRepository extends JpaRepository<Action, Integer> {

    List<Action> findByName(String name);
    List<Action> findByCategory(Integer category);
}