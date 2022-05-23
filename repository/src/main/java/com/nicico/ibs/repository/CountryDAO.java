package com.nicico.ibs.repository;

import com.nicico.ibs.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryDAO extends JpaRepository<Country, Long>, JpaSpecificationExecutor<Country> {
}
