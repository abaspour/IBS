package com.nicico.ibs.repository;

import com.nicico.ibs.model.Custom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomDAO extends JpaRepository<Custom, Long>, JpaSpecificationExecutor<Custom> {
}
