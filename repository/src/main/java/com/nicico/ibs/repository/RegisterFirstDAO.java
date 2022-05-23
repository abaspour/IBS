package com.nicico.ibs.repository;

import com.nicico.ibs.model.RegisterFirst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterFirstDAO extends JpaRepository<RegisterFirst, Long>, JpaSpecificationExecutor<RegisterFirst> {
    RegisterFirst findByRegisterNoAndRegisterCountry( String registerNo, String registerCountry);
    RegisterFirst findByRegisterId(String registerId);
}
