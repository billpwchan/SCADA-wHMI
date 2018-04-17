package com.thalesgroup.scadasoft.opmmgt.db;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProfileRepository extends JpaRepository<Profile, Integer> {

    List<Profile> findByName(String name);
}